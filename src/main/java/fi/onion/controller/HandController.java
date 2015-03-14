package fi.onion.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.onion.dao.HandDAO;
import fi.onion.model.Hand;
import fi.onion.model.PlayerAction;
import fi.onion.util.AppContext;

@Controller
public class HandController {

	@RequestMapping(value="/hands", method = RequestMethod.GET)
	public @ResponseBody List<Hand> handList() {
		HandDAO handDAO = (HandDAO) AppContext.getApplicationContext().getBean("handDAO");
		List<Hand> hands = handDAO.getAllHands();

		return hands;
	}
	
	@RequestMapping(value="/actions", method = RequestMethod.GET)
	public @ResponseBody List<PlayerAction> actionList() {
		HandDAO handDAO = (HandDAO) AppContext.getApplicationContext().getBean("handDAO");
		List<PlayerAction> actions = handDAO.getAllActions();
		
		return actions;
	}
}
