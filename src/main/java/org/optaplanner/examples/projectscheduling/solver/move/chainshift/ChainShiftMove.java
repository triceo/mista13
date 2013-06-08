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

    private void addAllocation(final ProjectSchedule p, final Job j) {
        final Allocation a = p.getAllocation(j);
        this.toProcess.put(a, a.getStartDate() + this.startDateDifference);
    }

    public ChainShiftMove(final ProjectSchedule p, final Job j, final int startDateDifference) {
        this.startDateDifference = startDateDifference;
        this.job = j;
        for (final Job successor : j.getRecursiveSuccessors()) {
            if (successor.isSink()) {
                continue;
            }
            this.addAllocation(p, successor);
        }
    }

    @Override
    public String toString() {
        return "ChainShiftMove [startDateDifference=" + this.startDateDifference + ", job=" + this.job + "]";
    }

    @Override
    public boolean isMoveDoable(final ScoreDirector scoreDirector) {
        if (this.startDateDifference == 0) {
            return false;
        }
        for (final Map.Entry<Allocation, Integer> entry : this.toProcess.entrySet()) {
            final int value = entry.getValue();
            final Allocation key = entry.getKey();
            final int minimum = key.getJob().getMinimalPossibleStartDate();
            final int previousValue = key.getStartDate();
            final boolean breaksMinimum = value < minimum;
            final boolean brokeMinimumBefore = previousValue < minimum;
            final boolean willImprove = !breaksMinimum || (brokeMinimumBefore && value >= previousValue); 
            if (!willImprove) {
                return false;
            }
        }
        return true;
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
