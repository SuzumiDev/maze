package nl.uu.maze.execution.concrete.objectinstantiation;

import sootup.java.core.JavaSootMethod;

import java.util.Random;

public class RandomInstantiator extends ObjectInstantiator {

    private static final Random rand = new Random();

    protected RandomInstantiator(Class<?> clazz, JavaSootMethod method) {
        super(clazz, method);
    }

    @Override
    public void selectConstructor() {
        int c = rand.nextInt(0, clazz.getConstructors().length);
        this.selectedConstructor = clazz.getConstructors()[c];
    }

    @Override
    public void selectSetters() {
        //todo: think of what to do here
    }
}
