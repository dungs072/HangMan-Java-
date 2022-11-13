package Animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Entities.MyRunable;
import main.Entities.Vector2;

public class CoinAnimationManager implements MyRunable {
    private final long timeMovePerCoin = 200;
    private long timeDeltaTime = 0;
    private long timeAC = 0;
    private int currentIndexAnim = 0;
    private ArrayList<Animation> coinAnimations;
    private Vector2 destination;
    private Vector2 startPosition;
    private int speed = 3;
    private int amountCoinsNeed = 10;
    
    private boolean isDrawCoinMove = false;
    public CoinAnimationManager(int startX, int startY, int endX, int endY,int amount, BufferedImage[] coinImages)
    {
        coinAnimations = new ArrayList<>();
        this.startPosition = new Vector2(startX, startY);
        this.destination = new Vector2(endX, endY);
        createCoins(amount, coinImages);
        changeStateCanDrawForCoinAnims(false);
    }
    private void createCoins(int amount,BufferedImage[] coinImages)
    {
        for(int i =0;i<amount;i++)
        {
            coinAnimations.add(new Animation(startPosition.getX(), startPosition.getY(), 50, 50,
                                             coinImages, true, 25));
            
        }
    }
    public void setDestination(int x, int y)
    {
        destination.setPos(x, y);
    }
    public void setStartPosition(int x, int y)
    {
        startPosition.setPos(x, y);
    }
    public void setAmountCoinNeed(int amount)
    {
        amountCoinsNeed = amount<coinAnimations.size()?amount:coinAnimations.size();
        currentIndexAnim=0;
        resetPositionForCoinAnimation();
    }
    private void resetPositionForCoinAnimation()
    {
        for(var anim:coinAnimations)
        {
            anim.setPosition(startPosition.getX(), startPosition.getY());
        }
    }
    private int calculateNextYPosition(int x)
    {
        float coff = ((float)startPosition.getY()-(float)destination.getY())/
                        ((float)startPosition.getX()-(float)destination.getX());
        return (int)(coff*(float)x+((float)destination.getY()-coff*(float)destination.getX()));
    }
    public void setIsDrawCoinMove(boolean state)
    {
        isDrawCoinMove = state;
    }
    @Override
    public void update(long timeDeltaTime) {
        this.timeDeltaTime = timeDeltaTime;
        if(currentIndexAnim<amountCoinsNeed)
        {
            timeAC+=timeDeltaTime;
            if(timeAC>=timeMovePerCoin)
            {
                timeAC = 0;
                coinAnimations.get(currentIndexAnim).setIsCanDraw(true);
                currentIndexAnim++;
            }
        }
       
        for(var coinAnim: coinAnimations)
        {
            if(!coinAnim.getIsCanDraw()){continue;}
            int x = coinAnim.getCurrentPosition().getX()+speed;
        
            int y = calculateNextYPosition(x);
            coinAnim.setPosition(x, y);
            if(x>=destination.getX()&&y<=destination.getY())
            {
                coinAnim.setIsCanDraw(false);
            }
        }
    }
    private void changeStateCanDrawForCoinAnims(boolean state)
    {
        for(var anim: coinAnimations)
        {
            anim.setIsCanDraw(state);
        }
    }
    public void switchOffDisplayCoins()
    {
        changeStateCanDrawForCoinAnims(false);
        resetPositionForCoinAnimation();
    }
    @Override
    public void paint(Graphics2D g2) {
        if(!isDrawCoinMove){return;}
        for(var coinAnim:coinAnimations)
        {
            coinAnim.paint(g2, timeDeltaTime);
        }
    }
}
