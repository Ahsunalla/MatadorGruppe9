package Model;
import Model.Account;
import Model.Fields.Field;
import Model.Fields.Owneble;

import java.util.ArrayList;

public class Spiller {
    private String name;
    public Account account;
    public int position = 0;
    public int previousPosition = 0;
    public int playerNumber;

    public boolean setOutofJailCard;
    public boolean isJail;
    public int jailTurns;
    public boolean passingMoney = true;
    public boolean extraTurn = false;
    public int extraTurns;

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Spiller(String name, int startingBalance) {
        this.name = name;
        this.account = new Account(startingBalance);
        this.position = 0;
        this.isJail = false;
    }

    public boolean isSetOutofJailCard() {
        return setOutofJailCard;
    }

    public void setSetOutofJailCard(boolean setOutofJailCard) {
        this.setOutofJailCard = setOutofJailCard;
    }
    public void setPosition(int pos){
        this.previousPosition = getPosition();
        this.position = pos%39;
    }
    public int getPosition(){
        return position;
    }
    public boolean isJail() {
        return isJail;
    }
    public void setJail(boolean jail) {
        isJail = jail;
    }

    public boolean isPassingMoney() {
        return passingMoney;
    }
    public void setPassingMoney(boolean passingMoney) {
        this.passingMoney = passingMoney;
    }

    public int getJailTurns() {
        return jailTurns;
    }
    public void setJailTurns(int jailTurns) {
        this.jailTurns = jailTurns;
    }
    public Account getAccount() {
        return account;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public boolean getExtraTurn() {
        return extraTurn;
    }

    public void setExtraTurn(boolean b) {
        extraTurn = b;
    }
}