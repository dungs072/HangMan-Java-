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
    private int lengthImageAnim = 0;
    private ArrayList<Animation> coinAnimations;
    private Vector2 destination;
    private Vector2 startPosition;
    
    private boolean isDrawCoinMove = false;
    public CoinAnimationManager()
    {
        coinAnimations = new ArrayList<>();
        destination = new Vector2(0, 0);
        startPosition = new Vector2(0, 0);
    }
    public void createCoins(int amount,BufferedImage[] coinImages)
    {
        lengthImageAnim = coinImages.length;
        for(int i =0;i<amount;i++)
        {
            coinAnimations.add(new Animation(startPosition.getX(), startPosition.getY(), 50, 50,
                                             coinImages, true, 100));
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
    private int calculateNextYPosition(int x)
    {
        int coff = (startPosition.getY()-destination.getY())/(startPosition.getX()-destination.getX());
        return coff*x+(destination.getY()-coff*destination.getX());
    }
    public void setIsDrawCoinMove(boolean state)
    {
        isDrawCoinMove = state;
    }
    // def calculate_next_y_point(self,xpoint):
    //     coff1 = (self.startY-self.desY)/(self.startX-self.desX)
    //     return int(coff1*xpoint + (self.desY-coff1*self.desX))
    @Override
    public void update(long timeDeltaTime) {
        this.timeDeltaTime = timeDeltaTime;
        timeAC+=timeDeltaTime;
        if(timeAC>=timeMovePerCoin)
        {
            Animation coinAnim = coinAnimations.get(currentIndexAnim);
            int x = coinAnim.getCurrentPosition().getX()+1;
            int y = calculateNextYPosition(x);
            coinAnim.setPosition(x, y);
            currentIndexAnim = (currentIndexAnim+1)%lengthImageAnim;

        }
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
