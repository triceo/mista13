package org.drools.planner.examples.mista2013.solver.score.move;

import org.drools.planner.core.heuristic.selector.common.decorator.SelectionFilter;
import org.drools.planner.core.heuristic.selector.move.generic.ChangeMove;
import org.drools.planner.core.score.director.ScoreDirector;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.JobMode;

/**
 * This isn't going to be necessary once Planner fixes FROM_ENTITY on planning
 * values.
 */
public class FromEntityWorkaroundFilter implements SelectionFilter<ChangeMove> {

    @Override
    public boolean accept(final ScoreDirector scoreDirector, final ChangeMove selection) {
        final Allocation entity = (Allocation) selection.getEntity();
        final Object newValue = selection.getToPlanningValue();
        if (newValue instanceof Integer) {
            return entity.getStartDates().contains(newValue);
        } else if (newValue instanceof JobMode) {
            return entity.getJobModes().contains(newValue);
        }
        throw new IllegalArgumentException("Invalid value type!");
    }

}
