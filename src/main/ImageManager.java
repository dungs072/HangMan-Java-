package main;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
public class ImageManager {
    private final int ALPHA_IMAGES_SIZE = 26;
    private BufferedImage[] alphaImages = new BufferedImage[ALPHA_IMAGES_SIZE];

    public ImageManager()
    {
        loadAlphaImages();
    }
    private void loadAlphaImages()
    {
        try
        {
            for(int i =0;i<ALPHA_IMAGES_SIZE;i++)
            {
                String path = "/Assets/Alphas/"+Character.toString('A'+i)+".png";
                alphaImages[i] = ImageIO.read(getClass().getResourceAsStream(path));
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
 
    }
    public BufferedImage[] getAlphaImages(){return alphaImages;}
}
