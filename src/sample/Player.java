package sample;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Player {
    private String pName;
    private double balance;
    private Map<String, PlayerStock> myStock = new HashMap<String, PlayerStock>();//a map of all the stocks the player owns
    public Player(String pn, double b) {//gets and sets the data for the player
        pName = pn;
        balance = b;
    }
    public PlayerStock getStock(String name) {//returns the specific stock from the map of all owned stocks
        return myStock.get(name);
    }
    public ArrayList<String> getMyStock() {//returns an arraylist of all the owned stocks
        ArrayList<String> listStocks = new ArrayList<>();
        for (Map.Entry<String, PlayerStock> entry : myStock.entrySet()) {//adds the key of each stock in the map of stocks, which is the symbol for every stock
            listStocks.add(entry.getKey());
        }
        return listStocks;
    }
    public void buyStock(String stockName, double share) throws Exception {//buys shares of a stock
        PlayerStock x;
        x = myStock.get(stockName); //searches myStock for the stock chosen
        changeBalance(stockName, share);//updates the balance after the purchase
        if (x == null) { //if the stock is new, creates new instance of the stock
            x = new PlayerStock(stockName, share);
        }
         else {
             x.addShare(share); //if stock exists, then just add share
        }
         myStock.put(stockName, x); //updates stock list
    }
    public void sellStock(String stockName, double share) throws Exception {//sells shares of a stock
        PlayerStock x;
        x = myStock.get(stockName);//searches for the stock
        if (x.getShare() >= share) {//as long as the number of shares owned is greater than or equal to the amount of shares to be sold, then the action is valid
            changeBalance(stockName, -share);//updates the balance after selling. shares will be negative in order to increase the balance because selling shares generates profit
            x.removeShare(share);//removes the number of shares sold from the number of shares owned
            if (x.getShare() == 0) {//if the player sold all of the shares of a stock, they no longer own that stock, and it is removed from the map of all their owned stocks
                myStock.remove(stockName, x);
            }
        }
        else {
            throw new Exception("Not Enough Shares");//if the player wants to sell more than they owned, the action is forbidden and the error will be caught
        }
    }
    public void changeBalance(String stockName, double share) throws Exception {//changes the balance based on amount of shares bought or sold
        if (balance >= share * StockList.getStockPrice(stockName)) {//ensures that the user has enough money to make the transaction
            balance -= share * StockList.getStockPrice(stockName);
        }
        else {
            throw new Exception("Not Enough Money");//if not enough money, then error will be caught
        }
    }
    public String getPName() {//returns the name of the player
        return pName;
    }
    public String getRoundBalance() {//rounds the balance to two decimal places for visual purposes
        DecimalFormat df = new DecimalFormat("#.##");//sets format to have two decimal places
        df.setRoundingMode(RoundingMode.CEILING);
        //System.out.println(df.format(num));
        return df.format(balance);
    }
}
