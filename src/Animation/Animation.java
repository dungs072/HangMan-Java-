package Animation;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.Entities.InvisibleEntity;
public class Animation extends InvisibleEntity{

    private BufferedImage[] images;
    private long timeDrawPerImage = 0;
    private long currentTimeAc = 0;
    private int currentIndex = 0;
    private boolean isLoop = true;
    private boolean isCanDraw = true;
    public Animation(int x, int y, int width, int height,BufferedImage[] images,boolean isLoop,long timedrawPerImage) {
        super(x, y, width, height);
        this.images = images;
        this.isLoop = isLoop;
        this.timeDrawPerImage = timedrawPerImage;
    }
    public void paint(Graphics2D g2,long timeDeltaTime)
    {
        if(!isCanDraw){return;}
        drawImagePerTimeSpecify(g2);
        currentTimeAc+=timeDeltaTime;
        if(currentTimeAc>=timeDrawPerImage)
        {
            currentTimeAc = 0;
            if(isLoop)
            {
                
                setNextCurrentIndexImage();
            }
            else
            {
                if((currentIndex+1)%images.length==0){return;}
                setNextCurrentIndexImage();
            }
               
        }
    }
    public void setIsCanDraw(boolean state)
    {
        isCanDraw = state;
    }
    public boolean getIsCanDraw(){return isCanDraw;}
    private void drawImagePerTimeSpecify(Graphics2D g2)
    {
        g2.drawImage(images[currentIndex],currentPosition.getX(),currentPosition.getY(),
                                currentSize.getX(),currentSize.getY(),null);
        
    }

    public void setTimeToDrawPerFrame(long time)
    {
        this.timeDrawPerImage = time;
    }
    public void setNextCurrentIndexImage()
    {
        currentIndex = (currentIndex+1)%images.length;
    }
    public void setPosition(int x, int y)
    {
        currentPosition.setPos(x, y);
    }
    
   
}
