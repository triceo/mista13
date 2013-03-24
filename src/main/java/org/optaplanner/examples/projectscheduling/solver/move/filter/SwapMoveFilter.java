package org.optaplanner.examples.projectscheduling.solver.move.filter;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Project;

/**
 * Make sure that only values for {@link Allocation}s in the same {@link Project} are
 * swapped.
 */
public class SwapMoveFilter implements SelectionFilter<SwapMove> {

    @Override
    public boolean accept(final ScoreDirector scoreDirector, final SwapMove selection) {
        final Allocation left = (Allocation) selection.getLeftEntity();
        final Allocation right = (Allocation) selection.getRightEntity();
        return left.getJob().getParentProject() == right.getJob().getParentProject();
    }

}
