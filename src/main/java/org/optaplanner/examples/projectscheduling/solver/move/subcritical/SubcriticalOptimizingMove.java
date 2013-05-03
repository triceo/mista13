package org.optaplanner.examples.projectscheduling.solver.move.subcritical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.ExecutionMode;
import org.optaplanner.examples.projectscheduling.domain.Project;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;
import org.optaplanner.examples.projectscheduling.solver.move.subcritical.CriticalPathFinder.CriticalPath;

/**
 * Do not, under any circumstances, cache this move.
 */
public class SubcriticalOptimizingMove implements Move {

    private final Project project;
    private final CriticalPathFinder pathFinder;
    private final CriticalPath currentCriticalPath;
    private final Collection<Allocation> notOnCriticalPath = new LinkedHashSet<Allocation>();
    private final Map<Allocation, ExecutionMode> newExecutionModes = new LinkedHashMap<Allocation, ExecutionMode>();

    public SubcriticalOptimizingMove(final ProjectSchedule schedule, final Project p, final Random random) {
        this.project = p;
        this.pathFinder = new CriticalPathFinder(schedule);
        this.currentCriticalPath = this.pathFinder.getCriticalPath(p);
        // determine allocations that are not on the critical path
        for (final Allocation a : schedule.getAllocations()) {
            if (a.getJob().getParentProject() != p) {
                continue;
            }
            if (this.currentCriticalPath.contains(a.getExecutionMode())) {
                continue;
            }
            this.notOnCriticalPath.add(a);
        }
        // now different execution modes for those allocations
        for (final Allocation a : this.notOnCriticalPath) {
            final List<ExecutionMode> range = new ArrayList<ExecutionMode>(a.getExecutionModeRange());
            if (range.size() < 2) {
                continue;
            }
            final ExecutionMode currentMode = a.getExecutionMode();
            ExecutionMode newMode = currentMode;
            while (newMode == currentMode) {
                final int randomElementId = random.nextInt(range.size());
                newMode = range.get(randomElementId);
            }
            this.newExecutionModes.put(a, newMode);
        }
    }

    @Override
    public boolean isMoveDoable(final ScoreDirector scoreDirector) {
        final CriticalPath current = this.currentCriticalPath;
        final int oldLength = current.length();
        final int newLength = this.pathFinder.getCriticalPath(this.project, this.newExecutionModes).length();
        return oldLength >= newLength;
    }

    @Override
    public Move createUndoMove(final ScoreDirector scoreDirector) {
        final Map<Allocation, ExecutionMode> oldModes = new LinkedHashMap<Allocation, ExecutionMode>();
        for (final Map.Entry<Allocation, ExecutionMode> entry : this.newExecutionModes.entrySet()) {
            oldModes.put(entry.getKey(), entry.getKey().getExecutionMode());
        }
        return new GenericUndoMove(Collections.unmodifiableMap(oldModes));
    }

    @Override
    public void doMove(final ScoreDirector scoreDirector) {
        for (final Allocation a : this.newExecutionModes.keySet()) {
            scoreDirector.beforeVariableChanged(a, "executionMode");
            a.setExecutionMode(this.newExecutionModes.get(a));
            scoreDirector.afterVariableChanged(a, "executionMode");
        }
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return Collections.unmodifiableSet(this.newExecutionModes.keySet());
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        return Collections.unmodifiableCollection(this.newExecutionModes.values());
    }

}
