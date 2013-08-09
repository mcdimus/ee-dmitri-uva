// 102 - Ecological Bin Packing
// http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=38
// Time limit: 3.000 seconds
// 08.08.2013

// 1 submission: 

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;

class Main {
/**
   * Utility function to read from stdin.
   *
   * @param maxLg - maximum length of line
   *
   * @return - one line from stdin
   */
  static String readLine(int maxLg)  // utility function to read from stdin
  {
    byte lin[] = new byte[maxLg];
    int lg = 0, car = -1;
    String line = "";

    try {
      while (lg < maxLg) {
        car = System.in.read();
        if ((car < 0) || (car == '\n')) {
          break;
        }
        lin[lg++] += car;
      }
    }
    catch (IOException e) {
      return (null);
    }

    if ((car < 0) && (lg == 0)) {
      return (null);  // eof
    }
    return (new String(lin, 0, lg));
  }

  public static void main(String args[])
  {
    Main myWork = new Main();  // create a dynamic instance
    myWork.begin();            // the true entry point
  }

  public static final int BROWN = 0;
  public static final int GREEN = 1;
  public static final int CLEAR = 2;
  public static final int BIN_1 = 0;
  public static final int BIN_2 = 1;
  public static final int BIN_3 = 2;

  void begin() {
    String input;
    StringTokenizer idata;
    StringBuilder builder = new StringBuilder();
    int b1,g1,c1, b2,g2,c2, b3,g3,c3;
    int moves = 0;
    Bin bin1= null, bin2 = null, bin3 = null;

    int[][] inputMatrix = new int[3][3];
    int[][] transformedMatrix = null;
    
    while ((input = Main.readLine(255)) != null) {
      idata = new StringTokenizer(input, " ");
      if (!idata.hasMoreElements()) {
        break;
      }
      b1 = Integer.parseInt(idata.nextToken());
      g1 = Integer.parseInt(idata.nextToken());
      c1 = Integer.parseInt(idata.nextToken());
      // bin1 = new Bin(b1,g1,c1);
      b2 = Integer.parseInt(idata.nextToken());
      g2 = Integer.parseInt(idata.nextToken());
      c2 = Integer.parseInt(idata.nextToken()); 
      // bin2 = new Bin(b2,g2,c2);
      b3 = Integer.parseInt(idata.nextToken());
      g3 = Integer.parseInt(idata.nextToken());
      c3 = Integer.parseInt(idata.nextToken());
      // bin3 = new Bin(b3,g3,c3);

      inputMatrix[BIN_1][BROWN] = b1;
      inputMatrix[BIN_1][GREEN] = g1;
      inputMatrix[BIN_1][CLEAR] = c1;
      inputMatrix[BIN_2][BROWN] = b2;
      inputMatrix[BIN_2][GREEN] = g2;
      inputMatrix[BIN_2][CLEAR] = c2;
      inputMatrix[BIN_3][BROWN] = b3;
      inputMatrix[BIN_3][GREEN] = g3;
      inputMatrix[BIN_3][CLEAR] = c3;

      transformedMatrix = transformMatrix(inputMatrix);

      countSums(transformedMatrix);
    }
    printMatrix(inputMatrix);
    printMatrix(transformedMatrix);
    // System.out.println(bin1);
    // System.out.println(bin2);
    // System.out.println(bin3);

  }

  private void countSums(int[][] matrix) {
    Map<Integer, String> set = new HashMap<Integer, String>();
    int minSum = Integer.MAX_VALUE;
    String minChars = "";
    for (int bin1 = 0; bin1 < matrix.length; bin1++ ) {
      for (int bin2 = 0; bin2 < matrix.length; bin2++ ) {
        if (bin2 == bin1) {continue;}
        for (int bin3 = 0; bin3 < matrix.length; bin3++ ) {
          if (bin3 == bin1 || bin3 == bin2) {continue;}
          int sum = matrix[bin1][BROWN] + matrix[bin2][GREEN] + matrix[bin3][CLEAR];
          if (sum < minSum) {
            String chars = convertMinCharsIntToString(bin1*100 + bin2*10 + bin3);
            set.put(sum, chars);
            if (set.contains(sum) && set.get(sum).compareTo(chars) == -1) {
              minSum = sum;
              minChars = chars;
            }
          }
          System.out.printf(
            "%d+%d+%d = %d (%d%d%d)%n"
            , matrix[bin1][BROWN]
            , matrix[bin2][GREEN]
            , matrix[bin3][CLEAR]
            , sum
            , bin1, bin2, bin3
          );
          
        } 
      }
    }
    System.out.printf("Min: %d (%s)%n", minSum, minChars);
  }

