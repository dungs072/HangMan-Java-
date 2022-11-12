package main;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import ui.ImageReference;
public class ImageManager {
    private final int ALPHA_IMAGES_SIZE = 26;
    private final int UNDERSCORE_IMAGES_SIZE = 3;
    private final int HANGMAN_IMAGES_SIZE = 10;
    private final int CIRCLE_IMAGES_SIZE = 6;
    private final int X_IMAGES_SIZE = 6;
    private BufferedImage[] alphaImages = new BufferedImage[ALPHA_IMAGES_SIZE];
    private BufferedImage[] underscoreImages = new BufferedImage[UNDERSCORE_IMAGES_SIZE];
    private BufferedImage[] hangmanImages = new BufferedImage[HANGMAN_IMAGES_SIZE];
    private BufferedImage[] circleImages = new BufferedImage[CIRCLE_IMAGES_SIZE];
    private BufferedImage[] xImages = new BufferedImage[X_IMAGES_SIZE];

    private ImageReference popUpImageRefer;
    private ImageReference replayButtonClickedRefer;
    private ImageReference replayButtonRefer;
    private Random random = new Random();
    public ImageManager()
    {
        loadAlphaImages();
        loadUnderscoreImages();
        loadHangManImages();
        loadCircleImages();
        loadXImages();

        popUpImageRefer = new ImageReference();
        loadRawImage(popUpImageRefer,"/Assets/PopUps/Pop_up.png");
        replayButtonRefer = new ImageReference();
        loadRawImage(replayButtonRefer, "/Assets/Buttons/PopUp/replay.png");
        replayButtonClickedRefer = new ImageReference();
        loadRawImage(replayButtonClickedRefer, "/Assets/Buttons/PopUp/replay_clicked.png");

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
        loadRawImages(underscoreImages, UNDERSCORE_IMAGES_SIZE, "/Assets/Underscores/underscore");
    }
    private void loadHangManImages()
    {
        loadRawImages(hangmanImages, HANGMAN_IMAGES_SIZE, "/Assets/Hangmans/");
    }
    private void loadCircleImages()
    {
        loadRawImages(circleImages, CIRCLE_IMAGES_SIZE, "/Assets/Circles/");
    }
    private void loadXImages()
    {
        loadRawImages(xImages, X_IMAGES_SIZE, "/Assets/Xs/");
    }
    private void loadRawImages(BufferedImage[] images, int size, String forePath)
    {
        try
        {
            for(int i =0;i<size;i++)
            {
                String path = forePath+Integer.toString(i)+".png";
                images[i] = ImageIO.read(getClass().getResourceAsStream(path));
            }

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    private void loadRawImage(ImageReference imageRefer, String path)
    {
        try
        {
            imageRefer.setImage(ImageIO.read(getClass().getResourceAsStream(path)));

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
    public BufferedImage[] getCircleImages(){return circleImages;}
    public BufferedImage[] getXImages(){return xImages;}
    public BufferedImage getPopupImage(){return popUpImageRefer.getImage();}
    public BufferedImage getReplayButtonImage(){return replayButtonRefer.getImage();}
    public BufferedImage getReplayClickedButtonImage(){return replayButtonClickedRefer.getImage();}
    public int getLengthHangmanImages(){return HANGMAN_IMAGES_SIZE;}
}
