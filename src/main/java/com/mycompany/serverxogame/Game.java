/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.serverxogame;

import java.sql.Date;

/**
 *
 * @author amr04
 */
public class Game {

    private int gameId;
    private int user1Id;
    private int user2Id;
    private int winnerId;
    private Date gameDate;

    public Game() {
    }

    public Game(int gameId, int user1Id, int user2Id, int winnerId, Date gameDate) {
        this.gameId = gameId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.winnerId = winnerId;
        this.gameDate = gameDate;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }
    
    

   

}
