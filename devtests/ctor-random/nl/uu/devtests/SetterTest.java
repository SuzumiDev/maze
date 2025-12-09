package nl.uu.devtests;

public class SetterTest {

    private int i;

    public void setI(int i) {
        this.i = i;
    }

    public boolean test() {
        if (i < 5) {
            return true;
        }
        return false;
    }

}
