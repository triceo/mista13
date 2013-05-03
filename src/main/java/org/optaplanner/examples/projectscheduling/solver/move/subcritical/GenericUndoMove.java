package org.optaplanner.examples.projectscheduling.solver.move.subcritical;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.ExecutionMode;

public class GenericUndoMove implements Move {

    private final Map<Allocation, ExecutionMode> newModes;

    public GenericUndoMove(final Map<Allocation, ExecutionMode> newModes) {
        this.newModes = newModes;
    }

    @Override
    public boolean isMoveDoable(final ScoreDirector scoreDirector) {
        return true;
    }

    @Override
    public Move createUndoMove(final ScoreDirector scoreDirector) {
        throw new NotImplementedException();
    }

    @Override
    public void doMove(final ScoreDirector scoreDirector) {
        for (final Allocation a : this.newModes.keySet()) {
            scoreDirector.beforeVariableChanged(a, "executionMode");
            a.setExecutionMode(this.newModes.get(a));
            scoreDirector.afterVariableChanged(a, "executionMode");
        }
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return Collections.unmodifiableSet(this.newModes.keySet());
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        return Collections.unmodifiableCollection(this.newModes.values());
    }

}
