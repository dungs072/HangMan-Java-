package Animation;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.Entities.InvisibleEntity;
public class Animation extends InvisibleEntity{

    private BufferedImage[] images;
    private long timeToDrawPerFrame = 0;
    private long currentTimeAc = 0;
    private int currentIndex = 0;
    private boolean isLoop = true;
    public Animation(int x, int y, int width, int height,BufferedImage[] images,boolean isLoop) {
        super(x, y, width, height);
        this.images = images;
        this.isLoop = isLoop;
    }
    public void paint(Graphics2D g2,long timeDeltaTime)
    {
        if(isLoop)
        {
            currentTimeAc+=timeDeltaTime;
            if(currentTimeAc>=timeToDrawPerFrame)
            {
                currentTimeAc = 0;
                drawImagePerTimeSpecify(g2);
                currentIndex = (currentIndex+1)%images.length;
            }
        }
        else
        {
            
            drawImagePerTimeSpecify(g2);
            
        }
       
    }
    private void drawImagePerTimeSpecify(Graphics2D g2)
    {
        g2.drawImage(images[currentIndex],currentPosition.getX(),currentPosition.getY(),
                                currentSize.getX(),currentSize.getY(),null);
        
    }

    public void setTimeToDrawPerFrame(long time)
    {
        this.timeToDrawPerFrame = time;
    }
    public void setNextCurrentIndexImage()
    {
        currentIndex = (currentIndex+1)%images.length;
    }
    
   
}
