package nl.uu.maze.execution.concrete.objectinstantiation.setters;

import nl.uu.maze.analysis.JavaAnalyzer;
import nl.uu.maze.execution.ArgMap;
import nl.uu.maze.execution.MethodType;
import nl.uu.maze.execution.concrete.ObjectInstantiation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UsageSettersSelector extends SettersSelector {

    private static final Logger logger = LoggerFactory.getLogger(UsageSettersSelector.class);

    public UsageSettersSelector(JavaSootMethod method, Class<?> clazz, JavaSootMethod[] methods, JavaAnalyzer analyzer) {
        super(method, clazz, methods, analyzer);
    }

    @Override
    public List<JavaSootMethod> selectSetters(Constructor<?> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        List<JavaSootMethod> selectedSetters = new ArrayList<>();
        Set<String> usedFields = ObjectInstantiation.getAccessedVariables(method, clazz);
        ArgMap argMap = new ArgMap();
        Object[] args = ObjectInstantiation.generateArgs(constructor.getParameters(), MethodType.CTOR, argMap, "");

        for (JavaSootMethod method1 : methods) {
            if (method1.equals(method)) continue;
            Object instance = constructor.newInstance(args);
            try {
                Method m = analyzer.getJavaMethod(method1.getSignature());
                for (String field : ObjectInstantiation.getSideEffects(instance, m)) {
                    if (usedFields.contains(field)) {
                        selectedSetters.add(method1);
                        break;
                    }
                }
            } catch (NoSuchMethodException ignored) {}
        }

        return selectedSetters;
    }
}
