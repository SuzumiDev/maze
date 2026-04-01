package nl.uu.devtests;

public class Arraytest {

    private int[] arr;

    public void setArr(int[] arr) {
        this.arr = arr;
    }

    public int testArr(int i) {
        if (arr.length > i) {
            return 2;
        }
        return 1;
    }
}