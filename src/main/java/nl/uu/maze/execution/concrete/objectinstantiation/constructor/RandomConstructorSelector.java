package nl.uu.maze.execution.concrete.objectinstantiation.constructor;

import sootup.java.core.JavaSootMethod;

import java.lang.reflect.Constructor;
import java.util.Random;

public class RandomConstructorSelector extends ConstructorSelector {

    private static final Random rand = new Random();

    public RandomConstructorSelector(JavaSootMethod method, Class<?> clazz) {
        super(method, clazz);
    }

    @Override
    public Constructor<?> selectConstructor() {
        int c = rand.nextInt(0, clazz.getConstructors().length);
        return clazz.getConstructors()[c];
    }
}
