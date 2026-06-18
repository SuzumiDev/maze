package nl.uu.maze.execution.symbolic;

import java.util.*;

import nl.uu.maze.execution.DSEController;
import nl.uu.maze.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sootup.core.graph.StmtGraph;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.Body;
import sootup.java.core.JavaSootMethod;

/**
 * Tracks the coverage of statements during symbolic execution.
 */
public class CoverageTracker {
    private static CoverageTracker instance;
    private static final Logger logger = LoggerFactory.getLogger(CoverageTracker.class);

    public static CoverageTracker getInstance() {
        if (instance == null) {
            instance = new CoverageTracker();
        }
        return instance;
    }

    private final Set<Stmt> coveredStmts;
    private final Set<StmtPair> coveredTransitions;
    private int timesCovered;

    private CoverageTracker() {
        // Use identity hash map to avoid potentially expensive equals() calls on
        // statements (which are unique by reference, so reference equality suffices)
        coveredStmts = Collections.newSetFromMap(new IdentityHashMap<>());
        coveredTransitions = new HashSet<>();
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
        String beforeString;
        if (before == null) {
            beforeString = "";
        } else {
            beforeString = before.toString();
        }
        String afterString;
        if (after == null) {
            afterString = "";
        } else {
            afterString = after.toString();
        }
        StmtPair pair = new StmtPair(beforeString, afterString);
        return coveredTransitions.add(pair);
    }

    /**
     * Checks whether a statement is covered.
     */
    public boolean isCovered(Stmt stmt) {
        return coveredStmts.contains(stmt);
    }

    public boolean isCovered(Stmt before, Stmt after) {
        String beforeString = before.toString();
        String afterString = after.toString();
        return coveredTransitions.contains(new StmtPair(beforeString, afterString));
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

    public static int countRealTransitions(SymbolicState init, JavaSootMethod method, StmtGraph<?> cfg, int depthLimit) {
        int transitions = 0;
        return transitions; // todo: to future person working on this: this doesn't work properly, but I didn't end up needing this. Good luck!
        /*Stack<SymbolicState> states = new Stack<>();
        states.add(init);

        while (!states.isEmpty()) {
            SymbolicState currentState = states.pop();
            if (currentState.isCtorState() && currentState.getSuccessors().isEmpty()) {
                currentState.switchToMethodState();
                currentState.setMethod(method, cfg);
            }
            for (Stmt succ : currentState.getSuccessors()) {
                transitions++;
                SymbolicState newState = currentState.clone();
                newState.incrementDepth();
                if (newState.getDepth() > depthLimit)
                    continue;
                newState.setStmt(succ);
                states.add(newState);
            }
        }

        return transitions;*/
    }

    record StmtPair(String before, String after) {
        @Override
        public boolean equals(Object o) {
            return o instanceof StmtPair(
                    String before1, String after1
            ) && before.equals(before1) && after.equals(after1);
        }

        @Override
        public int hashCode() {
            return 31 * System.identityHashCode(before) + System.identityHashCode(after);
        }
    }
}
