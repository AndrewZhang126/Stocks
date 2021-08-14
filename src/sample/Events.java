package sample;

import java.util.ArrayList;

public class Events {//contains the data for each event
    private String name;
    private String category;
    private double severity;
    private double severity2;
    public Events(String eName, String eCat, double eSev, double eSev2) {//gets and sets the data for the event
        name = eName;
        category = eCat;
        severity = eSev;
        severity2 = eSev2;
    }
    public String getName() {//returns the event's description
        return name;
    }
    public String getCategory() {//returns the categories affected by the event
        return category;
    }
    public double getSeverity() {//returns the severity of the event
        return severity;
    }
    public double getSeverity2() {
        return severity2;
    }

}
