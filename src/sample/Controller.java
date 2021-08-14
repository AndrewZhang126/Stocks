package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Slider;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Controller {
    @FXML
    private ListView lst1;
    @FXML
    private ListView lst2;
    @FXML
    private ListView lst3;
    @FXML
    private Label lbl2;
    @FXML
    private Label lblSName;
    @FXML
    private Label lblCat;
    @FXML
    private Label lbl4;
    @FXML
    private Label lblPB;
    @FXML
    private Label lblP;
    @FXML
    private Label lblNS;
    @FXML
    private Label lblE;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Slider slider;
    @FXML
    private TextField txt1;
    @FXML
    private TextField txt2;
    @FXML
    private TextField txtName;
    @FXML
    private ImageView image;
    @FXML
    private LineChart graphStock;
    ChangeStock newDay = new ChangeStock();
    private Map<String, Player> myPlayers = new HashMap<String, Player>();
    private Map<String, ArrayList<StockHistoric>> historicData = new HashMap<String, ArrayList<StockHistoric>>();
    private ArrayList<Events> myEvents = new ArrayList<>();
    ArrayList<String> list = new ArrayList<String>();
    private ObservableList displayList = FXCollections.observableArrayList();
    StockList stockList = new StockList();
    StockData stockData = new StockData();
    @FXML
    public void initialize() {//runs at the very start
        image.setImage(new Image("images/money.jpg"));
        try{//this will gather all the data for the stocks
            FileReader reader = new FileReader("src/resources/StockInfo");//selects the file to be read
            Scanner in = new Scanner(reader);
            while(in.hasNextLine()) //will keep reading as long as there is a next line
            {
                String temp = in.nextLine();//determines the data to be read
                String[] stockInfo = temp.split(",", 0);//splits the data into three parts determined by where the "," is, and a limit of 0 means the split will happen as many times as possible
                String sym = stockInfo[0];//the first part is the stock symbol
                String name = stockInfo[1];//the second part is the stock name
                String category = stockInfo[2];//the last part is the stock category
                stockList.addNewStock(new Stock(sym, name, category,stockData.getStockPrice(sym)));//creates a new stock object with the data and the current price of the stock from the API
                lst2.getItems().add(Objects.requireNonNull(stockList.getStock(sym)).getSymbol());//adds the stock to the listview displaying all stocks
                historicData.put(sym, stockData.getHistoricStockPrice(sym));//adds the stock to the map of stocks with an arraylist of their past prices
                list.add(temp);
                displayList.add(temp);
            }
        } catch (FileNotFoundException ex) {
            lbl4.setText("Error");//in case the file does not exist
        } catch (IOException e) {
            e.printStackTrace();//prints throwable and backtrace to error
        }
        try{//this will gather all the data for events
            FileReader reader2 = new FileReader("src/resources/EventInfo");//selects the file to be read
            Scanner in = new Scanner(reader2);
            while(in.hasNextLine())//will keep reading as long as there is another line
            {
                String temp2 = in.nextLine();
                String[] eventInfo = temp2.split(",", 0);//will split the data into three parts through the "," placement
                String eventName = eventInfo[0];//the first part is the actual event description
                String eventCat = eventInfo[1];//the second part is the categories or specific stocks that the event affects
                String eventSev = eventInfo[2];//the last part is how severe the event is, or how much it will change the stock price
                String eventSev2 = eventInfo[3];
                myEvents.add(new Events(eventName, eventCat, Double.parseDouble(eventSev),Double.parseDouble(eventSev2)));//creates a new event object with the data
            }
        } catch (FileNotFoundException ex) {
            lbl4.setText("Error");//in case the file does not exist
        }
    }
    @FXML
    private void handlebtn1() {//this button adds a player
        myPlayers.put(txtName.getText(), new Player(txtName.getText(), 1000));//creates a new player and adds to the map of all players. each player will start with 1000 dollars
        lst1.getItems().add(myPlayers.get(txtName.getText()).getPName());//adds the player to the listview of players
    }
    @FXML
    private void handlelst1() {//this listview displays all the players
        if (lst1.getSelectionModel().getSelectedItem() != null) {
            lst3.getItems().clear();
            String playerChoice = lst1.getSelectionModel().getSelectedItem().toString();//gets the player that the user chose
            lblPB.setText("Balance: $" + myPlayers.get(playerChoice).getRoundBalance());//displays the balance of the selected player
            for (String stockName : myPlayers.get(playerChoice).getMyStock()) {//gets each stock that the selected player owns and adds it to the listview displaying player-owned stocks
                lst3.getItems().add(stockName);
            }
        }
    }
    @FXML
    private void handlelst2() throws IOException {//this listview displays all the stocks that can be bought
        if (lst2.getSelectionModel().getSelectedItem() != null) {
            graphStock.getData().clear();
            checkBox.setSelected(false);//will uncheck the box that graphically compares all stocks so that only the stock the user selected will be displayed
            String stockChoice = lst2.getSelectionModel().getSelectedItem().toString();//gets the stock the user chose
            Stock stock = StockList.getStock(stockChoice);//finds the chosen stock in the arraylist of all stocks
            assert stock != null;//ensures there is a stock
            lbl2.setText("Price: $" + stock.getRoundPrice());//displays the current stock's price
            lblSName.setText("Name: " + stock.getSName());//displays the stock's name
            lblCat.setText("Category: " + stock.getCategory());//displays the stock's category
            graph(stock.getSymbol());//graphs the stock's price data
        }
    }
    @FXML
    private void handlelst3() {//this listview displays all the stocks the player owns
        if (lst3.getSelectionModel().getSelectedItem() != null) {
            String playerChoice = lst1.getSelectionModel().getSelectedItem().toString();//gets the player selected
            String playerStockChoice = lst3.getSelectionModel().getSelectedItem().toString();//gets the stock selected
            lblP.setText("Price: $" + Objects.requireNonNull(StockList.getStock(playerStockChoice)).getRoundPrice());//displays the price of the selected stock
            lblNS.setText("Number Of Shares: " + (int)myPlayers.get(playerChoice).getStock(playerStockChoice).getShare());//displays the number of shares the player owns of the stock
        }
    }
    @FXML
    private void handlebtn2(){//this button allows the user to buy shares of a stock
        if (lst1.getSelectionModel().getSelectedItem() == null || lst2.getSelectionModel().getSelectedItem() == null) {//ensures the user selected both a player and any stock
            lbl4.setText("Choose Person And Stock");
        }
        else {
            int amount = 0;
            String player = lst1.getSelectionModel().getSelectedItem().toString();//gets the selected player
            String company = lst2.getSelectionModel().getSelectedItem().toString();//gets the selected stock
            try {
                amount = Integer.parseInt(txt1.getText());//checks to see if user entered an integer
            }
            catch(NumberFormatException ex) {
                lbl4.setText("Not Valid Input");//notifies user if they did not enter an integer
            }
            if (amount >= 0) {
                try {
                    myPlayers.get(player).buyStock(company, amount);//buys a specified amount of shares of the stock
                } catch (Exception e) {
                    lbl4.setText(e.getMessage());//handles errors in buying process
                }
                lblPB.setText("Balance: $" + myPlayers.get(player).getRoundBalance());//displays balance after purchase
                lblP.setText("Price: $" + Objects.requireNonNull(StockList.getStock(company)).getRoundPrice());//displays the price of the stock
                if (myPlayers.get(player).getStock(company) == null) {//ensures there still is a stock to display number of shares because if a player sells all of their shares of a stock, they no longer own that stock
                    lblNS.setText("Number Of Shares: " + 0);
                }
                else {
                    lblNS.setText("Number Of Shares: " + (int)myPlayers.get(player).getStock(company).getShare());//this means the player still owns the stock and will still have shares of that stock
                }
                lst3.getItems().clear();//clears the listview to allow new data
                for (String stockName : myPlayers.get(player).getMyStock()) {//re-enters the stocks the player now has after the purchase
                    lst3.getItems().add(stockName);
                }
            }
            else {
                lbl4.setText("Not Valid Input");
            }
        }
    }
    @FXML
    private void handlebtn3() {//this button allows the user to sell shares of a stock
        if (lst1.getSelectionModel().getSelectedItem() == null || lst3.getSelectionModel().getSelectedItem() == null) {//ensures the user selected both a player and one of their owned stocks
            lbl4.setText("Choose Person And Stock");
        }
        else {
            int amount = 0;
            try {
                amount = Integer.parseInt(txt2.getText());
            }
            catch(NumberFormatException ex) {
                lbl4.setText("Not Valid Input");
            }
            String player = lst1.getSelectionModel().getSelectedItem().toString();//gets the selected player
            String company = lst3.getSelectionModel().getSelectedItem().toString();//gets the selected stock
            try {
                myPlayers.get(player).sellStock(company, amount);//sells the specified number of shares of the stock
            } catch (Exception e) {
                lbl4.setText(e.getMessage());//handles errors in the selling process
            }
            lblPB.setText("Balance: $" + myPlayers.get(player).getRoundBalance());//displays the balance after selling
            lblP.setText("Price: $" + Objects.requireNonNull(StockList.getStock(company)).getRoundPrice());//displays the stock's price
            if (myPlayers.get(player).getStock(company) == null) {//ensures there still is a stock to display number of shares because if a player sells all of their shares of a stock, they no longer own that stock
                lblNS.setText("Number Of Shares: " + 0);
            }
            else {
                lblNS.setText("Number Of Shares: " + (int)myPlayers.get(player).getStock(company).getShare());//this means the player still owns the stock and will still have shares of that stock
            }
            lst3.getItems().clear();
            for (String stockName : myPlayers.get(player).getMyStock()) {//refreshes the stocks the player has
                lst3.getItems().add(stockName);
            }
        }
    }
    private boolean checkInput(String input) {
        try {
            Integer.parseInt(input);
        }
        catch(NumberFormatException ex) {
            return false;
        }
        return true;
    }
    @FXML
    private void handlebtn4() {//this button starts a new day and will select a random event that determines the new prices of the stocks for the day
        lst2.getItems().clear();
        int random = (int)(Math.random() * myEvents.size());
        lblE.setText(myEvents.get(random).getName());//displays a randomly selected event's description
        for (Stock stock : StockList.getMyStock()) {//looks through each stock
            if (stock.getCategory().equals(myEvents.get(random).getCategory()) || stock.getSymbol().equals(myEvents.get(random).getCategory())){//if the event category matches the category or symbol of a stock, then the price will either go up or down
                if (myEvents.get(random).getSeverity() > 0) {//if the severity is positive, then the stock price will go up
                    stockList.getStock(stock.getSymbol()).changeUnitPrice(newDay.priceChange(stockList.getStock(stock.getSymbol()).getUnitPrice(), myEvents.get(random).getSeverity(), myEvents.get(random).getSeverity2()));//changes the price of the specific stock based on the severity
                    historicData.get(stock.getSymbol()).add(new StockHistoric(stock.getSymbol(), stock.getUnitPrice()));//adds a new historic stock object as there is now a new day with a new stock price
                }
                else {//if severity is negative, then the stock price will go down
                    stockList.getStock(stock.getSymbol()).changeUnitPrice(newDay.priceChange(stockList.getStock(stock.getSymbol()).getUnitPrice(), myEvents.get(random).getSeverity(), myEvents.get(random).getSeverity2()));
                    historicData.get(stock.getSymbol()).add(new StockHistoric(stock.getSymbol(), stock.getUnitPrice()));
                }
            }
            else {//if the stock is not affected by the event, then the stock price could go either up or down by a random percentage
                stockList.getStock(stock.getSymbol()).changeUnitPrice(newDay.priceUnsure(stockList.getStock(stock.getSymbol()).getUnitPrice(), (Math.random() / 100)));
                historicData.get(stock.getSymbol()).add(new StockHistoric(stock.getSymbol(), stock.getUnitPrice()));
            }
            lst2.getItems().add(Objects.requireNonNull(stockList.getStock(stock.getSymbol())).getSymbol());//displays the stocks with their new prices
        }
    }

    @FXML
    private void graph(String stockSym) throws IOException {//graphs the data of the stock
        ArrayList<StockHistoric> stockGraph = historicData.get(stockSym);//the data used is from the past data gathered for a specific stock
        XYChart.Series series = new XYChart.Series();
        series.getData().clear();
        for (int i = 0; i < slider.getValue(); i++) {
            series.getData().add(new XYChart.Data(""+ (slider.getValue() - i), stockGraph.get(i).getClosePrice()));//adds all the data into the series until the amount of days that the user selects
        }
        graphStock.getData().add(series);//displays the graph with all the data
    }
    @FXML
    private void handleCheckBox() throws IOException {//checks whether to compare the stock prices or not
        if (checkBox.isSelected()) {//if checkbox is checked then graphs of all stock's prices should be displayed
            graph("MSFT");
            graph("AAPL");
        }
        else {
            graphStock.getData().clear();//if checkbox is unselected then the graph should be cleared
        }
    }
    @FXML
    private void handleSlider() throws IOException {//determines the number of days the graph should display
        graphStock.getData().clear();
        if (lst2.getSelectionModel().getSelectedItem() != null) {//if there is a specific stock selected to be graphed, then that stock's data is graphed
            graph(lst2.getSelectionModel().getSelectedItem().toString());
        }
        else if (checkBox.isSelected()) {//if the checkbox is checked then all stocks should be graphed
            handleCheckBox();
        }
    }
}
