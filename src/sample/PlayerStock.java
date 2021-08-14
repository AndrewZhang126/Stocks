package sample;

public class PlayerStock {
    private String psname;
    private double share;
    public PlayerStock(String psn, double sh) {//gets and sets the data for the stock owned by the player
        psname = psn;
        share = sh;
    }
    public double getShare() {//returns the number of shares owned by the player
        return share;
    }
    public void addShare(double playerShare) {//adds to the shares owned by the player
        share += playerShare;
    }
    public void removeShare(double playerShare) {//removes from the shares owned by the player
        share -= playerShare;
    }
}
