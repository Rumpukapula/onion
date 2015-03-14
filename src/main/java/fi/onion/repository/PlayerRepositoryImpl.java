package fi.onion.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import fi.onion.model.Player;

@Component
class PlayerRepositoryImpl implements PlayerRepository {
	private HashMap<Integer,Player> players = new HashMap<Integer,Player>();
	
	@Override
	public Player findById(int id) {
		return players.get(id);
	}

	@Override
	public List<Player> findAll() {
		List<Player> playerList = new ArrayList<Player>(players.values());
		Player testPlayer = new Player();
		testPlayer.setId(0);
		testPlayer.setName("Test Player");
		playerList.add(testPlayer);
		return playerList;
	}

}
