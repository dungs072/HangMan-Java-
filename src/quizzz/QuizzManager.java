package quizzz;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;
public class QuizzManager {
    private Hashtable<String,quizz> dictionary = new Hashtable<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> usedTitles = new ArrayList<>();
    private Random random = new Random();
    public QuizzManager()
    {
      readFile();
    }
    private void readFile()
    {
        try {
            File quizzFile = new File("src/quizzz/quizz.txt");
            Scanner myReader = new Scanner(quizzFile);
            while (myReader.hasNextLine()) {
              String line = myReader.nextLine();
              if(line.isEmpty()){break;}
              String[] texts = line.split(": ");
              String[] answers = texts[1].split(", ");
              quizz tempQuizz = new quizz();
              for(var answer: answers)
              {
                  tempQuizz.addWord(answer);
              }
              titles.add(texts[0]);
              dictionary.put(texts[0],tempQuizz);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
    public String getQuestion()
    {
        String tempTitle = "-1";
        String tempAnswer = "-1";
        
        while(titles.size()>usedTitles.size())
        {
            int randomIndex = random.nextInt(titles.size());
            tempTitle = titles.get(randomIndex);
            quizz q = dictionary.get(tempTitle);
            if(q.isFullUsedWords())
            {
              if(usedTitles.contains(tempTitle)){continue;}
              usedTitles.add(tempTitle);
              continue;
            }
            tempAnswer = dictionary.get(tempTitle).getWord();
            break;
        }
        return tempTitle+":"+tempAnswer;
    }
    public void resetUsedTitle()
    {
        usedTitles.clear();
        for(var key:titles)
        {
            dictionary.get(key).resetUsedWords();
        }
    }
}
