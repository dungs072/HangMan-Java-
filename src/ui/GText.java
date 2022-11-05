package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Font;
import main.Entities.Entity;
import main.Entities.MyRunable;
import main.Entities.Vector2;

public class GText extends Entity implements MyRunable{

    private Font font;
    private String title;
    public GText(int x, int y,int width, int height, BufferedImage displayImage,String title,int fontSize) {
        super(x, y,width,height, displayImage);
        font = new Font("TimesRoman", Font.PLAIN, fontSize);
        this.title = title;
    }
    @Override
    public void update() {
        handleEventTrigger();
    }
    @Override
    public void paint(Graphics2D g2) {
        g2.drawImage(this.displayImage,currentPosition.getX(),currentPosition.getY(),
                                currentSize.getX(),currentSize.getY(),null);
        if(title.length()==0){return;}
        Vector2 titlePos = calculateTitleMiddlePosInRect();
        g2.setFont(font);
        g2.drawString(title, titlePos.getX(), titlePos.getY());
        
    }
    private Vector2 calculateTitleMiddlePosInRect()
    {
        int x = 0;
        int y = 0;
        int offsetPerChar = font.getSize()/2;
        x = currentPosition.getX()+ currentSize.getX()/2 - (title.length()/2)*offsetPerChar;
        y = currentPosition.getY()+currentSize.getY()/2+font.getSize()/6;
        return new Vector2(x, y);
    }
    protected void handleEventTrigger(){}
    
}
