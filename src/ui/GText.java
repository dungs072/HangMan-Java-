package ui;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Font;
import main.Entities.Entity;
import main.Entities.MyRunable;

public class GText extends Entity implements MyRunable{

    private Font font;
    protected String title;
    private AffineTransform affineTransform;
    private FontRenderContext frc;
    public GText(int x, int y,int width, int height, BufferedImage displayImage,String title,int fontSize) {
        super(x, y,width,height, displayImage);
        font = new Font("TimesRoman", Font.PLAIN, fontSize);
        this.title = title;
        affineTransform = new AffineTransform();     
        frc = new FontRenderContext(affineTransform,true,true); 
    }
    @Override
    public void update(long timeDeltaTime) {
    }
    @Override
    public void paint(Graphics2D g2) {
        //if(this.displayImage==null){return;}
        g2.drawImage(this.displayImage,currentPosition.getX(),currentPosition.getY(),
                                currentSize.getX(),currentSize.getY(),null);
        if(title.length()==0){return;}
        g2.setFont(font);
        int textWidth = (int)(font.getStringBounds(title, frc).getWidth());
        int textHeight = (int)(font.getStringBounds(title, frc).getHeight());

        int x = currentPosition.getX()+ currentSize.getX()/2 - textWidth/2;
        int y = currentPosition.getY()+currentSize.getY()/2+textHeight/4;
        g2.drawString(title, x, y);
        
    }
    public void setDisplayImage(BufferedImage displayImage)
    {
        this.displayImage = displayImage;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    
}
