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
    private final int COIN_IMAGE_SIZE = 10;
    private final int HANGMAN_WIN_SIZE = 10;
    private final int CLOUD_SIZE = 5;
    private BufferedImage[] alphaImages = new BufferedImage[ALPHA_IMAGES_SIZE];
    private BufferedImage[] underscoreImages = new BufferedImage[UNDERSCORE_IMAGES_SIZE];
    private BufferedImage[] hangmanImages = new BufferedImage[HANGMAN_IMAGES_SIZE];
    private BufferedImage[] circleImages = new BufferedImage[CIRCLE_IMAGES_SIZE];
    private BufferedImage[] xImages = new BufferedImage[X_IMAGES_SIZE];
    private BufferedImage[] coinImages = new BufferedImage[COIN_IMAGE_SIZE];
    private BufferedImage[] hangManWinImages = new BufferedImage[HANGMAN_WIN_SIZE];
    private BufferedImage[] cloudeImages = new BufferedImage[CLOUD_SIZE];

    private ImageReference popUpImageRefer;
    private ImageReference replayButtonClickedRefer;
    private ImageReference replayButtonRefer;
    private ImageReference nextButtonClickedRefer;
    private ImageReference nextButtonRefer;
    private ImageReference suggestionButtonClickedRefer;
    private ImageReference suggestionButtonRefer;
    private ImageReference pauseButtonClickedRefer;
    private ImageReference pauseButtonRefer;
    private ImageReference backgroundButtonRefer;
    private ImageReference backgroundButtonClickedRefer;
    private ImageReference backgroundButtonExitRefer;
    private ImageReference backgroundButtonExitClickedRefer;
    private ImageReference backgroundMenuRefer;

    private ImageReference newRecordRefer;
    private ImageReference chestRefer;
    private ImageReference coinRefer;
    private ImageReference titleRefer;
    private ImageReference timesRefer;
    private ImageReference pauseMenuRefer;
    private Random random = new Random();
    public ImageManager()
    {
        loadAlphaImages();
        loadUnderscoreImages();
        loadHangManImages();
        loadCircleImages();
        loadXImages();
        loadCoinImages();
        loadHangmanWinImages();
        loadCloudImages();

        popUpImageRefer = new ImageReference();
        loadRawImage(popUpImageRefer,"/Assets/PopUps/Pop_up.png");
        replayButtonRefer = new ImageReference();
        loadRawImage(replayButtonRefer, "/Assets/Buttons/PopUp/replay.png");
        replayButtonClickedRefer = new ImageReference();
        loadRawImage(replayButtonClickedRefer, "/Assets/Buttons/PopUp/replay_clicked.png");
        
        pauseButtonClickedRefer = new ImageReference();
        loadRawImage(pauseButtonClickedRefer, "/Assets/Buttons/Pause/pause_button_clicked.png");
        pauseButtonRefer = new ImageReference();
        loadRawImage(pauseButtonRefer, "/Assets/Buttons/Pause/pause_button_unclicked.png");
      
        nextButtonRefer = new ImageReference();
        loadRawImage(nextButtonRefer, "/Assets/Buttons/PopUp/next.png");
        nextButtonClickedRefer = new ImageReference();
        loadRawImage(nextButtonClickedRefer, "/Assets/Buttons/PopUp/next_clicked.png");

        backgroundButtonRefer = new ImageReference();
        loadRawImage(backgroundButtonRefer, "/Assets/Buttons/Background_button_unclicked.png");
        backgroundButtonClickedRefer = new ImageReference();
        loadRawImage(backgroundButtonClickedRefer, "/Assets/Buttons/Background_button_clicked.png");

        backgroundButtonExitRefer = new ImageReference();
        loadRawImage(backgroundButtonExitRefer, "/Assets/Buttons/Background_exit_unclicked.png");
        backgroundButtonExitClickedRefer = new ImageReference();
        loadRawImage(backgroundButtonExitClickedRefer, "/Assets/Buttons/Background_button_exit_clicked.png");

        newRecordRefer = new ImageReference();
        loadRawImage(newRecordRefer, "/Assets/Items/MyRecord.png");
        chestRefer = new ImageReference();
        loadRawImage(chestRefer, "/Assets/Items/Chest.png");
        coinRefer = new ImageReference();
        loadRawImage(coinRefer, "/Assets/Items/Coin.png");
        titleRefer = new ImageReference();
        loadRawImage(titleRefer, "/Assets/ImageText.png");
        suggestionButtonRefer = new ImageReference();
        loadRawImage(suggestionButtonRefer,"/Assets/Buttons/Suggestion/suggest.png");
        suggestionButtonClickedRefer = new ImageReference();
        loadRawImage(suggestionButtonClickedRefer,"/Assets/Buttons/Suggestion/suggest_clicked.png");
        timesRefer = new ImageReference();
        loadRawImage(timesRefer,"/Assets/Buttons/Suggestion/times.png");
        pauseMenuRefer = new ImageReference();
        loadRawImage(pauseMenuRefer, "/Assets/Menus/Pause/Pause_Menu.png");
        backgroundMenuRefer = new ImageReference();
        loadRawImage(backgroundMenuRefer, "/Assets/Menus/MainMenu/Background_menu.png");
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
    private void loadCoinImages()
    {
        loadRawImages(coinImages, COIN_IMAGE_SIZE, "/Assets/Items/Coins/Coin");
    }
    private void loadHangmanWinImages()
    {
        loadRawImages(hangManWinImages, HANGMAN_WIN_SIZE, "/Assets/WinHangmans/00");
    }
    private void loadCloudImages()
    {
        loadRawImages(cloudeImages, CLOUD_SIZE, "/Assets/Items/Clouds/00");
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
    public BufferedImage[] getCoinImages(){return coinImages;}
    public BufferedImage[] getHangmanWinImages(){return hangManWinImages;}
    public BufferedImage[] getCloudImages(){return cloudeImages;}
    public BufferedImage getPopupImage(){return popUpImageRefer.getImage();}
    public BufferedImage getReplayButtonImage(){return replayButtonRefer.getImage();}
    public BufferedImage getReplayClickedButtonImage(){return replayButtonClickedRefer.getImage();}
    public BufferedImage getNextButtonImage(){return nextButtonRefer.getImage();}
    public BufferedImage getNextClickedButtonImage(){return nextButtonClickedRefer.getImage();}
    public BufferedImage getBackgroundButton(){return backgroundButtonRefer.getImage();}
    public BufferedImage getBackgroundClickedButton(){return backgroundButtonClickedRefer.getImage();}
    public BufferedImage getBackgroundExitButton(){return backgroundButtonExitRefer.getImage();}
    public BufferedImage getBackgroundExitClickedButton(){return backgroundButtonExitClickedRefer.getImage();}
    public BufferedImage getChestImage(){return chestRefer.getImage();}
    public BufferedImage getNewRecordImage(){return newRecordRefer.getImage();}
    public BufferedImage getCoinImage(){return coinRefer.getImage();}
    public BufferedImage getTitleImage(){return titleRefer.getImage();}
    public BufferedImage getSuggestionButtonImage(){return suggestionButtonRefer.getImage();}
    public BufferedImage getSuggestionClickedButtonImage(){return suggestionButtonClickedRefer.getImage();}
    public BufferedImage getPauseButtonImage(){return pauseButtonRefer.getImage();}
    public BufferedImage getPauseClickedButtonImage(){return pauseButtonClickedRefer.getImage();}
    public BufferedImage getTimesImage(){return timesRefer.getImage();}
    public BufferedImage getPauseMenuImage(){return pauseMenuRefer.getImage();}
    public BufferedImage getBackgroundMenuImage(){return backgroundMenuRefer.getImage();}
    public int getLengthHangmanImages(){return HANGMAN_IMAGES_SIZE;}
}
