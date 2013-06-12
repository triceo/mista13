package org.optaplanner.examples.projectscheduling.solver.move.chainshift;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;
import org.optaplanner.examples.projectscheduling.solver.move.StartDateUndoMove;

/**
 * FIXME this is a concept. it is absolutely horrific performance-wise; reimplement when proven worthy.
 * 
 */
public class ChainShiftMove implements Move {

    private final int startDateDifference;
    private final Job job;
    private final Map<Allocation, Integer> toProcess = new LinkedHashMap<Allocation, Integer>();
    
    private boolean isStartDateAccepted(final Allocation a, final int newStartDate) {
        return newStartDate >= 0; 
    }

    public ChainShiftMove(final ProjectSchedule p, final Job j, final int startDateDifference) {
        this.startDateDifference = startDateDifference;
        this.job = j;
        if (startDateDifference == 0) {
            return;
        }
        for (final Job successor : j.getRecursiveSuccessors()) {
            if (successor.isSink()) {
                continue;
            }
            final Allocation a = p.getAllocation(successor);
            final int newStartDate = a.getStartDate() + this.startDateDifference; 
            if (this.isStartDateAccepted(a, newStartDate)) {
                this.toProcess.put(a, newStartDate);
            }
        }
    }

    @Override
    public String toString() {
        return "ChainShiftMove [startDateDifference=" + this.startDateDifference + ", job=" + this.job + "]";
    }

    @Override
    public boolean isMoveDoable(final ScoreDirector scoreDirector) {
        return this.toProcess.size() > 0;
    }

    @Override
    public Move createUndoMove(final ScoreDirector scoreDirector) {
        final Map<Allocation, Integer> oldDates = new LinkedHashMap<Allocation, Integer>();
        for (final Map.Entry<Allocation, Integer> entry : this.toProcess.entrySet()) {
            oldDates.put(entry.getKey(), entry.getKey().getStartDate());
        }
        return new StartDateUndoMove(Collections.unmodifiableMap(oldDates));
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
