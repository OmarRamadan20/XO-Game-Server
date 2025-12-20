/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.serverxogame;

/**
 *
 * @author user
 */
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.JSONArray;

class ClientHandler extends Thread {

    private Socket socket;
    private DataInputStream dis;
    private PrintStream ps;

    ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (socket != null && !socket.isClosed()) {
                String msg = dis.readLine();
                if (msg == null) {
                    break;
                }

                JSONObject request = new JSONObject(msg);
                String type = request.getString("type");

                switch (type) {
                    case "login":
                        handleLogin(request);
                        break;
                    case "signup":
                        handleSignUp(request);
                        break;
                    case "getTopPlayers":
                        handleGetTopPlayer();
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected.");
        } finally {
            closeConnection();
        }
    }

    private void handleLogin(JSONObject request) {
        String gmail = request.getString("gmail");
        String pass = request.getString("password");

        try {
            User userLogin = DAO.login(gmail, pass);

            JSONObject response = new JSONObject();
            response.put("type", "login_response");

            if (userLogin != null) {
                response.put("status", "success");

                response.put("name", userLogin.getName());
                response.put("gmail", userLogin.getGmail());
                response.put("score", userLogin.getScore());

            } else {
                response.put("status", "fail");
                response.put("message", "Invalid email or password");
            }

            ps.println(response.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void handleSignUp(JSONObject request) {
        try {
            User user = new User();
            user.setName(request.getString("name"));
            user.setGmail(request.getString("gmail"));
            user.setPassword(request.getString("password"));
            user.setScore(0);
            user.setState("offline");
            int result = DAO.SignUp(user);
            JSONObject response = new JSONObject();
            response.put("type", "signUp_response");
            if (result > 0) {
                response.put("status", "success");
            } else {
                response.put("status", "success");
            }
            ps.println(response.toString());

        } catch (SQLException ex) {
            System.getLogger(ClientHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    private void handleGetTopPlayer() {
        try {
            ArrayList<User> players = DAO.getTopPlayers();
            JSONObject response = new JSONObject();
            response.put("type", "getTopPlayers");
            JSONArray arrOfPlayers = new JSONArray();
            for (User user : players) {
                JSONObject obj = new JSONObject();
                obj.put("name", user.getName());
                obj.put("gmail", user.getGmail());
                obj.put("score", user.getScore());
                obj.put("state", user.getState());
                arrOfPlayers.put(obj);

            }
            response.put("players", arrOfPlayers);
            ps.println(response.toString());

        } catch (SQLException ex) {
            System.getLogger(ClientHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    public void closeConnection() {
        try {
            if (dis != null) {
                dis.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        onTurnoff.clientsVector.remove(this);
    }
}
