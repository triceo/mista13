package org.optaplanner.examples.projectscheduling.solver.score.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

/**
 * Validates feasibility requirement (7). How many precedence relations are
 * broken.
 */
public class PrecedenceRelationsTracker {

    private final int expectedJobCount;

    private static final int ESTIMATED_NUMBER_OF_BONDS = 4;

    private static int getBrokenPrecedenceValidation(final Job to) {
        return 1 + to.getRecursiveSuccessors().size();
    }

    private static boolean isPrecedenceCorrect(final Allocation pre, final Allocation current) {
        return current.getStartDate() > pre.getDueDate();
    }

    private int totalCachedResult = 0;

    /**
     * Allocations that have been {@link #add(Allocation)}ed and not yet
     * {@link #remove(Allocation)}d.
     */
    private final Map<Job, Allocation> allocations;

    /**
     * Bonds that have been through {@link #breakBond(Job, Job, Map)} and not
     * yet through {@link #mendBond(Job, Job, Map)}. This map always has a
     * predecessor as a key and a direct successor as a value.
     */
    private final Map<Job, Set<Job>> brokenBonds;

    public PrecedenceRelationsTracker(final ProjectSchedule solution) {
        this.expectedJobCount = solution.getAllocations().size();
        this.allocations = new HashMap<Job, Allocation>(this.expectedJobCount);
        this.brokenBonds = new HashMap<Job, Set<Job>>(this.expectedJobCount);
    }

    public void add(final Allocation a) {
        final Job current = a.getJob();
        this.allocations.put(current, a);
        for (final Job j : current.getPredecessors()) {
            this.bind(j, a);
        }
        for (final Job j : current.getSuccessors()) {
            this.bind(a, j);
        }
        if (a.getStartDate() < current.getParentProject().getReleaseDate()) {
            this.totalCachedResult += PrecedenceRelationsTracker.getBrokenPrecedenceValidation(current);
        }
    }

    private void bind(final Allocation fromAlloc, final Allocation toAlloc) {
        final Job from = fromAlloc.getJob();
        final Job to = toAlloc.getJob();
        if (from.isSource() || to.isSink()) {
            return;
        }
        if (this.isBondBroken(from, to)) {
            return;
        }
        if (!PrecedenceRelationsTracker.isPrecedenceCorrect(fromAlloc, toAlloc)) {
            this.totalCachedResult += this.breakBond(from, to);
        }
    }

    private void bind(final Allocation from, final Job to) {
        final Allocation a = this.allocations.get(to);
        if (a == null) {
            return;
        }
        this.bind(from, a);
    }

    private void bind(final Job from, final Allocation to) {
        final Allocation a = this.allocations.get(from);
        if (a == null) {
            return;
        }
        this.bind(a, to);
    }

    private int breakBond(final Job from, final Job to) {
        Set<Job> relation = this.brokenBonds.get(from);
        if (relation == null) {
            relation = new HashSet<Job>(PrecedenceRelationsTracker.ESTIMATED_NUMBER_OF_BONDS);
            this.brokenBonds.put(from, relation);
        }
        relation.add(to);
        return PrecedenceRelationsTracker.getBrokenPrecedenceValidation(to);
    }

    public int getBrokenPrecedenceRelationsMeasure() {
        return this.totalCachedResult;
    }

    private boolean isBondBroken(final Job from, final Job to) {
        final Set<Job> related = this.brokenBonds.get(from);
        if (related == null) {
            return false;
        }
        return related.contains(to);
    }

    private int mendBond(final Job from, final Job to) {
        this.brokenBonds.get(from).remove(to);
        return PrecedenceRelationsTracker.getBrokenPrecedenceValidation(to);
    }

    public void remove(final Allocation a) {
        final Job current = a.getJob();
        for (final Job j : current.getPredecessors()) {
            this.unbind(j, a);
        }
        for (final Job j : current.getSuccessors()) {
            this.unbind(a, j);
        }
        this.allocations.remove(current);
        if (a.getStartDate() < current.getParentProject().getReleaseDate()) {
            this.totalCachedResult -= PrecedenceRelationsTracker.getBrokenPrecedenceValidation(current);
        }
    }

    private void unbind(final Allocation fromAlloc, final Allocation toAlloc) {
        final Job from = fromAlloc.getJob();
        final Job to = toAlloc.getJob();
        if (from.isSource() || to.isSink()) {
            return;
        }
        if (this.isBondBroken(from, to)) {
            this.totalCachedResult -= this.mendBond(from, to);
        }
    }

    private void unbind(final Allocation from, final Job to) {
        final Allocation a = this.allocations.get(to);
        if (a == null) {
            return;
        }
        this.unbind(from, a);
    }

    private void unbind(final Job from, final Allocation to) {
        final Allocation a = this.allocations.get(from);
        if (a == null) {
            return;
        }
        this.unbind(a, to);
    }
}
