package main.Mechanisms;

import java.util.ArrayList;

public class Suggestion {
    private ArrayList<String> answers;

    public Suggestion()
    {
        answers = new ArrayList<>();
    }
    public void setAnswer(String answer)
    {
        for(int i =0;i<answer.length();i++)
        {
            answers.add(Character.toString(answer.charAt(i)));
        }
    }
    public void setCharIsAnswered(String chr)
    {
        for(int i =0;i<answers.size();i++)
        {
            if(chr.equals(answers.get(i)))
            {
                answers.remove(i);
                break;
            }
        }
    }
    public String getCharSuggestion()
    {
        if(answers.size()>0)
        {
            return answers.get(0);
        }
        return "";
    }
    public void reset()
    {
        answers.clear();
    }
    public void HString()
    {
        
        for(var ch: answers)
        {
             System.out.print(ch);
        }
        System.out.println();
    }

}
