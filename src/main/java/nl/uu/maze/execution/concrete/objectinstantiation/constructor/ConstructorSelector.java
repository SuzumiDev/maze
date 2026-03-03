package nl.uu.maze.execution.concrete.objectinstantiation.constructor;

import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;

public abstract class ConstructorSelector {

    protected JavaSootMethod method;
    protected Class<?> clazz;

    protected ConstructorSelector(JavaSootMethod method, Class<?> clazz) {
        this.method = method;
        this.clazz = clazz;
    }

    public abstract Constructor<?> selectConstructor();

}
