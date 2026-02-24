package nl.uu.maze.fuzzing;

import nl.uu.maze.execution.ArgMap;
import sootup.java.core.JavaSootMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a test suite as argument lists
 */
public class Suite {
    private Map<JavaSootMethod, ArgMap> argMapMap;

    private float lineCoverage;
    private float conditionCoverage;

    public Suite() {
        this.argMapMap = new HashMap<>();
        this.lineCoverage = 0.0f;
        this.conditionCoverage = 0.0f;
    }

    public Suite(Map<JavaSootMethod, ArgMap> argMapMap) {
        this.argMapMap = argMapMap;
        this.lineCoverage = 0.0f;
        this.conditionCoverage = 0.0f;
    }

    /**
     * Get the arguments for the provided soot method
     * @param method The method to get the arguments for
     * @return The arguments for the provided method
     */
    public ArgMap getArguments(JavaSootMethod method) {
        return argMapMap.get(method);
    }

    /**
     * Put a new argument set in the map
     * @param method The soot method to couple the arguments to
     * @param argMap The argument map to put
     * @return Whether the operation was successful
     */
    public boolean putArgMap(JavaSootMethod method, ArgMap argMap) {
        if (argMapMap.containsKey(method)) return false;

        argMapMap.put(method, argMap);
        return true;
    }

    /**
     * Change an existing argument map in the map
     * @param method The method to change the map for
     * @param argMap The new argument map
     * @return Whether the operation was successful
     */
    public boolean setArgMap(JavaSootMethod method, ArgMap argMap) {
        if (!argMapMap.containsKey(method)) return false;

        argMapMap.replace(method, argMap);
        return true;
    }

    public void setLineCoverage(float coverage) {
        this.lineCoverage = coverage;
    }

    public void setConditionCoverage(float coverage) {
        this.conditionCoverage = coverage;
    }

    public float getLineCoverage() {
        return lineCoverage;
    }

    public float getConditionCoverage() {
        return conditionCoverage;
    }
}
