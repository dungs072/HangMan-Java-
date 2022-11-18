package ui;

import java.awt.image.BufferedImage;

import java.awt.Rectangle;

import java.awt.event.*;

import java.awt.event.MouseEvent;
import java.awt.Graphics2D;

// should addlistener of every instance of button you created
public class GButton extends GText implements MouseListener {

    private static final long TIME_TRANSITION_DISPLAY_IMAGE = 100;
    private long currentTimeAC = 0;
    private long currentTimeReleaseAC = 0;
    private BufferedImage currentImage;
    private BufferedImage clickedImageButton;
    private IEvent event = null;
    private Object inforObj = null;
    private boolean isClickable = true;
    private Rectangle rect;
    private boolean isPressed = false;
    private boolean isPressedUI = false;
    private boolean isReleaseUI = false;
    private boolean isDisplaying = true;
    
    public GButton(int x, int y, int width, int height, BufferedImage displayImage,BufferedImage clickedImageButton, String title, int fontSize,IEvent event) {
        super(x, y, width, height, displayImage, title, fontSize);
        this.event = event;
        this.clickedImageButton = clickedImageButton;
        this.currentImage = displayImage;
        rect = getBounds();
        
    }
    public void setIsDisplaying(boolean state)
    {
        isDisplaying = state;
    }
    private Rectangle getBounds()
    {
        return new Rectangle(currentPosition.getX(),currentPosition.getY(),currentSize.getX(),currentSize.getY());
    }
    public void setInforObj(Object inforObj)
    {
        this.inforObj = inforObj;
    }
    public void subscribeEvent(IEvent event)
    {
        this.event = event;
        
    }
    public void setIsClickable(boolean state)
    {
        this.isClickable = state;
    }
    public void update(long timeDeltaTime,int x, int y)
    {
        if(isPressedUI && clickedImageButton!=null)
        {
            currentTimeAC+=timeDeltaTime;

            
            if(currentTimeAC>=TIME_TRANSITION_DISPLAY_IMAGE)
            {
                currentTimeAC = 0;
                isPressedUI = false;
                currentImage = displayImage;
                isReleaseUI = true;
                
            }
            else
            {
                return;
            }
        }
        if(isReleaseUI)
        {
            currentTimeReleaseAC+=timeDeltaTime;
            if(currentTimeReleaseAC>=TIME_TRANSITION_DISPLAY_IMAGE)
            {
                isReleaseUI = false;
                currentTimeReleaseAC = 0;
                RunEvent(x, y,false);
            }
        }
        
        if(clickedImageButton==null)
        {
            RunEvent(x, y,true);
        }
        
    }
    private void RunEvent(int x, int y,boolean isNeedPress)
    {
        if(!isPressed&&isNeedPress){return;}
        
        if(!isClickable){return;}
        
        if(!rect.contains(x,y)){return;}
        
        if(event==null){return;}
        event.trigger(inforObj);
    }   

    public void paint(Graphics2D g2) {
        if(this.currentImage==null){return;}
        g2.drawImage(this.currentImage,currentPosition.getX(),currentPosition.getY(),
                                currentSize.getX(),currentSize.getY(),null);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
       
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if(!isDisplaying){return;}
        if(!rect.contains(e.getX(),e.getY())){return;}
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            isPressed = true;
            if(this.clickedImageButton==null){return;}
            currentImage = clickedImageButton;
            isPressedUI = true;
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(!isDisplaying){return;}
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            isPressed = false;
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

}
