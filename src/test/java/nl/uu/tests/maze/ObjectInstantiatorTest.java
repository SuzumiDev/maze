package nl.uu.tests.maze;

import nl.uu.maze.analysis.JavaAnalyzer;
import nl.uu.maze.execution.concrete.objectinstantiation.constructor.BiggestConstructorSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.constructor.ConstructorSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.constructor.SmallestConstructorSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.constructor.UsageConstructorSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.setters.AllSettersSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.setters.NoSettersSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.setters.SettersSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.setters.UsageSettersSelector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaSootClass;
import sootup.java.core.JavaSootMethod;
import sootup.java.core.views.JavaView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectInstantiatorTest {
    public class TesterClass {
        private int i;
        private int j;
        private int k;

        private int a;
        private int b;

        public TesterClass(int i) {
            this.i = i;
        }

        public TesterClass(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public TesterClass(int i, int j, int k) {
            this.i = i;
            this.j = j;
            this.k = k;
        }

        public void setA(int a) {
            this.a = a;
        }

        public void setB(int b) {
            this.b = b;
        }

        public int testI() {
            int m = i + 1;
            return m;
        }

        public int testIj() {
            if (i == j) {
                return 1;
            }
            return 2;
        }

        public int testIjk() {
            if (i == j) {
                return 1;
            }
            if (i == k) {
                return 2;
            }
            return 3;
        }

        public int testJa() {
            if (j == a) {
                return 1;
            }
            return 2;
        }

        public int testkB() {
            if (k == b) {
                return 1;
            }
            return 2;
        }

    }

    public JavaSootClass getDummyClass() {
        AnalysisInputLocation inputLocation = new JavaClassPathAnalysisInputLocation(Paths.get("target/test-classes").toString());
        JavaView view = new JavaView(List.of(inputLocation));
        if (!view.getClasses().isEmpty()) {
            return view.getClass(view.getIdentifierFactory().getClassType(TesterClass.class.getName())).orElseThrow();
        }
        throw new RuntimeException("class not found");
    }

    public Constructor<?> getDefaultConstructor() {
        return TesterClass.class.getConstructors()[0];
    }

    public JavaSootMethod getDefaultMethod(JavaSootClass javaSootClass) {
        return javaSootClass.getMethods().stream().findFirst().orElseThrow();
    }

    public Constructor<?>[] getSortedConstructors() {
        Constructor<?>[] ctors = TesterClass.class.getDeclaredConstructors();
        Arrays.sort(ctors, Comparator.comparingInt(Constructor::getParameterCount));
        return ctors;
    }

    @BeforeAll
    public static void prepare() throws MalformedURLException {
        try {
            JavaAnalyzer.initialize(Paths.get("target/test-classes").toString(), TesterClass.class.getClassLoader());
        } catch (Exception ignored) {
            JavaAnalyzer.reinitialize(Paths.get("target/test-classes").toString(), TesterClass.class.getClassLoader());
        }
    }

    @Test
    public void testSmallestCon() {
        JavaSootClass javaSootClass = getDummyClass();
        ConstructorSelector selector = new SmallestConstructorSelector(getDefaultMethod(javaSootClass), TesterClass.class);
        Constructor<?> smallest = getSortedConstructors()[0];

        Constructor<?> selected = selector.selectConstructor();

        assertEquals(smallest, selected);
    }

    @Test
    public void testBiggestCon() {
        JavaSootClass javaSootClass = getDummyClass();
        ConstructorSelector selector = new BiggestConstructorSelector(getDefaultMethod(javaSootClass), TesterClass.class);
        Constructor<?> biggest = getSortedConstructors()[TesterClass.class.getDeclaredConstructors().length - 1];

        Constructor<?> selected = selector.selectConstructor();

        assertEquals(biggest, selected);
    }

    @Test
    public void testUsageCon() {
        JavaSootClass javaSootClass = getDummyClass();
        JavaSootMethod testIj = javaSootClass.getMethodsByName("testIj").stream().findFirst().orElseThrow();
        Constructor<?> ij = Arrays.stream(getSortedConstructors()).filter((c) -> c.getParameterCount() == 3).findFirst().orElseThrow(); // needs to be 3 for inner classes
        ConstructorSelector selector = new UsageConstructorSelector(testIj, TesterClass.class);

        Constructor<?> selected = selector.selectConstructor();

        assertEquals(ij, selected);
    }

    @Test
    public void testNoSetter() throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        JavaSootClass javaSootClass = getDummyClass();
        SettersSelector settersSelector = new NoSettersSelector(getDefaultMethod(javaSootClass), TesterClass.class, javaSootClass.getMethods().toArray(JavaSootMethod[]::new), JavaAnalyzer.getInstance());

        List<JavaSootMethod> selected = settersSelector.selectSetters(getDefaultConstructor());

        assertEquals(0, selected.size());
    }

    @Test
    public void testAllSetters() throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        JavaSootClass javaSootClass = getDummyClass();
        SettersSelector settersSelector = new AllSettersSelector(getDefaultMethod(javaSootClass), TesterClass.class, javaSootClass.getMethods().toArray(JavaSootMethod[]::new), JavaAnalyzer.getInstance());

        List<JavaSootMethod> selected = settersSelector.selectSetters(getDefaultConstructor());

        assertEquals(2, selected.size());
        Set<JavaSootMethod> testSet = new HashSet<>();
        for (JavaSootMethod method : selected) {
            assertTrue(testSet.add(method));
        }

    }

    @Test
    public void testUsageSetter() throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        JavaSootClass javaSootClass = getDummyClass();
        JavaSootMethod method = javaSootClass.getMethods().stream().filter((m) -> m.getName().equals("testJa")).findFirst().orElseThrow();
        JavaSootMethod setter = javaSootClass.getMethods().stream().filter((m) -> m.getName().equals("setA")).findFirst().orElseThrow();
        JavaSootMethod[] setters = { setter };
        SettersSelector settersSelector = new UsageSettersSelector(method, TesterClass.class, javaSootClass.getMethods().toArray(JavaSootMethod[]::new), JavaAnalyzer.getInstance());

        List<JavaSootMethod> selected = settersSelector.selectSetters(getDefaultConstructor());

        assertArrayEquals(setters, selected.toArray(JavaSootMethod[]::new));

    }

}
