package org.optaplanner.examples.projectscheduling.solver.move.filter;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;

/**
 * Make sure that only mutually acceptable values are swapped.
 */
public class SwapMoveFilter implements SelectionFilter<SwapMove> {

    @Override
    public boolean accept(final ScoreDirector scoreDirector, final SwapMove selection) {
        final Allocation left = (Allocation) selection.getLeftEntity();
        final Allocation right = (Allocation) selection.getRightEntity();
        if (!left.getStartDateRange().contains(right.getStartDate())){
            return false;
        } else if (!right.getStartDateRange().contains(left.getStartDate())) {
            return false;
        } else {
            return true;
        }
    }

}
