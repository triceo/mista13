package org.drools.planner.examples.mista2013.solver.move;

import org.drools.planner.core.heuristic.selector.common.decorator.SelectionFilter;
import org.drools.planner.core.heuristic.selector.move.generic.ChangeMove;
import org.drools.planner.core.score.director.ScoreDirector;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.JobMode;

public class ValueSelectorWorkaroundFilter implements SelectionFilter<ChangeMove> {

    @Override
    public boolean accept(final ScoreDirector scoreDirector, final ChangeMove selection) {
        final Allocation a = (Allocation) selection.getEntity();
        final Object newValue = selection.getToPlanningValue();
        if (newValue instanceof JobMode) {
            return a.getJobModes().contains(newValue);
        } else if (newValue instanceof Integer) {
            return a.getStartDates().contains(newValue);
        } else {
            throw new IllegalStateException("Invalid planning variable!");
        }
    }

}
