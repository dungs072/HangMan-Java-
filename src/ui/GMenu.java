package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Entities.Entity;
import main.Entities.MyRunable;

public class GMenu extends Entity implements MyRunable {

    private ArrayList<GButton> buttons;
    private ArrayList<GText> texts;
    public GMenu(int x, int y, int width, int height, BufferedImage displayImage) {
        super(x, y, width, height, displayImage);
        buttons = new ArrayList<>();
        texts = new ArrayList<>();
    }
    public GButton createButton(int x, int y, int width, int height, BufferedImage displayImage, BufferedImage clickedButtonImage,IEvent event)
    {
        buttons.add(new GButton(x,y,width,height,displayImage,clickedButtonImage,"",1,event));
        return buttons.get(buttons.size()-1);
    }
    public GText createText(int x, int y, int width, int height,BufferedImage BufferedImage,BufferedImage displayImage, String title, int fontSize)
    {
        texts.add(new GText(x, y, width, height, displayImage, title, fontSize));
        return texts.get(texts.size()-1);
    }
    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void paint(Graphics2D g2) {
        g2.drawImage(displayImage, currentPosition.getX(), currentPosition.getY(), 
                            currentSize.getX(),currentSize.getY(),null);
        for(var button:buttons)
        {
            button.paint(g2);
        }
        for(var text: texts)
        {
            text.paint(g2);
        }

        
    }

    
}
