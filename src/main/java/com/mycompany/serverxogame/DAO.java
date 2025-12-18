/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.serverxogame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            connect = DriverManager.getConnection("jdbc:derby://localhost:1527/User", "Team4", "Team4");
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
        PreparedStatement ps = connect.prepareStatement("UPDATE TEAM4.USERS SET state=? where email=?");
        ps.setString(1, email);
        ps.setString(2, email);
        ps.executeUpdate();
    }
}
