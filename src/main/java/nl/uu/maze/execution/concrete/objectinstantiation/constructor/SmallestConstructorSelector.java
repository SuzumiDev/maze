package nl.uu.maze.execution.concrete.objectinstantiation.constructor;

import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;

public class SmallestConstructorSelector extends ConstructorSelector {

    protected SmallestConstructorSelector(JavaSootMethod method, Class<?> clazz) {
        super(method, clazz);
    }

    @Override
    public Constructor<?> selectConstructor() {
        Constructor<?> smallest = null;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (smallest == null) {
                smallest = constructor;
                continue;
            }
            if (smallest.getParameterCount() > constructor.getParameterCount())
                smallest = constructor;
        }

        return smallest;
    }
}
