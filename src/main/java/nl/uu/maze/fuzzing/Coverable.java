package nl.uu.maze.fuzzing;

public interface Coverable {

    public float getLineCoverage();
    public int getTimesCovered();
    public float getTransitionCoverage();

    public void setLineCoverage(float lineCoverage);
    public void setTimesCovered(int timesCovered);
    public void setTransitionCoverage(float transitionCoverage);
}
