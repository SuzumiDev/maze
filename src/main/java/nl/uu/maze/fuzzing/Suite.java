package nl.uu.maze.fuzzing;

import nl.uu.maze.execution.ArgMap;
import sootup.java.core.JavaSootMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a test suite as argument lists
 */
public class Suite implements Comparable<Suite>, Coverable {
    private List<ArgMap> argMaps;

    private float lineCoverage;
    private int timesCovered;
    private float transitionCoverage;


    public Suite() {
        this.argMaps = new ArrayList<>();
        this.lineCoverage = 0.0f;
        this.timesCovered = 0;
        this.transitionCoverage = 0.0f;
    }

    public Suite(List<ArgMap> argMaps) {
        this.argMaps = argMaps;
        this.lineCoverage = 0.0f;
        this.timesCovered = 0;
        this.transitionCoverage = 0.0f;
    }

    public Suite(List<ArgMap> argMaps, float lineCoverage) {
        this.argMaps = argMaps;
        this.lineCoverage = lineCoverage;
    }

    public void addArgMap(ArgMap argMap) {
        argMaps.add(argMap);
    }

    public void setLineCoverage(float coverage) {
        this.lineCoverage = coverage;
    }

    public float getLineCoverage() {
        return lineCoverage;
    }

    public void setTimesCovered(int timesCovered) {
        this.timesCovered = timesCovered;
    }

    public int getTimesCovered() {
        return timesCovered;
    }

    public void setTransitionCoverage(float transitionCoverage) {
        this.transitionCoverage = transitionCoverage;
    }

    public float getTransitionCoverage() {
        return transitionCoverage;
    }


    @Override
    public int compareTo(Suite other) {
        int comp = Float.compare(other.getLineCoverage(), this.getLineCoverage());
        if (comp != 0) return comp;
        return Integer.compare(other.getTimesCovered(), this.getTimesCovered());
    }

    public List<ArgMap> getArgMaps() {
        return argMaps;
    }
}
