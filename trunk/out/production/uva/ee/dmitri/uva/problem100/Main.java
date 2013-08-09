package ee.dmitri.uva.problem100;

// 100 - The 3n + 1 problem
// http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=36
// Time limit: 3.000 seconds
// 25.07.2013

// 1 submission: 100    32531   12102172    2013-07-25 10:05:38     1.248
// 2 submission: 100    31197   12102357    2013-07-25 10:58:54     1.135
// 3 submission: 100    31194   12102481    2013-07-25 11:30:10     1.132
// 4 submission: 100    30661   12102637    2013-07-25 12:05:26     1.102

import java.io.IOException;
import java.util.StringTokenizer;

public class Main {
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

  void begin() {
    String input;
    StringTokenizer idata;
    StringBuilder builder = new StringBuilder();
    int a, b, count, countMax;
    
    while ((input = Main.readLine(255)) != null) {
      idata = new StringTokenizer(input, " ");
      if (!idata.hasMoreElements()) {
        break;
      }
      a = Integer.parseInt(idata.nextToken());
      b = Integer.parseInt(idata.nextToken());
      boolean change = false;
      if (a > b) {
        int temp = a;
        a = b;
        b = temp;
        change = true;
      }

      countMax = -1;
      for (int i = a; i <= b; i++) {
        count = count(i);
        if (count > countMax) {
          countMax = count;
        }
      }
      if (change) {
        builder.append(b).append(" ").append(a).append(" ");
      }
      else {
        builder.append(a).append(" ").append(b).append(" ");
      }
      builder.append(countMax).append("\n");
    }

    System.out.print(builder.toString());
  }

  int count(int n) {
    int count = 0;

    while (n != 1) {
      count++;
      if (n % 2 == 1) {
        n = 3 * n + 1;
        continue;
      }
      n = n / 2;
    }

    return ++count;
  }

}