package org.optaplanner.examples.projectscheduling.solver.move.subcritical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.ExecutionMode;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.Project;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

public class CriticalPathFinder {

    public static class CriticalPath implements Comparable<CriticalPath> {

        private final List<ExecutionMode> criticalPath;
        private int length = 0;

        private CriticalPath(final List<ExecutionMode> criticalPath) {
            this.criticalPath = Collections.unmodifiableList(criticalPath);
            for (final ExecutionMode e : criticalPath) {
                this.length += e.getDuration();
            }
        }

        private CriticalPath() {
            this(new ArrayList<ExecutionMode>());
        }

        public CriticalPath add(final ExecutionMode e) {
            final List<ExecutionMode> newPath = new ArrayList<ExecutionMode>(this.criticalPath);
            newPath.add(0, e);
            return new CriticalPath(newPath);
        }

        public int length() {
            return this.length;
        }

        public CriticalPath replace(final ExecutionMode e1, final ExecutionMode e2) {
            final List<ExecutionMode> newPath = new ArrayList<ExecutionMode>(this.criticalPath);
            final int index = newPath.indexOf(e1);
            if (index < 0) {
                throw new IllegalArgumentException("Not on critical path: " + e1);
            }
            newPath.remove(index);
            newPath.add(index, e2);
            return new CriticalPath(newPath);
        }

        public boolean contains(final ExecutionMode e) {
            return this.criticalPath.contains(e);
        }

        @Override
        public int compareTo(final CriticalPath arg0) {
            if (this.length > arg0.length) {
                return 1;
            } else if (this.length == arg0.length) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    private final ProjectSchedule schedule;

    public CriticalPathFinder(final ProjectSchedule schedule) {
        this.schedule = schedule;
    }

    private CriticalPath findFromJob(final Project p, final Job j, final Map<Allocation, ExecutionMode> substitutions) {
        if (j.isSink()) {
            return new CriticalPath();
        }
        final SortedSet<CriticalPath> paths = new TreeSet<CriticalPath>();
        for (final Job successor : j.getSuccessors()) {
            paths.add(this.findFromJob(p, successor, substitutions));
        }
        if (j.isSource()) {
            return paths.last();
        } else {
            final Allocation a = this.schedule.getAllocation(j);
            final ExecutionMode mode = substitutions.containsKey(a) ? substitutions.get(a) : a.getExecutionMode();
            return paths.last().add(mode);
        }

    }

    public CriticalPath getCriticalPath(final Project p) {
        if (!this.schedule.getProblem().getProjects().contains(p)) {
            throw new IllegalArgumentException("Non-existent project: " + p);
        }
        return this.findFromJob(p, p.getJobs().get(0), new LinkedHashMap<Allocation, ExecutionMode>());
    }

    public CriticalPath getCriticalPath(final Project p, final Map<Allocation, ExecutionMode> substitutions) {
        if (!this.schedule.getProblem().getProjects().contains(p)) {
            throw new IllegalArgumentException("Non-existent project: " + p);
        }
        return this.findFromJob(p, p.getJobs().get(0), substitutions);
    }

}
