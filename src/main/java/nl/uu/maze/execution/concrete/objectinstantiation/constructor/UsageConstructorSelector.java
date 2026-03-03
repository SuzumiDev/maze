package nl.uu.maze.execution.concrete.objectinstantiation.constructor;

import nl.uu.maze.execution.concrete.ObjectInstantiation;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

public class UsageConstructorSelector extends ConstructorSelector {
    protected UsageConstructorSelector(JavaSootMethod method, Class<?> clazz) {
        super(method, clazz);
    }

    @Override
    public Constructor<?> selectConstructor() {
        Set<String> usedFields = ObjectInstantiation.getAccessedVariables(method, clazz);
        Constructor<?> selected = null;
        int highestRelevant = 0;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            int relevant = 0;
            for (String field : ObjectInstantiation.getSideEffects(clazz, constructor)) {
                if (usedFields.contains(field))
                    relevant++;
            }

            if (selected == null) {
                selected = constructor;
                highestRelevant = relevant;
                continue;
            }

            if (relevant > highestRelevant) {
                selected = constructor;
                highestRelevant = relevant;
            }


        }
        return selected;
    }
}
