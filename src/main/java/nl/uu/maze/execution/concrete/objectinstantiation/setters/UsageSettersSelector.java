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
        for (var v : argMap.args.entrySet()) {
            logger.debug("var {} with type {}", v.getKey(), v.getValue().getClass().getTypeName());
        }

        for (JavaSootMethod method1 : methods) {
            if (method1.equals(method)) continue;
            Object instance = constructor.newInstance(args);
            Method m = analyzer.getJavaMethod(method1.getSignature());
            logger.debug("checking method {} for setter", method1.getName());
            for (String field : ObjectInstantiation.getSideEffects(instance, m)) {
                logger.debug("side effect {}", field);
                if (usedFields.contains(field)) {
                    logger.debug("selected setter {}", method1.getName());
                    selectedSetters.add(method1);
                    break;
                }
            }
        }

        return selectedSetters;
    }
}
