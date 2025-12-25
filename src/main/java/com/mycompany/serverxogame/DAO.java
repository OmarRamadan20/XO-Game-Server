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
            connect = DriverManager.getConnection("jdbc:derby://localhost:1527/TEAM4", "Team4", "team4");
        }
    }

    public static int SignUp(User user) throws SQLException {
        ensureConnection();
        PreparedStatement pst = connect.prepareStatement("INSERT INTO TEAM4.USERS (name, gmail, password, score, state) VALUES (?, ?, ?, ?, ?)");
        pst.setString(1, user.getName());
        pst.setString(2, user.getGmail());
        pst.setString(3, user.getPassword());
        pst.setInt(4, user.getScore());
        pst.setString(5, user.getState());
        updateState(user.getGmail(), "onlineAvailable");

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
                user.setState("onlineAvailable");
                updateState(email, "onlineAvailable");
            }
        } catch (SQLException ex) {
            System.getLogger(DAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return user;

    }

    public static void updateState(String email, String status) throws SQLException {
        ensureConnection();
        PreparedStatement ps = connect.prepareStatement(
                "UPDATE TEAM4.USERS SET state=? WHERE gmail=?"
        );
        ps.setString(1, status);
        ps.setString(2, email);
        ps.executeUpdate();
    }

    public static int getScore(String gmail) throws SQLException {
        ensureConnection();

        PreparedStatement ps = connect.prepareStatement(
                "SELECT score FROM TEAM4.USERS WHERE gmail = ?"
        );

        ps.setString(1, gmail);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("score");
        }

        return 0;
    }

    public static boolean isPlayerAlreadyLoggedIn(String gmail) throws SQLException {
        ensureConnection();
        PreparedStatement ps = connect.prepareStatement("SELECT * FROM TEAM4.USERS WHERE gmail = ? AND state = 'onlineGame' ");
        ps.setString(1, gmail);
        ResultSet rs = ps.executeQuery();
        return rs.next();

    }

    public static int[] getPlayersStateCounts() throws SQLException {
        ensureConnection();
        int[] counts = new int[3];

        PreparedStatement ps = connect.prepareStatement(
                "SELECT state, COUNT(*) as cnt FROM TEAM4.USERS GROUP BY state"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String state = rs.getString("state");
            int cnt = rs.getInt("cnt");
            switch (state) {
                case "onlineGame":
                    counts[0] = cnt;
                    break;
                case "onlineAvailable":
                    counts[1] = cnt;
                    break;
                case "Offline":
                    counts[2] = cnt;
                    break;
            }
        }
        return counts;
    }

    public static void updateScore(String gmail, int score, char operator) throws SQLException {
        ensureConnection();
        PreparedStatement ps = connect.prepareStatement(
                "UPDATE TEAM4.USERS SET score = score " + operator + " ? WHERE gmail = ?"
        );
        if(score>0)
        ps.setInt(1, score);
        else 
         ps.setInt(1, 0);   
        
        ps.setString(2, gmail);
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
                "SELECT * FROM TEAM4.USERS WHERE state IN ('onlineAvailable') ORDER BY score DESC"
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

    public static void insertGameResult(String gmail1, String gmail2, String winner_gmail) throws SQLException {
        ensureConnection();

        String sql = "INSERT INTO TEAM4.GAMES (GMAIL1, GMAIL2, GMAILWIN, GAMEDATE) "
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        PreparedStatement pr = connect.prepareStatement(sql);
        pr.setString(1, gmail1);
        pr.setString(2, gmail2);
        pr.setString(3, winner_gmail);

        pr.executeUpdate();
        pr.close();
    }

    public static ArrayList<Game> getPlayerHistory(String email) throws SQLException {
        ensureConnection();
        ArrayList<Game> games = new ArrayList<>();

        String sql = "SELECT * FROM TEAM4.GAMES WHERE GMAIL1 = ? OR GMAIL2 = ? ORDER BY GAMEDATE DESC";
        PreparedStatement psGames = connect.prepareStatement(sql);
        psGames.setString(1, email);
        psGames.setString(2, email);

        ResultSet rsGames = psGames.executeQuery();

        while (rsGames.next()) {
            Game game = new Game(
                    rsGames.getInt("GAMEID"),
                    rsGames.getString("GMAIL1"),
                    rsGames.getString("GMAIL2"),
                    rsGames.getString("GMAILWIN"),
                    rsGames.getTimestamp("GAMEDATE")
            );

            games.add(game);
        }

        rsGames.close();
        psGames.close();

        return games;
    }

}
