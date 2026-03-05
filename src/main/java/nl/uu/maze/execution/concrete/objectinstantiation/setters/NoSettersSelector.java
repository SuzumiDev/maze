package nl.uu.maze.execution.concrete.objectinstantiation.setters;

import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class NoSettersSelector extends SettersSelector{
    public NoSettersSelector(JavaSootMethod method, Class<?> clazz) {
        super(method, clazz);
    }

    @Override
    public List<Method> selectSetters(Constructor<?> constructor) {
        return List.of();
    }
}
