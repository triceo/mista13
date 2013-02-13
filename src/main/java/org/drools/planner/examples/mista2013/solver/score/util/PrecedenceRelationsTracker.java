package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.Project;

/**
 * Validates feasibility requirement (7). How many precedence relations are
 * broken.
 */
public class PrecedenceRelationsTracker {

    private class PerProjectTracker {

        private final Map<Job, Allocation> allocations;
        private final Map<Job, Integer> cache;
        private int totalCachedResult = 0;

        public PerProjectTracker(final int numJobs) {
            this.allocations = new HashMap<Job, Allocation>(numJobs);
            this.cache = new HashMap<Job, Integer>(numJobs);
        }

        public void add(final Allocation a) {
            final Job current = a.getJob();
            this.allocations.put(current, a);
            // find all predecessors
            for (final Job predecessor : current.getPredecessors()) {
                this.recache(predecessor);
            }
            // invalidate cache for all successors
            for (final Job j : current.getSuccessors()) {
                this.recache(j);
            }
            this.recache(current);
        }

        private int countBrokenPrecedenceRelations(final Job currentJob) {
            final Allocation currentAllocation = this.allocations.get(currentJob);
            final Project p = currentJob.getParentProject();
            if (currentAllocation.getStartDate() < p.getReleaseDate()) {
                // make sure we never start before we're allowed to
                return 1 + currentJob.getRecursiveSuccessors().size();
            }
            int total = 0;
            final int currentDoneBy = currentAllocation.getDueDate();
            for (final Job succeedingJob : currentJob.getSuccessors()) {
                if (succeedingJob.isSink()) {
                    continue;
                }
                final Allocation succeedingAllocation = this.allocations.get(succeedingJob);
                if (succeedingAllocation == null) {
                    continue;
                }
                final Integer nextStartedAt = succeedingAllocation.getStartDate();
                if (nextStartedAt <= currentDoneBy) {
                    /*
                     * successor starts before its predecessor ends
                     */
                    total += Math.max(1, succeedingJob.getRecursiveSuccessors().size());
                }
            }
            // if the successors are broken, then also count this one as broken
            if (total > 0) {
                total++;
            }
            return total;
        }

        public int getBrokenPrecedenceRelationsCount() {
            return this.totalCachedResult;
        }

        private void recache(final Job j) {
            final Integer cache = this.cache.remove(j);
            if (cache != null) {
                this.totalCachedResult -= cache;
            }
            if (this.allocations.containsKey(j)) {
                final int broken = this.countBrokenPrecedenceRelations(j);
                this.cache.put(j, broken);
                this.totalCachedResult += broken;
            }
        }

        public void remove(final Allocation a) {
            final Job current = a.getJob();
            this.allocations.remove(current);
            for (final Job j : current.getPredecessors()) {
                this.recache(j);
            }
            for (final Job j : current.getSuccessors()) {
                this.recache(j);
            }
            this.recache(current);
        }
    }

    private final Map<Project, PerProjectTracker> trackers;

    private int totalCachedResult = 0;

    public PrecedenceRelationsTracker(final Collection<Project> projects) {
        this.trackers = new HashMap<Project, PerProjectTracker>(projects.size());
    }

    public void add(final Allocation a) {
        final Project p = a.getJob().getParentProject();
        PerProjectTracker inProject = this.trackers.get(p);
        if (inProject == null) {
            inProject = new PerProjectTracker(p.getJobs().size());
            this.trackers.put(p, inProject);
        }
        this.totalCachedResult -= inProject.getBrokenPrecedenceRelationsCount();
        inProject.add(a);
        this.totalCachedResult += inProject.getBrokenPrecedenceRelationsCount();
    }

    public int getBrokenPrecedenceRelationsCount() {
        return this.totalCachedResult;
    }

    public void remove(final Allocation a) {
        final Project p = a.getJob().getParentProject();
        final PerProjectTracker inProject = this.trackers.get(p);
        this.totalCachedResult -= inProject.getBrokenPrecedenceRelationsCount();
        inProject.remove(a);
        this.totalCachedResult += inProject.getBrokenPrecedenceRelationsCount();
    }
}
