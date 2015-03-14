package fi.onion.repository;

import java.util.List;

import fi.onion.model.Player;

public interface PlayerRepository {
	Player findById(int id);
	List<Player> findAll();
}
