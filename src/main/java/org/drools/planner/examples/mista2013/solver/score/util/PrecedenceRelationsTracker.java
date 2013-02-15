package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;

/**
 * Validates feasibility requirement (7). How many precedence relations are
 * broken.
 */
public class PrecedenceRelationsTracker {

    private static final int ESTIMATED_NUMBER_OF_RELATIONS = 4;

    private static int getBrokenRelationValidation(final Job to) {
        return 1 + to.getRecursiveSuccessors().size();
    }

    private static boolean isPrecedenceCorrect(final Allocation pre, final Allocation current) {
        return current.getStartDate() > pre.getDueDate();
    }

    private static void relate(final Job from, final Job to, final Map<Job, Set<Job>> relations) {
        Set<Job> relation = relations.get(from);
        if (relation == null) {
            relation = new HashSet<Job>(PrecedenceRelationsTracker.ESTIMATED_NUMBER_OF_RELATIONS);
            relations.put(from, relation);
        }
        relation.add(to);
    }

    private static void unrelate(final Job from, final Job to, final Map<Job, Set<Job>> relations) {
        relations.get(from).remove(to);
    }

    private int totalCachedResult = 0;

    private final Map<Job, Allocation> allocations = new HashMap<Job, Allocation>();

    private final Map<Job, Set<Job>> relations = new HashMap<Job, Set<Job>>();

    public PrecedenceRelationsTracker() {
    }

    public void add(final Allocation a) {
        final Job current = a.getJob();
        this.allocations.put(current, a);
        for (final Job j : current.getPredecessors()) {
            this.formRelation(j, current);
        }
        for (final Job j : current.getSuccessors()) {
            this.formRelation(current, j);
        }
        if (a.getStartDate() < current.getParentProject().getReleaseDate()) {
            this.totalCachedResult += PrecedenceRelationsTracker.getBrokenRelationValidation(current);
        }
    }

    private void breakRelation(final Job from, final Job to) {
        if (!this.hasRelation(from, to)) {
            return;
        }
        final Allocation fromAlloc = this.allocations.get(from);
        if (fromAlloc == null) {
            return;
        }
        final Allocation toAlloc = this.allocations.get(to);
        if (toAlloc == null) {
            return;
        }
        PrecedenceRelationsTracker.unrelate(from, to, this.relations);
        if (!PrecedenceRelationsTracker.isPrecedenceCorrect(fromAlloc, toAlloc)) {
            this.totalCachedResult -= PrecedenceRelationsTracker.getBrokenRelationValidation(to);
        }
    }

    private void formRelation(final Job from, final Job to) {
        if (this.hasRelation(from, to)) {
            return;
        }
        final Allocation fromAlloc = this.allocations.get(from);
        if (fromAlloc == null) {
            return;
        }
        final Allocation toAlloc = this.allocations.get(to);
        if (toAlloc == null) {
            return;
        }
        PrecedenceRelationsTracker.relate(from, to, this.relations);
        if (!PrecedenceRelationsTracker.isPrecedenceCorrect(fromAlloc, toAlloc)) {
            this.totalCachedResult += PrecedenceRelationsTracker.getBrokenRelationValidation(to);
        }
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
            this.breakRelation(j, current);
        }
        for (final Job j : current.getSuccessors()) {
            this.breakRelation(current, j);
        }
        this.allocations.remove(current);
        if (a.getStartDate() < current.getParentProject().getReleaseDate()) {
            this.totalCachedResult -= PrecedenceRelationsTracker.getBrokenRelationValidation(current);
        }
    }
}
