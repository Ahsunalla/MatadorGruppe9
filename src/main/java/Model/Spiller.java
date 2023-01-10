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
