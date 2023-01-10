package Controler;

import Model.*;
import Model.Chance.ChanceCard;
import Model.Fields.*;
import View.BoardGUI;
import View.ViewGUI;
import gui_fields.GUI_Car;
import gui_fields.GUI_Ownable;
import gui_fields.GUI_Player;
import gui_main.GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static Model.Account.*;

public class Spil {

    final int MAXPLAYERS = 6;
    final int MINPLAYERS = 3;
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

        String amountPlayers = gui.getUserButtonPressed("Vælg antallet af spiller", "1","2","3", "4", "5", "6");

        SpillerListe sl = new SpillerListe(Integer.parseInt(amountPlayers));

        sl.setCurrentPlayer(0);

        viewGUI.addPlayers(sl);

        while(true) {
            spiller = sl.getCurrentPlayer();

            spiller.setPassingMoney(true);

            if (spiller.isJail()){
                spiller.setJailTurns(spiller.getJailTurns()+1);

                String jailString = gui.getUserButtonPressed("Vælg en måde du vil komme ud af fængslet på", "Betal bøde på 1000", "Benyt lødsladeseskort", "Prøv at kaste 2 ens");

                if (jailString.equals("Betal bøde på 1000"))  {
                    if (spiller.getAccount().getBalance() > 1000) {
                        withdraw(spiller.getAccount(), 1000);
                        viewGUI.updateBalance(sl);
                        spiller.setJail(false);
                    }
                }

                if (jailString.equals("Benyt lødsladeseskort")){
                    if(spiller.isSetOutofJailCard()) {
                        spiller.setSetOutofJailCard(false);
                        spiller.setJail(false);
                    }
                }
                if(jailString.equals("Prøv at kaste 2 ens")){
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
        gui.showMessage("Kast med tærningerne " + spiller.getName());
        viewGUI.setDice(dice1.roll(), dice2.roll());
        spiller.setExtraTurn(dice1.getFaceValue() == dice2.getFaceValue());
        OnOwneble(dice1, dice2, fl, gui, viewGUI, spiller);
        Field currentField = fl.getField(spiller.getPosition());

        if (currentField instanceof Tax) {
            withdraw(spiller.getAccount(), ((Tax) currentField).getTax());
            viewGUI.updateBalance(sl);
        }

        if (currentField instanceof Chance){
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
