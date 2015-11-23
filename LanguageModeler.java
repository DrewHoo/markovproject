import java.io.File;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

/**
 * Creates an order K Markov model for a text sample.
 *
 */
public class LanguageModeler {
   private int k;
   public HashMap<String, String> markov;
   private Random rand = new Random();
   private ArrayList<String> keys;
   private File text;
   private String firstSeed;

    // create an order K model for text
   public LanguageModeler(int K, File text) {
      markov = new HashMap<String, String>();
      keys = new ArrayList<String>();
      this.text = text;
      k = K;
      String key = "";
      String val = "";
      firstSeed = "";
      
      /* getting first seed */
      try {
         @SuppressWarnings("resource")
         Scanner s = new Scanner(new BufferedReader(new FileReader(text)));
         s.useDelimiter("");
         int i = 0;
         while (s.hasNext() && i < k) {
            firstSeed = firstSeed + s.next();
            i++;
         }
         String seed = new String(firstSeed);
         keys.add(seed);
         if (s.hasNext()) { 
            val = s.next();
            markov.put(seed, val);
         }
         
         //update seed to include prev val, discard prev 0th letter
         
         while (s.hasNext()) {
            seed = new String(seed.substring(1, k) + val);
            keys.add(seed); //add to arraylist of keys
            val = s.next(); //update val
            String testVal = markov.get(seed);
            if (testVal == null) markov.put(seed, val);
            else markov.put(seed, (testVal + val));
         }
         if (text.length() != k) {
            seed = new String(seed.substring(1, k) + val);
            keys.add(seed);
            markov.put(seed, null);
         }
      }
      
      catch (Exception e) {
         throw new IllegalArgumentException("Error loading word list: " + text.toString() + ": " + e);
      }
   
   }
   
    // create an order K model for text
   public LanguageModeler(int K, String text) {
      // markov = new HashMap<String, String>();
//       int i = 0;
//       k = K;      
//       String str;
//       String key = "";
//       String val = "";
// 
//       while (i < text.length() - k) {
//          str = new String(text.substring(i, i + k + 1));
//          key = str.substring(0, k);
//          val = str.substring(k);
//          String oldVal = markov.get(key);
//          if (oldVal == null) markov.put(key, val);
//          else markov.put(key, oldVal + val);
//          i++;
//       }
   
   }
   
    // return the first K characters of the sample text
   public String firstSeed() {
      return firstSeed;
   }
   
    // return K consecutive characters from the sample text,
    // selected uniformly at random
   public String randomSeed() {
      String[] keyring = new String[keys.size()];
      return keys.toArray(keyring)[rand.nextInt(keys.size())];
   }
   
    // return a character that follows seed in the sample text,
    // selected according to the probability distribution of all
    // characters that follow seed in the sample text
   public char nextChar(String seed) {
      String s = "";
      if (markov.get(seed) == null) s = markov.get(randomSeed());
      else s = markov.get(seed);
      return s.charAt(rand.nextInt(s.length()));
   }
 
}