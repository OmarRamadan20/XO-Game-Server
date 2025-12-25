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
    private User loggedUser;

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

                    case "update_score":
                        handleScore(request);
                        break;
 
                    case "game_result":
                        handleInsertGameResult(request);
                        break;
 
                    case "getTopPlayers":
                        handleGetTopPlayer();
                        break;
                    case "getAvailablePlayers":
                        handleGetAvailablePlayers();
                        break;
                    case "playerHistory":
                        handlePlayerHistory(request);
                        break;

                    case "logout":
                        handleLogout(request);
                        break;
                    case "invite":
                        handleInvite(request);
                        break;
                    case "move":
                        handleMove(request);
                        break;
                    case "invite_response":
                        handleInviteResponse(request);
                        break;
                    case "isPlayerAlreadyLoggedIn":
                        isPlayerAlreadyLoggedIn(request);
                        break;
                    case "getscore":
                        handleGetScore(request);
         
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected.");
        } finally {
            closeConnection();
        }
    }

  public void handleGetScore(JSONObject request) {
    JSONObject response = new JSONObject();
    String gmail = request.optString("gmail", "unknown");  

    try {
        int score = DAO.getScore(gmail);

        response.put("type", "getScore_response");
        response.put("status", "success");
        response.put("score", score);
        response.put("gmail", gmail);  

    } catch (SQLException e) {
        response.put("type", "getScore_response");
        response.put("status", "error");
        response.put("message", "Database error");
        response.put("gmail", gmail); 
    }

    ps.println(response.toString());
}

    
    private void handleLogin(JSONObject request) {
        String gmail = request.getString("gmail");
        String pass = request.getString("password");

        try {
            User userLogin = DAO.login(gmail, pass);

            JSONObject response = new JSONObject();
            response.put("type", "login");

            if (userLogin != null) {
                response.put("status", "success");

                response.put("name", userLogin.getName());
                response.put("gmail", userLogin.getGmail());
                response.put("score", userLogin.getScore());
                this.loggedUser = userLogin;

            } else {
                response.put("status", "fail");
                response.put("message", "Invalid email or password");
                this.loggedUser = null;
            }

            ps.println(response.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void handleSignUp(JSONObject request) {
        JSONObject response = new JSONObject();
        response.put("type", "signup");
        try {
            User user = new User();
            user.setName(request.getString("name"));
            user.setGmail(request.getString("gmail"));
            user.setPassword(request.getString("password"));
            user.setScore(0);
            user.setState("onlineAvailable");
            int result = DAO.SignUp(user);

            if (result > 0) {

                response.put("status", "success");
            } else {
                response.put("status", "fail");
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException ex) {

            response.put("status", "fail");
            response.put("message", "This Email is already registered!");
        } catch (SQLException ex) {
            System.getLogger(ClientHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } finally {
            ps.println(response.toString());
        }

    }

    private void handleScore(JSONObject request) {
        try {
            String gmail = request.getString("gmail");
            int score = request.getInt("score");
            char operator = request.getString("operator").charAt(0);

            DAO.updateScore(gmail, score, operator);

            JSONObject response = new JSONObject();
            response.put("type", "update_score");
            response.put("status", "success");

            ps.println(response.toString());

        } catch (SQLException e) {
            JSONObject response = new JSONObject();
            response.put("type", "update_score");
            response.put("status", "fail");
            response.put("message", e.getMessage());

            ps.println(response.toString());
        }
    }

private void handleInsertGameResult(JSONObject request) {
    try {
        String user1Gmail = request.getString("gmail1");
        String user2Gmail = request.getString("gmail2");
        String winGmail = request.getString("gmailWin");

         DAO.insertGameResult(user1Gmail, user2Gmail, winGmail);

         if (winGmail != null && !winGmail.isEmpty()) {
            String loserGmail = winGmail.equals(user1Gmail) ? user2Gmail : user1Gmail;
            
         
            DAO.updateScore(winGmail, 10, '+');    
             
          int scoreCheck =  DAO.getScore(loserGmail);
               if(scoreCheck > 0)   DAO.updateScore(loserGmail, 5, '-');   
                
         
         }

        JSONObject response = new JSONObject();
        response.put("type", "game_result");
        response.put("status", "success");

        ps.println(response.toString());

    } catch (Exception e) {
        JSONObject response = new JSONObject();
        response.put("type", "game_result");
        response.put("status", "fail");
        response.put("message", e.getMessage());

        ps.println(response.toString());
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

    private void handleGetAvailablePlayers() {
        try {
            ArrayList<User> players = DAO.getAvailablePlayers();
            JSONObject response = new JSONObject();
            response.put("type", "getAvailablePlayers");
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

   private void handlePlayerHistory(JSONObject request) {
    try {
        String gmail = request.getString("gmail");   
        ArrayList<Game> games = DAO.getPlayerHistory(gmail);

        JSONObject response = new JSONObject();
        response.put("type", "playerHistory");

        JSONArray arrPlayerHistory = new JSONArray();
        for (Game game : games) {
            JSONObject obj = new JSONObject();
            obj.put("gmail1", game.getGmail1());     
            obj.put("gmail2", game.getGmail2());      
            obj.put("gmailwinner", game.getGmailWin());
            obj.put("date", game.getGameDate().toString());
            arrPlayerHistory.put(obj);
        }

        response.put("games", arrPlayerHistory);
        ps.println(response.toString());

    } catch (SQLException ex) {
        System.getLogger(ClientHandler.class.getName())
              .log(System.Logger.Level.ERROR, (String) null, ex);
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
        OnTurnOff.clientsVector.remove(this);
    }

private void isPlayerAlreadyLoggedIn(JSONObject request) {
    JSONObject response = new JSONObject();
    response.put("type", "isPlayerAlreadyLoggedIn");

    try {
        String gmail = request.optString("gmail", null);

        if (gmail == null || gmail.isEmpty()) {
            response.put("status", "error");
            response.put("message", "gmail is missing");
        } else {
            boolean isLogged = DAO.isPlayerAlreadyLoggedIn(gmail);
            response.put("status", "success");
            response.put("isLogin", isLogged);
        }

    } catch (SQLException ex) {
        response.put("status", "error");
        response.put("message", "database error");
        ex.printStackTrace();
    }

    ps.println(response.toString());
}


    public static void updateState(String email, String status) throws SQLException {
        DAO.updateState(email, status);
    }

    private void handleLogout(JSONObject request) {
        JSONObject response = new JSONObject();
        response.put("type", "logout_response");

        if (loggedUser != null) {
            try {
                DAO.updateState(loggedUser.getGmail(), "offline");
            } catch (Exception e) {
                e.printStackTrace();
            }

            loggedUser = null;
            response.put("status", "success");
        } else {
            response.put("status", "fail");
            response.put("message", "User not logged in");
        }

        ps.println(response.toString());
    }

     private void handleInvite(JSONObject request) {
        String toPlayer = request.getString("to");
        String fromPlayer = request.getString("from");

        boolean found = false;
        for (ClientHandler client : OnTurnOff.clientsVector) {

            if (client.loggedUser != null && client.loggedUser.getName().equals(toPlayer)) {
                JSONObject msg = new JSONObject();
                msg.put("type", "invite_recieved");
                msg.put("from", fromPlayer);
                client.ps.println(msg.toString());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Target player " + toPlayer + " not found!");
        }
    }

    private void handleInviteResponse(JSONObject request) {
        String toPlayer = request.getString("to");
        String status = request.getString("status");
        String fromPlayer = request.getString("from");

        for (ClientHandler client : OnTurnOff.clientsVector) {

            if (client.loggedUser != null && client.loggedUser.getName().equals(toPlayer)) {
                JSONObject response = new JSONObject();
                response.put("type", "invite_status_back");
                response.put("status", status);
                response.put("from", fromPlayer);
                client.ps.println(response.toString());
                break;
            }
        }
    }

    private void handleMove(JSONObject request) {
        String targetPlayerName = request.getString("to");
        String move = request.getString("move");
        String senderName = request.getString("from");

        for (ClientHandler client : OnTurnOff.clientsVector) {

            if (client.loggedUser != null && client.loggedUser.getName().equals(targetPlayerName)) {
                JSONObject msg = new JSONObject();
                msg.put("type", "player_move");
                msg.put("move", move);
                msg.put("from", senderName);
                client.ps.println(msg.toString());
                break;
            }
        }
    }
}
