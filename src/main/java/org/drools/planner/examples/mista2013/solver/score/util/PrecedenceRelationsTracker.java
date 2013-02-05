package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.Collection;
import java.util.HashSet;
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

        private final Map<Job, Allocation> allocations;
        private final Map<Job, Set<Job>> predecessors;
        private final Map<Job, Integer> cache;
        private final Set<Job> dirtyJobs;
        private int totalCachedResult = 0;

        public PerProjectTracker(final int numJobs) {
            this.allocations = new LinkedHashMap<Job, Allocation>(numJobs);
            this.predecessors = new LinkedHashMap<Job, Set<Job>>(numJobs);
            this.cache = new LinkedHashMap<Job, Integer>(numJobs);
            this.dirtyJobs = new HashSet<Job>(numJobs);
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
            for (final Job j : this.dirtyJobs) {
                final Allocation a = this.allocations.get(j);
                if (a == null) {
                    continue;
                }
                final int result = this.countBrokenPrecedenceRelations(a);
                this.cache.put(j, result);
                this.totalCachedResult += result;
            }
            this.dirtyJobs.clear();
            return this.totalCachedResult;
        }

        private int countBrokenPrecedenceRelations(final Allocation currentAllocation) {
            int total = 0;
            final Job currentJob = currentAllocation.getJob();
            final Project p = currentJob.getParentProject();
            int recursiveSuccessors = currentJob.getRecursiveSuccessors().size();
            if (currentAllocation.getStartDate() < p.getReleaseDate()) {
                // make sure we never start before we're allowed to
                total += recursiveSuccessors;
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
                    total += recursiveSuccessors;
                }
            }
            return total;
        }

        private void invalidateCache(final Job j) {
            final Integer cache = this.cache.remove(j);
            if (cache != null) {
                this.totalCachedResult -= cache;
            }
            this.dirtyJobs.add(j);
        }

        public void remove(final Allocation a) {
            final Job current = a.getJob();
            this.allocations.remove(current);
            for (final Job j : this.predecessors.remove(current)) {
                this.invalidateCache(j);
            }
            for (final Job j : current.getSuccessors()) {
                final Collection<Job> predecessors = this.predecessors.get(j);
                if (predecessors != null && predecessors.size() > 0) {
                    predecessors.remove(current);
                    this.invalidateCache(j);
                }
            }
            this.invalidateCache(current);
        }
    }

    private final Map<Project, PerProjectTracker> trackers;

    private final Map<Project, Integer> cache;

    private final Set<Project> dirtyProjects;

    private int totalCachedResult = 0;

    public PrecedenceRelationsTracker(final Collection<Project> projects) {
        this.trackers = new LinkedHashMap<Project, PerProjectTracker>(projects.size());
        this.cache = new LinkedHashMap<Project, Integer>(projects.size());
        this.dirtyProjects = new HashSet<Project>(projects.size());
    }

    public void add(final Allocation a) {
        final Project p = a.getJob().getParentProject();
        PerProjectTracker prtpp = this.trackers.get(p);
        if (prtpp == null) {
            prtpp = new PerProjectTracker(p.getJobs().size());
            this.trackers.put(p, prtpp);
        }
        prtpp.add(a);
        this.invalidateCache(p);
    }

    public int countBrokenPrecedenceRelations() {
        for (final Project p : this.dirtyProjects) {
            final int cache = this.trackers.get(p).countBrokenPrecedenceRelations();
            this.cache.put(p, cache);
            this.totalCachedResult += cache;
        }
        this.dirtyProjects.clear();
        return this.totalCachedResult;
    }

    private void invalidateCache(final Project p) {
        final Integer cache = this.cache.remove(p);
        if (cache != null) {
            this.totalCachedResult -= cache;
        }
        this.dirtyProjects.add(p);
    }

    public void remove(final Allocation a) {
        final Project p = a.getJob().getParentProject();
        final PerProjectTracker prtpp = this.trackers.get(p);
        prtpp.remove(a);
        this.invalidateCache(p);
    }
}
