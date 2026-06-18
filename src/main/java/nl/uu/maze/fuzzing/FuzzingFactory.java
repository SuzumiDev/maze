package nl.uu.maze.fuzzing;

public class FuzzingFactory {

    public enum FuzzingStrategy {
        NONE, RANDOM, GENETIC
    }

    public enum GeneticLevel {
        SUITE, GENOTYPE, BOTH
    }
}
