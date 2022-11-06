package quizzz;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;
public class QuizzManager {
    private Hashtable<String,quizz> dictionary = new Hashtable<>();

    private void readFile()
    {
        try {
            File quizzFile = new File("quizz.txt");
            Scanner myReader = new Scanner(quizzFile);
            while (myReader.hasNextLine()) {
            //   String line = myReader.nextLine();
            //   String title = line.
            
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
}
