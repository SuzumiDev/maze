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
    public List<JavaSootMethod> selectSetters(Constructor<?> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        List<JavaSootMethod> selectedSetters = new ArrayList<>();
        ArgMap argMap = new ArgMap();

        Object[] args = ObjectInstantiation.generateArgs(constructor.getParameters(), MethodType.CTOR, argMap, "");

        for (JavaSootMethod method1 : methods) {
            if (method1.equals(method)) continue;
            Object instance = constructor.newInstance(args);
            try {
                Method m = analyzer.getJavaMethod(method1.getSignature());
                if (!ObjectInstantiation.getSideEffects(instance, m).isEmpty()) {
                    selectedSetters.add(method1);
                }
            } catch (NoSuchMethodException ignored) {} // SootUp does not have a getDeclaredMethods functionality, so if the method cannot be found it usually means it's not declared

        }

        return selectedSetters;
    }
}
