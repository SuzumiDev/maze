package nl.uu.maze.execution.concrete.objectinstantiation;

import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;

public class SmallestInstantiator extends ObjectInstantiator {

    protected SmallestInstantiator(Class<?> clazz, JavaSootMethod method) {
        super(clazz, method);
    }

    @Override
    public void selectConstructor() {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (this.selectedConstructor == null) {
                this.selectedConstructor = constructor;
                continue;
            }
            if (this.selectedConstructor.getParameterCount() > constructor.getParameterCount())
                this.selectedConstructor = constructor;
        }
    }

    @Override
    public void selectSetters() {

    }
}
