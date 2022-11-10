package main;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
public class ImageManager {
    private final int ALPHA_IMAGES_SIZE = 26;
    private final int UNDERSCORE_IMAGES_SIZE = 3;
    private final int HANGMAN_IMAGES_SIZE = 10;
    private BufferedImage[] alphaImages = new BufferedImage[ALPHA_IMAGES_SIZE];
    private BufferedImage[] underscoreImages = new BufferedImage[UNDERSCORE_IMAGES_SIZE];
    private BufferedImage[] hangmanImages = new BufferedImage[HANGMAN_IMAGES_SIZE];
    private Random random = new Random();
    public ImageManager()
    {
        loadAlphaImages();
        loadUnderscoreImages();
        loadHangManImages();
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
    private void loadUnderscoreImages()
    {
        try
        {
            for(int i =0;i<UNDERSCORE_IMAGES_SIZE;i++)
            {
                String path = "/Assets/Underscores/underscore"+Integer.toString(i)+".png";
                underscoreImages[i] = ImageIO.read(getClass().getResourceAsStream(path));
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    private void loadHangManImages()
    {
        try
        {
            for(int i =0;i<HANGMAN_IMAGES_SIZE;i++)
            {
                String path = "/Assets/Hangmans/"+Integer.toString(i)+".png";
                hangmanImages[i] = ImageIO.read(getClass().getResourceAsStream(path));
            }

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public BufferedImage[] getAlphaImages(){return alphaImages;}
    public BufferedImage getRandomUnderscoreImage()
    {
        int randIndex = random.nextInt(UNDERSCORE_IMAGES_SIZE);
        return underscoreImages[randIndex];
    }
    public BufferedImage[] getHangmanImages(){return hangmanImages;}
}
