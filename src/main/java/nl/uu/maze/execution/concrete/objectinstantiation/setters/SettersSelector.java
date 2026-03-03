package nl.uu.maze.execution.concrete.objectinstantiation.setters;

import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Method;
import java.util.List;

public abstract class SettersSelector {

    protected JavaSootMethod method;
    protected Class<?> clazz;

    protected SettersSelector(JavaSootMethod method, Class<?> clazz) {
        this.method = method;
        this.clazz = clazz;
    }

    public abstract List<Method> selectSetters();
}
