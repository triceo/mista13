package org.optaplanner.examples.mista2013.solver.score.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.optaplanner.examples.mista2013.domain.Allocation;
import org.optaplanner.examples.mista2013.domain.Job;
import org.optaplanner.examples.mista2013.domain.Mista2013;

/**
 * Validates feasibility requirement (7). How many precedence relations are
 * broken.
 */
public class PrecedenceRelationsTracker {

    private final int expectedJobCount;

    private static final int ESTIMATED_NUMBER_OF_BONDS = 4;

    private static int breakBond(final Job from, final Job to, final Map<Job, Set<Job>> relations) {
        Set<Job> relation = relations.get(from);
        if (relation == null) {
            relation = new HashSet<Job>(PrecedenceRelationsTracker.ESTIMATED_NUMBER_OF_BONDS);
            relations.put(from, relation);
        }
        relation.add(to);
        return PrecedenceRelationsTracker.getBrokenPrecedenceValidation(to);
    }

    private static int getBrokenPrecedenceValidation(final Job to) {
        return 1 + to.getRecursiveSuccessors().size();
    }

    private static boolean isPrecedenceCorrect(final Allocation pre, final Allocation current) {
        return current.getStartDate() > pre.getDueDate();
    }

    private static int mendBond(final Job from, final Job to, final Map<Job, Set<Job>> relations) {
        relations.get(from).remove(to);
        return PrecedenceRelationsTracker.getBrokenPrecedenceValidation(to);
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

    public PrecedenceRelationsTracker(final Mista2013 solution) {
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
            this.totalCachedResult += PrecedenceRelationsTracker.breakBond(from, to, this.brokenBonds);
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
            this.totalCachedResult -= PrecedenceRelationsTracker.mendBond(from, to, this.brokenBonds);
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
