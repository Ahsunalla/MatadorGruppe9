package Model;

public class Felter {
    public Felter(){}
    int[] felter = {0, 250,  -100,  100,  -20,  180,  0,  -70, 60,  -80, -50,  650};

    String[][] felterString = {{"Tower, Godt gået! Du har modtaget 250 point"}, {"Crater, Desværre! du har mistet 100 point"},
            {"palaceGates, Tillykke! du har modtaget 100 point"},
            {"coldDesert, Åh nej! du har mistet 20 point"},
            {"walledCity, Sådan! du har mogtaget 180"},
            {"Monastery, wow! du har hverken modtaget eller mistet point"},
            {"blackCave, Wwa Wa Wa! du har mistet 70 point"},
            {"hutsInTheMountain, Success! du har modtaget 60 point"} ,
            {"theWerewall, Du har mistet 80 point"},
            {"thePit, Du har mistet 50 point"},
            {"Goldmine, Tillykke du har modtaget 650 point"}};

    public int getSquare(int sumTerning){
        return felter[sumTerning];

    }

    public String getTower(int sumTerning){
        return felter[sumTerning] + " " + felterString[sumTerning-1][1];
    }
    public int getCrater(int sumTerning) {
        return felter[1];
    }
    public int getPalaceGates() {
        return felter[2];
    }
    public int getColdDesert() {
        return felter[3];
    }
    public int getWalledCity() {
        return felter[4];
    }
    public int getMonastery() {
        return felter[5];
    }
    public int getBlackCave() {
        return felter[6];
    }
    public int getHutsInTheMountain() {
        return felter[7];
    }
    public int getTheWerewall() {
        return felter[8];
    }
    public int getThePit() {
        return felter[9];
    }
    public int getGoldmine() {
        return felter[10];
    }
}

