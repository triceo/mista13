package org.optaplanner.examples.projectscheduling.solver.move;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.ExecutionMode;


public class ProlongingExecutionModeFilter implements SelectionFilter<ChangeMove> {

    @Override
    public boolean accept(ScoreDirector scoreDirector, ChangeMove selection) {
        ExecutionMode newValue = (ExecutionMode) selection.getToPlanningValue();
        ExecutionMode currentValue = ((Allocation)selection.getEntity()).getExecutionMode();
        if (newValue == currentValue) {
            return false;
        } else if (newValue.getDuration() < currentValue.getDuration()) {
            return false;
        } else {
            return true;
        }
    }

}
