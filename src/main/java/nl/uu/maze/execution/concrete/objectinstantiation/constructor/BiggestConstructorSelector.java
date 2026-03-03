package nl.uu.maze.execution.concrete.objectinstantiation.constructor;

import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;

public class BiggestConstructorSelector extends ConstructorSelector{
    protected BiggestConstructorSelector(JavaSootMethod method, Class<?> clazz) {
        super(method, clazz);
    }

    @Override
    public Constructor<?> selectConstructor() {
        Constructor<?> biggest = null;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (biggest == null) {
                biggest = constructor;
                continue;
            }
            if (biggest.getParameterCount() < constructor.getParameterCount())
                biggest = constructor;
        }

        return biggest;
    }
}
