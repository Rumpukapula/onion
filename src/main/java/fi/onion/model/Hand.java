package fi.onion.model;

import java.util.Date;

public class Hand {
	int id;
	int cardroom;
	String cardroom_hand_number;
	String gametype;
	int dealer_player;
	int small_blind_player;
	int big_blind_player;
	String card_flop1;
	String card_flop2;
	String card_flop3;
	String card_turn;
	String card_river;
	Date date;
	
	public Hand(int id, int cardroom, String handnro, String gametype, int dealer, int sb, int bb, String f1, String f2, String f3, String t, String r) {
		this.id = id;
		this.cardroom = cardroom;
		this.cardroom_hand_number = handnro;
		this.gametype = gametype;
		this.dealer_player = dealer;
		this.small_blind_player = sb;
		this.big_blind_player = bb;
		this.card_flop1 = f1;
		this.card_flop2 = f2;
		this.card_flop3 = f3;
		this.card_turn = t;
		this.card_river = r;
	}
	
	@Override
	public String toString() {
		return "Hand - id:"+this.id+" - cardroom:"+this.cardroom+" - cardoom hand:"+this.cardroom_hand_number+" - gametype:"+this.gametype+" - dealer:"+this.dealer_player+" - SB:"+this.small_blind_player+" - BB:"+this.big_blind_player+" - cards:"+this.card_flop1+" "+this.card_flop2+" "+this.card_flop3+" "+this.card_turn+" "+this.card_river;
	}
	
	public Hand() {
		
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return this.date;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setCardroom(int cardroom) {
		this.cardroom = cardroom;
	}
	
	public int getCardroom() {
		return this.cardroom;
	}
	
	public void setCardroomHand(String cardroom_hand_number) {
		this.cardroom_hand_number = cardroom_hand_number;
	}
	
	public String getCardroomHand() {
		return this.cardroom_hand_number;
	}
	
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	
	public String getGametype() {
		return this.gametype;
	}
	
	public void setDealer(int dealer_player) {
		this.dealer_player = dealer_player;
	}
	
	public void setSB(int small_blind_player) {
		this.small_blind_player = small_blind_player;
	}
	
	public void setBB(int big_blind_player) {
		this.big_blind_player = big_blind_player;
	}
	
	public void setFlop1(String card) {
		this.card_flop1 = card;
	}
	
	public String getFlop1() {
		return this.card_flop1;
	}
	
	public void setFlop2(String card) {
		this.card_flop2 = card;
	}
	
	public String getFlop2() {
		return this.card_flop2;
	}
	
	public void setFlop3(String card) {
		this.card_flop3 = card;
	}
	
	public String getFlop3() {
		return this.card_flop3;
	}
	
	public void setTurn(String card) {
		this.card_turn = card;
	}
	
	public String getTurn() {
		return this.card_turn;
	}
	
	public void setRiver(String card) {
		this.card_river = card;
	}
	
	public String getRiver() {
		return this.card_river;
	}
	
	public int getDealer() {
		return this.dealer_player;
	}
	
	public int getBigBlindPlayer() {
		return this.big_blind_player;
	}
	
	public int getSmallBlindPlayer() {
		return this.small_blind_player;
	}
}
