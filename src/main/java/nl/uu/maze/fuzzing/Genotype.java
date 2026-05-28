package nl.uu.maze.fuzzing;

import nl.uu.maze.execution.ArgMap;

public class Genotype implements Coverable {

    private ArgMap argMap;
    private float lineCoverage;
    private int timesCovered;
    private float transitionCoverage;

    public Genotype() {
        this.argMap = new ArgMap();
    }

    public Genotype(ArgMap argMap) {
        this.argMap = argMap;
    }

    public ArgMap getArgMap() {
        return argMap;
    }

    public float getLineCoverage() {
        return lineCoverage;
    }

    public int getTimesCovered() {
        return timesCovered;
    }

    public float getTransitionCoverage() {
        return transitionCoverage;
    }

    public void setLineCoverage(float lineCoverage) {
        this.lineCoverage = lineCoverage;
    }

    public void setTimesCovered(int timesCovered) {
        this.timesCovered = timesCovered;
    }

    public void setTransitionCoverage(float transitionCoverage) {
        this.transitionCoverage = transitionCoverage;
    }
}