  private String convertMinCharsIntToString(int minChars) {
    String ret = "";
    int binNrWithBrowns = minChars / 100;
    int binNrWithGreens = (minChars - binNrWithBrowns*100) / 10;
    int binNrWithClears = (minChars - binNrWithBrowns*100 - binNrWithGreens*10);

    if (binNrWithBrowns == 0) {
      ret += "B";
    } else if (binNrWithGreens == 0) {
      ret += "G";
    } else if (binNrWithClears == 0) {
      ret += "C";
    }
    if (binNrWithBrowns == 1) {
      ret += "B";
    } else if (binNrWithGreens == 1) {
      ret += "G";
    } else if (binNrWithClears == 1) {
      ret += "C";
    }
    if (binNrWithBrowns == 2) {
      ret += "B";
    } else if (binNrWithGreens == 2) {
      ret += "G";
    } else if (binNrWithClears == 2) {
      ret += "C";
    }

    return ret;
  }
  /**
  * [1][B] = [2][B] + [3][B] 
  * [2][B] = [1][B] + [3][B]
  * [3][B] = [1][B] + [2][B]
  * ...
  */
  private int[][] transformMatrix(int[][] matrix) {
    int[][] transMatrix = new int[3][3];

    for (int binColor = 0; binColor < matrix.length; binColor++ ) {
      for (int binNr = 0; binNr < matrix.length; binNr++ ) {
        for (int binNrInner = 0; binNrInner < matrix.length; binNrInner++ ) {
          if (binNrInner == binNr) {
            continue;
          }
          transMatrix[binNr][binColor] += matrix[binNrInner][binColor];
        } 
      }
    }
    return transMatrix;
  }

  private void printMatrix(int[][] matrix) {
    for (int[] bin : matrix) {
      for (int color : bin) {
        System.out.print(color + " ");
      }
      System.out.println();
    }
  }
  public static class Bin {

    private final int initialBrown;
    private final int initialGreen;
    private final int initialClear;

    private int brown;
    private int green;
    private int clear;

    public Bin(int b, int g, int c) {
      initialBrown = b;
      initialGreen = g;
      initialClear = c;
      brown = b;
      green = g;
      clear = c;
    }

    public void reset() {
      brown = initialBrown;
      green = initialGreen;
      clear = initialClear;
    }

    public int moveBrownTo(Bin otherBin) {
      int ret = getAllBrown();
      otherBin.putBrown(ret);
      return ret;
    }
    public int moveGreenTo(Bin otherBin) {
      int ret = getAllGreen();
      otherBin.putGreen(ret);
      return ret;
    }
    public int moveClearTo(Bin otherBin) {
      int ret = getAllClear();
      otherBin.putClear(ret);
      return ret;
    }

    public int getAllBrown() {
      int b = brown;
      brown = 0;
      return b;
    }
    public int getAllGreen() {
      int g = green;
      green = 0;
      return g;
    }
    public int getAllClear() {
      int c = clear;
      clear = 0;
      return c;
    }

    public void putBrown(int b) {
      brown += b;
    } 
    public void putGreen(int g) {
      green += g;
    }    
    public void putClear(int c) {
      clear += c;
    }

    @Override
    public String toString() {
      return "Bin{" +
             "brown=" + brown +
             ", green=" + green +
             ", clear=" + clear +
             '}';
    }

  }


}