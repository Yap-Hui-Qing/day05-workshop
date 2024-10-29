package language;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;

public class LanguageModel {

    private String filename;
    private Map<String, Map<String, Integer>> nextWord;

    public Map<String, Map<String, Integer>> getNextWord() {
        return nextWord;
    }

    private Random rand = new SecureRandom();

    // constructor
    public LanguageModel(String filename){
        this.filename = filename;
        this.nextWord = new HashMap<>();
    }
    
    public void buildModel() throws FileNotFoundException, IOException{
        // read the file
        File file = new File(filename);
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);

        String line;
        while ((line = br.readLine()) != null){
            // clean the line
            line = line.trim().replaceAll("\\p{Punct}", " ");  
            if (line.length() <= 0){
                continue;
            }
            // System.out.println(line + "\n");
            
            String[] words = line.split("\\s+");
            for (int i = 0; i < words.length - 1; i ++){
                String current = words[i];
                String next = words[i+1];
                addToWordDistribution(current, next);
            }
        }
        br.close();
        reader.close();
    }

    public void addToWordDistribution(String current, String next){
        Map<String, Integer> countWord;
        if (nextWord.containsKey(current)){
            countWord = nextWord.get(current);
            if (countWord.containsKey(next)){
                int count = countWord.get(next);
                count += 1;
                countWord.replace(next,count);
            } else{
                int count = 1;
                countWord.put(next, count);
            }
            nextWord.replace(current, countWord);
        } else{
            countWord = new HashMap<>();
            int count = 1;
            countWord.put(next, count);
            nextWord.put(current, countWord);
        }
    }

    public void dumpModel(){
        for (String current : nextWord.keySet()){
            System.out.printf(">> %s\n", current);
            Map<String, Integer> count = nextWord.get(current);
            for (String next : count.keySet()){
                System.out.printf("\t\t%s = %d\n",next, count.get(next));
            }
        }
    }

    // greedy algo - exploitation -- choose the word with highest occurrence
    public String nextWord(String current){
        Map<String, Integer> countWord = nextWord.get(current);
        if (countWord == null){
            return "";
        }
        int count = 0;
        String theWord = "";
        for (String next : countWord.keySet()){
            if (countWord.get(next) > count){
                count = countWord.get(next);
                theWord = next;
            }
        }
        return theWord;
    }

    // exploration -- randomly choose a next word
    public String randomNext(String current){
        Map<String, Integer> dist = nextWord.get(current);
        if (null == dist){
            return "";
        }

        int numWords = dist.size();
        // randomly pick a number from the list of next words
        int theWord = rand.nextInt(numWords);
        int i = 0;
        String nw = "";
        for (String w: dist.keySet()){
            nw = w;
            if (i > theWord){
                return w;
            }
            i ++;
        }
        return nw;
    }

    public String generate(String root, int numWords){
        String theSentence = root;
        String current = root;
        String next = "";

        for (int i = 0; i < numWords; i++){
            // next = nextWord(current);
            next = randomNext(current);
            
            // no next word
            if (next.length() <= 0){
                return theSentence;
            } else{
                theSentence += " " + next;
                current = next;
            }
        }
        return theSentence;
    }

    public String generate(String root, int numWords, float hallucination){
        String theSentence = root;
        String current = root;
        String next = "";
        float totalRand = 1 - hallucination;
        
        for(int i = 0; i< numWords; i++){
            // rand.nextfloat() returns a float value between 0.0 (inclusive) and 1.0 (exclusive)
            if (rand.nextFloat() > totalRand){
                next = nextWord(current); // exploitation
            } else {
                next = randomNext(current); // exploration
            }
            if (next.length() <= 0){
                return theSentence;
            }
            theSentence += " " + next;
            current = next;
        }
        return theSentence;
    }
        
}

    