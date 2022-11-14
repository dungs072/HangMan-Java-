package main;

public class Player {
    private int score = 0;
    private int coin = 0;
    public Player(int score, int coin)
    {
        this.score = score;
        this.coin = coin;
    }
    public int getCoin(){return coin;}
    public int getScore(){return score;}
    public void setCoin(int amount)
    {
        this.coin = amount;
    }
    public void setScore(int amount)
    {
        this.score = amount;
    }
    public void addCoin(int amount)
    {
        this.coin+=amount;
    }
    public void addScore(int amount)
    {
        this.score+=amount;
    }
}
