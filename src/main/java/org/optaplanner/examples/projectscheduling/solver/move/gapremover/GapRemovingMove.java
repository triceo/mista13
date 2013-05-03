package org.optaplanner.examples.projectscheduling.solver.move.gapremover;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.math.IntRange;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;
import org.optaplanner.examples.projectscheduling.solver.move.StartDateUndoMove;

public class GapRemovingMove implements Move {

    private final IntRange gap;
    private final Map<Allocation, Integer> newDates = new LinkedHashMap<Allocation, Integer>();

    public void processFollowups(final ProjectSchedule schedule, final Allocation a, final int difference) {
        final Job j = a.getJob();
        for (final Job successor : j.getRecursiveSuccessors()) {
            if (successor.isSink()) {
                continue;
            }
            final Allocation successorAllocation = schedule.getAllocation(successor);
            if (!this.newDates.containsKey(successorAllocation)) {
                this.newDates.put(successorAllocation, a.getStartDate() - difference);
            }
        }
    }

    @Override
    public String toString() {
        return "GapRemovingMove [gap=" + this.gap + "]";
    }

    public GapRemovingMove(final ProjectSchedule schedule, final IntRange gap) {
        this.gap = gap;
        final int gapSize = gap.getMaximumInteger() - gap.getMinimumInteger();
        if (gapSize == 0) {
            return;
        }
        for (final Allocation a : schedule.getAllocations()) {
            if (a.getStartDate() == gap.getMaximumInteger() + 1) {
                this.newDates.put(a, gap.getMinimumInteger());
                this.processFollowups(schedule, a, gapSize);
            }
        }
    }

    @Override
    public boolean isMoveDoable(final ScoreDirector scoreDirector) {
        return this.gap.getMaximumInteger() > 0;
    }

    @Override
    public Move createUndoMove(final ScoreDirector scoreDirector) {
        final Map<Allocation, Integer> oldDates = new LinkedHashMap<Allocation, Integer>();
        for (final Map.Entry<Allocation, Integer> entry : this.newDates.entrySet()) {
            oldDates.put(entry.getKey(), entry.getKey().getStartDate());
        }
        return new StartDateUndoMove(Collections.unmodifiableMap(oldDates));
    }

    @Override
    public void doMove(final ScoreDirector scoreDirector) {
        for (final Allocation a : this.newDates.keySet()) {
            scoreDirector.beforeVariableChanged(a, "startDate");
            a.setStartDate(this.newDates.get(a));
            scoreDirector.afterVariableChanged(a, "startDate");
        }
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return Collections.unmodifiableSet(this.newDates.keySet());
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        return Collections.unmodifiableCollection(this.newDates.values());
    }

}
