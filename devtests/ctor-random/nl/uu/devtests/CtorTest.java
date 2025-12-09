package nl.uu.devtests;

public class CtorTest {

    private int i;

    public CtorTest(int i) {
        this.i = i;
    }

    public boolean test() {
        if (i < 5) {
            return true;
        }
        return false;
    }

}
