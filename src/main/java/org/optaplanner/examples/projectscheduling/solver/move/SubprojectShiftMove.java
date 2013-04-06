package org.optaplanner.examples.projectscheduling.solver.move;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

/**
 * FIXME this is a concept. it is absolutely horrific performance-wise; reimplement when proven worthy.
 * 
 */
public class SubprojectShiftMove implements Move {

    private final ProjectSchedule project;
    private final Job startWith;
    private final int startDateDifference;
    private final Map<Allocation, Integer> toProcess = new LinkedHashMap<Allocation, Integer>();

    private void addAllocation(final Job j) {
        final Allocation a = this.project.getAllocation(j);
        this.toProcess.put(a, a.getStartDate() + this.startDateDifference);
    }

    public SubprojectShiftMove(final ProjectSchedule p, final Job j, final int startDateDifference) {
        this.project = p;
        this.startWith = j;
        this.startDateDifference = startDateDifference;
        this.addAllocation(j);
        for (final Job successor : j.getRecursiveSuccessors()) {
            if (successor.isSink()) {
                continue;
            }
            this.addAllocation(successor);
        }
    }

    @Override
    public boolean isMoveDoable(final ScoreDirector scoreDirector) {
        if (this.startDateDifference == 0) {
            return false;
        }
        for (final Map.Entry<Allocation, Integer> entry : this.toProcess.entrySet()) {
            if (!entry.getKey().getStartDateRange().contains(entry.getValue())) {
                /*
                 * make sure that if we shift start date at the beginning, the start date at the end isn't going to go
                 * out of range.
                 */
                return false;
            }
        }
        return true;
    }

    @Override
    public Move createUndoMove(final ScoreDirector scoreDirector) {
        return new SubprojectShiftUndoMove(this.project, this.startWith, this.startDateDifference);
    }

    @Override
    public void doMove(final ScoreDirector scoreDirector) {
        for (final Map.Entry<Allocation, Integer> entry : this.toProcess.entrySet()) {
            final Allocation a = entry.getKey();
            scoreDirector.beforeVariableChanged(a, "startDate");
            a.setStartDate(entry.getValue());
            scoreDirector.afterVariableChanged(a, "startDate");
        }
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return Collections.unmodifiableCollection(this.toProcess.keySet());
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        return Collections.unmodifiableCollection(this.toProcess.values());
    }

}
