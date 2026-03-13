package nl.uu.devtests;

public class IfTest {
    private int j;
    private String k;
    private String m;
    private int l;

    public IfTest(int j) {
        this.j = j;
    }

    public IfTest(String k, int j) {
        this.k = k;
        this.j = j;
    }

    public void setL(int l) {
        this.l = l;
    }

    public void setK(String k) {
        this.k = k;
    }

    public void setM(String m) {
        this.m = m;
    }


    public int testIf(int i) {
        if (i > j) {
            return 1;
        }
        if (i < l) {
            return 6;
        }
        if (k == "a") {
            return 7;
        }
        if (m == "b") {
            return 8;
        }

        return 3;
    }
}