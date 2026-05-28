package nl.uu.devtests;

public class MethodTest {

    private int test;

    public MethodTest(int test) {
        this.test = test;
    }

    public int testMethod(int test2) {
        if (test2 > test) {
            return 1;
        }
        if (test2 == 50439) {
            return 2;
        }
        return 3;
    }
}