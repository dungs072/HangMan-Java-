package quizzz;

import java.util.ArrayList;
import java.util.Random;

public class quizz {
    private ArrayList<String> words = new ArrayList<String>();
    private ArrayList<String> usedWords = new ArrayList<String>();
    private Random random = new Random();
    public void addWord(String word)
    {
        words.add(word);
    }
    public String getWord()
    {
        String tempWord = "";
        if(words.size()==usedWords.size()){ return "";}
        while(true)
        {
            int randomIndex = random.nextInt(words.size());
            tempWord = words.get(randomIndex);
            if(usedWords.contains(tempWord)){continue;}
            usedWords.add(tempWord);
            break;
        }
        return tempWord;
    }
    public void reset()
    {
        words.clear();
        usedWords.clear();
    }

}
