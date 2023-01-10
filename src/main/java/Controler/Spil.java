package Controler;

import Model.*;
import Model.Chance.ChanceCard;
import Model.Fields.*;
import View.BoardGUI;
import View.ViewGUI;
import gui_main.GUI;
import java.awt.*;
import static Model.Account.*;

public class Spil {
    final int JAILFIELD = 10;
    public void play() {

        Dice dice1 = new Dice(6,1);
        Dice dice2 = new Dice(6,1);
        BoardGUI boardGUI = new BoardGUI();
        FieldList fl = new FieldList();
        Field[] fields  = fl.CreateFieldList();
        ChanceDeck deck = new ChanceDeck();

        GUI gui = new GUI(boardGUI.guiFields(fields), Color.WHITE);
        ViewGUI viewGUI = new ViewGUI(gui);
        Spiller spiller;

        String amountPlayers = gui.getUserButtonPressed("Vælg antallet af spillere", "3", "4", "5", "6");

        SpillerListe sl = new SpillerListe(Integer.parseInt(amountPlayers));

        sl.setCurrentPlayer(0);

        viewGUI.addPlayers(sl);

        while(true) {
            spiller = sl.getCurrentPlayer();

            spiller.setPassingMoney(true);

            if (spiller.isJail()){
                spiller.setJailTurns(spiller.getJailTurns()+1);

                String jailString = gui.getUserButtonPressed("Vælg den måde du vil komme ud af fængslet på", "Betal en bøde på 1000", "Benyt et løsladelseskort", "Forsøg at kaste 2 ens terninger");

                if (jailString.equals("Betal en bøde på 1000"))  {
                    if (spiller.getAccount().getBalance() > 1000) {
                        withdraw(spiller.getAccount(), 1000);
                        viewGUI.updateBalance(sl);
                        spiller.setJail(false);
                    }
                }

                if (jailString.equals("Benyt løsladelseskort")){
                    if(spiller.isSetOutofJailCard()) {
                        spiller.setSetOutofJailCard(false);
                        spiller.setJail(false);
                    }
                }

                if(jailString.equals("Prøv at kaste med 2 ens terninger")){
                    viewGUI.setDice(dice1.roll(), dice2.roll());
                    if(dice1.getFaceValue() == dice2.getFaceValue()){
                        spiller.setJail(false);
                        OnOwneble(dice1, dice2, fl, gui, viewGUI, spiller);
                    }
                }
                if(spiller.getJailTurns() >= 3){
                    spiller.setJail(false);
                }
            } else {
                spiller.extraTurns = 0;

                takeTurn(gui,viewGUI,spiller,dice1,dice2,fl,sl, deck);

                while(spiller.getExtraTurn()){
                    if(spiller.extraTurns < 2){
                        takeTurn(gui,viewGUI,spiller,dice1,dice2,fl,sl, deck);
                    } else {
                        gui.showMessage("Du går til fængslet");
                        spiller.setPassingMoney(false);
                        spiller.setJail(true);
                        viewGUI.moveCarToField(spiller, JAILFIELD);
                        break;
                    }
                }
                sl.getNextPlayer();
            }
        }
    }



