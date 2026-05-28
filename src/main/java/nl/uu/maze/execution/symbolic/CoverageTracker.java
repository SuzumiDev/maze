package nl.uu.maze.execution.symbolic;

import java.util.Set;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Stack;

import nl.uu.maze.util.Pair;
import sootup.core.graph.StmtGraph;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.Body;
import sootup.java.core.JavaSootMethod;

/**
 * Tracks the coverage of statements during symbolic execution.
 */
public class CoverageTracker {
    private static CoverageTracker instance;

    public static CoverageTracker getInstance() {
        if (instance == null) {
            instance = new CoverageTracker();
        }
        return instance;
    }

    private final Set<Stmt> coveredStmts;
    private final Set<Pair<Stmt, Stmt>> coveredTransitions;
    private int timesCovered;

    private CoverageTracker() {
        // Use identity hash map to avoid potentially expensive equals() calls on
        // statements (which are unique by reference, so reference equality suffices)
        coveredStmts = Collections.newSetFromMap(new IdentityHashMap<>());
        coveredTransitions = Collections.newSetFromMap(new IdentityHashMap<>());
        timesCovered = 0;
    }

    /**
     * Marks a statement as covered.
     * 
     * @return {@code true} if the statement was not covered before, {@code false}
     *         otherwise
     */
    public boolean setCovered(Stmt stmt) {
        timesCovered++;
        return coveredStmts.add(stmt);
    }

    public boolean setCovered(Stmt before, Stmt after) {
        return coveredTransitions.add(new Pair<>(before, after));
    }

    /**
     * Checks whether a statement is covered.
     */
    public boolean isCovered(Stmt stmt) {
        return coveredStmts.contains(stmt);
    }

    public boolean isCovered(Stmt before, Stmt after) {
        return coveredTransitions.contains(new Pair<>(before, after));
    }

    public int getCoveredNumber() {
        return coveredStmts.size();
    }

    public int getTimesCovered() {
        return timesCovered;
    }

    public int getCoveredTransitionsNumber() {
        return coveredTransitions.size();
    }

    /**
     * Resets the coverage tracker.
     * 
     * @apiNote This method need <b>not</b> be called between different methods
     *          under
     *          test for the same class, because test cases for one method can cover
     *          statements in another method as well!
     */
    public void reset() {
        coveredStmts.clear();
        coveredTransitions.clear();
        timesCovered = 0;
    }

    public static int countTransitions(Body body) {
        int transitions = 0;
        for (Stmt stmt : body.getStmts()) {
            transitions += stmt.getExpectedSuccessorCount();
        }

        return transitions;
    }

    public static int countRealTransitions(SymbolicState init, JavaSootMethod method, StmtGraph<?> cfg) {
        int transitions = 0;
        Stack<SymbolicState> states = new Stack<>();
        states.add(init);

        while (!states.isEmpty()) {
            SymbolicState currentState = states.pop();
            if (currentState.isCtorState() && currentState.isFinalState()) {
                currentState.switchToMethodState();
                currentState.setMethod(method, cfg);
            }
            for (Stmt succ : currentState.getSuccessors()) {
                transitions++;
                SymbolicState newState = currentState.clone();
                newState.setStmt(succ);
                states.add(newState);
            }
        }

        return transitions;
    }
}
