package aufgabe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aufgabe {
    
    private static ArrayList<String> patterns;
    private static String text;
    
    private static void getPatterns(String fileName){

        BufferedReader br;
        FileReader fileReader;
        
        patterns = new ArrayList<>();
        try {
            fileReader = new FileReader(fileName);
            br = new BufferedReader(fileReader);
        } catch (FileNotFoundException ex) {
            System.out.printf("%s not found.\n", fileName);
            return;
        }

        String line;

        try {
            while((line = br.readLine()) != null){
                if(line.charAt(0) != '>'){
                    patterns.add(line);
                }
            }
        } catch (IOException ex) {
            System.out.printf("%s interrupted.\n", fileName);
            Logger.getLogger(Aufgabe.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private static void getSequence(String fileName){

        BufferedReader br;
        InputStream in;
        InputStreamReader s;

        try {
            in = new FileInputStream(fileName);
            s = new InputStreamReader(in);
            br = new BufferedReader(s);
            StringBuilder sb = new StringBuilder();
            String line;
            
            text = br.readLine();   // "> human genomic DNA Chromosome 20"
            
            text = new String();
            
            while((line = br.readLine()) != null)
              sb = sb.append(line);
            
            text = sb.toString();

        } catch (FileNotFoundException ex ) {
            System.out.printf("%s not found.\n", fileName);
            Logger.getLogger(Aufgabe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.printf("%s interrupted.\n", fileName);
            Logger.getLogger(Aufgabe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) throws IOException{
        
        //System.out.printf("Argumenten: %d, args[0]: %s, args[1]: %s\n", args.length, args[0], args[1]);
            
        getPatterns(args[0]);   // getPatterns("patterns.fasta");
        getSequence(args[1]);   // getSequence("sequence.fasta");

        for(String pattern : patterns){

            BoyerMoore BM = new BoyerMoore();
            BM.preprocessBCR(pattern);
            BM.preprocessGSR(pattern);
            BM.stringMatching(pattern, text);
            System.out.printf("%s: %d\n%s\n", pattern, BM.getHowMany(), BM.getPositions());
        }
    }
}
