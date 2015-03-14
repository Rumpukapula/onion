package fi.onion.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fi.onion.dao.UserDAO;
import fi.onion.util.AppContext;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDAO userDAO = (UserDAO) AppContext.getApplicationContext().getBean("userDAO");
		fi.onion.model.User user = (fi.onion.model.User) userDAO.loadUserByUsername(username);
		
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		
		return new org.springframework.security.core.userdetails.User(
				user.getName(),
				user.getPassword(),
				enabled,
				accountNonExpired,
				credentialsNonExpired,
				accountNonLocked,
				getAuthorities(user));
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(fi.onion.model.User user) {
		List<GrantedAuthority> authList = getRoles(user);
		return authList;
	}
	
	public static List<GrantedAuthority> getRoles(fi.onion.model.User user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(String role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}
