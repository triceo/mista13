package org.drools.planner.examples.mista2013.solver.move;

import org.drools.planner.core.heuristic.selector.common.decorator.SelectionFilter;
import org.drools.planner.core.heuristic.selector.move.generic.SwapMove;
import org.drools.planner.core.score.director.ScoreDirector;
import org.drools.planner.examples.mista2013.domain.Allocation;

public class InJobSwapMoveFilter implements SelectionFilter<SwapMove> {

    @Override
    public boolean accept(ScoreDirector scoreDirector, SwapMove selection) {
        Allocation left = (Allocation)selection.getLeftEntity();
        Allocation right = (Allocation)selection.getRightEntity();
        return (left.getJob() == right.getJob());
    }

}
