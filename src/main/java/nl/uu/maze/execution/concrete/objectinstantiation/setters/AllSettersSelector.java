package nl.uu.maze.execution.concrete.objectinstantiation.setters;

import nl.uu.maze.execution.concrete.ObjectInstantiation;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AllSettersSelector extends SettersSelector{
    protected AllSettersSelector(JavaSootMethod method, Class<?> clazz) {
        super(method, clazz);
    }

    @Override
    public List<Method> selectSetters() {
        List<Method> methods = new ArrayList<>();
        for (Method m : clazz.getDeclaredMethods()) {
            // todo: find out how you want to get a constructor here
        }
    }
}
