/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.serverxogame;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author amr04
 */
public class Game {
    private int gameId;
    private String gmail1;
    private String gmail2;
    private String gmailWin;
    private Timestamp gameDate;

    public Game(int gameId, String gmail1, String gmail2, String gmailWin, Timestamp gameDate) {
        this.gameId = gameId;
        this.gmail1 = gmail1;
        this.gmail2 = gmail2;
        this.gmailWin = gmailWin;
        this.gameDate = gameDate;
    }

    public int getGameId() {
        return gameId;
    }
 

    public String getGmail1() {
        return gmail1;
    }

    public void setGmail1(String gmail1) {
        this.gmail1 = gmail1;
    }

    public String getGmail2() {
        return gmail2;
    }

    public void setGmail2(String gmail2) {
        this.gmail2 = gmail2;
    }

    public String getGmailWin() {
        return gmailWin;
    }

    public void setGmailWin(String gmailWin) {
        this.gmailWin = gmailWin;
    }

    public Timestamp getGameDate() {
        return gameDate;
    }

    public void setGameDate(Timestamp gameDate) {
        this.gameDate = gameDate;
    }
   
}

  
 