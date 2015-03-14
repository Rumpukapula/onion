package fi.onion.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import fi.onion.dao.PlayerDAO;
import fi.onion.model.Hand;
import fi.onion.model.Player;

public class PlayerJDBC implements PlayerDAO {

	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void insert(Player player) {
		String sql = "INSERT INTO onion.player " +
						"(id,nick) VALUES (?,?)";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getNewPlayerId());
			ps.setString(2, player.getName());
			ps.executeUpdate();
			
			conn.commit();
			ps.close();
		} catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
	
	public int getNewPlayerId() {
		String sql = "SELECT MAX(id) as id FROM onion.player";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			int newId = -1;
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				newId = rs.getInt("id")+1;
			}
			
			rs.close();
			ps.close();
			
			return newId;
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
	
	@Override
	public List<Hand> getAllHandsByPlayer(int id) {
		List<Hand> handList = new ArrayList<Hand>();
		
		String sql = "SELECT id, cardroom, cardroom_hand_number, gametype, dealer_player, small_blind_player, big_blind_player, card_flop_1, card_flop_2, card_flop_3, card_turn, card_river, hand_date "+ 
						" FROM onion.hand "+
						" WHERE id IN ( "+
						"	SELECT DISTINCT hand_id "+
						"	FROM onion.player_action "+
						"	WHERE player_id=? "+
						" ) ";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Hand hand = new Hand();
				hand.setId(rs.getInt("id"));
				hand.setCardroom(rs.getInt("cardroom"));
				hand.setCardroomHand(rs.getString("cardroom_hand_number"));
				hand.setGametype(rs.getString("gametype"));
				hand.setDealer(rs.getInt("dealer_player"));
				hand.setSB(rs.getInt("small_blind_player"));
				hand.setBB(rs.getInt("big_blind_player"));
				hand.setFlop1(rs.getString("card_flop_1"));
				hand.setFlop2(rs.getString("card_flop_2"));
				hand.setFlop3(rs.getString("card_flop_3"));
				hand.setTurn(rs.getString("card_turn"));
				hand.setRiver(rs.getString("card_turn"));
				Date date = new Date(rs.getTimestamp("hand_date").getTime());
				hand.setDate(date);
				
				handList.add(hand);
			}
			
			rs.close();
			ps.close();
			
			return handList;
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

	@Override
	public Player findByPlayerId(int id) {
		String sql = "SELECT id,nick FROM onion.player WHERE id=?";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			Player player = null;
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				player = new Player(rs.getInt("id"),rs.getString("nick"));
			}
			
			rs.close();
			ps.close();
			return player;
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

	@Override
	public List<Player> addNewPlayersAndReturnAllWithCorrectIds(List<Player> playerList) {
		String sql = "SELECT id FROM onion.player WHERE nick=?";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			for(int i = 0; i < playerList.size(); i++) {
				ps.setString(1, playerList.get(i).getName());
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					playerList.get(i).setId(rs.getInt("id"));
				} else {
					Player player = new Player(playerList.get(i).getName());
					insert(player);
					rs = ps.executeQuery();
					rs.next();
					playerList.get(i).setId(rs.getInt("id"));
				}
			}
			
			ps.close();
			
			return playerList;
			
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

	@Override
	public List<Player> getAllPlayers() {
		String sql = "SELECT id,nick FROM onion.player";
		Connection conn = null;
		
		ArrayList<Player> players = new ArrayList<Player>();
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Player player = new Player(rs.getInt("id"),rs.getString("nick"));
				players.add(player);
			}
			
			rs.close();
			ps.close();
			return players;
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
