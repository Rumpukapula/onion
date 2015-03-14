package fi.onion.bgtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import fi.onion.dao.HandDAO;
import fi.onion.dao.PlayerDAO;
import fi.onion.model.Hand;
import fi.onion.model.HandHoleCards;
import fi.onion.model.Player;
import fi.onion.model.PlayerAction;
import fi.onion.util.AppContext;
import fi.onion.util.Constant;

public class ParseLogTask implements Runnable {
	MultipartFile file;
	
	public ParseLogTask(MultipartFile file) {
		this.file = file;
	}
	
	@Override
	public void run() {
		try {
			InputStream inputStream;
			inputStream = file.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			ArrayList<Hand> hand_list = new ArrayList<Hand>();
			ArrayList<Player> player_list = new ArrayList<Player>();
			ArrayList<HandHoleCards> card_list = new ArrayList<HandHoleCards>();
			ArrayList<PlayerAction> action_list = new ArrayList<PlayerAction>();
			
			int step = Constant.STEP0;
			
			String line;
			Hand currentHand = null;
			int cardroom = 0;
			
			ApplicationContext context = AppContext.getApplicationContext();
			PlayerDAO playerDAO = (PlayerDAO) context.getBean("playerDAO");
			HandDAO handDAO = (HandDAO) context.getBean("handDAO");
			
			String button_seat_nro = "";
			String dealer = "";
			int actionSequence = 0;
			while((line = bufferedReader.readLine()) != null) {
				
				/**
				 * PokerStars parsing
				 */
				if(line.startsWith("***********")) {
					if(currentHand!=null) {
						hand_list.add(currentHand);
					}
					step = Constant.STEP1;
					button_seat_nro = "";
					dealer = "";
					actionSequence = 0;
					currentHand = null;
					currentHand = new Hand();
					currentHand.setCardroom(cardroom);
					currentHand.setId(handDAO.getNewHandId()+hand_list.size());
					
				} else if(line.startsWith("*** HOLE CARDS ***")) {
					step = Constant.STEP2;
				} else if(line.startsWith("*** FLOP ***")) {
					step = Constant.STEP3;
				} else if(line.startsWith("*** TURN ***")) {
					step = Constant.STEP4;
				} else if(line.startsWith("*** RIVER ***")) {
					step = Constant.STEP5;
				} else if(line.startsWith("*** SHOW DOWN ***")) {
					step = Constant.STEP6;
				} else if(line.startsWith("*** SUMMARY ***")) {
					step = Constant.STEP7;
				}
				
				if(step==Constant.STEP1) {
					if(line.startsWith("PokerStars")) {
						String hand_number = line.substring((line.indexOf("#")+1), line.indexOf(":"));
						currentHand.setCardroomHand(hand_number);
						String[] splitted = line.split(",");
						String gametype = splitted[1].substring(1, splitted[1].indexOf("-", (splitted[1].indexOf("-")+1)));
						currentHand.setGametype(gametype);
						
						String[] splittedLine = line.split("-");
						String dateString = splittedLine[splittedLine.length-1];
						dateString = dateString.replace("ET", "");
						dateString = dateString.trim();
						DateFormat df = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
						try {
							Date result = df.parse(dateString);
							currentHand.setDate(result);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
					} else if(line.contains(" is the button")) {
						button_seat_nro = line.substring(line.indexOf(" is the button")-1, line.indexOf(" is the button"));
					} else if(line.startsWith("Seat ")) {
						Player player = new Player();
						//player.setId(player_list.size());
						player.setName(line.substring(line.indexOf(":")+2, (line.indexOf("(")-1)));
						if(!isDuplicatePlayer(player_list,player)) {
							player_list.add(player);
						}
						
						if(line.substring(5, 6).equals(button_seat_nro)) {
							dealer = player.getName();
						}
						
					} else if(line.contains("posts small blind")) {
						/**
						 * TÄSSÄ VAIHEESSA SULLA ON KAIKKI KÄDEN PELAAJAT, TARKASTA LÖYTYYKÖ TIETOKANNASTA + ANNA ID:T, PALAUTA TÄNNE
						 */
						playerDAO.addNewPlayersAndReturnAllWithCorrectIds(player_list);
						
						String sb_player = line.substring(0, line.indexOf(":"));
						currentHand.setSB(findIdByName(sb_player,player_list));
						
						currentHand.setDealer(findIdByName(dealer,player_list));
					} else if(line.contains("posts big blind")) {
						String bb_player = line.substring(0, line.indexOf(":"));
						currentHand.setBB(findIdByName(bb_player,player_list));
					}
					
				} else if(step==Constant.STEP2) {
					/**
						System.out.println("** hand "+currentHand.getCardroomHand() +" **");
						System.out.println("Dealer: "+playerDAO.findByPlayerId(currentHand.getDealer()).getName());
						System.out.println("SB: "+playerDAO.findByPlayerId(currentHand.getSmallBlindPlayer()).getName());
						System.out.println("BB: "+playerDAO.findByPlayerId(currentHand.getBigBlindPlayer()).getName());
					*/
					
					if(line.contains("Dealt to ")) {
						// PRIVATE HOLE CARDS
						String cardsString = line.substring(line.indexOf("[")+1, line.indexOf("]"));
						//System.out.println("hole cards : " + cardsString);
						HandHoleCards cards = new HandHoleCards();
						cards.setHandNumber(currentHand.getId());
						cards.setHole1(cardsString.split(" ")[0]);
						cards.setHole2(cardsString.split(" ")[1]);
						//System.out.println("hole cards : " + cards.getHoleCards());
						String player = line.substring(9,line.indexOf("[")-1);
						//System.out.println(player+"--");
						cards.setPlayerId(findIdByName(player,player_list));
						// this hand is shown only to the real player, not everyone
						// publicity can/should be changed in step 6
						cards.setPublic(false);
						card_list.add(cards);
						
					} else if(line.contains(":") && !line.contains("\"") && !line.contains("doesn't show")) {
						// this is preflop action
						action_list = addAction(line,0,actionSequence,currentHand,player_list,action_list);
						actionSequence++;
					} 
					
					String winner = checkForWinner(line);
					if(winner!=null) {
						PlayerAction winningAction = new PlayerAction();
						winningAction.setActionId(4);
						winningAction.setActionSeq(actionSequence);
						winningAction.setHandId(currentHand.getId());
						winningAction.setPhaseId(0);
						winningAction.setPlayerId(findIdByName(winner,player_list));
						float amount = 0;
						String amountString = line.substring(line.indexOf(" collected ")+" collected ".length()).replace(" from pot", "").trim();
						amount = Float.valueOf(amountString);
						winningAction.setAmount(amount);	
						actionSequence++;
						action_list.add(winningAction);
					}
					
				} else if(step==Constant.STEP3) {
					if(line.contains(":") && !line.contains("\"") && !line.contains("doesn't show")) {
						// this is action after flop
						action_list = addAction(line,1,actionSequence,currentHand,player_list,action_list);
						actionSequence++;
					} 
					
					String winner = checkForWinner(line);
					if(winner!=null) {
						PlayerAction winningAction = new PlayerAction();
						winningAction.setActionId(4);
						winningAction.setActionSeq(actionSequence);
						winningAction.setHandId(currentHand.getId());
						winningAction.setPhaseId(1);
						winningAction.setPlayerId(findIdByName(winner,player_list));
						float amount = 0;
						String amountString = line.substring(line.indexOf(" collected ")+" collected ".length()).replace(" from pot", "").trim();
						amount = Float.valueOf(amountString);
						winningAction.setAmount(amount);	
						actionSequence++;
						action_list.add(winningAction);
					}
					
				} else if(step==Constant.STEP4) {
					if(line.contains(":") && !line.contains("\"") && !line.contains("doesn't show")) {
						// this is action after turn
						action_list = addAction(line,2,actionSequence,currentHand,player_list,action_list);
						actionSequence++;
					} 
					
					String winner = checkForWinner(line);
					if(winner!=null) {
						PlayerAction winningAction = new PlayerAction();
						winningAction.setActionId(4);
						winningAction.setActionSeq(actionSequence);
						winningAction.setHandId(currentHand.getId());
						winningAction.setPhaseId(2);
						winningAction.setPlayerId(findIdByName(winner,player_list));
						float amount = 0;
						String amountString = line.substring(line.indexOf(" collected ")+" collected ".length()).replace(" from pot", "").trim();
						amount = Float.valueOf(amountString);
						winningAction.setAmount(amount);	
						actionSequence++;
						action_list.add(winningAction);
					}
					
				} else if(step==Constant.STEP5) {
					if(line.contains(":") && !line.contains("\"") && !line.contains("doesn't show")) {
						// this is action after river
						action_list = addAction(line,3,actionSequence,currentHand,player_list,action_list);
						actionSequence++;
					} 

					String winner = checkForWinner(line);
					if(winner!=null) {
						PlayerAction winningAction = new PlayerAction();
						winningAction.setActionId(4);
						winningAction.setActionSeq(actionSequence);
						winningAction.setHandId(currentHand.getId());
						winningAction.setPhaseId(3);
						winningAction.setPlayerId(findIdByName(winner,player_list));
						float amount = 0;
						String amountString = line.substring(line.indexOf(" collected ")+" collected ".length()).replace(" from pot", "").trim();
						amount = Float.valueOf(amountString);
						winningAction.setAmount(amount);	
						actionSequence++;
						action_list.add(winningAction);
					}
					
				} else if(step==Constant.STEP6) {
					if(line.contains("shows [")) {
						String playerName = line.substring(0, line.indexOf(":"));
						
						// PUBLIC HOLE CARDS
						String cardsString = line.substring(line.indexOf("[")+1, line.indexOf("]"));
						//System.out.println("hole cards : " + cardsString);
						HandHoleCards cards = new HandHoleCards();
						cards.setHandNumber(currentHand.getId());
						cards.setHole1(cardsString.split(" ")[0]);
						cards.setHole2(cardsString.split(" ")[1]);
						//System.out.println("hole cards : " + cards.getHoleCards());
						//System.out.println(player+"--");
						cards.setPlayerId(findIdByName(playerName,player_list));
						// this hand is shown only to the real player, not everyone
						// publicity can/should be changed in step 6
						cards.setPublic(true);
						
						// if duplicates --> change existing publicity to true
						boolean duplicate = false;
						for(int i=0; i<card_list.size();i++) {
							if(card_list.get(i).getHandNumber()==cards.getHandNumber() && card_list.get(i).getPlayerId()==cards.getPlayerId()) {
								duplicate = true;
								card_list.get(i).setPublic(true);
							}
						}
						
						if(!duplicate) {
							card_list.add(cards);
						}
					}
					
					String winner = checkForWinner(line);
					if(winner!=null) {
						PlayerAction winningAction = new PlayerAction();
						winningAction.setActionId(4);
						winningAction.setActionSeq(actionSequence);
						winningAction.setHandId(currentHand.getId());
						winningAction.setPhaseId(4);
						winningAction.setPlayerId(findIdByName(winner,player_list));
						
						float amount = 0;
						String amountString = line.substring(line.indexOf(" collected ")+" collected ".length()).replace(" from pot", "").trim();
						amount = Float.valueOf(amountString);
						winningAction.setAmount(amount);	
						
						actionSequence++;
						action_list.add(winningAction);
					}
					
				} else if(step==Constant.STEP7) {
					if(line.contains("Board")) {
						String boardString = line.substring(line.indexOf("[")+1, line.indexOf("]"));
						String[] boardCards = boardString.split(" ");
						if(boardCards.length==5) {
							currentHand.setFlop1(boardCards[0]);
							currentHand.setFlop2(boardCards[1]);
							currentHand.setFlop3(boardCards[2]);
							currentHand.setTurn(boardCards[3]);
							currentHand.setRiver(boardCards[4]);
						} else if(boardCards.length==4) {
							currentHand.setFlop1(boardCards[0]);
							currentHand.setFlop2(boardCards[1]);
							currentHand.setFlop3(boardCards[2]);
							currentHand.setTurn(boardCards[3]);
							currentHand.setRiver(null);
						} else if(boardCards.length==3) {
							currentHand.setFlop1(boardCards[0]);
							currentHand.setFlop2(boardCards[1]);
							currentHand.setFlop3(boardCards[2]);
							currentHand.setTurn(null);
							currentHand.setRiver(null);
						} else {
							currentHand.setFlop1(null);
							currentHand.setFlop2(null);
							currentHand.setFlop3(null);
							currentHand.setTurn(null);
							currentHand.setRiver(null);
						}
					}
				}
				
				

			}
			// we add the last hand that was not added because new hand did not appear...
			hand_list.add(currentHand);
			
			/*
			for(int i = 0; i < card_list.size(); i++) {
				System.out.println(card_list.get(i).getHoleCards() + " " + card_list.get(i).isPublic());
			}
			*/
			
			inputStream.close();
			bufferedReader.close();
			
			/*
			// created lists
			ArrayList<Hand> hand_list = new ArrayList<Hand>();
			ArrayList<Player> player_list = new ArrayList<Player>();
			ArrayList<HandHoleCards> card_list = new ArrayList<HandHoleCards>();
			ArrayList<PlayerAction> action_list = new ArrayList<PlayerAction>();
			*/
			
			/*
			// These are used to debug that the parser works as intended
			System.out.println("*** PLAYERS ***");
			for(int i=0;i<player_list.size();i++) {
				System.out.println(player_list.get(i).toString());
			}
			
			System.out.println("\n");
			System.out.println("*** HANDS ***");
			for(int i=0;i<hand_list.size();i++) {
				System.out.println(hand_list.get(i).toString());
			}
			
			System.out.println("\n");
			System.out.println("*** HOLE CARDS ***");
			for(int i=0;i<card_list.size();i++) {
				System.out.println(card_list.get(i).toString());
			}
			
			System.out.println("\n");
			System.out.println("*** ACTIONS ***");
			for(int i=0;i<action_list.size();i++) {
				System.out.println(action_list.get(i).toString());
			}
			*/
			
			// add all info to db
			handDAO.addHands(hand_list);
			handDAO.addHoleCards(card_list);
			handDAO.addPlayerActions(action_list);
			
			System.out.println("** parsing finished **");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String checkForWinner(String line) {
		if(line.contains(" collected ") && line.contains("from pot")) {
			String winnerName = line.substring(0, line.indexOf(" collected "));
			return winnerName;
		} else {
			return null;
		}
	}
	
	private ArrayList<PlayerAction> addAction(String line, int actionPhase, int actionSequence, Hand currentHand,ArrayList<Player> player_list,ArrayList<PlayerAction> action_list) {
		PlayerAction action = new PlayerAction();
		action.setActionSeq(actionSequence);
		action.setHandId(currentHand.getId());
		action.setPlayerId(findIdByName(line.substring(0, line.indexOf(":")),player_list));
		//preflop phase id = 0
		action.setPhaseId(actionPhase);

		String actionString = line.split(":")[1];
		if(actionString.contains("fold")) {
			action.setActionId(0);
		} else if(actionString.contains("call")) {
			action.setActionId(1);
			float amount = 0;
			if(actionString.contains(" and is all-in")) {
				action.setAllIn(true);
			} else {
				action.setAllIn(false);
			}
			String amountString = actionString.replace(" calls ", "").replace(" and is all-in", "").trim();
			amount = Float.valueOf(amountString);
			action.setAmount(amount);
			
		} else if(actionString.contains("check")) {
			action.setActionId(2);
		} else if(actionString.contains("bet") || actionString.contains("raise")) {
			action.setActionId(3);
			if(actionString.contains(" and is all-in")) {
				action.setAllIn(true);
			} else {
				action.setAllIn(false);
			}
			
			if(actionString.contains("bets")) {
				float amount = 0;
				String amountString = actionString.replace(" bets ", "").replace(" and is all-in", "").trim();
				amount = Float.valueOf(amountString);
				action.setAmount(amount);	
			} else if(actionString.contains("raises")) {
				float amount = 0;
				String amountString = actionString.substring(actionString.indexOf(" to ")+" to ".length()).replace(" and is all-in", "").trim();
				amount = Float.valueOf(amountString);
				action.setAmount(amount);	
			}
			
		} else {
			action.setActionId(-1);
		}
		
		action_list.add(action);
		return action_list;
		//System.out.println(action.getHandId()+ ": " + action.getPlayerId() + " does " + action.getActionId() + " and is seq nro " + action.getActionSeq());
	}
	
	private int findIdByName(String name, ArrayList<Player> player_list) {
		for(int i=0; i<player_list.size();i++) {
			if(player_list.get(i).getName().equals(name)) {
				return player_list.get(i).getId();
			}
		}
		
		return -1;
	}
	
	private boolean isDuplicatePlayer(ArrayList<Player> player_list, Player player) {
		for(int i = 0; i < player_list.size(); i++) {
			if(player_list.get(i).getName().equals(player.getName())) {
				return true;
			}
		}
		return false;
	}
	
	
}
