package languageCode;

import java.io.*;
import java.util.*;

public class StopWords {

    private Set<String> stopWords = new HashSet<>();

    public void load(String file) throws FileNotFoundException, IOException{
        Reader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);

        String word;
        while ((word = br.readLine()) != null){
            stopWords.add(word.trim().toLowerCase());
        }

        br.close();
        reader.close();
    }

    public boolean isStopWord(String word){
        return stopWords.contains(word);
    }
}
