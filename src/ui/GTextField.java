package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;



public class GTextField extends GText {
    private IEvent event = null;
    private int lengthTitle = 10;

    public GTextField(int x, int y, int width, int height, BufferedImage displayImage, String title, int fontSize) {
        super(x, y, width, height, displayImage, title, fontSize);
    }
    public void setLengthTitle(int length)
    {
        lengthTitle = length;
    }
    public void subscribeEvent(IEvent event)
    {
        this.event = event;
    }
    private void runEnterCommand()
    {
        if(event==null){return;}
        event.trigger(null);
    }
    public void updateTitle(char command)
    {
        if((command>='A'&&command<='Z')||(command>='a'&&command<='z'))
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

   
    
}
