package fi.onion.model;

import java.util.List;

public class PlayerStats {
	public Player player;
	public List<Hand> hands;
	public List<HandHoleCards> handHoleCards;
	public List<PlayerAction> actions;
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void setHands(List<Hand> hands) {
		this.hands = hands;
	}
	
	public List<Hand> getHands() {
		return this.hands;
	}
	
	public List<HandHoleCards> getHandHoleCards() {
		return this.handHoleCards;
	}
	
	public void setHandHoleCards(List<HandHoleCards> handHoleCards) {
		this.handHoleCards = handHoleCards;
	}
	
	public List<PlayerAction> getActions() {
		return this.actions;
	}
	
	public void setActions(List<PlayerAction> actions) {
		this.actions = actions;
	}
	
}
