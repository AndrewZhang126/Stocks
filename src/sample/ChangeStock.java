package sample;
import java.text.DecimalFormat;
import java.math.RoundingMode;

public class ChangeStock {//this will determine the amount of change based on the percentage
    public double priceChange(double stockPrice, double severe, double severe2) {//there is a determined severity
        return stockPrice * (severe + (Math.random() * (severe2 - severe))); //there will be a one percent increase for a severity of 1, a 2 percent increase for a severity of 2, a one percent decrease for a severity of -1, and a 2 percent decrease for a severity of -2
    }
    public double priceUnsure(double stockPrice, double severe) {//there is no determined severity
        double temp = Math.random();
        if (temp < 0.5) {//there is a equal chance of the price going up or down
            return stockPrice * severe;//will randomly go up by a random percentage
        }
        else {
            return -stockPrice * severe;//will randomly go down by a random percentage
        }
    }
}
