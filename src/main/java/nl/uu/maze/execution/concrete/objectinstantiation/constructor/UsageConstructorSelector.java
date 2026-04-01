package nl.uu.maze.execution.concrete.objectinstantiation.constructor;

import nl.uu.maze.analysis.JavaAnalyzer;
import nl.uu.maze.execution.concrete.ObjectInstantiation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.util.Set;

public class UsageConstructorSelector extends ConstructorSelector {
    private JavaSootMethod[] methods;
    private static Logger logger = LoggerFactory.getLogger(UsageConstructorSelector.class);

    public UsageConstructorSelector(JavaSootMethod method, Class<?> clazz, JavaSootMethod[] methods) {
        super(method, clazz);
        this.methods = methods;
    }

    @Override
    public Constructor<?> selectConstructor() {
        Set<String> usedFields = ObjectInstantiation.getAccessedVariables(method, clazz);
        JavaSootMethod selected = null;
        int highestRelevant = 0;
        for (JavaSootMethod method1 : methods) {
            if (!method1.getName().contains("<init>")) continue;
            int relevant = 0;
            for (String field : ObjectInstantiation.getSideEffects(method1)) {
                if (usedFields.contains(field))
                    relevant++;
            }

            if (selected == null) {
                selected = method1;
                highestRelevant = relevant;
                continue;
            }

            if (relevant > highestRelevant) {
                selected = method1;
                highestRelevant = relevant;
                continue;
            }

            if (relevant == highestRelevant) {
                if (selected.getParameterCount() > method1.getParameterCount()) {
                    selected = method1;
                }
            }
        }

        JavaAnalyzer analyzer = JavaAnalyzer.getInstance();
        try {
            if (selected != null)
                return analyzer.getJavaConstructor(selected, clazz);
            else
                return null; // since there is always an empty constructor we should never reach this code
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            logger.error("No constructor found for class {}", clazz.getName());
            return null;
        }
    }
}
