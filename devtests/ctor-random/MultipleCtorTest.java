public class MultipleCtorTest {
    int i;
    int j;

    public MultipleCtorTest(int i) {
        this(i, 0);
    }

    public MultipleCtorTest(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int testMultipleCtor() {
        if (this.i > 5 && this.j > 5) {
            return 0;
        }

        if (this.i < 5 && this.j > 5) {
            return 1;
        }

        if (this.i > 5 && this.j < 5) {
            return 2;
        }
    }

}