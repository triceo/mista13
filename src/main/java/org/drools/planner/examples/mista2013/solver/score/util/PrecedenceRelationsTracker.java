package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.Mista2013;

/**
 * Validates feasibility requirement (7). How many precedence relations are
 * broken.
 */
public class PrecedenceRelationsTracker {

    private final int expectedJobCount;

    private static final int ESTIMATED_NUMBER_OF_RELATIONS = 4;

    private static int getBrokenRelationValidation(final Job to) {
        return 1 + to.getRecursiveSuccessors().size();
    }

    private static boolean isPrecedenceCorrect(final Allocation pre, final Allocation current) {
        return current.getStartDate() > pre.getDueDate();
    }

    private static int relate(final Job from, final Job to, final Map<Job, Set<Job>> relations) {
        Set<Job> relation = relations.get(from);
        if (relation == null) {
            relation = new HashSet<Job>(PrecedenceRelationsTracker.ESTIMATED_NUMBER_OF_RELATIONS);
            relations.put(from, relation);
        }
        relation.add(to);
        return PrecedenceRelationsTracker.getBrokenRelationValidation(to);
    }

    private static int unrelate(final Job from, final Job to, final Map<Job, Set<Job>> relations) {
        relations.get(from).remove(to);
        return PrecedenceRelationsTracker.getBrokenRelationValidation(to);
    }

    private int totalCachedResult = 0;

    /**
     * Allocations that have been {@link #add(Allocation)}ed and not yet
     * {@link #remove(Allocation)}d.
     */
    private final Map<Job, Allocation> allocations;

    /**
     * Relations that have been through {@link #relate(Job, Job, Map)} and not
     * yet through {@link #unrelate(Job, Job, Map)}.
     */
    private final Map<Job, Set<Job>> relations;

    public PrecedenceRelationsTracker(final Mista2013 solution) {
        this.expectedJobCount = solution.getAllocations().size();
        this.allocations = new HashMap<Job, Allocation>(this.expectedJobCount);
        this.relations = new HashMap<Job, Set<Job>>(this.expectedJobCount);
    }

    public void add(final Allocation a) {
        final Job current = a.getJob();
        this.allocations.put(current, a);
        for (final Job j : current.getPredecessors()) {
            this.formRelation(j, a);
        }
        for (final Job j : current.getSuccessors()) {
            this.formRelation(a, j);
        }
        if (a.getStartDate() < current.getParentProject().getReleaseDate()) {
            this.totalCachedResult += PrecedenceRelationsTracker.getBrokenRelationValidation(current);
        }
    }

    private void breakRelation(final Allocation fromAlloc, final Allocation toAlloc) {
        final Job from = fromAlloc.getJob();
        final Job to = toAlloc.getJob();
        if (from.isSource() || to.isSink()) {
            return;
        }
        if (this.hasRelation(from, to)) {
            this.totalCachedResult -= PrecedenceRelationsTracker.unrelate(from, to, this.relations);
        }
    }

    private void breakRelation(final Allocation from, final Job to) {
        final Allocation a = this.allocations.get(to);
        if (a == null) {
            return;
        }
        this.breakRelation(from, a);
    }

    private void breakRelation(final Job from, final Allocation to) {
        final Allocation a = this.allocations.get(from);
        if (a == null) {
            return;
        }
        this.breakRelation(a, to);
    }

    private void formRelation(final Allocation fromAlloc, final Allocation toAlloc) {
        final Job from = fromAlloc.getJob();
        final Job to = toAlloc.getJob();
        if (from.isSource() || to.isSink()) {
            return;
        }
        if (this.hasRelation(from, to)) {
            return;
        }
        if (!PrecedenceRelationsTracker.isPrecedenceCorrect(fromAlloc, toAlloc)) {
            this.totalCachedResult += PrecedenceRelationsTracker.relate(from, to, this.relations);
        }
    }

    private void formRelation(final Allocation from, final Job to) {
        final Allocation a = this.allocations.get(to);
        if (a == null) {
            return;
        }
        this.formRelation(from, a);
    }

    private void formRelation(final Job from, final Allocation to) {
        final Allocation a = this.allocations.get(from);
        if (a == null) {
            return;
        }
        this.formRelation(a, to);
    }

    public int getBrokenPrecedenceRelationsMeasure() {
        return this.totalCachedResult;
    }

    private boolean hasRelation(final Job from, final Job to) {
        final Set<Job> related = this.relations.get(from);
        if (related == null) {
            return false;
        }
        return related.contains(to);
    }

    public void remove(final Allocation a) {
        final Job current = a.getJob();
        for (final Job j : current.getPredecessors()) {
            this.breakRelation(j, a);
        }
        for (final Job j : current.getSuccessors()) {
            this.breakRelation(a, j);
        }
        this.allocations.remove(current);
        if (a.getStartDate() < current.getParentProject().getReleaseDate()) {
            this.totalCachedResult -= PrecedenceRelationsTracker.getBrokenRelationValidation(current);
        }
    }
}
