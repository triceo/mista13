package org.optaplanner.examples.projectscheduling.solver.move;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.commons.lang.NotImplementedException;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

public class SubprojectShiftUndoMove implements Move {

    private final int startDateDifference;
    private final Collection<Allocation> toProcess = new LinkedHashSet<Allocation>();

    public SubprojectShiftUndoMove(final ProjectSchedule p, final Job j, final int startDateDifference) {
        this.startDateDifference = startDateDifference;
        this.toProcess.add(p.getAllocation(j));
        for (final Job successor : j.getRecursiveSuccessors()) {
            if (successor.isSink() || successor.isSource()) {
                continue;
            }
            this.toProcess.add(p.getAllocation(successor));
        }
    }

    @Override
    public boolean isMoveDoable(final ScoreDirector scoreDirector) {
        throw new NotImplementedException();
    }

    @Override
    public Move createUndoMove(final ScoreDirector scoreDirector) {
        throw new NotImplementedException();
    }

    @Override
    public void doMove(final ScoreDirector scoreDirector) {
        for (final Allocation a : this.toProcess) {
            scoreDirector.beforeVariableChanged(a, "startDate");
            a.setStartDate(a.getStartDate() - this.startDateDifference);
            scoreDirector.afterVariableChanged(a, "startDate");
        }
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        throw new NotImplementedException();
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        throw new NotImplementedException();
    }

}
