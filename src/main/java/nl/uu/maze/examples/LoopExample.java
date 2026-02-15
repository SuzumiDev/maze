package nl.uu.maze.examples;

public class LoopExample {

    public int foobar(int x, int y, int[] z) {
        int r = 0;

        for (int i : z) {
            if (i < 4) {
                return 1;
            }
        }

        return r;
    }
}
