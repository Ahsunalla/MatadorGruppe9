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