    private void takeTurn(GUI gui, ViewGUI viewGUI, Spiller spiller, Dice dice1, Dice dice2, FieldList fl, SpillerListe sl, ChanceDeck deck){
        gui.showMessage("Kast med terninger " + spiller.getName());
        viewGUI.setDice(dice1.roll(), dice2.roll());
        spiller.setExtraTurn(dice1.getFaceValue() == dice2.getFaceValue());
        OnOwneble(dice1, dice2, fl, gui, viewGUI, spiller);
        Field currentField = fl.getField(spiller.getPosition());

        if (currentField instanceof Tax) {
            withdraw(spiller.getAccount(), ((Tax) currentField).getTax());
            viewGUI.updateBalance(sl);
        }

        if (currentField instanceof Chance){
            String drawCard = gui.getUserButtonPressed("Træk et hvilket som helst kort", "Træk kort");
            ChanceCard chanceCard = deck.drawCard();
            chanceCard.doCard(spiller, viewGUI);
            viewGUI.showChanceCard(chanceCard.getDescription());

        }

        if (spiller.getPosition() == 30) {
            gui.showMessage("Du går til fængslet");
            spiller.setPassingMoney(false);
            spiller.setJail(true);
            viewGUI.moveCarToField(spiller, JAILFIELD);
        }

        if(spiller.isPassingMoney() && spiller.previousPosition > spiller.getPosition()){
            deposit(spiller.getAccount(), 4000);
        }

        int j = 0;
        String[] ownedFieldsNames;
        Owneble[] ownedFields;

        for (int i = 0; i < fl.getFields().length; i++) {
            Field f = fl.getField(i);
            if (f instanceof Street && ((Street) f).getOwner() == spiller) {
                j++;
            }
        }
        ownedFieldsNames = new String[j];
        ownedFields = new Owneble[j];
        int k = 0;
        for (int i = 0; i < fl.getFields().length; i++) {
            Field f = fl.getField(i);
            if (f instanceof Street && ((Street) f).getOwner() == spiller){
                ownedFieldsNames[k] = f.getName();
                ownedFields[k] = (Owneble) f;
                k++;
            }
        }


        if(ownedFieldsNames.length != 0 ) {
            String buyHouseString = gui.getUserButtonPressed("Vil du købe et hus el. hotel?", "Ja", "Nej");
            if (buyHouseString.equals("Ja")){
                String ownedString = gui.getUserSelection("Hvilken grund vil du købe et hus eller et hotel på", ownedFieldsNames);
                int index = 0;
                Street buyHouse = null;
                for (int i = 0; i < fl.getFields().length; i++) {
                    if (ownedString.equals(fl.getField(i).getName())) {
                        buyHouse = (Street) fl.getField(i);
                        index = i;
                    }
                }
                assert buyHouse != null;
                buyHouse.setHouseAmount(buyHouse.getHouseAmount() + 1);
                if(buyHouse.getHouseAmount() >= 4){
                    withdraw(spiller.getAccount(),5*buyHouse.getHousePrice());
                } else {
                    withdraw(spiller.getAccount(),buyHouse.getHousePrice());
                }
                viewGUI.buyHouseHotel(buyHouse, index);
            }
        }
        viewGUI.updateBalance(sl);
        spiller.extraTurns += 1;
    }

    private void OnOwneble(Dice dice1, Dice dice2, FieldList fl, GUI gui, ViewGUI viewGUI, Spiller spiller) {
        viewGUI.moveCar(spiller, dice1.getFaceValue() + dice2.getFaceValue());
        Field currentField = fl.getField(spiller.getPosition());
        if (currentField instanceof Owneble && ((Owneble) currentField).getOwner() == null && spiller.getAccount().getBalance() > ((Owneble) currentField).getPrice()) {
            String buy = gui.getUserButtonPressed("Vil du købe " + currentField.getName() + " for " + ((Owneble) currentField).getPrice(), "Ja", "Nej");
            if (buy.equals("Ja")) {
                withdraw(spiller.getAccount(), ((Owneble) currentField).getPrice());
                ((Owneble) currentField).setOwner(spiller);
                viewGUI.buyOwneble(spiller);
            }
        } else if(currentField instanceof Owneble && ((Owneble) currentField).getOwner() != null){
            pay(spiller.getAccount(), ((Owneble) currentField).getOwner().getAccount() ,((Owneble) currentField).getRent());
        }
    }

    private boolean hasLost(SpillerListe sl){
        for (int i = 0; i < sl.getPlayerAmount(); i++) {
            return sl.getPlayerList(i).getAccount().getBalance() > 0;
        }
        return false;
    }
}