package sample;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class StockData {//gets data from the API
    public double getStockPrice(String symbol) throws IOException { //code from https://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
        URL url = new URL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+symbol+"&apikey=D21FI95GNJ2VGEG3"); //selects the URL of the API. this API has the current prices of a specific stock
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();//allows a connection for access
        conn.setRequestMethod("GET");//only gets data from the API
        conn.setRequestProperty("Accept", "application/json");//json format
        if (conn.getResponseCode() != 200)
        {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());//prevents runtime errors
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));//reads the lines of the API
        StringBuilder apiOutput = new StringBuilder();//string that will contain all data from the API
        String input;
        while ((input = (br.readLine())) != null) {//while there is a line to be read, the line will be appended to a string
            apiOutput.append(input);
        }
        conn.disconnect();//stops the reading
        return passJson(apiOutput.toString());
    }
    public ArrayList<StockHistoric> getHistoricStockPrice(String symbol) throws IOException { //code from https://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
        URL url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=DEX6U86LCG48AF2P");//this API has past data for a specific stock
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();//establishes a connection
        conn.setRequestMethod("GET");//only gets data from API
        conn.setRequestProperty("Accept", "application/json");//json format
        if (conn.getResponseCode() != 200)
        {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());//prevents runtime errors
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));//reads the lines of the API
        StringBuilder apiOutput = new StringBuilder();
        String input;
        while ((input = (br.readLine())) != null) {//will add to the string of all data as long as there is another line
            apiOutput.append(input);
        }
        conn.disconnect();//stops reading
        return passHistoricJson(apiOutput.toString());
    }
    public double passJson(String response) {//returns the current price of a stock
        Gson gson = new Gson(); //open source Java library that serializes Java objects to json
        String value;
        Map map = gson.fromJson(response, Map.class); //converts the json to gson with response as the key and Map.class as the object
        Map result = (Map)map.get("Global Quote"); //global quote is the object that holds all the info for the stock
        if(result.get("05. price") != null) { //the individual attributes of the global quote is the specific info that is needed, and the price is accessed through "05. price"
            value = (String)result.get("05. price");
        }
        else {//if there is not current price, then the price is set to the previous day's price
            value = (String)result.get("08. previous close");
        }
        return Double.parseDouble(value);
    }
    public ArrayList<StockHistoric> passHistoricJson(String response) {//returns an arraylist of objects that contain a stock's past prices
        ArrayList<StockHistoric> allStockInfo = new ArrayList<>();
        Gson gson = new Gson();
        Map map = gson.fromJson(response, Map.class);//converts from json to gson with response as the key and Map.class as the object
        Map result = (Map)map.get("Time Series (Daily)");//time series is that object that holds all the past data
        Iterator hmIterator = result.entrySet().iterator();//code from https://www.geeksforgeeks.org/traverse-through-a-hashmap-in-java/ will move through the set
        while (hmIterator.hasNext()) {//as long as there is more sets in the map, iterator will keep traversing
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            String sDate = (String)mapElement.getKey();//the key of the map is the date
            Map info = (Map)mapElement.getValue();//the value of the key is the info from that date
            String closeP = (String)info.get("4. close");//gets the data associated with "4. close", which is the closing price of the stock on that day
            StockHistoric stockHistoric = new StockHistoric(sDate, Double.parseDouble(closeP));//creates a new object of that day and the price
            allStockInfo.add(stockHistoric);//adds the object to the arraylist
        }
        return allStockInfo;
    }
}
