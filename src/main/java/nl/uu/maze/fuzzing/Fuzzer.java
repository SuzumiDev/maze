package nl.uu.maze.fuzzing;

import nl.uu.maze.execution.symbolic.SymbolicStateValidator;
import nl.uu.maze.search.strategy.ConcreteSearchStrategy;
import sootup.java.core.JavaSootMethod;

import java.util.List;

public abstract class Fuzzer {

    protected List<Suite> suiteList;
    protected List<JavaSootMethod> methodList;

    /**
     * @return the name of this fuzzer
     */
    public abstract String getName();

    protected Fuzzer(List<JavaSootMethod> methodList) {
        this.methodList = methodList;
        generateInitialSuite();
    }

    protected Fuzzer(List<JavaSootMethod> methodList, List<Suite> initialSuite) {
        this.methodList = methodList;
        this.suiteList = initialSuite;
    }

    public abstract Suite getNextSuite(ConcreteSearchStrategy strategy, SymbolicStateValidator validator, int executionDeadline);

    protected abstract void generateInitialSuite();


}
