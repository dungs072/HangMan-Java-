package ui;

import java.awt.image.BufferedImage;

import javax.swing.event.MouseInputListener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.MouseInfo;

public class GButton extends GText implements Clickable,MouseInputListener {

    private IEvent event;
    private boolean isMousePressed = false;
    public GButton(int x, int y, int width, int height, BufferedImage displayImage, String title, int fontSize,IEvent event) {
        super(x, y, width, height, displayImage, title, fontSize);
        this.event = event;
    }
    protected void handleEventTrigger()
    {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        if(!contain((int)mousePosition.getX(), (int)mousePosition.getY())){return;}
        if(!isMousePressed){return;}
        event.run();
    }
    @Override
    public boolean contain(int x, int y) {
        return (x<(currentPosition.getX()+currentSize.getX()) && x>currentPosition.getX()) &&
                (y<(currentPosition.getY()+currentSize.getY()) && y>currentPosition.getY());
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mousePressed(MouseEvent e) {
        isMousePressed = true;
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    
}
