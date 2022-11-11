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

import quizzz.QuizzManager;
import ui.GButton;
import ui.GText;
import ui.IEvent;

public class GamePanel extends JPanel implements Runnable,IEvent {
    // SCREEN SETTINGS
    private final int screenWidth = 900;
    private final int screenHeight = 650;
    private final int FPS = 60;
    private final int ALPHA_BUTTON_SIZE = 26;
    private long timeDeltaTime = 0;
    
    private BufferedImage backgroundImage;
    private Thread gameThread;

    private ImageManager imageManager;
    private QuizzManager quizzManager;
    private String answer = "";

    private GText hangManImageDisplay;

    private ArrayList<GText> underscores;
    private ArrayList<GText> charAnswers;

    private GButton[] alphaButtons = new GButton[ALPHA_BUTTON_SIZE];

    private int currentIndexHangMan = 0;
    
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
        imageManager = new ImageManager();
        quizzManager = new QuizzManager();
        
        createQuestion();
        loadAlphaButton();
        loadHangMan();
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
                timeDeltaTime = (long) timeDeltaTime;
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
        for(int i =0;i<ALPHA_BUTTON_SIZE;i++)
        {
            alphaButtons[i].update((int)mousePoint.getX(), (int)mousePoint.getY());
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
        drawAlphaButton(g2);
        drawUnderscoreText(g2);
        drawHangmanAnimation(g2);
        g2.dispose();//save memory
    }
    private void drawAlphaButton(Graphics2D g2)
    {
        for(var button:alphaButtons)
        {
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
        hangManImageDisplay.paint(g2);
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
                            45,45,imageManager.getAlphaImages()[i],"",10,this);
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
    @Override
    public void trigger() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void trigger(String info) {
        boolean isRightChar = false;
        alphaButtons[info.charAt(0)-'A'].setIsClickable(false);
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
            handleRightChar();
        }
        else
        {
           handleWrongChar();
        }
        
    }
    private void handleRightChar()
    {

    }
    private void handleWrongChar()
    {
        currentIndexHangMan = (currentIndexHangMan+1)%imageManager.getLengthHangmanImages();
        hangManImageDisplay.setDisplayImage(imageManager.getHangmanImages()[currentIndexHangMan]);
        
    }
}
