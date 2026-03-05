package nl.uu.maze.execution.concrete.objectinstantiation;

import nl.uu.maze.execution.ArgMap;
import nl.uu.maze.execution.concrete.ExecutionResult;
import nl.uu.maze.execution.concrete.objectinstantiation.constructor.ConstructorSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.setters.SettersSelector;

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

    public ExecutionResult createInstance(ArgMap argMap) {
        if (selectedConstructor == null) throw new IllegalStateException("No constructor selected!");
        if (selectedSetters == null) throw new IllegalStateException("Setters not scanned!");

        try {
            selectedConstructor.setAccessible(true); // todo: this needs to be looked at properly
            Object instance = selectedConstructor.newInstance(argMap);

            for (Method m : selectedSetters) {
                m.invoke(instance, argMap);
            }

            return new ExecutionResult(instance, null, false);
        } catch (Exception e) {
            return new ExecutionResult(null, e, true);
        }
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
