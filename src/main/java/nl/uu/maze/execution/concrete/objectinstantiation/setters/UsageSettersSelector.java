package nl.uu.maze.execution.concrete.objectinstantiation.setters;

import nl.uu.maze.execution.ArgMap;
import nl.uu.maze.execution.MethodType;
import nl.uu.maze.execution.concrete.ObjectInstantiation;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UsageSettersSelector extends SettersSelector {

    public UsageSettersSelector(JavaSootMethod method, Class<?> clazz) {
        super(method, clazz);
    }

    @Override
    public List<Method> selectSetters(Constructor<?> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Method> methods = new ArrayList<>();
        Set<String> usedFields = ObjectInstantiation.getAccessedVariables(method, clazz);
        ArgMap argMap = new ArgMap();
        ObjectInstantiation.generateArgs(constructor.getParameters(), MethodType.CTOR, argMap);
        Object instance = constructor.newInstance(argMap);

        for (Method m : clazz.getDeclaredMethods()) {
            for (String field : ObjectInstantiation.getSideEffects(instance, m)) {
                if (usedFields.contains(field)) {
                    methods.add(m);
                    break;
                }
            }
        }

        return methods;
    }
}
