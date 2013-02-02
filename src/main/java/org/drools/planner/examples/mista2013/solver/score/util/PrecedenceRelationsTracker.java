package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.Project;

/**
 * Validates feasibility requirement (7). How many precedence relations are
 * broken.
 */
public class PrecedenceRelationsTracker {

    private class PerProjectTracker {

        Map<Job, Allocation> allocations = new LinkedHashMap<Job, Allocation>();
        Map<Job, Set<Job>> predecessors = new LinkedHashMap<Job, Set<Job>>();
        Map<Job, Integer> cache = new LinkedHashMap<Job, Integer>();

        public PerProjectTracker() {
        }

        public void add(final Allocation a) {
            final Job current = a.getJob();
            this.allocations.put(current, a);
            // retrieve existing predecessors
            if (!this.predecessors.containsKey(current)) {
                this.predecessors.put(current, new LinkedHashSet<Job>());
            }
            final Set<Job> predecessors = this.predecessors.get(current);
            // find all predecessors
            for (final Job j : this.allocations.keySet()) {
                if (j.getSuccessors().contains(current)) {
                    predecessors.add(j);
                    this.invalidateCache(j);
                }
            }
            // invalidate cache for all successors
            for (final Job j : current.getSuccessors()) {
                this.invalidateCache(j);
            }
            this.invalidateCache(current);
        }

        public int countBrokenPrecedenceRelations() {
            int total = 0;
            for (final Allocation a : this.allocations.values()) {
                final Job j = a.getJob();
                if (!this.cache.containsKey(j)) {
                    this.cache.put(j, this.countBrokenPrecedenceRelations(a));
                } else {
                }
                total += this.cache.get(j);
            }
            return total;
        }

        private int countBrokenPrecedenceRelations(final Allocation currentAllocation) {
            int total = 0;
            final Job currentJob = currentAllocation.getJob();
            final Project p = currentJob.getParentProject();
            if (currentAllocation.getStartDate() < p.getReleaseDate()) {
                // make sure we never start before we're allowed to
                total++;
            }
            final int currentDoneBy = currentAllocation.getDueDate();
            for (final Job succeedingJob : currentJob.getSuccessors()) {
                if (succeedingJob.isSink()) {
                    continue;
                }
                // find its successors
                final Allocation succeedingAllocation = this.allocations.get(succeedingJob);
                if (succeedingAllocation == null) {
                    continue;
                }
                final Integer nextStartedAt = succeedingAllocation.getStartDate();
                if (nextStartedAt <= currentDoneBy) {
                    /*
                     * successor starts before its predecessor ends
                     */
                    total++;
                }
            }
            return total;
        }

        private void invalidateCache(final Job j) {
            this.cache.remove(j);
        }

        public void remove(final Allocation a) {
            final Job current = a.getJob();
            this.allocations.remove(current);
            for (final Job j : this.predecessors.remove(current)) {
                this.invalidateCache(j);
            }
            for (final Job j : this.allocations.keySet()) {
                final boolean contained = this.predecessors.get(j).remove(current);
                if (contained) {
                    this.invalidateCache(j);
                }
            }
            for (final Job j : current.getSuccessors()) {
                this.invalidateCache(j);
            }
            this.invalidateCache(current);
        }
    }

    private final Map<Project, PerProjectTracker> trackers = new LinkedHashMap<Project, PerProjectTracker>();

    private final Map<Project, Integer> cache = new LinkedHashMap<Project, Integer>();

    public PrecedenceRelationsTracker() {

    }

    public void add(final Allocation a) {
        final Project p = a.getJob().getParentProject();
        PerProjectTracker prtpp = this.trackers.get(p);
        if (prtpp == null) {
            prtpp = new PerProjectTracker();
            this.trackers.put(p, prtpp);
        }
        prtpp.add(a);
        this.cache.remove(p);
    }

    public int countBrokenPrecedenceRelations() {
        int total = 0;
        for (final Map.Entry<Project, PerProjectTracker> entry : this.trackers.entrySet()) {
            final Project p = entry.getKey();
            Integer cache = this.cache.get(p);
            if (cache == null) {
                cache = entry.getValue().countBrokenPrecedenceRelations();
                this.cache.put(p, cache);
            }
            total += cache;
        }
        return total;
    }

    public void remove(final Allocation a) {
        final Project p = a.getJob().getParentProject();
        final PerProjectTracker prtpp = this.trackers.get(p);
        prtpp.remove(a);
        this.cache.remove(p);
    }
}
