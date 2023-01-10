package Model.Fields;

import java.awt.*;

public class Street extends Owneble {
    int housePrice;
    public Street(String name, String message, int price, int housePrice, int[] rent, Color color) {
        super(name, message, price, rent, color);
        this.housePrice = housePrice;
    }
}