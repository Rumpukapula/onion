package fi.onion.dao;

import java.util.List;

import fi.onion.model.Hand;
import fi.onion.model.Player;

public interface PlayerDAO {
	public void insert(Player player);
	public Player findByPlayerId(int id);
	public List<Player> addNewPlayersAndReturnAllWithCorrectIds(List<Player> playerList);
	
	public List<Player> getAllPlayers();
	
	public List<Hand> getAllHandsByPlayer(int id);
}
