package fi.onion.dao;

import org.springframework.security.core.userdetails.UserDetailsService;

import fi.onion.model.User;

public interface UserDAO extends UserDetailsService {
	User findByName(String name);
}
