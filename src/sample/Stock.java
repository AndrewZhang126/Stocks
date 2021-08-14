package sample;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Stock {
    private String symbol;
    private String sName;
    private String category;
    private double unitPrice;
    public Stock(String sy, String sn, String c, double p) {//gets and sets the data for a stock
        symbol = sy;
        sName = sn;
        category = c;
        unitPrice = p;
    }
    public String getSymbol() {//returns the symbol of the stock
        return symbol;
    }
    public String getSName() {//returns the name of the stock
        return sName;
    }
    public String getCategory() {//returns the category of the stock
        return category;
    }
    public double getUnitPrice() {//returns the price of the stock
        return unitPrice;
    }
    public void changeUnitPrice(double price) {//will change the price of the stock
        unitPrice += price;
    }
    public String getRoundPrice() {//rounds the price for visual purposes
        DecimalFormat df = new DecimalFormat("#.##");//sets the format to two decimal places
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(unitPrice);
    }
}

