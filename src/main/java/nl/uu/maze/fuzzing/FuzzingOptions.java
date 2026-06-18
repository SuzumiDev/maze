package nl.uu.maze.fuzzing;

import nl.uu.maze.fuzzing.FuzzingFactory.GeneticLevel;

public class FuzzingOptions {
    private final int geneticGenerations;
    private final int geneticSuites;
    private final int geneticGenotypes;
    private final int geneticMaximumSuites;
    private final int geneticMaximumGenotypes;
    private final int randomInitialStates;
    private final GeneticLevel geneticLevel;
    private final int limitCoverage;

    public FuzzingOptions(int geneticGenerations, int geneticSuites, int geneticGenotypes, int geneticMaximumSuites, int randomInitialStates, int geneticMaximumGenotypes, GeneticLevel geneticLevel, int limitCoverage) {
        this.geneticGenerations = geneticGenerations;
        this.geneticSuites = geneticSuites;
        this.geneticGenotypes = geneticGenotypes;
        this.geneticMaximumSuites = geneticMaximumSuites;
        this.randomInitialStates = randomInitialStates;
        this.geneticMaximumGenotypes = geneticMaximumGenotypes;
        this.geneticLevel = geneticLevel;
        this.limitCoverage = limitCoverage;
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

    public GeneticLevel getGeneticLevel() {
        return geneticLevel;
    }

    public int getLimitCoverage() {
        return limitCoverage;
    }


}
