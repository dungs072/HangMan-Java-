package ui;

import java.awt.image.BufferedImage;

import javax.swing.event.MouseInputListener;

import java.awt.event.MouseEvent;

public class GButton extends GText implements Clickable,MouseInputListener {

    private IEvent event;
    private String info = "";
    public GButton(int x, int y, int width, int height, BufferedImage displayImage, String title, int fontSize,IEvent event) {
        super(x, y, width, height, displayImage, title, fontSize);
        this.event = event;
    }
    public void setInfo(String info)
    {
        this.info = info;
    }
    public void subscribeEvent(IEvent event)
    {
        this.event = event;
    }
    @Override
    public boolean contain(int x, int y) {
        return (x<(currentPosition.getX()+currentSize.getX()) && x>currentPosition.getX()) &&
                (y<(currentPosition.getY()+currentSize.getY()) && y>currentPosition.getY());
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        
        if(!contain((int)e.getX(), (int)e.getY())){return;}
        System.out.println("1");
        
        if(event==null)

        event.trigger(info);
        
    }
    @Override
    public void mousePressed(MouseEvent e) {
      
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {

        
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
