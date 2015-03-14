package fi.onion.dao;

import java.util.ArrayList;
import java.util.List;

import fi.onion.model.Hand;
import fi.onion.model.HandHoleCards;
import fi.onion.model.PlayerAction;

public interface HandDAO {
	public int getNewHandId();
	public void addHands(ArrayList<Hand> handList);
	public void addHoleCards(ArrayList<HandHoleCards> cardList);
	public void addPlayerActions(ArrayList<PlayerAction> actionList);
	
	public List<Hand> getAllHands();
	public List<PlayerAction> getAllActions();
}
