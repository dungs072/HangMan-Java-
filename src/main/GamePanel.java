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
    private long timeDeltaTime = 0;

    
    private BufferedImage backgroundImage;
    private Thread gameThread;

    private ImageManager imageManager;
    private QuizzManager quizzManager;
    private String answer = "";

    private GText hangManImageDisplay;


    private ArrayList<GText> underscores;
    private ArrayList<GText> charAnswers;

    private ArrayList<Animation> circleAnims;
    private ArrayList<Animation> xAnims;

    private GMenu popUpOverWindow;
    private GButton replayButton;

    private GButton[] alphaButtons = new GButton[ALPHA_BUTTON_SIZE];

    private int currentIndexHangMan = 0;

    private boolean isGameOver = false;
    
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
        // for(int i =0;i<ALPHA_BUTTON_SIZE;i++)
        // {
        //     alphaButtons[i].update(timeDeltaTime,(int)mousePoint.getX(), (int)mousePoint.getY());
        // }
        replayButton.update(timeDeltaTime, (int)mousePoint.getX(), (int)mousePoint.getY());
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
        drawUnderscoreText(g2);
        drawHangmanAnimation(g2);
        drawCircleAnimations(g2);
        drawXAnimations(g2);
        drawMenus(g2);
        g2.dispose();//save memory
    }
    private void drawAlphaButton(Graphics2D g2)
    {
        // for(var button:alphaButtons)
        // {
        //     if(button==null){continue;}
        //     button.paint(g2);
        // }
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
        if(popUpOverWindow!=null)
        {
            popUpOverWindow.paint(g2);
        }
        
    }
    private void createQuestion()
    {
        String[] texts = quizzManager.getQuestion().split(":");
        answer = texts[1];
        System.out.println(answer);
        if(texts[1].length()>0)
        {
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
        int posy = 325;
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
        int startPointY = 400;
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
            alphaButtons[i].setInfo(Character.toString(i+'A'));
            addMouseListener(alphaButtons[i]);
            col++;
            
        }
    
    }
    private void loadHangMan()
    {

        hangManImageDisplay = new GText(150, 0, 550, 325, 
                                imageManager.getHangmanImages()[0],"",20);
    }
    private void loadMenus()
    {
        popUpOverWindow = new GMenu(0, 325, 900,325, imageManager.getPopupImage());
        //popUpOverWindow.setCanDisplay(false);
        popUpOverWindow.createText(325, 20, 200, 50, null,"OOPS... YOU FAILED!",25);
        popUpOverWindow.createText(325, 75, 200, 50,null,"Your score: ",25);
        replayButton = popUpOverWindow.createButton(390, 150, 100,100,imageManager.getReplayButtonImage(),
                                                    imageManager.getReplayClickedButtonImage(),null);
        addMouseListener(replayButton);
    }
    
    @Override
    public void trigger() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void trigger(String info) {
        boolean isRightChar = false;
        GButton currentButton = alphaButtons[info.charAt(0)-'A'];
        currentButton.setIsClickable(false);
        for(int i =0;i<answer.length();i++)
        {
            if(info.toLowerCase().compareTo(Character.toString(answer.charAt(i)))==0)
            {
                charAnswers.get(i).setDisplayImage(imageManager.getAlphaImages()[info.charAt(0)-'A']);
                isRightChar = true;
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
    }
    private void handleWrongChar(GButton currentButtonClicked)
    {
        Vector2 pos = currentButtonClicked.getCurrentPosition();
        Vector2 size = currentButtonClicked.getCurrentSize();
        xAnims.add(new Animation(pos.getX(), pos.getY(), size.getX(), size.getY(), 
                    imageManager.getXImages(),false,TIME_DRAW_ANIM_PER_IMAGE));

        
        currentIndexHangMan = (currentIndexHangMan+1)%imageManager.getLengthHangmanImages();
        hangManImageDisplay.setDisplayImage(imageManager.getHangmanImages()[currentIndexHangMan]);
        if(currentIndexHangMan==imageManager.getLengthHangmanImages()-2)
        {
            handleGameOver();
        }
        
    }
    private void handleGameOver()
    {
        isGameOver = true;
        popUpOverWindow.setCanDisplay(true);
        currentIndexHangMan = (currentIndexHangMan+1)%imageManager.getLengthHangmanImages();
    }
}
