/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.serverxogame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author amr04
 */
public class DataAccessObject {

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
}
