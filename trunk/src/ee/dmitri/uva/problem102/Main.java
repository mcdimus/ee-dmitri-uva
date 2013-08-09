package ee.dmitri.uva.problem102;

// 102 - Ecological Bin Packing
// http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=38
// Time limit: 3.000 seconds
// 08.08.2013

// 1 submission: 102 	18668 	12183059 	2013-08-09 08:16:51 	2.136
// 2 submission: 102 	17517 	12183398 	2013-08-09 10:06:25 	0.795

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
  public static final int BROWN = 0;
  public static final int GREEN = 1;
  public static final int CLEAR = 2;
  public static final int BIN_1 = 0;
  public static final int BIN_2 = 1;
  public static final int BIN_3 = 2;
  public static final int SIZE = 3;


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

  public static void main(String args[]) {
    Main myWork = new Main();  // create a dynamic instance
    myWork.begin();            // the true entry point
  }

  void begin() {
    String input;
    StringTokenizer idata;
    StringBuilder builder = new StringBuilder();

    int[][] inputMatrix = new int[SIZE][SIZE];
    int[][] transformedMatrix = null;

    while ((input = Main.readLine(255)) != null) {
      idata = new StringTokenizer(input, " ");
      if (!idata.hasMoreElements()) {
        break;
      }
      inputMatrix[BIN_1][BROWN] = Integer.parseInt(idata.nextToken());
      inputMatrix[BIN_1][GREEN] = Integer.parseInt(idata.nextToken());
      inputMatrix[BIN_1][CLEAR] = Integer.parseInt(idata.nextToken());
      inputMatrix[BIN_2][BROWN] = Integer.parseInt(idata.nextToken());
      inputMatrix[BIN_2][GREEN] = Integer.parseInt(idata.nextToken());
      inputMatrix[BIN_2][CLEAR] = Integer.parseInt(idata.nextToken());
      inputMatrix[BIN_3][BROWN] = Integer.parseInt(idata.nextToken());
      inputMatrix[BIN_3][GREEN] = Integer.parseInt(idata.nextToken());
      inputMatrix[BIN_3][CLEAR] = Integer.parseInt(idata.nextToken());

      transformedMatrix = transformMatrix(inputMatrix);
      builder.append(findSolution(transformedMatrix));
    }
    System.out.print(builder.toString());
  }

  private String findSolution(int[][] matrix) {
    Map<Integer, String> set = new HashMap<Integer, String>();
    int minSum = Integer.MAX_VALUE;
    String minChars = "";
    for (int bin1 = 0; bin1 < SIZE; bin1++) {
      for (int bin2 = 0; bin2 < SIZE; bin2++) {
        if (bin2 == bin1) {
          continue;
        }
        for (int bin3 = 0; bin3 < SIZE; bin3++) {
          if (bin3 == bin1 || bin3 == bin2) {
            continue;
          }
          int sum = matrix[bin1][BROWN] + matrix[bin2][GREEN] + matrix[bin3][CLEAR];
          String chars = convertMinCharsIntToString(bin1, bin2, bin3);
          if (sum < minSum) {
            minSum = sum;
            minChars = chars;
            set.put(sum, chars);
          } else if (sum == minSum) {
            if (set.get(sum).compareTo(chars) > 0) {
              set.put(sum, chars);
              minChars = chars;
            }
          }

        } // for bin3
      } // for bin2
    } // for bin1
    return new StringBuilder().append(minChars).append(" ").append(minSum).append("\n").toString();
  }

  private String convertMinCharsIntToString(int bin1, int bin2, int bin3) {
    StringBuilder builder = new StringBuilder();

    if (bin1 == 0) {
      builder.append("B");
    }
    else if (bin2 == 0) {
      builder.append("G");
    }
    else if (bin3 == 0) {
      builder.append("C");
    }
    if (bin1 == 1) {
      builder.append("B");
    }
    else if (bin2 == 1) {
      builder.append("G");
    }
    else if (bin3 == 1) {
      builder.append("C");
    }
    if (bin1 == 2) {
      builder.append("B");
    }
    else if (bin2 == 2) {
      builder.append("G");
    }
    else if (bin3 == 2) {
      builder.append("C");
    }

    return builder.toString();
  }

  /**
   * [1][B] = [2][B] + [3][B]
   * [2][B] = [1][B] + [3][B]
   * [3][B] = [1][B] + [2][B]
   * ...
   */
  private int[][] transformMatrix(int[][] matrix) {
    int[][] transMatrix = new int[SIZE][SIZE];

    for (int binColor = 0; binColor < SIZE; binColor++) {
      for (int binNr = 0; binNr < SIZE; binNr++) {
        for (int binNrInner = 0; binNrInner < SIZE; binNrInner++) {
          if (binNrInner == binNr) {
            continue;
          }
          transMatrix[binNr][binColor] += matrix[binNrInner][binColor];
        }
      }
    }
    return transMatrix;
  }

}