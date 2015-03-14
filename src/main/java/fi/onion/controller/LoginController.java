package fi.onion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView loginForm() {
		return new ModelAndView("login");
	}
	
	@RequestMapping(value="/login-error", method=RequestMethod.GET)
	public ModelAndView invalidLogin() {
		return new ModelAndView("login-error");
	}
}
