package nl.uu.maze.execution.concrete.objectinstantiation.setters;

import nl.uu.maze.analysis.JavaAnalyzer;
import nl.uu.maze.execution.ArgMap;
import nl.uu.maze.execution.MethodType;
import nl.uu.maze.execution.concrete.ObjectInstantiation;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AllSettersSelector extends SettersSelector{

    public AllSettersSelector(JavaSootMethod method, Class<?> clazz, JavaSootMethod[] methods, JavaAnalyzer analyzer) {
        super(method, clazz, methods, analyzer);
    }

    @Override
    public List<JavaSootMethod> selectSetters(Constructor<?> constructor) {
        List<JavaSootMethod> selectedSetters = new ArrayList<>();

        for (JavaSootMethod method1 : methods) {
            if (method1.equals(method)) continue;
            if (method1.getName().contains("<init>")) continue;
            if (!ObjectInstantiation.getSideEffects(method1).isEmpty()) {
                selectedSetters.add(method1);
            }
        }

        return selectedSetters;
    }
}
