package sample;

import java.util.ArrayList;

public class StockList {
    private static ArrayList<Stock> allStocks = new ArrayList<>(); //this list is shared by all the players, so only one instance is required
    public void addNewStock(Stock stock) {//will add a new stock to the arraylist of all stocks
        allStocks.add(stock);
    }
    public static double getStockPrice(String stockName) throws Exception {//returns the price of a stock
        Stock stock = getStock(stockName);
        if (stock == null) {
            throw new Exception("Stock Does Not Exist");//catches error in case stock does not exist
        }
        return stock.getUnitPrice();
    }
    public static Stock getStock(String stockName) {//returns a stock
        for (Stock stock : allStocks) {//searches through the arraylist of all stocks
            if (stock.getSymbol().equalsIgnoreCase(stockName)) {//if the name of a stock matches the name of the stock to be searched, it is returned
                return stock;
            }
        }
        return null;//stock is not found
    }
    public static ArrayList<Stock> getMyStock() {//returns the arraylist of all stocks
        return allStocks;
    }
}
