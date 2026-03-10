package nl.uu.devtests;

public class IfTest {
    private int j;
    private String k;
    private String l;

    public IfTest(int j) {
        this.j = j;
    }

    public IfTest(String k, int j) {
        this.k = k;
        this.j = j;
    }

    public void setL(String l) {
        this.l = l;
    }


    public int testIf(int i) {
        if (i > j) {
            return 1;
        }
        if (i > 4) {
            return 5;
        }
        //if (k != null) {
        //    return 2;
        //}
        //if (l != null) {
        //    return 4;
        //}

        return 3;
    }
}