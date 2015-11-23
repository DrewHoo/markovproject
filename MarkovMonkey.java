import java.util.Set;
import java.util.SortedSet;
import java.util.List;
import java.util.TreeSet;
import java.util.Stack;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Run with the following command: "java MarkovMonkey [int: order k of the markov model] [int: length of output] [input file] [output file]"
 * e.g. "java MarkovMonkey 5 1000 dickens.txt output.txt"
 */
public class MarkovMonkey {
   
   public static void main(String[] args) {
      if (args.length < 4)    { System.out.print("Error: Not enough CL args"); return; }
      int k = Integer.parseInt(args[0]);
      int outputLength = Integer.parseInt(args[1]);
      if (k < 0)              { System.out.print("Error: Invalid k"); return; }
      if (outputLength < 0)   { System.out.print("Error: Invalid length"); return; }
      File source = new File(args[2]);
      //if (!source.canRead())  { System.out.print("Error: cannot read source file"); return; }
      if (!(source.length() > k)) { System.out.print("Error: k >= source file length"); return; }
      
      File results = new File(args[3]);
      if (results.exists()) results.delete();
      
      try {
         results.createNewFile();
      } catch (Exception e) {
         System.out.print("Error: Cannot create results.txt on specified file path");
      }
      
      LanguageModeler lm = new LanguageModeler(k, source);
      StringBuilder markovText = new StringBuilder(lm.randomSeed());
      String seed = markovText.toString();
      String nextChar;
      while (markovText.length() < outputLength) {
         seed = new String(markovText.substring(markovText.length() - k));
         markovText.append(Character.toString(lm.nextChar(seed))); 
      }
      
      //Check for IO error
      if (!results.canWrite()) {
         System.out.println("Error: Cannot write to results file"); 
         System.exit(1);
      }
      
      /** writing the text to the output file results */
      try {
         Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(results)));
         w.write(markovText.toString());
         w.close();
      } catch (IOException e) {
           System.err.println("Error: Problem writing to" + results.toString() + ": " + e.toString());
      }
   }
}