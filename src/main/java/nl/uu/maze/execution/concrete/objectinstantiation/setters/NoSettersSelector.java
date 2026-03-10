package nl.uu.maze.execution.concrete.objectinstantiation.setters;

import nl.uu.maze.analysis.JavaAnalyzer;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class NoSettersSelector extends SettersSelector{
    public NoSettersSelector(JavaSootMethod method, Class<?> clazz, JavaSootMethod[] methods, JavaAnalyzer analyzer) {
        super(method, clazz, methods, analyzer);
    }

    @Override
    public List<JavaSootMethod> selectSetters(Constructor<?> constructor) {
        return List.of();
    }
}
