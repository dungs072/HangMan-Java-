package ui;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import java.awt.event.*;

import java.awt.event.MouseEvent;
import java.awt.Graphics2D;


public class GButton extends GText implements MouseListener {

    private IEvent event = null;
    private String info = "";
    private boolean isClickable = true;
    private Rectangle rect;
    private boolean isPressed = false;
    public GButton(int x, int y, int width, int height, BufferedImage displayImage, String title, int fontSize,IEvent event) {
        super(x, y, width, height, displayImage, title, fontSize);
        this.event = event;
        rect = getBounds();
    }
    private Rectangle getBounds()
    {
        return new Rectangle(currentPosition.getX(),currentPosition.getY(),currentSize.getX(),currentSize.getY());
    }
    public void setInfo(String info)
    {
        this.info = info;
    }
    public void subscribeEvent(IEvent event)
    {
        this.event = event;
        
    }
    public void setIsClickable(boolean state)
    {
        this.isClickable = state;
    }
    public void update(int x, int y)
    {
        if(!isPressed){return;}
        if(!isClickable){return;}
      
        if(!rect.contains(x,y)){return;}
        
        if(event==null){return;}
        System.out.println(info);
        event.trigger(info);
    }

    public void paint(Graphics2D g2) {
        if(this.displayImage==null){return;}
        g2.drawImage(this.displayImage,currentPosition.getX(),currentPosition.getY(),
                                currentSize.getX(),currentSize.getY(),null);
        g2.drawRect(currentPosition.getX(), currentPosition.getY(), currentSize.getX(), currentSize.getY());
    }
   
    
    @Override
    public void mouseClicked(MouseEvent e) {
       
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            isPressed = true;
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
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
