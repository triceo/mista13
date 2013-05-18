package org.optaplanner.examples.projectscheduling.solver.move.realign;

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
 * Winning solutions show that best scores have most of the jobs succeeding a particular 
 * job starting at the same time, and that time is immediately after due time of the original 
 * job. (= There is no gap.) This move will attempt to do exactly that.
 */
public class RealignMove implements Move {

    private final Job job;
    private final Map<Allocation, Integer> toProcess = new LinkedHashMap<Allocation, Integer>();

    public RealignMove(final ProjectSchedule p, final Job realignAfter) {
        this.job = realignAfter;
        int realignmentDate = realignAfter.isSource() ? realignAfter.getParentProject().getReleaseDate() : p.getAllocation(realignAfter).getDueDate() + 1; 
        for (final Job successor : realignAfter.getSuccessors()) {
            if (successor.isSink()) {
                continue;
            }
            this.toProcess.put(p.getAllocation(successor), realignmentDate);
        }
    }

    @Override
    public String toString() {
        return "RealignMove [job=" + job + "]";
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
