package languageCode;

import java.io.*;

public class MainCode {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Reader reader = new FileReader(args[0]);
        LanguageModelCode lm = new LanguageModelCode(reader);
        lm.buildModel();
        // lm.dumpModel();

        Console cons = System.console();
        while (true){
            String startWord = cons.readLine("> start word: ");
            int numWords = Integer.parseInt(cons.readLine("> num words: "));
            String generated = lm.generate(startWord, numWords, .75f);

            System.out.printf("\n=============\n%s\n", generated);
        }
    }
}
