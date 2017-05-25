package aufgabe;

import static java.lang.Integer.max;
import java.util.ArrayList;

public class BoyerMoore {
    
    private int[][] BCR;                 // Bad Character Rule
    private int[] GSR;                   // Good Suffix Rule
    private ArrayList<Integer> pos;      // 10 matches
    private int howMany;                 // total matches

    BoyerMoore(){
        pos = new ArrayList<>();
    }

    public int getHowMany(){
        return howMany;
    }

    public ArrayList<Integer> getPositions(){
        return pos;
    }

    public void preprocessBCR(String P){        // Extended Bad Character Rule

        int i, j;
        int m = P.length();
        BCR = new int[5][m];                    // 0<->'a', 1<->'c', 2<->'g', 3<->'t', 4<->'n'

        for(i = m - 1; i >= 0; i--){

            BCR[0][i] = i+1;
            BCR[1][i] = i+1;
            BCR[2][i] = i+1;
            BCR[3][i] = i+1;
            BCR[4][i] = i+1;
            for(j = 0; j < i; j++){

                switch(P.charAt(j))                       // Beispiel:
                {                                         // P:     n a a c t g n a c t g
                    case('a'):                            // a<->0  1 2 1 1 2 3 4 5 1 2 3
                        BCR[0][i] = i-j;                  // c<->1  1 2 3 4 1 2 3 4 5 1 2
                        break;                            // g<->2  1 2 3 4 5 6 1 2 3 4 5
                    case('c'):                            // t<->3  1 2 3 4 5 1 2 3 4 5 1
                        BCR[1][i] = i-j;                  // n<->4  1 1 2 3 4 5 6 1 2 3 4
                        break;
                    case('g'):
                        BCR[2][i] = i-j;
                        break;
                    case('t'):
                        BCR[3][i] = i-j;
                        break;
                    case('n'):
                        BCR[4][i] = i-j;
                        break;
                }
            }
        }
        
        
        /*for(j = 0; j < m; j++)                      // print BCR table
            System.out.printf("%c ", P.charAt(j));
        System.out.println();
        System.out.printf("BCR Table:\n");
        for(i = 0; i < 5; i++){
            for(j = 0; j < m; j++)
                System.out.printf("%d ", BCR[i][j]);
            System.out.printf("\n");
        }*/
    }

    public void preprocessGSR(String P){            // Good Suffix Rule

        int i, j, k;                                // Beispiel:
        int m = P.length();                         // n a a c g t n a c g t
        GSR = new int[m + 1];                       // 0 0 0 0 0 0 0 5 0 0 0 0
        for(i = 1; i < m; i++){

            String suffix = P.substring(i);
            String PP = P.substring(0, m - 1);      // P without the last letter
            int index = PP.lastIndexOf(suffix);
            if(index == 0 || index != -1 && (index > 0 && i > 0 && PP.charAt(index - 1) != PP.charAt(i - 1))) // match at the begging or somewhere inside
                GSR[i] = i - index;
        }
        
        /*System.out.printf("GSR Table:\n");
        for(j = 0; j < m + 1; j++)
            System.out.printf("%d ", GSR[j]);
        System.out.printf("\n");
        for(j = 0; j < m; j++)
            System.out.printf("%c ", P.charAt(j));
        System.out.printf("\n");*/
        
    }

    void stringMatching(String P, String T){

        int m = P.length();
        int i, j;
        int ten = 0;

        i = m - 1;
        j =  m - 1;
        while(i < T.length()){
            
            int textIndex = i;
            j = m - 1;
            while(j >= 0){
                
                if(T.charAt(textIndex) != P.charAt(j))
                    break;
                textIndex--;
                j--;
            }
            if(j == -1){
                if(ten < 10)
                    pos.add(textIndex + 2); // textIndex ist hinter die Position(+1), Indexe fangen mit 0 an(+1)
                howMany++;
                ten++;
                i++;
            }
            else    // match == false
                i += shift(j, T.charAt(textIndex));
        }
    }
    
    int shift(int index, char mismatch){
        
        int tmp = 0;
        switch(mismatch) // 0<->'a', 1<->'c', 2<->'g', 3<->'t', 4<->'n'
                {
                    case('a'):
                        tmp = max(BCR[0][index], GSR[index+1]);
                        break;
                    case('c'):
                        tmp = max(BCR[1][index], GSR[index+1]);
                        break;
                    case('g'):
                        tmp = max(BCR[2][index], GSR[index+1]);
                        break;
                    case('t'):
                        tmp = max(BCR[3][index], GSR[index+1]);
                        break;
                    case('n'):
                        tmp = max(BCR[4][index], GSR[index+1]);
                        break;
                }
        return tmp;
    }
}
