package fi.onion.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.onion.dao.PlayerDAO;
import fi.onion.model.Hand;
import fi.onion.model.HandHoleCards;
import fi.onion.model.Player;
import fi.onion.model.PlayerAction;
import fi.onion.model.PlayerStats;
import fi.onion.util.AppContext;

@Controller
@RequestMapping("/players")
public class PlayerController {

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Player> list() {
		PlayerDAO playerDAO = (PlayerDAO) AppContext.getApplicationContext().getBean("playerDAO");
		List<Player> players = playerDAO.getAllPlayers();
		return players;
	}
	
	@RequestMapping(value="/{playerId}", method = RequestMethod.GET)
	public @ResponseBody PlayerStats find(@PathVariable("playerId") int playerId) {
		PlayerDAO playerDAO = (PlayerDAO) AppContext.getApplicationContext().getBean("playerDAO");
		PlayerStats stats = new PlayerStats();
		
		Player player = playerDAO.findByPlayerId(playerId);
		List<Hand> hands = playerDAO.getAllHandsByPlayer(playerId);
		List<HandHoleCards> holeCards = null;
		List<PlayerAction> actions = null;
		
		stats.setPlayer(player);
		stats.setHands(hands);
		stats.setHandHoleCards(holeCards);
		stats.setActions(actions);
		
		return stats;
	}
}
