package org.optaplanner.examples.projectscheduling.solver.move;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;

public class StartDateUndoMove implements Move {

    private final Map<Allocation, Integer> newDates;

    public StartDateUndoMove(final Map<Allocation, Integer> newDates) {
        this.newDates = newDates;
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
        for (final Allocation a : this.newDates.keySet()) {
            scoreDirector.beforeVariableChanged(a, "startDate");
            a.setStartDate(this.newDates.get(a));
            scoreDirector.afterVariableChanged(a, "startDate");
        }
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return this.newDates.keySet();
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        return this.newDates.values();
    }

}
