package nl.uu.maze.execution.concrete.objectinstantiation;

import nl.uu.maze.analysis.JavaAnalyzer;
import nl.uu.maze.execution.ArgMap;
import nl.uu.maze.execution.MethodType;
import nl.uu.maze.execution.concrete.ExecutionResult;
import nl.uu.maze.execution.concrete.ObjectInstantiation;
import nl.uu.maze.execution.concrete.objectinstantiation.constructor.ConstructorSelector;
import nl.uu.maze.execution.concrete.objectinstantiation.setters.SettersSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ObjectInstantiator {

    private final Constructor<?> selectedConstructor;
    private final List<JavaSootMethod> selectedSetters;
    private final JavaAnalyzer analyzer;
    private static final Logger logger = LoggerFactory.getLogger(ObjectInstantiator.class);


    public ObjectInstantiator(ConstructorSelector constructorSelector, SettersSelector settersSelector, JavaAnalyzer analyzer) throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        this.selectedConstructor = constructorSelector.selectConstructor();
        this.selectedSetters = settersSelector.selectSetters(this.selectedConstructor);
        this.analyzer = analyzer;
    }

    public ExecutionResult createInstance(ArgMap argMap) {
        if (selectedConstructor == null) throw new IllegalStateException("No constructor selected!");
        if (selectedSetters == null) throw new IllegalStateException("Setters not scanned!");

        try {
            Object[] args = ObjectInstantiation.generateArgs(selectedConstructor.getParameters(), MethodType.CTOR, argMap, "");
            selectedConstructor.setAccessible(true); // todo: this needs to be looked at properly
            Object instance = selectedConstructor.newInstance(args);

            logger.debug("creating instance with argmap {}", argMap);

            for (var arg : argMap.args.entrySet()) {
                logger.debug("arg {} is {} in argmap", arg.getKey(), arg.getValue().getClass().getTypeName());
            }

            for (JavaSootMethod method : selectedSetters) {
                Method m = analyzer.getJavaMethod(method.getSignature());
                Object[] methodArgs = ObjectInstantiation.generateArgs(m.getParameters(), MethodType.METHOD, argMap, m.getName());
                m.invoke(instance, methodArgs);
            }

            return new ExecutionResult(instance, null, false);
        } catch (Exception e) {
            return new ExecutionResult(null, e, true);
        }
    }

    public ExecutionResult createInstance(Object[] ctorArgs, List<Object[]> settersArgs) {
        if (selectedConstructor == null) throw new IllegalStateException("No constructor selected!");
        if (selectedSetters == null) throw new IllegalStateException("Setters not scanned!");

        try {
            selectedConstructor.setAccessible(true);
            Object instance = selectedConstructor.newInstance(ctorArgs);

            for (int i = 0; i < selectedSetters.size(); i++) {
                analyzer.getJavaMethod(selectedSetters.get(i).getSignature()).invoke(instance, settersArgs.get(i));
            }

            return new ExecutionResult(instance, null, false);
        } catch (Exception e) {
            return new ExecutionResult(null, e, true);
        }
    }

    public Constructor<?> getSelectedConstructor() {
        return this.selectedConstructor;
    }
    public List<JavaSootMethod> getSelectedSetters() {
        return this.selectedSetters;
    }

    public enum ConstructorSelectionStrategy {
        Biggest, Random, Smallest, Usage
    }

    public enum SettersSelectionStrategy {
        All, None, Usage
    }


}
