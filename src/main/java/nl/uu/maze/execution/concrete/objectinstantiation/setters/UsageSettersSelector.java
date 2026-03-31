package nl.uu.maze.execution.concrete.objectinstantiation.setters;

import nl.uu.maze.analysis.JavaAnalyzer;
import nl.uu.maze.execution.ArgMap;
import nl.uu.maze.execution.MethodType;
import nl.uu.maze.execution.concrete.ObjectInstantiation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.java.core.JavaSootClass;
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
    public List<JavaSootMethod> selectSetters(Constructor<?> constructor) {
        List<JavaSootMethod> selectedSetters = new ArrayList<>();
        Set<String> usedFields = ObjectInstantiation.getAccessedVariables(method, clazz);

        for (JavaSootMethod method1 : methods) {
            if (method1.equals(method)) continue;
            if (method1.getName().contains("<init>")) continue;

            Set<String> alteredFields = ObjectInstantiation.getSideEffects(method1);
            for (String field : alteredFields) {
                if (usedFields.contains(field)) {
                    selectedSetters.add(method1);
                    break;
                }
            }
        }

        return selectedSetters;
    }
}
