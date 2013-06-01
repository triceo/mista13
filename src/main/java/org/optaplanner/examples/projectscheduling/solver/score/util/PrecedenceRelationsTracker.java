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

    private static int getBrokenPrecedenceValidation(final Allocation from, final Allocation to) {
        final int endFrom = from.getDueDate();
        final int startTo = to.getStartDate();
        if (startTo > endFrom) {
            return 0;
        } else {
            // jobs must start after others end, hence the +1
            return endFrom - startTo + 1;
        }
    }

    private static boolean isPrecedenceCorrect(final Allocation pre, final Allocation current) {
        return current.getStartDate() > pre.getDueDate();
    }

    private int totalCachedResult = 0;

    /**
     * Allocations that have been {@link #add(Allocation)}ed and not yet {@link #remove(Allocation)}d.
     */
    private final Map<Job, Allocation> allocations;

    /**
     * Bonds that have been through {@link #breakBond(Job, Job, Map)} and not
     * yet through {@link #mendBond(Job, Job, Map)}. This map always has a
     * predecessor as a key and a direct successor as a value.
     */
    private final Map<Allocation, Set<Allocation>> brokenBonds;

    public PrecedenceRelationsTracker(final ProjectSchedule solution) {
        this.expectedJobCount = solution.getAllocations().size();
        this.allocations = new HashMap<Job, Allocation>(this.expectedJobCount);
        this.brokenBonds = new HashMap<Allocation, Set<Allocation>>(this.expectedJobCount);
    }

    public void add(final Allocation a) {
        final Job current = a.getJob();
        this.allocations.put(current, a);
        for (final Job j : current.getPredecessors()) {
            if (j.isSource()) {
                // first job in the project starts before project release date
                final int difference = a.getStartDate() - current.getParentProject().getReleaseDate();
                if (difference < 0) {
                    this.totalCachedResult -= difference;
                }
                // other than that, we're not interested in such jobs
                continue;
            }
            this.bind(j, a);
        }
        for (final Job j : current.getSuccessors()) {
            if (j.isSink()) {
                continue;
            }
            this.bind(a, j);
        }
    }

    private void bind(final Allocation from, final Allocation to) {
        if (this.isBondBroken(from, to)) {
            return;
        }
        if (!PrecedenceRelationsTracker.isPrecedenceCorrect(from, to)) {
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

    private int breakBond(final Allocation from, final Allocation to) {
        Set<Allocation> relation = this.brokenBonds.get(from);
        if (relation == null) {
            relation = new HashSet<Allocation>(PrecedenceRelationsTracker.ESTIMATED_NUMBER_OF_BONDS);
            this.brokenBonds.put(from, relation);
        }
        relation.add(to);
        return PrecedenceRelationsTracker.getBrokenPrecedenceValidation(from, to);
    }

    public int getBrokenPrecedenceRelationsMeasure() {
        return this.totalCachedResult;
    }
    
    private boolean isBondBroken(final Allocation from, final Allocation to) {
        if (from == null || to == null) {
            return false;
        }
        final Set<Allocation> related = this.brokenBonds.get(from);
        if (related == null) {
            return false;
        }
        return related.contains(to);
    }

    private int mendBond(final Allocation from, final Allocation to) {
        this.brokenBonds.get(from).remove(to);
        return PrecedenceRelationsTracker.getBrokenPrecedenceValidation(from, to);
    }

    public void remove(final Allocation a) {
        final Job current = a.getJob();
        for (final Job j : current.getPredecessors()) {
            if (j.isSource()) {
                // counter-adjust for the same operation performed in add().
                final int difference = a.getStartDate() - current.getParentProject().getReleaseDate();
                if (difference < 0) {
                    this.totalCachedResult += difference;
                }
                // other than that, we're not interested in such jobs
                continue;
            }
            this.unbind(j, a);
        }
        for (final Job j : current.getSuccessors()) {
            if (j.isSink()) {
                continue;
            }
            this.unbind(a, j);
        }
        this.allocations.remove(current);
    }

    private void unbind(final Allocation from, final Allocation to) {
        if (this.isBondBroken(from, to)) {
            this.totalCachedResult -= this.mendBond(from, to);
        }
    }

    private void unbind(final Allocation from, final Job to) {
        this.unbind(from, this.allocations.get(to));
    }

    private void unbind(final Job from, final Allocation to) {
        this.unbind(this.allocations.get(from), to);
    }
}
