package nl.uu.maze.execution.concrete.objectinstantiation;

import nl.uu.maze.execution.ArgMap;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class ObjectInstantiator {

    protected Class<?> clazz;
    protected Constructor<?> selectedConstructor;
    protected List<Method> setters;
    protected JavaSootMethod method;

    protected ObjectInstantiator(Class<?> clazz, JavaSootMethod method) {
        this.clazz = clazz;
        this.method = method;

    }

    public Object createInstance(ArgMap argMap) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (selectedConstructor == null) throw new IllegalStateException("No constructor selected!");
        if (setters == null) throw new IllegalAccessException("Setters not scanned!");
        Object instance = selectedConstructor.newInstance(argMap);
        for (Method m : setters) {
            m.invoke(instance, argMap);
        }
        return instance;
    }

    public abstract void selectConstructor();

    public abstract void selectSetters();


}
