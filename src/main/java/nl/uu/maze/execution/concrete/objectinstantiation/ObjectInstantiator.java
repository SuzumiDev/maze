package nl.uu.maze.execution.concrete.objectinstantiation;

import nl.uu.maze.execution.ArgMap;
import nl.uu.maze.execution.concrete.objectinstantiation.constructor.ConstructorSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.setters.SettersSelector;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ObjectInstantiator {

    private final Constructor<?> selectedConstructor;
    private final List<Method> selectedSetters;


    public ObjectInstantiator(ConstructorSelector constructorSelector, SettersSelector settersSelector) {
        this.selectedConstructor = constructorSelector.selectConstructor();
        this.selectedSetters = settersSelector.selectSetters();
    }

    public Object createInstance(ArgMap argMap) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (selectedConstructor == null) throw new IllegalStateException("No constructor selected!");
        if (selectedSetters == null) throw new IllegalAccessException("Setters not scanned!");
        Object instance = selectedConstructor.newInstance(argMap);
        for (Method m : selectedSetters) {
            m.invoke(instance, argMap);
        }
        return instance;
    }


}
