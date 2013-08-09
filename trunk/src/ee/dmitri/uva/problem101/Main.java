package ee.dmitri.uva.problem101;

/*
101 - The Blocks Problem
http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&page=show_problem&problem=37
Time limit: 3.000 seconds
07.08.2013
Users that solved it: 9969

1 submission: 101  9704  12175680  2013-08-07 13:15:33   0.609
*/

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  /**
   * Utility function to read from stdin.
   *
   * @param maxLg - maximum length of line
   *
   * @return - one line from stdin
   */
  static String readLine(int maxLg) {
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
    StringTokenizer idata = null;
    int n = initN(idata);
    List<Integer>[] array = new List[n];
    initArray(array);
    boolean run = true;
    while (run) {
      input = Main.readLine(255);
      idata = new StringTokenizer(input, " ");

      Command cmd = new Command(idata);

      run = cmd.execute(array);
    }
    printArray(array);
  }

  int initN(StringTokenizer idata) {
    idata = new StringTokenizer(Main.readLine(255), " ");
    return Integer.parseInt(idata.nextToken());
  }

  void initArray(List<Integer>[] array) {
    for (int i = 0; i < array.length; i++) {
      array[i] = new LinkedList<Integer>();
      array[i].add(i);
    }
  }

  void printArray(List<Integer>[] array) {
    for (int i = 0; i < array.length; i++) {
      System.out.printf("%d:", i);
      for (Integer integer : array[i]) {
        System.out.printf(" %d", integer);
      }
      System.out.println();
    }
  }

  public static class Command {

    private final Action action;
    private final Type type;
    private final int a;
    private final int b;

    private List<Integer>[] array;

    public Command(StringTokenizer idata) {
      this.action = Action.valueOf(idata.nextToken().toUpperCase());
      this.a = idata.hasMoreTokens() ? Integer.parseInt(idata.nextToken()) : 0;
      this.type = idata.hasMoreTokens() ? Type.valueOf(idata.nextToken().toUpperCase()) : null;
      this.b = idata.hasMoreTokens() ? Integer.parseInt(idata.nextToken()) : 0;
    }

    private boolean isIllegal(Position aPos, Position bPos) {
      return (a == b) || aPos.getX() == bPos.getX();
    }

    public boolean execute(List<Integer>[] arr) {
      array = arr;
      Position aPos = findBlock(a);
      Position bPos = findBlock(b);
      switch (action) {
        case MOVE:
          if (isIllegal(aPos, bPos)) { return true; }
          switch (type) {
            case ONTO:
              moveOnto(aPos, bPos);
              break;
            case OVER:
              moveOver(aPos, bPos);
              break;
          }
          break;
        case PILE:
          if (isIllegal(aPos, bPos)) { return true; }
          switch (type) {
            case ONTO:
              pileOnto(aPos, bPos);
              break;
            case OVER:
              pileOver(aPos, bPos);
              break;
          }
          break;
        case QUIT:
          return false;
      }
      return true;
    }

    private Position findBlock(int blockId) {
      // check in initial place
      List<Integer> list = array[blockId];
      int index = list.indexOf(blockId);
      if (index != -1) {
        return new Position(blockId, index);
      }
      // check from the beggining
      for (int i = 0; i < array.length; i++) {
        if (i == blockId) {
          continue;
        }
        List<Integer> list1 = array[i];
        int index1 = list1.indexOf(blockId);
        if (index1 != -1) {
          return new Position(i, index1);
        }
      }
      return null;
    }

    private void moveOnto(Position aPos, Position bPos) {
      returnAnyBlocksThatAreStackedOnTopOfBlockToTheirInitialPositions(aPos);
      returnAnyBlocksThatAreStackedOnTopOfBlockToTheirInitialPositions(bPos);

      int a = get(aPos);
      put(a, bPos);
    }

    private void moveOver(Position aPos, Position bPos) {
      returnAnyBlocksThatAreStackedOnTopOfBlockToTheirInitialPositions(aPos);

      int a = get(aPos);
      putOnTop(a, bPos);
    }

    private void pileOnto(Position aPos, Position bPos) {
      returnAnyBlocksThatAreStackedOnTopOfBlockToTheirInitialPositions(bPos);

      List<Integer> aList = getAll(aPos);
      putAll(aList, bPos);
    }

    private void pileOver(Position aPos, Position bPos) {
      List<Integer> aList = getAll(aPos);
      putAll(aList, bPos);
    }

    private int get(Position pos) {
      int a = array[pos.getX()].get(pos.getY());
      array[pos.getX()].remove(pos.getY());
      return a;
    }

    private List<Integer> getAll(Position pos) {
      List<Integer> list = new LinkedList<Integer>();
      for (int i = array[pos.getX()].size(); i > pos.getY(); i--) {
        int a = array[pos.getX()].get(i - 1);
        array[pos.getX()].remove(i - 1);
        list.add(a);
      }

      Collections.reverse(list);
      return list;
    }

    private void put(int a, Position pos) {
      array[pos.getX()].add(pos.getY() + 1, a);
    }

    private void putAll(List<Integer> list, Position pos) {
      array[pos.getX()].addAll(list);
    }

    private void putOnTop(int a, Position pos) {
      array[pos.getX()].add(a);
    }

    private void returnAnyBlocksThatAreStackedOnTopOfBlockToTheirInitialPositions(Position pos) {
      List<Integer> bList = array[pos.getX()];
      for (int i = bList.size() - 1; i > pos.getY(); i--) {
        putBack(new Position(pos.getX(), i));
      }
    }

    private void putBack(Position pos) {
      int c = get(pos);
      array[c].add(c);
    }

    @Override
    public String toString() {
      return "Command{" +
             action +
             " " + a +
             " " + type +
             " " + b +
             '}';
    }

    enum Action {
      MOVE, PILE, QUIT;
    }

    enum Type {
      ONTO, OVER;
    }
  }

  public static class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
  }

}