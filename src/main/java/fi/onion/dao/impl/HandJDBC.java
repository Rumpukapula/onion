package fi.onion.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import fi.onion.dao.HandDAO;
import fi.onion.model.Hand;
import fi.onion.model.HandHoleCards;
import fi.onion.model.PlayerAction;

public class HandJDBC implements HandDAO {

	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public int getNewHandId() {
		String sql = "SELECT MAX(id) as id FROM onion.hand";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			int newId = -1;
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				newId = rs.getInt("id")+1;
			}
			
			rs.close();
			ps.close();
			
			return newId;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					
				}
			}
		}
	}

	@Override
	public void addHands(ArrayList<Hand> handList) {
		String sqlHandExists = "SELECT id FROM onion.hand WHERE cardroom=? AND cardroom_hand_number=?";
		String sqlInsertHand = "INSERT INTO onion.hand VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement(sqlHandExists);
			PreparedStatement psInsert = conn.prepareStatement(sqlInsertHand);
			for(int i = 0; i < handList.size(); i++) {
				Hand hand = handList.get(i);
				ps.setInt(1, hand.getCardroom());
				ps.setString(2, hand.getCardroomHand());
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					Timestamp sqlDate = new Timestamp(hand.getDate().getTime());
					
					psInsert.setInt(1, hand.getId());
					psInsert.setInt(2, hand.getCardroom());
					psInsert.setString(3, hand.getCardroomHand());
					psInsert.setString(4, hand.getGametype());
					psInsert.setInt(5, hand.getDealer());
					psInsert.setInt(6, hand.getSmallBlindPlayer());
					psInsert.setInt(7, hand.getBigBlindPlayer());
					psInsert.setString(8, hand.getFlop1());
					psInsert.setString(9, hand.getFlop2());
					psInsert.setString(10, hand.getFlop3());
					psInsert.setString(11, hand.getTurn());
					psInsert.setString(12, hand.getRiver());
					psInsert.setTimestamp(13, sqlDate);
					psInsert.executeUpdate();
				}
			}
			conn.commit();
			ps.close();
		} catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					
				}
			}
		}
		
	}

	@Override
	public void addHoleCards(ArrayList<HandHoleCards> cardList) {
		String sqlMaxId = "SELECT MAX(id) as max FROM onion.hand_hole_cards";
		String sqlCardsExist = "SELECT id FROM onion.hand_hole_cards WHERE hand_number=? AND player_id=?";
		String sqlInsert = "INSERT INTO onion.hand_hole_cards VALUES (?,?,?,?,?,?)";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement psMax = conn.prepareStatement(sqlMaxId);
			PreparedStatement ps = conn.prepareStatement(sqlCardsExist);
			PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
			
			ResultSet rsMax = psMax.executeQuery();
			int nextId = 0;
			if(rsMax.next()) {
				nextId = rsMax.getInt("max")+1;
			}
			
			for(int i = 0; i < cardList.size(); i++) {
				HandHoleCards cards = cardList.get(i);
				
				ps.setInt(1, cards.getHandNumber());
				ps.setInt(2, cards.getPlayerId());
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					psInsert.setInt(1, nextId+i);
					psInsert.setInt(2, cards.getHandNumber());
					psInsert.setInt(3, cards.getPlayerId());
					psInsert.setString(4, cards.getHole1());
					psInsert.setString(5, cards.getHole2());
					if(cards.isPublic()) {
						psInsert.setInt(6, 1);
					} else {
						psInsert.setInt(6, 0);
					}

					psInsert.executeUpdate();
				}
			}
			conn.commit();
			ps.close();
		} catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					
				}
			}
		}
		
	}

	@Override
	public void addPlayerActions(ArrayList<PlayerAction> actionList) {
		String sqlMaxId = "SELECT MAX(id) as max FROM onion.player_action";
		String sqlActionExists = "SELECT id FROM onion.player_action WHERE hand_id=? AND action_sequence=?";
		String sqlInsert = "INSERT INTO onion.player_action VALUES (?,?,?,?,?,?,?,?)";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			
			PreparedStatement psMax = conn.prepareStatement(sqlMaxId);
			PreparedStatement ps = conn.prepareStatement(sqlActionExists);
			PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
			
			ResultSet rsMax = psMax.executeQuery();
			int nextId = 0;
			if(rsMax.next()) {
				nextId = rsMax.getInt("max")+1;
			}
			
			for(int i = 0; i < actionList.size(); i++) {
				PlayerAction action = actionList.get(i);
				
				ps.setInt(1, action.getHandId());
				ps.setInt(2, action.getActionSeq());
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					psInsert.setInt(1, nextId+i);
					psInsert.setInt(2, action.getHandId());
					psInsert.setInt(3, action.getPhaseId());
					psInsert.setInt(4, action.getActionSeq());
					psInsert.setInt(5, action.getPlayerId());
					psInsert.setInt(6, action.getActionId());
					psInsert.setFloat(7, action.getAmount());
					if(action.isAllIn()) {
						psInsert.setInt(8, 1);
					} else {
						psInsert.setInt(8, 0);
					}
					psInsert.executeUpdate();
				}
			}
			conn.commit();
			ps.close();
		} catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					
				}
			}
		}
		
	}
	
	@Override
	public List<Hand> getAllHands() {
		String sql = "SELECT id,cardroom,cardroom_hand_number,gametype,dealer_player,small_blind_player,big_blind_player,card_flop_1,card_flop_2,card_flop_3,card_turn,card_river,hand_date FROM onion.hand";
		Connection conn = null;
		
		ArrayList<Hand> hands = new ArrayList<Hand>();
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Hand hand = new Hand();
				hand.setId(rs.getInt("id"));
				hand.setCardroom(rs.getInt("cardroom"));
				hand.setCardroomHand(rs.getString("cardroom_hand_number"));
				hand.setGametype(rs.getString("gametype"));
				hand.setDealer(rs.getInt("dealer_player"));
				hand.setSB(rs.getInt("small_blind_player"));
				hand.setBB(rs.getInt("big_blind_player"));
				hand.setFlop1(rs.getString("card_flop_1"));
				hand.setFlop2(rs.getString("card_flop_2"));
				hand.setFlop3(rs.getString("card_flop_3"));
				hand.setTurn(rs.getString("card_turn"));
				hand.setRiver(rs.getString("card_river"));
				hand.setDate(rs.getDate("hand_date"));
				hands.add(hand);
			}
			
			rs.close();
			ps.close();
			return hands;
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					
				}
			}
		}
	}

	@Override
	public List<PlayerAction> getAllActions() {
		String sql = "SELECT id,hand_id,phase_id,action_sequence,player_id,action_id,amount,is_all_in FROM onion.player_action";
		Connection conn = null;
		
		ArrayList<PlayerAction> actions = new ArrayList<PlayerAction>();
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PlayerAction action = new PlayerAction();
				action.setId(rs.getInt("id"));
				action.setHandId(rs.getInt("hand_id"));
				action.setPhaseId(rs.getInt("phase_id"));
				action.setActionSeq(rs.getInt("action_sequence"));
				action.setPlayerId(rs.getInt("player_id"));
				action.setActionId(rs.getInt("action_id"));
				action.setAmount(rs.getFloat("amount"));
				if(rs.getInt("is_all_in")==1) {
					action.setAllIn(true);
				} else {
					action.setAllIn(false);
				}
				
				actions.add(action);
			}
			
			rs.close();
			ps.close();
			return actions;
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					
				}
			}
		}
	}
}
