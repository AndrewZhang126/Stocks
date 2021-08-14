package sample;

public class StockHistoric {
    String stockDate;
    double closePrice;
    public StockHistoric(String day, double cPrice) {//gets and sets past data for a stock
        stockDate = day;
        closePrice = cPrice;
    }
    public String getDate() {
        return stockDate;
    }
    public double getClosePrice() {//returns the price
        return closePrice;
    }
}
