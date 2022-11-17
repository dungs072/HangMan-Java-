package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.lang.model.util.ElementScanner14;
import javax.swing.JPanel;

import Animation.Animation;
import Animation.CoinAnimationManager;
import main.Entities.Vector2;
import main.Mechanisms.HighScore;
import main.Mechanisms.Suggestion;
import main.Sounds.Sound;
import quizzz.QuizzManager;
import ui.GButton;
import ui.GMenu;
import ui.GText;
import ui.IEvent;
enum SOUND_STATE
{
    PLAY,
    STOP,
    LOOP
}
public class GamePanel extends JPanel implements Runnable,IEvent {
    // SCREEN SETTINGS
    private final int screenWidth = 900;
    private final int screenHeight = 650;
    private final int FPS = 60;
    private final int ALPHA_BUTTON_SIZE = 26;
    private final long TIME_DRAW_ANIM_PER_IMAGE = 50;
    private final int DEFAULT_PER_WIN = 10;
    private final int DEFAULT_SCORE = 0;
    private final int DEFAULT_COIN = 100;

    private long timeDeltaTime = 0;

    private BufferedImage backgroundImage;
    private Thread gameThread;

    private ImageManager imageManager;
    private QuizzManager quizzManager;
    private String answer = "";

    private CoinAnimationManager coinAnimationManager;
    private Animation hangManWinAnimation;

    private Animation firstCloud;
    private Animation secondCloud;

    private Player player;

    private GText hangManImageDisplay;
    private GText answerDisplay;
    private GText coinDisplay;
    private GText coinAmount;
    private GText titleDisplay;
    private GText scoreAmount;
    private GText scoreDisplay;
    private GText scoreOverPopup;


    private ArrayList<GText> underscores;
    private ArrayList<GText> charAnswers;

    private ArrayList<Animation> circleAnims;
    private ArrayList<Animation> xAnims;

    private GButton pauseButton;

    private GMenu mainMenu;
    private GButton continueButton;
    private GButton playGameButton;
    private GButton highScoreButton;
    private GButton exitGameButton;
    private Animation poleAnimation;
    private Animation titleAnimation;

    private GMenu pauseMenu;
    private GButton playAgainButton;
    private GButton resumeButton;
    private GButton exitMainMenuButton;

    private GMenu popUpOverWindow;
    private GButton replayButton;

    private GMenu popUpWinWindow;
    private GButton nextQuesButton;

    private GButton[] alphaButtons = new GButton[ALPHA_BUTTON_SIZE];

    private GButton suggestionButton;
    private GText timesText;
    private GText coinTextInTimes;
    private GText timesAmountText;

    private GText highScoreTitle;
    private GText[] rankTexts;
    private GText[] nameTexts;
    private GText[] scoreTexts;
    private GButton exitMainMenuHighScoreButton;
    private GText exitMainMenuHighScoreButtonText;
    private HighScore highScore;

    private Suggestion suggestion;

    private int currentIndexHangMan = 0;
    private int countNumberCharRight = 0;
    private int countNumberCharFalse = 0;
    private int coinAddedPerLevel = 0;

    private boolean isGameOver = false;
    private boolean isWin = false;
    private boolean isDisplayAnswer = false;
    private boolean isPausing = false;
    private boolean isShowUpMenu = true;
    private boolean isContinued = false;
    private boolean isHighScore = false;
    
