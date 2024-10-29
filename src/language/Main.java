package language;

import java.io.*;
import java.util.*;

public class Main {
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        if (args.length <= 0){
            System.out.println("Please enter a filename");
            System.exit(0);
        }
        
        LanguageModel model = new LanguageModel(args[0]);
        model.buildModel();
        // model.dumpModel();

        Map<String, Map<String, Integer>> nextWord = model.getNextWord();
        for (String word: nextWord.keySet()){
            System.out.printf("The word after %s is %s\n", word, model.nextWord(word));
        }

        System.out.println("\n");
        for (String word: nextWord.keySet()){
            System.out.printf("The word after %s is %s\n", word, model.randomNext(word));
        }
        
    }
}
