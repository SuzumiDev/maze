package nl.uu.maze.execution.concrete.objectinstantiation.setters;

import nl.uu.maze.analysis.JavaAnalyzer;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class SettersSelector {

    protected JavaSootMethod method;
    protected JavaSootMethod[] methods;
    protected Class<?> clazz;
    protected JavaAnalyzer analyzer;

    protected SettersSelector(JavaSootMethod method, Class<?> clazz, JavaSootMethod[] methods, JavaAnalyzer analyzer) {
        this.method = method;
        this.methods = methods;
        this.clazz = clazz;
        this.analyzer = analyzer;
    }

    public abstract List<JavaSootMethod> selectSetters(Constructor<?> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException;
}
