package nl.uu.maze.fuzzing;

public class FuzzingOptions {
    private final int geneticGenerations;
    private final int geneticSuites;
    private final int geneticGenotypes;
    private final int geneticMaximumSuites;
    private final int geneticMaximumGenotypes;
    private final int randomInitialStates;

    public FuzzingOptions(int geneticGenerations, int geneticSuites, int geneticGenotypes, int geneticMaximumSuites, int randomInitialStates, int geneticMaximumGenotypes) {
        this.geneticGenerations = geneticGenerations;
        this.geneticSuites = geneticSuites;
        this.geneticGenotypes = geneticGenotypes;
        this.geneticMaximumSuites = geneticMaximumSuites;
        this.randomInitialStates = randomInitialStates;
        this.geneticMaximumGenotypes = geneticMaximumGenotypes;
    }

    public int getGeneticGenerations() {
        return geneticGenerations;
    }

    public int getGeneticSuites() {
        return geneticSuites;
    }

    public int getGeneticGenotypes() {
        return geneticGenotypes;
    }

    public int getGeneticMaximumSuites() {
        return geneticMaximumSuites;
    }

    public int getGeneticMaximumGenotypes() {
        return geneticMaximumGenotypes;
    }

    public int getRandomInitialStates() {
        return randomInitialStates;
    }



}
