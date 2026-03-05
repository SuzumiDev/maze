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


    public ObjectInstantiator(ConstructorSelector constructorSelector, SettersSelector settersSelector) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        this.selectedConstructor = constructorSelector.selectConstructor();
        this.selectedSetters = settersSelector.selectSetters(this.selectedConstructor);
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

    public Constructor<?> getSelectedConstructor() {
        return this.selectedConstructor;
    }

    public enum ConstructorSelectionStrategy {
        Biggest, Random, Smallest, Usage
    }

    public enum SettersSelectionStrategy {
        All, None, Usage
    }


}
