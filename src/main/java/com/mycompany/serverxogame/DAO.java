/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.serverxogame;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author amr04
 */
public class DAO {

    public DAO() {
    }

    private static Connection connect;

    private static void ensureConnection() throws SQLException {
        if (connect == null || connect.isClosed()) {
            DriverManager.registerDriver(new ClientDriver());
            connect = DriverManager.getConnection("jdbc:derby://localhost:1527/User", "Team4", "team4");
        }
    }

    public static int inser(User user) throws SQLException {
        ensureConnection();
        PreparedStatement pst = connect.prepareStatement("INSERT INTO TEAM4.USERS (name, gmail, password, score, state) VALUES (?, ?, ?, ?, ?)");
        pst.setString(1, user.getName());
        pst.setString(2, user.getGmail());
        pst.setString(3, user.getPassword());
        pst.setInt(4, user.getScore());
        pst.setString(5, user.getState());

        return pst.executeUpdate();
    }

    public static User login(String email, String password) throws SQLException {
        ensureConnection();
        User user = null;

        PreparedStatement ps = connect.prepareStatement("SELECT * FROM TEAM4.USERS WHERE gmail = ? AND password = ?");
        try {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setName(rs.getString("name"));
                user.setGmail(rs.getString("gmail"));
                user.setScore(rs.getInt("score"));
                user.setState("Online");
                updateState(email, "Online");
            }
        } catch (SQLException ex) {
            System.getLogger(DAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return user;

    }

    public static void updateState(String email, String status) throws SQLException {
        ensureConnection();
        PreparedStatement ps = connect.prepareStatement("UPDATE TEAM4.USERS SET state=? WHERE gmail=?");
        ps.setString(1, email);
        ps.setString(2, status);
        ps.executeUpdate();
    }

    public static boolean isPlayerAlreadyLoggedIn(String gmail) throws SQLException {
        ensureConnection();
        PreparedStatement ps = connect.prepareStatement("SELECT * FROM TEAM4.USERS WHERE gmail = ? AND state = 'Online' ");
        ps.setString(1, gmail);
        ResultSet rs = ps.executeQuery();
        return rs.next();

    }

    public static int getPlayersCountByStatus(String status) throws SQLException {
        ensureConnection();
        int count = 0;
        PreparedStatement ps = connect.prepareStatement("SELECT COUNT(*) FROM TEAM4.USERS WHERE state=?");
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            count = rs.getInt(1);
        }
        return count;
    }

    public static void updateScore(int userId, int score) throws SQLException {
        ensureConnection();
        PreparedStatement ps = connect.prepareStatement(
                "UPDATE TEAM4.USERS SET score = ? WHERE id = ?"
        );
        ps.setInt(1, score);
        ps.setInt(2, userId);
        ps.executeUpdate();
    }

    public static ArrayList<User> getTopPlayers() throws SQLException {
        ensureConnection();
        ArrayList<User> topPlayers = new ArrayList<>();
        PreparedStatement ps = connect.prepareStatement(" SELECT name, gmail, score, state FROM TEAM4.USERS ORDER BY score DESC FETCH FIRST 10 ROWS ONLY");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setName(rs.getString("name"));
            user.setGmail(rs.getString("gmail"));
            user.setScore(rs.getInt("score"));
            user.setState(rs.getString("state"));

            topPlayers.add(user);
        }
        return topPlayers;
    }

    public static ArrayList<User> getAvailablePlayers() throws SQLException {
        ensureConnection();
        ArrayList<User> availablePlayers = new ArrayList<>();
        PreparedStatement ps = connect.prepareStatement(
                "SELECT * FROM TEAM4.USERS WHERE state IN ('Online', 'Available') ORDER BY score DESC"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setName(rs.getString("name"));
            user.setGmail(rs.getString("gmail"));
            user.setScore(rs.getInt("score"));
            user.setState(rs.getString("state"));

            availablePlayers.add(user);
        }
        return availablePlayers;
    }

    public static void InsertGameResult(int user1_id, int user2_id, int winner_id, Date game_date) throws SQLException {
        ensureConnection();
        PreparedStatement pr = connect.prepareStatement("INSERT INTO TEAM4.GAME (user1_id, user2_id, winner_id, game_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)");
        pr.setInt(1, user1_id);
        pr.setInt(2, user2_id);
        pr.setInt(3, winner_id);
        pr.executeUpdate();
    }

    public static ArrayList<Game> getPlayerHistory(String email) throws SQLException {
        ensureConnection();
        ArrayList<Game> games = new ArrayList<>();

        PreparedStatement psUser = connect.prepareStatement(
                "SELECT id FROM TEAM4.USERS WHERE gmail = ?"
        );
        psUser.setString(1, email);
        ResultSet rsUser = psUser.executeQuery();

        if (!rsUser.next()) {
            return games;
        }

        int userId = rsUser.getInt("id");

        PreparedStatement psGames = connect.prepareStatement(
                "SELECT * FROM TEAM4.GAME WHERE user1_id = ? OR user2_id = ? ORDER BY game_date DESC"
        );
        psGames.setInt(1, userId);
        psGames.setInt(2, userId);

        ResultSet rsGames = psGames.executeQuery();

        while (rsGames.next()) {
            Game game = new Game();
            game.setUser1Id(rsGames.getInt("user1_id"));
            game.setUser2Id(rsGames.getInt("user2_id"));
            game.setWinnerId(rsGames.getInt("winner_id"));
            game.setGameDate(rsGames.getDate("game_date"));

            games.add(game);
        }

        return games;
    }

    

}
