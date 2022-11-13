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
import javax.swing.JPanel;

import Animation.Animation;
import Animation.CoinAnimationManager;
import main.Entities.Vector2;
import quizzz.QuizzManager;
import ui.GButton;
import ui.GMenu;
import ui.GText;
import ui.IEvent;

public class GamePanel extends JPanel implements Runnable,IEvent {
    // SCREEN SETTINGS
    private final int screenWidth = 900;
    private final int screenHeight = 650;
    private final int FPS = 60;
    private final int ALPHA_BUTTON_SIZE = 26;
    private final long TIME_DRAW_ANIM_PER_IMAGE = 50;
    private final int DEFAULT_PER_WIN = 10;

    private long timeDeltaTime = 0;

    
    private BufferedImage backgroundImage;
    private Thread gameThread;

    private ImageManager imageManager;
    private QuizzManager quizzManager;
    private String answer = "";

    private CoinAnimationManager coinAnimationManager;
    private Animation hangManWinAnimation;


    private GText hangManImageDisplay;
    private GText answerDisplay;
    private GText coinDisplay;
    private GText coinAmount;
    private GText titleDisplay;

    private ArrayList<GText> underscores;
    private ArrayList<GText> charAnswers;

    private ArrayList<Animation> circleAnims;
    private ArrayList<Animation> xAnims;

    private GMenu popUpOverWindow;
    private GButton replayButton;

    private GMenu popUpWinWindow;
    private GButton nextQuesButton;

    private GButton[] alphaButtons = new GButton[ALPHA_BUTTON_SIZE];

    private int currentIndexHangMan = 0;
    private int countNumberCharRight = 0;
    private int countNumberCharFalse = 0;

    private boolean isGameOver = false;
    private boolean isWin = false;
    private boolean isDisplayAnswer = false;
    
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
        
        createHangmanWinDisplay();
        createCoinAnimationManager();
        createTitleDisplay();
        createAnswerDisplay();
        createCoinDisplay();
        createQuestion();
        loadAlphaButton();
        loadHangMan();
        loadMenus();
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
       
       
        if(isGameOver)
        {
            replayButton.update(timeDeltaTime, (int)mousePoint.getX(), (int)mousePoint.getY());
        }
        else if(isWin)
        {
            nextQuesButton.update(timeDeltaTime,(int)mousePoint.getX(),(int)mousePoint.getY());
            coinAnimationManager.update(timeDeltaTime);
        }
        else
        {
            for(int i =0;i<ALPHA_BUTTON_SIZE;i++)
            {
                alphaButtons[i].update(timeDeltaTime,(int)mousePoint.getX(), (int)mousePoint.getY());
            }
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
        if(!isGameOver)
        {
            drawAlphaButton(g2);
        }
        drawHangmanWinAnimation(g2);
        drawTitleDisplay(g2);
        drawCoinDisplay(g2);
        drawAnswerDisplay(g2);
        drawUnderscoreText(g2);
        drawHangmanAnimation(g2);
        drawCircleAnimations(g2);
        drawXAnimations(g2);
        drawMenus(g2);
        drawCoinAnimationManager(g2);
        g2.dispose();//save memory
    }
    private void drawHangmanWinAnimation(Graphics2D g2)
    {
        if(!isWin){return;}
        hangManWinAnimation.paint(g2, timeDeltaTime);
    }
    private void drawCoinAnimationManager(Graphics2D g2)
    {
        coinAnimationManager.paint(g2);
    }
    private void drawTitleDisplay(Graphics2D g2)
    {
        if(isGameOver||isWin){return;}
        titleDisplay.paint(g2);
    }
    private void drawCoinDisplay(Graphics2D g2)
    {
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
   
    private void createCoinAnimationManager()
    {
        coinAnimationManager = new CoinAnimationManager(420,430, 740,20,
                                                DEFAULT_PER_WIN,imageManager.getCoinImages());
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
    private void createCoinDisplay()
    {
        coinDisplay = new GText(740, 20, 50, 50, 
                imageManager.getCoinImage(), "", 1);
        coinAmount = new GText(780, 25, 50, 50, null,"9999", 20);
    }
    private void createQuestion()
    {
        String[] texts = quizzManager.getQuestion().split(":");
        answer = texts[1];
        System.out.println(answer);
        if(texts[1].length()>0)
        {
            titleDisplay.setTitle(texts[0].toUpperCase());
            createUnderscoreDisplay(answer.length());
        }
        else
        {
            //write code there
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
        popUpOverWindow.createText(325, 75, 200, 50,null,"Your score: ",25);
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
        boolean isRightChar = false;
        GButton currentButton = alphaButtons[info.charAt(0)-'A'];
        currentButton.setIsClickable(false);
        for(int i =0;i<answer.length();i++)
        {
            if(info.toLowerCase().compareTo(Character.toString(answer.charAt(i)))==0)
            {
                charAnswers.get(i).setDisplayImage(imageManager.getAlphaImages()[info.charAt(0)-'A']);
                isRightChar = true;
                countNumberCharRight++;
            }
        }
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
        
    }
    private void handleGameOver()
    {
        isGameOver = true;
        popUpOverWindow.setCanDisplay(true);
        changeStateClickableAlphaButtons(false);
        changeStateDisplayAnswer(true);
        currentIndexHangMan = (currentIndexHangMan+1)%imageManager.getLengthHangmanImages();
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
                resetLevel();
                popUpOverWindow.setCanDisplay(false);
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
            }
        });
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
}
class EventBinding implements IEvent
{

    @Override
    public void trigger(Object obj) {
        return;
    }
    
}
