package main.Mechanisms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HighScore {
    private final int MAX_HIGH_SCORE = 10;
    private ArrayList<String> highScores;
    public HighScore()
    {
        highScores = new ArrayList<>();
        readFile();
    }
    public void readFile()
    {
        try {
            File highScoreFile = new File("src/main/Mechanisms/high_score.txt");
            Scanner myReader = new Scanner(highScoreFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if(line.isEmpty()){break;}
                highScores.add(line);

            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
    public void writeFile()
    {
        try {
            FileWriter myWriter = new FileWriter("src/main/Mechanisms/high_score.txt");
            for(int i =0;i<highScores.size();i++)
            {
                myWriter.write(highScores.get(i));
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean isHighScore(int score)
    {
        for(int i =0;i<MAX_HIGH_SCORE;i++)
        {
            int currentScore =Integer.valueOf(highScores.get(i).split(" ")[1]);
            if(score>=currentScore)
            {
                return true;
            }
        }
        return false;
    }
    public void addHighScore(String name, int score)
    {
        int index = -1;
        for(int i =0;i<MAX_HIGH_SCORE;i++)
        {
            int currentScore =Integer.valueOf(highScores.get(i).split(" ")[1]);
            if(score>=currentScore)
            {
                index = i;
                break;
            }
        }
        if(index==-1){return;}
        highScores.add(index, name+" "+Integer.toString(score));
    }
}
