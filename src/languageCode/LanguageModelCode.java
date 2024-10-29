package languageCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LanguageModelCode {

   private Reader reader;
   private Map<String, Map<String, Integer>> nextWords;
   private Random rand = new SecureRandom();

   public LanguageModelCode(Reader reader){
      this.reader = reader;
      this.nextWords = new HashMap<>();
   }

   public void buildModel() throws IOException{
      BufferedReader br = new BufferedReader(reader);
      String line;

      while ((line = br.readLine()) != null){
         // CLEAN THE LINE
         line = line.trim().replaceAll("\\p{Punct}", " ");
         if (line.length() <= 0){
            // continue with the next iteraton
            continue;
         }
         // split the string into string of array
         // with separator as space or multiple spaces
         String[] words = line.split("\\s+");
         for (int i = 0; i < words.length - 1; i++){
            String curr = words[i];
            String next = words[i+1];
            addToWordDistribution(curr, next);
         }
      }
   }

   public void dumpModel(){
      for (String curr: nextWords.keySet()){
         System.out.printf(">> %s\n", curr);
         Map<String,Integer> dist = nextWords.get(curr);
         for (String next: dist.keySet()){
            // \t -- tab
            System.out.printf("\t\t%s = %d\n", next, dist.get(next));
         }
      }
   }

   public String generate(String root, int numWords, float hallucination){
      String theSentence = root;
      String curr = root;
      String next = "";
      float totalRand = 1 - hallucination;
      for (int i = 0; i < numWords; i++){
         if (rand.nextFloat() > totalRand)
            next = nextWord(curr); // exploitation
         else
            next = randomNext(curr); // exploration
         if (next.length() <= 0)
            return theSentence;
         theSentence += " " + next;
         curr = next;
      }
         
      return theSentence;
   }

   public String generate(String root, int numWords){
      String theSentence = root;
      String curr = root;
      String next = "";
      for (int i = 0; i < numWords; i++){
         // next = nextWord(curr);
         next = randomNext(curr);
         if (next.length() <= 0){
            return theSentence;
         }
         theSentence += " " + next;
         curr = next;
      }
      return theSentence;
   }

   // exploration
   public String randomNext(String word){

      Map<String, Integer> dist = nextWords.get(word);
      if (null == dist){
         return "";
      }

      int numWords = dist.size();
      // randomly pick a number from the list of next words
      int theWord = rand.nextInt(numWords);
      int i = 0;
      String nw = "";
      for (String w: nextWords.keySet()){
         nw = w;
         if (i > theWord){
            return w;
         }
         i ++;
      }
      return nw;
   }

   // greedy algo - exploitation
   public String nextWord(String word){
      int wordCount = -1;
      String theWord = "";
      Map<String, Integer> dist = nextWords.get(word);
      if (null == dist){
         return "";
      }
      for (String w: dist.keySet()){
         if (dist.get(w) > wordCount){
            wordCount = dist.get(w);
            theWord = w;
         }
      }
      return theWord;
   }

   // Map<String, Map<String, Integer>>
   private void addToWordDistribution(String curr, String next){
      Map<String, Integer> wordDistrib;

      if (nextWords.containsKey(curr)){
         wordDistrib = nextWords.get(curr);
      } else {
         wordDistrib = new HashMap<>();
      }

      int count = 0;
      if (wordDistrib.containsKey(next)){
         count = wordDistrib.get(next);
      }

      count ++;
      wordDistrib.put(next,count);

      nextWords.put(curr,wordDistrib);
   }

   

}