    public GamePanel(){
        getBackgroundImageFromSource();
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        
    }
    private void getBackgroundImageFromSource()
    {
        try
        {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/Assets/Background.png"));
        }catch(IOException e)
        {
            e.printStackTrace();
        }
 
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    private void start()
    {
        underscores = new ArrayList<>();
        charAnswers = new ArrayList<>();
        circleAnims = new ArrayList<>();
        xAnims = new ArrayList<>();
        imageManager = new ImageManager();
        quizzManager = new QuizzManager();
        player = new Player(DEFAULT_SCORE,DEFAULT_COIN);
        suggestion = new Suggestion();
        highScore = new HighScore();
        createHighScore();
        createTitleAnimation();
        createPoleAnimation();
        createMainMenu();
        createPauseMenu();
        createPauseButton();
        createHangmanWinDisplay();
        createCoinAnimationManager();
        createTitleDisplay();
        createAnswerDisplay();
        createCoinDisplay();
        createScoreDisplay();
        createSuggestionButton();
        createCloud();
        loadAlphaButton();
        loadHangMan();
        loadMenus();
        handleSoundEffect(5, SOUND_STATE.LOOP);
    }
    @Override
    public void run() {
        start();
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime()+drawInterval;

        while(gameThread!=null)
        {           
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime-System.nanoTime();
                remainingTime = remainingTime/1000000;
                if(remainingTime < 0){
                    remainingTime = 0;
                }
                timeDeltaTime = (long) remainingTime;
                Thread.sleep(timeDeltaTime);
                nextDrawTime +=drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void handleEvent()//pain way
    {
        Point mousePoint = getMousePosition();
        if(mousePoint==null){return;}
        int mX = (int)mousePoint.getX();
        int mY = (int)mousePoint.getY();
       
        if(isGameOver)
        {
            replayButton.update(timeDeltaTime, mX, mY);
        }
        else if(isWin)
        {
            nextQuesButton.update(timeDeltaTime,mX,mY);
            coinAnimationManager.update(timeDeltaTime);
        }
        else if(isPausing)
        {
            resumeButton.update(timeDeltaTime,mX,mY);
            playAgainButton.update(timeDeltaTime,mX,mY);
            exitMainMenuButton.update(timeDeltaTime,mX,mY);
        }
        else if(isShowUpMenu)
        {
            continueButton.update(timeDeltaTime,mX,mY);
            playGameButton.update(timeDeltaTime,mX,mY);
            highScoreButton.update(timeDeltaTime,mX,mY);
            exitGameButton.update(timeDeltaTime,mX,mY);
            
        }
        else if(isHighScore)
        {
            exitMainMenuHighScoreButton.update(timeDeltaTime,mX,mY);
        }
        else
        {
            for(int i =0;i<ALPHA_BUTTON_SIZE;i++)
            {
                alphaButtons[i].update(timeDeltaTime,mX, mY);
            }
            suggestionButton.update(timeDeltaTime, mX, mY);
            pauseButton.update(timeDeltaTime,mX,mY);
        }
        
    }
    public void update()
    {
        handleEvent();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if(backgroundImage!=null)
        {
            g2.drawImage(backgroundImage,0,0,screenWidth,screenHeight,null);
        }
        if(!isGameOver&&!isPausing&&!isShowUpMenu&&!isHighScore)
        {
            drawAlphaButton(g2);
        }
        if(isShowUpMenu)
        {
            drawPoleAnimation(g2);
            drawTitleAnimation(g2);
            drawMainMenu(g2);
           
        }
        else if(isHighScore)
        {
            drawHighScore(g2);
        }
        else
        {
            if(!isPausing)
            {
                drawHangmanWinAnimation(g2);
                drawTitleDisplay(g2);
                drawCoinDisplay(g2);
                drawScoreDisplay(g2);
                drawAnswerDisplay(g2);
                drawUnderscoreText(g2);
                drawHangmanAnimation(g2);
                drawCircleAnimations(g2);
                drawXAnimations(g2);
                drawMenus(g2);
                drawClouds(g2);
                
                drawPauseButton(g2);
                drawSuggestionButton(g2);
                drawCoinAnimationManager(g2);
            }
            else
            {
                drawPauseMenu(g2);
            }
        }
        g2.dispose();//save memory
    }
    private void drawHighScore(Graphics2D g2)
    {
        if(highScoreTitle==null||exitMainMenuHighScoreButton==null){return;}
        if(rankTexts==null||nameTexts==null||scoreTexts==null){return;}
        highScoreTitle.paint(g2);
        exitMainMenuHighScoreButton.paint(g2);
        exitMainMenuHighScoreButtonText.paint(g2);

        for(int i =0;i<rankTexts.length;i++)
        {
            if(rankTexts[i]==null||nameTexts[i]==null||scoreTexts[i]==null){continue;}
            rankTexts[i].paint(g2);
            nameTexts[i].paint(g2);
            scoreTexts[i].paint(g2);
        }
    }
    private void drawTitleAnimation(Graphics2D g2)
    {
        if(titleAnimation==null){return;}
        titleAnimation.paint(g2, timeDeltaTime);
    }
    private void drawPoleAnimation(Graphics2D g2)
    {
        if(poleAnimation==null){return;}
        poleAnimation.paint(g2, timeDeltaTime);
    }
    private void drawMainMenu(Graphics2D g2)
    {
        if(mainMenu==null){return;}
        mainMenu.paint(g2);
    }
    private void drawPauseMenu(Graphics2D g2)
    {

        if(isGameOver||isWin){return;}
        if(pauseMenu==null){return;}
        pauseMenu.paint(g2);
    }
    private void drawPauseButton(Graphics2D g2)
    {
        if(isGameOver||isWin||isPausing){return;}
        if(pauseButton==null){return;}
        pauseButton.paint(g2);
    }
    private void drawClouds(Graphics2D g2)
    {
        if(isWin||isGameOver){return;}
        if(firstCloud==null||secondCloud==null){return;}
        firstCloud.paint(g2, timeDeltaTime);
        secondCloud.paint(g2, timeDeltaTime);
    }
    private void drawSuggestionButton(Graphics2D g2)
    {
        if(isGameOver||isWin){return;}
        if(suggestionButton==null){return;}
        suggestionButton.paint(g2);
        timesText.paint(g2);
        timesAmountText.paint(g2);
        coinTextInTimes.paint(g2);
    }
    private void drawHangmanWinAnimation(Graphics2D g2)
    {
        if(!isWin){return;}
        hangManWinAnimation.paint(g2, timeDeltaTime);
    }
    private void drawCoinAnimationManager(Graphics2D g2)
    {
        if(!isWin){return;}
        if(coinAnimationManager==null){return;}
        coinAnimationManager.paint(g2);
    }
    private void drawTitleDisplay(Graphics2D g2)
    {
        if(isGameOver||isWin){return;}
        if(titleDisplay==null){return;}
        titleDisplay.paint(g2);
    }
    private void drawScoreDisplay(Graphics2D g2)
    {
        if(isGameOver){return;}
        if(scoreDisplay==null||scoreAmount==null){return;}
        scoreDisplay.paint(g2);
        scoreAmount.paint(g2);
    }
    private void drawCoinDisplay(Graphics2D g2)
    {
        if(isGameOver){return;}
        if(coinDisplay==null||coinAmount==null){return;}
        coinDisplay.paint(g2);
        coinAmount.paint(g2);
    }
    private void drawAnswerDisplay(Graphics2D g2)
    {
        if(!isDisplayAnswer){return;}
        answerDisplay.paint(g2);
    }
    private void drawAlphaButton(Graphics2D g2)
    {
        for(var button:alphaButtons)
        {
            if(button==null){continue;}
            button.paint(g2);
        }
    }
    private void drawUnderscoreText(Graphics2D g2)
    {
        for(int i =0;i<underscores.size();i++)
        {
            underscores.get(i).paint(g2);
            charAnswers.get(i).paint(g2);
        }
    }
    private void drawHangmanAnimation(Graphics2D g2)
    {
        if(hangManImageDisplay==null){return;}
        hangManImageDisplay.paint(g2);
    }
    private void drawCircleAnimations(Graphics2D g2)
    {
        for(int i =0;i<circleAnims.size();i++)
        {
            circleAnims.get(i).paint(g2, timeDeltaTime);
        }
    }
    private void drawXAnimations(Graphics2D g2)
    {
        for(int i =0;i<xAnims.size();i++)
        {
            xAnims.get(i).paint(g2, timeDeltaTime);
        }
    }
    private void drawMenus(Graphics2D g2)
    {
        drawOverWindow(g2);
        drawWinWindow(g2);
        
    }
    private void drawOverWindow(Graphics2D g2) {
        if(popUpOverWindow!=null)
        {
            popUpOverWindow.paint(g2);
        }
    }
    private void drawWinWindow(Graphics2D g2)
    {
        if(popUpWinWindow!=null)
        {
            popUpWinWindow.paint(g2);
        }
    }
    
    private void createHighScore()
    {
        int maxScore = 10;
        int offsetY = 50;
        int offsetX = 225;
        int startY = 80;

        rankTexts = new GText[maxScore];
        nameTexts = new GText[maxScore];
        scoreTexts = new GText[maxScore];
        highScoreTitle = new GText(360, 20, 200, 50, 
                                null, "HIGH SCORE", 30);
        exitMainMenuHighScoreButton = new GButton(10, 590, 250, 50, 
                                                imageManager.getBackgroundButton(), 
                                                imageManager.getBackgroundClickedButton(), 
                                                "EXIT MAIN MENU",20,null);
        exitMainMenuHighScoreButtonText = new GText(10, 590, 250, 50, 
                                                null,"EXIT MENU", 25);
        
        String title = "";
        for(int i =0;i<maxScore;i++)
        {    
            if(i==0)
            {
                title = Integer.toString(i+1)+"st";
            }
            else if(i==1)
            {
                title = Integer.toString(i+1)+"nd";
            }
            else if(i==2)
            {
                title = Integer.toString(i+1)+"rd";
            }
            else
            {
                title = Integer.toString(i+1)+"th";
            }
            rankTexts[i] = new GText(200, i*offsetY+startY, 50, 50, 
                                    null, title, 30);
            nameTexts[i] = new GText(200+offsetX, i*offsetY+startY, 50, 50, 
                                    null, title, 30);
            scoreTexts[i] = new GText(200+offsetX*2, i*offsetY+startY, 50, 50, 
                                    null, title, 30);
        }
        addMouseListener(exitMainMenuHighScoreButton);
        bindingEventForExitMainMenuHighScoreButton();
    }
    private void createTitleAnimation()
    {
        titleAnimation = new Animation(100, 10, 700, 100, imageManager.getTitleImages(), true, 500);
    }
    private void createPoleAnimation()
    {
        poleAnimation = new Animation(500, 150, 400, 400, imageManager.getPoleImages(), true, 100);
    }
    private void createMainMenu()
    {
        mainMenu = new GMenu(30, 100, 350, 550, imageManager.getBackgroundMenuImage());
        continueButton = mainMenu.createButton(50, 90 , 250, 100, 
                                                imageManager.getBackgroundButton(), 
                                                imageManager.getBackgroundClickedButton(), null);
        mainMenu.createText(50, 90, 250, 100, null, "CONTINUE", 30);
        playGameButton = mainMenu.createButton(50, 190, 250, 100, 
                                                imageManager.getBackgroundButton(), 
                                                imageManager.getBackgroundClickedButton(), null);
        mainMenu.createText(50,190,250,100,null,"PLAY",30);
        highScoreButton = mainMenu.createButton(50, 290, 250, 100, 
                                                imageManager.getBackgroundButton(), 
                                                imageManager.getBackgroundClickedButton(), null);
        mainMenu.createText(50,290,250,100,null,"HIGH SCORE",30);
        exitGameButton = mainMenu.createButton(50, 390, 250, 100, 
                                                imageManager.getBackgroundExitButton(), 
                                                imageManager.getBackgroundExitClickedButton(), null);
        mainMenu.createText(50,390,250,100,null,"EXIT",30);
        addMouseListener(continueButton);
        addMouseListener(playGameButton);
        addMouseListener(highScoreButton);
        addMouseListener(exitGameButton);
        bindingEventForContinueButton();
        bindingEventForPlayGameButton();
        bindingEventForHighScoreButton();
        bindingEventForExitGameButton();

    }
    private void createPauseMenu()
    {
        pauseMenu = new GMenu(260, 100, 350, 450, imageManager.getPauseMenuImage());
        pauseMenu.createText(73, 10, 200, 30, null, "PAUSE", 30);
        resumeButton = pauseMenu.createButton(50, 70, 250, 100, 
                                                imageManager.getBackgroundButton(), 
                                                imageManager.getBackgroundClickedButton(), null);
        pauseMenu.createText(50,70, 250, 100, null,"RESUME",30);
        playAgainButton = pauseMenu.createButton(50, 195, 250, 100,
                                                    imageManager.getBackgroundButton() ,
                                                    imageManager.getBackgroundClickedButton(), null);
        pauseMenu.createText(50,190, 250, 100, null,"PLAY AGAIN",30);
        exitMainMenuButton = pauseMenu.createButton(50, 320, 250, 100,
                                                    imageManager.getBackgroundExitButton(),
                                                    imageManager.getBackgroundExitClickedButton(), null);
        pauseMenu.createText(50,320, 250, 100, null,"EXIT MENU",30);
        
        addMouseListener(exitMainMenuButton);
        addMouseListener(resumeButton);
        addMouseListener(playAgainButton);
        bindingEventForResumeButton();
        bindingEventForPlayAgainButton();
        bindingEventForExitMenuButton();
    
    }
    private void createPauseButton()
    {
        pauseButton = new GButton(20, 50, 75, 75, 
                    imageManager.getPauseButtonImage(), 
                    imageManager.getPauseClickedButtonImage(), 
                    "", 1, null);
        addMouseListener(pauseButton);
        bindingEventForPauseButton();
    }
    private void createCloud()
    {
        firstCloud = new Animation(125, 10, 150, 100, 
            imageManager.getCloudImages(), true, 500);
        secondCloud = new Animation(600, 30, 150, 100, 
            imageManager.getCloudImages(), true, 500);
    }
    private void createCoinAnimationManager()
    {
        coinAnimationManager = new CoinAnimationManager(420,430, 740,20,
                                                DEFAULT_PER_WIN,imageManager.getCoinImages());
        bindingEventForCoinAnimationManager();
    }
    private void createHangmanWinDisplay()
    {
        hangManWinAnimation = new Animation(150, -10, 
                    550, 325, imageManager.getHangmanWinImages(),true,100);
    }
    private void createTitleDisplay()
    {
        titleDisplay = new GText(345, 305, 200, 50, 
                                imageManager.getTitleImage(),"Your Title", 25);
    }
    private void createAnswerDisplay()
    {
        answerDisplay = new GText(450, 200, 200, 50,null,
                                "ANSWER: ", 20);
    }
    private void createScoreDisplay()
    {
        scoreDisplay = new GText(30, -10, 50, 50, 
                                null, "SCORE: ", 20);
        scoreAmount = new GText(90, -10, 50, 50,
                                null,Integer.toString(player.getScore()), 20);
    }
    private void createCoinDisplay()
    {
        coinDisplay = new GText(740, 20, 50, 50, 
                imageManager.getCoinImage(), "", 1);
        coinAmount = new GText(780, 20, 50, 50, null,
                                Integer.toString(player.getCoin()), 20);
    }
    private void createQuestion()
    {
        String[] texts = quizzManager.getQuestion().split(":");
        answer = texts[1];
        
        if(!texts[1].equals("-1"))
        {
            System.out.println(answer);
            titleDisplay.setTitle(texts[0].toUpperCase());
            createUnderscoreDisplay(answer.length());
            suggestion.setAnswer(answer);
        }
        else
        {
            //if full answer
            quizzManager.resetUsedTitle();
            createQuestion();
        }
        
        
    }
    private void createUnderscoreDisplay(int quantity)
    {
        underscores.clear();
        charAnswers.clear();
        int middleOffscreenPosX = screenWidth/2;
        int posy = 365;
        int offsetX = 10;
        int width = 50;
        int height = 50;
        int offsetY = 5;
        if(quantity%2==0)
        {
            int k =-quantity/2;
            for(int i =0;i<quantity;i++)
            {
                int posX = middleOffscreenPosX+(k*(offsetX+width));
                
                underscores.add(new GText(posX, posy, width, height, imageManager.getRandomUnderscoreImage(), "", 20));
                charAnswers.add(new GText(posX, posy-offsetY, width, height, null, "", 20));
                k++;
            }
        }
        else
        {
            int k = -quantity/2;
            for(int i =0;i<quantity;i++)
            {
                int temp = k>0?1:-1;
                int notWidth = k==1?0:width;
                int posX = middleOffscreenPosX+(k*(offsetX+notWidth)+temp*width/2);
                if(k>1)
                {
                    posX = middleOffscreenPosX+((k-1)*(width)+(k)*offsetX+temp*width/2);
                }
                if(k==0)
                {
                    posX = middleOffscreenPosX-width/2;
                    
                }
               
                underscores.add(new GText(posX, posy, width, height, imageManager.getRandomUnderscoreImage(), "", 20));
                charAnswers.add(new GText(posX, posy-offsetY, width, height, null, "", 20));
                k++;
            
            }
        }
        
        
    }
    private void createSuggestionButton()
    {
        coinTextInTimes = new GText(782,97, 20, 20, 
                                    imageManager.getCoinImage(), "", 1);
        timesAmountText = new GText(789, 88, 40, 40, 
                                    null, "x"+Integer.toString(suggestion.getCostForSuggestion()),
                                     15);
        timesText = new GText(785,92, 40, 40,
                            imageManager.getTimesImage(),"",15);
        suggestionButton = new GButton(780, 120, 50, 50, 
                                        imageManager.getSuggestionButtonImage(), 
                                        imageManager.getSuggestionClickedButtonImage(), 
                                        "", 1, null);
        addMouseListener(suggestionButton);
        bindingEventForSuggestionButton();
    }

    private void loadAlphaButton()
    {
        int startPointX = 100;
        int startPointY = 430;
        int offsetX = 75;
        int offsetY = 75;
        int row = 0;
        int col = 0;
        for(int i =0;i<ALPHA_BUTTON_SIZE;i++)
        {
            if(i==10)
            {
                col = 0;
                row++;
            }
            if(i==20)
            {
                col = 2;
                row++;
            }
            alphaButtons[i] = new GButton(startPointX+col*offsetX,startPointY+row*offsetY,
                            45,45,imageManager.getAlphaImages()[i],null,"",10,this);
            alphaButtons[i].setInforObj(Character.toString(i+'A'));
            addMouseListener(alphaButtons[i]);
            col++;
            
        }
    
    }
    private void loadHangMan()
    {

        hangManImageDisplay = new GText(150, -10, 550, 325, 
                                imageManager.getHangmanImages()[0],"",20);
    }
    private void loadMenus()
    {
        loadOverPopupWindow();
        loadWinPopupWindow();
    }
    private void loadOverPopupWindow() {
        popUpOverWindow = new GMenu(0, 325, 900,325, imageManager.getPopupImage());
        popUpOverWindow.setCanDisplay(false);
        popUpOverWindow.createText(325, 20, 200, 50, null,"OOPS... YOU FAILED!",25);
        scoreOverPopup =  popUpOverWindow.createText(325, 75, 200, 50,null,"Your score: ",25);
        replayButton = popUpOverWindow.createButton(390, 150, 100,100,imageManager.getReplayButtonImage(),
                                                    imageManager.getReplayClickedButtonImage(),null);
        bindingEventForReplayButton();
        addMouseListener(replayButton);
    }
    private void loadWinPopupWindow()
    {
       
        popUpWinWindow = new GMenu(0, 325, 900,325, imageManager.getPopupImage());
        popUpWinWindow.setCanDisplay(false);
        popUpWinWindow.createText(310,20,250,100, imageManager.getNewRecordImage(),"",1);
        popUpWinWindow.createText(330, 75,200,200,imageManager.getChestImage(),"",1);
        nextQuesButton = popUpWinWindow.createButton(410,245,50,50,
                            imageManager.getNextButtonImage(),imageManager.getNextClickedButtonImage(),null);
        bindingEventForNextButton();
        addMouseListener(nextQuesButton);
    }
    
    @Override
    public void trigger(Object obj) {
        if(!(obj instanceof String)){return;}
        String info = (String)obj;
        handleSelectAlphaButton(info);
        
    }
    private void handleSelectAlphaButton(String info) {

        boolean isRightChar = false;
        GButton currentButton = alphaButtons[info.charAt(0)-'A'];
        currentButton.setIsClickable(false);
        handleSoundEffect(1, SOUND_STATE.PLAY);
        for(int i =0;i<answer.length();i++)
        {
            if(info.toLowerCase().compareTo(Character.toString(answer.charAt(i)))==0)
            {
                charAnswers.get(i).setDisplayImage(imageManager.getAlphaImages()[info.charAt(0)-'A']);
                isRightChar = true;
                countNumberCharRight++;
                suggestion.setCharIsAnswered(info.toLowerCase());
            }
        }
        suggestion.HString();
        if(isRightChar)
        {
            handleRightChar(currentButton);
        }
        else
        {
           handleWrongChar(currentButton);
        }
    }
    
    private void handleRightChar(GButton currentButtonClicked)
    {
        Vector2 pos = currentButtonClicked.getCurrentPosition();
        Vector2 size = currentButtonClicked.getCurrentSize();
        circleAnims.add(new Animation(pos.getX(), pos.getY(), size.getX(), size.getY(),
                    imageManager.getCircleImages(),false,TIME_DRAW_ANIM_PER_IMAGE));
        if(countNumberCharRight==answer.length())
        {
            handleGameWin();
        }
    }
    private void handleWrongChar(GButton currentButtonClicked)
    {
        Vector2 pos = currentButtonClicked.getCurrentPosition();
        Vector2 size = currentButtonClicked.getCurrentSize();
        xAnims.add(new Animation(pos.getX(), pos.getY(), size.getX(), size.getY(), 
                    imageManager.getXImages(),false,TIME_DRAW_ANIM_PER_IMAGE));

        
        currentIndexHangMan = (currentIndexHangMan+1)%imageManager.getLengthHangmanImages();
        hangManImageDisplay.setDisplayImage(imageManager.getHangmanImages()[currentIndexHangMan]);
        countNumberCharFalse++;
        if(currentIndexHangMan==imageManager.getLengthHangmanImages()-2)
        {
            handleGameOver();
        }
        
    }
    private void handleGameWin()
    {
        isWin = true;
        
        popUpWinWindow.setCanDisplay(true);
        coinAnimationManager.setIsDrawCoinMove(true);
        changeStateClickableAlphaButtons(false);
        changeStateDisplayAnswer(true);
        coinAnimationManager.setAmountCoinNeed(DEFAULT_PER_WIN-countNumberCharFalse);
        isDisplayAnswer = true;
        currentIndexHangMan = 0;
        hangManImageDisplay.setDisplayImage(imageManager.getHangmanImages()[currentIndexHangMan]);
        int amount = player.getScore()+(DEFAULT_PER_WIN-countNumberCharFalse)*10;
        changeAmountScore(amount);
        handleSoundEffect(4, SOUND_STATE.PLAY);
    }
    private void handleGameOver()
    {
        isGameOver = true;
        popUpOverWindow.setCanDisplay(true);
        scoreOverPopup.setTitle("Your score: "+Integer.toString(player.getScore()));
        changeStateClickableAlphaButtons(false);
        changeStateDisplayAnswer(true);
        currentIndexHangMan = (currentIndexHangMan+1)%imageManager.getLengthHangmanImages();
        quizzManager.resetUsedTitle();
        handleSoundEffect(3, SOUND_STATE.PLAY);
        
    }
    private void changeStateDisplayAnswer(boolean state)
    {
        isDisplayAnswer = state;
        if(state)
        {
            answerDisplay.setTitle("ANSWER: "+answer.toUpperCase());
        }
    }
    private void changeStateClickableAlphaButtons(boolean state)
    {
        for(var button: alphaButtons)
        {
            button.setIsClickable(state);
        }
    }
    private void bindingEventForReplayButton()
    {
        replayButton.subscribeEvent(new EventBinding(){
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                isGameOver = false;
                quizzManager.resetUsedTitle();
                resetLevel();
                popUpOverWindow.setCanDisplay(false);
                coinAddedPerLevel = 0;
                changeAmountCoin(DEFAULT_COIN);
                changeAmountScore(DEFAULT_SCORE);
                handleSoundEffect(6, SOUND_STATE.LOOP);
            }
        });
    }
    private void bindingEventForNextButton()
    {
        nextQuesButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                isWin = false;
                resetLevel();
                popUpWinWindow.setCanDisplay(false);
                coinAnimationManager.switchOffDisplayCoins();
                int coinAdded = player.getCoin()+(DEFAULT_PER_WIN-countNumberCharFalse-coinAddedPerLevel);
                coinAddedPerLevel = 0;
                changeAmountCoin(coinAdded);
            }
        });
    }
    private void bindingEventForCoinAnimationManager()
    {
        coinAnimationManager.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                handleSoundEffect(2, SOUND_STATE.PLAY);
                player.addCoin(1);
                coinAddedPerLevel++;
                coinAmount.setTitle(Integer.toString(player.getCoin()));
            }
        });
    }
    private void bindingEventForSuggestionButton()
    {
        suggestionButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                if(player.getCoin()<suggestion.getCostForSuggestion()){return;}
                changeAmountCoin(player.getCoin()-suggestion.getCostForSuggestion());
                String info = suggestion.getCharSuggestion();
                if(info.isBlank()){return;}
                handleSelectAlphaButton(info.toUpperCase());
            }
        });
    }
    private void bindingEventForPauseButton()
    {
        pauseButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                
                super.trigger(obj);
                isPausing = true;
            }
        });
    }
    private void bindingEventForResumeButton()
    {
        resumeButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                isPausing = false;
            }
        }
        );
    }
    private void bindingEventForPlayAgainButton()
    {
        playAgainButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                isPausing = false;
                playGame();
                handleSoundEffect(6, SOUND_STATE.LOOP);
            }
        }
        );
    }
    private void bindingEventForExitMenuButton()
    {
        exitMainMenuButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                isPausing = false;
                isShowUpMenu = true;
                handleSoundEffect(5, SOUND_STATE.LOOP);
            }
        }
        );
    }
    private void bindingEventForContinueButton()
    {
        continueButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                isShowUpMenu = !isContinued;
                if(isShowUpMenu){return;}
                handleSoundEffect(6, SOUND_STATE.LOOP);
            }
        }
        );
    }
    private void bindingEventForPlayGameButton()
    {
        playGameButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                isShowUpMenu = false;
                playGame();
                handleSoundEffect(6, SOUND_STATE.LOOP);
            }
        });
    }
    private void bindingEventForHighScoreButton()
    {
        highScoreButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                loadHighScorePanel();
                isShowUpMenu = false;
                isHighScore = true;
            }
        }
        );
    }
    private void bindingEventForExitGameButton()
    {
        exitGameButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                System.exit(0);
            }
        }
        );
    }
    private void bindingEventForExitMainMenuHighScoreButton()
    {
        exitMainMenuHighScoreButton.subscribeEvent(new EventBinding()
        {
            @Override
            public void trigger(Object obj) {
                super.trigger(obj);
                isHighScore = false;
                isShowUpMenu = true;
            }
        }
        );
    }
    private void playGame()
    {
        isContinued = true;
        quizzManager.resetUsedTitle();
        resetLevel();
        
        coinAddedPerLevel = 0;
        changeAmountCoin(DEFAULT_COIN);
        changeAmountScore(DEFAULT_SCORE);
    }
    private void changeAmountCoin(int amount)
    {
        player.setCoin(amount);
        coinAmount.setTitle(Integer.toString(player.getCoin()));
    }
    private void changeAmountScore(int amount)
    {
        player.setScore(amount);
        scoreAmount.setTitle(Integer.toString(player.getScore()));
    }
    private void resetLevel()
    {
        countNumberCharRight = 0;
        countNumberCharFalse = 0;
        changeStateDisplayAnswer(false);
        changeStateClickableAlphaButtons(true);
        createQuestion();
        circleAnims.clear();
        xAnims.clear();
        currentIndexHangMan = 0;
        hangManImageDisplay.setDisplayImage(imageManager.getHangmanImages()[currentIndexHangMan]);
    }
    private void handleSoundEffect(int i , SOUND_STATE sound_STATE)
    {
        Sound instance = Sound.Instance();
        instance.setFile(i);
        if(sound_STATE==SOUND_STATE.PLAY)
        {
            instance.playSound();
        }
        else if(sound_STATE==SOUND_STATE.STOP)
        {
            instance.stopSound();
        }
        else
        {
            if(i==5)
            {
                instance.playIntroSound();
            }
            else if(i==6)
            {
                instance.playGamePlaySound();
            }
        }
    }
    private void loadHighScorePanel()
    {
        int maxScores = 10;
        String name = "";
        String score = "";
        for(int i =0;i<maxScores;i++)
        {
            String rawData = highScore.getHighScore(i);
            if(rawData==""){name = "";score = "";}
            else
            {
                String[] texts = rawData.split(" ");
                name = texts[0];
                score = texts[1];
            }
            nameTexts[i].setTitle(name);
            scoreTexts[i].setTitle(score);
        }
    }
}
class EventBinding implements IEvent
{
    @Override
    public void trigger(Object obj) {
        Sound.Instance().setFile(0);
        Sound.Instance().playSound();
        return;
    }
}
