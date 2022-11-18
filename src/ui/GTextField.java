package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.Rectangle;

public class GTextField extends GText implements MouseListener{
    private IEvent event = null;
    private int lengthTitle = 10;
    private boolean isFocusable = false;
    private Rectangle rect;
    public GTextField(int x, int y, int width, int height, BufferedImage displayImage, String title, int fontSize) {
        super(x, y, width, height, displayImage, title, fontSize);
        rect = getBounds();
    }
    private Rectangle getBounds()
    {
        return new Rectangle(currentPosition.getX(),currentPosition.getY(),currentSize.getX(),currentSize.getY());
    }
    public void setLengthTitle(int length)
    {
        lengthTitle = length;
    }
    public void subscribeEvent(IEvent event)
    {
        this.event = event;
    }
    public String getTitle(){return title;}
    private void runEnterCommand()
    {
        if(event==null){return;}
        event.trigger(null);
    }
    public void updateTitle(char command)
    {
        if(!isFocusable){return;}
        if((command>='A'&&command<='Z')||(command>='a'&&command<='z')||(command>='0'&&command<='9'))
        {
            if(title.length()>=lengthTitle){return;}
            title += Character.toString(command);
        }
        else if(command==8||command==127)
        {
            if(title.length()==0){return;}
            title = title.substring(0,title.length()-1);
        }
        else if(command == 13)
        {
            runEnterCommand();
        }
    }
    public void paint(Graphics2D g2) {
        super.paint(g2);
        if(displayImage==null)
        {
            g2.drawRect(currentPosition.getX(), currentPosition.getY(), 
                        currentSize.getX(), currentSize.getY());
            g2.setColor(Color.WHITE);
        }
    }
    public void setCanFocusable(boolean state)
    {
        isFocusable = state;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if(!rect.contains(e.getX(),e.getY())){ isFocusable = false;return;}
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            isFocusable = true;
        }
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }

   
    
}
