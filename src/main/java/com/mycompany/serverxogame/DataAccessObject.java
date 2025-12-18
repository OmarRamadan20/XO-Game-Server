/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.serverxogame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;
 

/**
 *
 * @author amr04
 */
public class DataAccessObject {

    private static Connection connect;
    

    public DataAccessObject() throws SQLException {
        try {
            DriverManager.registerDriver(new ClientDriver());
            connect = DriverManager.getConnection("jdbc:derby://localhost:1527/User", "Team4", "Team4");

        } catch (SQLException ex) {
            System.getLogger(PrimaryController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

}
