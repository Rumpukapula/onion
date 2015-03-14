package fi.onion.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fi.onion.dao.UserDAO;
import fi.onion.model.User;

public class UserJDBC implements UserDAO {
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.findByName(username);
		if(null==user) {
			throw new UsernameNotFoundException("The user with name " + username + " was not found");
		}
		return user;
	}

	@Override
	public User findByName(String name) {
		String sql = "SELECT username,password,enabled FROM onion.user WHERE username=?";
		String roleSql = "SELECT ROLE FROM onion.user_role WHERE username=?";
		Connection conn = null;
		User user = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				user = new User(rs.getString("username"),rs.getString("password"));
				
				// get user roles
				PreparedStatement ps2 = conn.prepareStatement(roleSql);
				ps2.setString(1, name);
				ResultSet rs2 = ps2.executeQuery();
				Set<String> roles = new HashSet<String>();
				while(rs2.next()) {
					roles.add(rs2.getString("ROLE"));
				}
				user.setRoles(roles);
				
				rs2.close();
				ps2.close();
			}
			
			rs.close();
			ps.close();
			return user;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					
				}
			}
		}
	}

}
