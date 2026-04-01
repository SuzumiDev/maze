package nl.uu.maze.benchmarks.setter;

/**
 * Benchmark class that classifies triangles based on the lengths of their
 * sides.
 * <p>
 * While the method itself is relatively simple, and there's only 11 distinct
 * test cases for the engine to find for each method, they operate different
 * types of numbers.
 * The double and float variants are significantly harder becuase of the
 * considerable time it takes to solve path constraints for them.
 * The integer variant takes less than a second.
 * Thus, this method effectively tests a search strategy's priorities. I.e.,
 * does it prioritize easy-to-solve paths, so it can quickly create many test
 * cases, and cover the entirity of the integer variant, or does it prioritize
 * other heuristics more, potentially causing it to spend too much time solving
 * constraints for the double and float methods, while ignoring the integer
 * variant (which could give easy coverage score)?
 * The query cost heuristic works well for this class, while DFS would most
 * likely go into one of the methods operating on floating-point numbers, and
 * never even reach the easy integer variant before running out of time.
 */
public class TriangleClassifier {
    public enum TriangleType {
        EQUILATERAL,
        ISOSCELES,
        SCALENE,
        INVALID
    }

    private double a;
    private double b;
    private double c;

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public TriangleType classifyDouble() {
        if (a <= 0 || b <= 0 || c <= 0) {
            return TriangleType.INVALID;
        }
        if (a + b <= c || a + c <= b || b + c <= a) {
            return TriangleType.INVALID;
        }

        if (a == b && b == c) {
            return TriangleType.EQUILATERAL;
        } else if (a == b || b == c || a == c) {
            return TriangleType.ISOSCELES;
        } else {
            return TriangleType.SCALENE;
        }
    }

    private float d;
    private float e;
    private float f;

    public void setD(float d) {
        this.d = d;
    }

    public void setE(float e) {
        this.e = e;
    }

    public void setF(float f) {
        this.f = f;
    }

    public TriangleType classifyFloat() {
        if (d <= 0 || e <= 0 || f <= 0) {
            return TriangleType.INVALID;
        }
        if (d + e <= f || d + f <= e || e + f <= d) {
            return TriangleType.INVALID;
        }

        if (d == e && e == f) {
            return TriangleType.EQUILATERAL;
        } else if (d == e || e == f || d == f) {
            return TriangleType.ISOSCELES;
        } else {
            return TriangleType.SCALENE;
        }
    }

    private int g;
    private int h;
    private int i;

    public void setG(int g) {
        this.g = g;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setI(int i) {
        this.i = i;
    }

    public TriangleType classifyInt() {
        if (g <= 0 || h <= 0 || i <= 0) {
            return TriangleType.INVALID;
        }
        if (g + h <= i || g + i <= h || h + i <= g) {
            return TriangleType.INVALID;
        }

        if (g == h && h == i) {
            return TriangleType.EQUILATERAL;
        } else if (g == h || h == i || g == i) {
            return TriangleType.ISOSCELES;
        } else {
            return TriangleType.SCALENE;
        }
    }
}
