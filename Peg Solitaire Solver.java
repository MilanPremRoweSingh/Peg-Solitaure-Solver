//This program provides solutions to peg solitaire via a brute force method
package milan;
import java.util.ArrayList;
import java.util.Stack;
public class pegSolver {
 
 public static int[][] posTrios = new int[][] { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 7, 8, 9 }, { 8, 9, 10 },
            { 9, 10, 11 }, { 10, 11, 12 }, { 13, 14, 15 }, { 14, 15, 16 }, { 15, 16, 17 }, { 16, 17, 18 },
            { 17, 18, 19 }, { 20, 21, 22 }, { 21, 22, 23 }, { 22, 23, 24 }, { 23, 24, 25 }, { 24, 25, 26 },
            { 27, 28, 29 }, { 30, 31, 32 }, { 12, 19, 26 }, { 11, 18, 25 }, { 2, 5, 10 }, { 5, 10, 17 },
            { 10, 17, 24 }, { 17, 24, 29 }, { 24, 29, 32 }, { 1, 4, 9 }, { 4, 9, 16 }, { 9, 16, 23 },
            { 16, 23, 28 }, { 23, 28, 31 }, { 0, 3, 8 }, { 3, 8, 15 }, { 8, 15, 22 }, { 15, 22, 27 },
            { 22, 27, 30 }, { 7, 14, 21 }, { 6, 13, 20 } };
 
 public static String baseSolution = "28W16 25W23 32W24 30W32 17W29 32W24 5W10 12W10 26W12 9W11 12W10 7W9 0W8 15W3 2W0 0W8 27W15 20W22 6W20 23W21 20W22 9W11 11W25 25W23 23W21 21W7 7W9 16W18 4W16 15W17 18W16"; 
 //baseSolution is the solution from the starting board and is used if the solver reaches the start board, which reduced the number of steps significantly
 
 public static void main(String[] args) {
 

  class HiRiQ {
   public int config;
   public byte weight;
   
   public boolean seen = false;
   public HiRiQ parent;
   public String solString;

   
   public ArrayList<HiRiQ> highStack = new ArrayList<HiRiQ>();
   public ArrayList<HiRiQ> lowStack = new ArrayList<HiRiQ>();
   
   public HiRiQ(int a, byte b, String c) {
    this.config = a;
    this.weight = b;
    solString = c;
   }

   boolean IsSolved() {
    return ((config == 65536 / 2) && (weight == 1));
   }

   // transforms the array of 33 booleans to an (int) config and a (byte) weight.
   public void store(boolean[] B) {
    int a = 1;
    config = 0;
    weight = (byte) 0;
    if (B[0]) {
     weight++;
    }
    for (int i = 1; i < 32; i++) {
     if (B[i]) {
      config = config + a;
      weight++;
     }
     a = 2 * a;
    }
    if (B[32]) { 
     config = -config;
     weight++;
    }
   }

   // transform the int representation to an array of booleans.
   // the weight (byte) is necessary because only 32 bits are memorized
   // and so the 33rd is decided based on the fact that the config has the
   // correct weight or not.
   public boolean[] load() {

    boolean[] B = new boolean[33];
    byte count = 0;
    int fig = config;
    B[32] = fig < 0;
    if (B[32]) {
     fig = -fig;
     count++;
    }
    int a = 2;
    for (int i = 1; i < 32; i++) {
     B[i] = fig % a > 0;
     if (B[i]) {
      fig = fig - a / 2;
      count++;
     }
     a = 2 * a;
    }
    B[0] = count < weight;
    return (B);
   }

   // prints the int representation to an array of booleans.
   //the weight (byte) is necessary because only 32 bits are memorized
   //and so the 33rd is decided based on the fact that the config has the
   //correct weight or not.

   public void printB(boolean Z) {
    if (Z) {
     System.out.print("[ ]");
    } 
    else {
     System.out.print("[@]");
    }
   }

   public void print() {
    byte count = 0;
    int fig = config;
    boolean next, last = fig < 0;
    if (last) {
     fig = -fig;
     count++;
    }
    int a = 2;
    for (int i = 1; i < 32; i++) {
     next = fig % a > 0;
     if (next) {
      fig = fig - a / 2;
      count++;
     }
     a = 2 * a;
    }
    next = count < weight;

    count = 0;
    fig = config;
    if (last) {
     fig = -fig;
     count++;
    }
    a = 2;

    System.out.print("      ");
    printB(next);
    for (int i = 1; i < 32; i++) {
     next = fig % a > 0;
     if (next) {
      fig = fig - a / 2;
      count++;
     }
     a = 2 * a;
     printB(next);
     if (i == 2 || i == 5 || i == 12 || i == 19 || i == 26 || i == 29) {
      System.out.println();
     }
     if (i == 2 || i == 26 || i == 29) {
      System.out.print("      ");
     }
    }
    printB(last);
    System.out.println();

   }
   
   public int solver(ArrayList<HiRiQ> allNodes) { 
    
    if(this.pairityChecker() == false){System.out.println("Unsolavable input"); return 0; }
    else{
     Stack<HiRiQ> low = new Stack<HiRiQ>();
     Stack<HiRiQ> high = new Stack<HiRiQ>();
     low.push(this);
     boolean lE = false;
     boolean hE = true;
     
     
     while(!(lE && hE)){       
      if(low.empty() == false){
       HiRiQ held = low.pop(); 
       for(int i = (allNodes.size()-1); i >= 0 ; i--){
        if(allNodes.get(i).config == held.config && allNodes.get(i).weight == held.weight){
         held.seen = true;
         break;
        }     
       }
       
       if (held.config == 65536 / 2 && held.weight == 1) {
        allNodes.add(held);
        System.out.println('\n'+"SOLUTION"+'\n');
        return 1;
       }   
       else if(held.config == -2147450879 && held.weight == 32) {
        allNodes.add(held);
        System.out.println('\n'+"SOLUTION"+'\n');
        return 2; //A solution to the initial puzzle is hardcoded so that the depth we ever reach is halved (a solution is found at highest weight and lowest weight)
       } 
       else if(held.seen == false){
        allNodes.add(held);           
        held.children();      
        for (int j = 0; j < held.highStack.size(); j++) {
         high.push(held.highStack.get(j));      
        }      
        for (int k = 0; k < held.lowStack.size(); k++) {
         low.push(held.lowStack.get(k));
        }         
       } 
      }
       
      else{
       HiRiQ heldHigh = high.pop();
       for( int i = (allNodes.size()-1); i >= 0; i--){
        if(allNodes.get(i).config == heldHigh.config && allNodes.get(i).weight == heldHigh.weight){
         heldHigh.seen = true;
         break;
        }     
       }
       if (heldHigh.config == 65536 / 2 && heldHigh.weight == 1) {
        allNodes.add(heldHigh);
        System.out.println('\n'+"SOLUTION"+'\n');
        return 1;
       } else if (heldHigh.config == -2147450879 && heldHigh.weight == 32){
        allNodes.add(heldHigh);
        System.out.println('\n'+"SOLUTION"+'\n');
        return 2;
       }
           
       else if(heldHigh.seen == false){
        allNodes.add(heldHigh);      
        heldHigh.children();      
        for (int i = 0; i < heldHigh.highStack.size(); i++) {
         high.push(heldHigh.highStack.get(i));      
        }      
        for (int i = 0; i < heldHigh.lowStack.size(); i++) {
         low.push(heldHigh.lowStack.get(i));
        }         
       }
      }
     lE = low.empty();
     hE = high.empty();
     }
     return 0;
    }
   }
 
   public void children() {
    boolean[] parent =new boolean[33]; 
    System.arraycopy(this.load(), 0, parent, 0, 33);
    boolean[] child = new boolean[33];
    
    

    for (int i = 0; i < 38; i++) {   
     System.arraycopy(parent, 0, child, 0, 33);

     if (parent[posTrios[i][0]] == !parent[posTrios[i][2]]) {
      child[posTrios[i][0]] = !child[posTrios[i][0]];
      child[posTrios[i][1]] = !child[posTrios[i][1]];
      child[posTrios[i][2]] = !child[posTrios[i][2]];

      if (parent[posTrios[i][1]] != false) {
       int[] temp = changeToInt(child);
       int[] lowerChildProperties = { temp[0], temp[1], posTrios[i][0], 1, posTrios[i][2]};
       HiRiQ tempObject = converter(lowerChildProperties);
       tempObject.parent = this;
       if(tempObject.pairityChecker() != false){
        this.lowStack.add(tempObject);
       }
      }

      else {
       int[] temp = changeToInt(child);
       int[] higherChildProperties = { temp[0], temp[1], posTrios[i][0], 0, posTrios[i][2]};
       HiRiQ tempObject = converter(higherChildProperties);
       tempObject.parent = this;
       if(tempObject.pairityChecker() != false){
        this.highStack.add(tempObject);
       }
      }
     }
    }
   }
    
   
   public HiRiQ converter(int[] a) {
    String convert = "";
    if(a[3] == 1){
     convert = a[2] + "W" + a[4];
    }
    else{
     convert = a[2] + "B" + a[4];
    }
    HiRiQ converted = new HiRiQ(a[0], (byte) a[1], convert);
    return converted;
   }

   
   public int[] changeToInt(boolean[] a) {
    int b = 1;
    int config = 0;
    int weight = (byte) 0;    
    if (a[0]) {
     weight++;
    }     
    for(int i = 1; i < 32; i++) {
     if (a[i]) {
      config = config + b;
      weight++;
      }
     b = 2 * b;
    }
     if (a[32]) {
      config = -config;
      weight++;
     }
     int[] properties = new int[] { config, weight};
     return properties;
   }

    
  
   public String answerString(ArrayList<HiRiQ> solution){
    HiRiQ tracer = solution.get((solution.size()-1));
    String answer = "";
    
    while((tracer.config != this.config) || (tracer.weight != this.weight)){
     answer = tracer.solString + " " + answer;

     tracer = tracer.parent;
    }
    
    return answer;
   }   
    
   public boolean pairityChecker(){
    boolean [] currentBool = this.load();
    int alphaCount = 0;
    int betaCount= 0;
    int deltaCount = 0;
    int[] alpha = {2, 4, 8, 14, 20, 11, 17, 23, 27, 26, 32};
    int[] beta = {0, 6, 5, 9, 15, 21, 12, 18, 24, 28, 30};
    int[] delta = {1, 3, 7, 13, 10, 16, 22, 19, 25, 29, 31}; 
    for(int i = 0; i < alpha.length; i++ ){
     if(currentBool[(alpha[i])] != false){
      alphaCount++;
     } 
    }
    for(int i = 0; i < beta.length; i++ ){
     if(currentBool[(beta[i])] != false){
      betaCount++;
     } 
    }
    for(int i = 0; i < delta.length; i++ ){
     if(currentBool[(delta[i])] != false){
      deltaCount++;
     } 
    }
     
    if(((alphaCount%2) == (betaCount%2)) && ((alphaCount%2) != (deltaCount%2))){
      return true;
    }
    return false;
   }    
  }
  
  
  HiRiQ inputBoard = new HiRiQ(-411153748, (byte) 13, "");
  inputBoard.print();
  ArrayList<HiRiQ> answer = new ArrayList<HiRiQ>();
  int answerFound = inputBoard.solver(answer);
   
  if(answerFound == 1){
   String answerString = inputBoard.answerString(answer);
   System.out.println(answerString);
  } 
  else if (answerFound == 2){
   String answerString = baseSolution + inputBoard.answerString(answer);
   System.out.println(answerString);
  }
 }
}