package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Adiacenza;
import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams(Map<String, Team> idMap) {
		String sql = "SELECT DISTINCT m.HomeTeam AS team " + 
				"FROM matches AS m " + 
				"GROUP BY m.HomeTeam " + 
				"ORDER BY team";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team t = new Team(res.getString("team"));
				result.add(t);
				idMap.put(res.getString("team"), t);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Team> listTeams(Season season, Map<String, Team> idMap) {
		String sql = "SELECT DISTINCT m.HomeTeam AS team " + 
				"FROM matches AS m " +
				"WHERE m.Season = ? " +
				"GROUP BY m.HomeTeam " + 
				"ORDER BY team";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, season.getSeason());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team t = new Team(res.getString("team"));
				result.add(t);
				idMap.put(res.getString("team"), t);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Adiacenza> getAdiacenze(Map<String, Team> idMap) {
		String sql = "SELECT m1.HomeTeam AS id1, m2.HomeTeam AS id2, COUNT(*) AS peso " +
				"FROM matches AS m1, matches AS m2 " +
				"WHERE m1.HomeTeam = m2.AwayTeam AND m1.AwayTeam = m2.HomeTeam " +
				"GROUP BY m1.HomeTeam, m2.HomeTeam";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(idMap.containsKey(res.getString("id1")) && idMap.containsKey(res.getString("id2"))) {
					result.add(new Adiacenza(idMap.get(res.getString("id1")), idMap.get(res.getString("id2")), res.getInt("peso")));
				}
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Integer getPeso(Team t1, Team t2) {
		String sql = "SELECT COUNT(*) AS peso " + 
				"FROM matches AS m " + 
				"WHERE (HomeTeam = ? AND AwayTeam = ?) " + 
				"OR (HomeTeam = ? AND AwayTeam = ?)";
		
		Integer peso = 0;
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t1.getTeam());
			st.setString(2, t2.getTeam());
			st.setString(3, t2.getTeam());
			st.setString(4, t1.getTeam());
			ResultSet res = st.executeQuery();

			if(res.next())
				peso = res.getInt("peso");

			conn.close();
			return peso;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Match> getMatches(Season season, Map<String, Team> idMap) {
		String sql = "SELECT DATE, HomeTeam, AwayTeam, FTHG, FTAG " + 
				"FROM matches " + 
				"WHERE season = ?";
		List<Match> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, season.getSeason());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Match(res.getDate("DATE").toLocalDate(), idMap.get(res.getString("HomeTeam")),
						idMap.get(res.getString("AwayTeam")), res.getInt("FTHG"), res.getInt("FTAG")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

