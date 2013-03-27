package org.optaplanner.examples.projectscheduling.domain.solver;

import java.util.Comparator;

import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;

/**
 * Allocations are more difficult when their job has more successors. In case
 * two allocations have the same number of successors, the allocation from a
 * project with an earlier release date is considered more difficult.
 */
public class AllocationDifficultyComparator implements Comparator<Allocation> {

    @Override
    public int compare(final Allocation a, final Allocation b) {
        final Job aJob = a.getJob();
        final Job bJob = b.getJob();
        final int aSize = aJob.getRecursiveSuccessors().size();
        final int bSize = bJob.getRecursiveSuccessors().size();
        if (aSize < bSize) {
            return -1;
        } else if (aSize == bSize) {
            final int aRelease = aJob.getParentProject().getReleaseDate();
            final int bRelease = bJob.getParentProject().getReleaseDate();
            if (aRelease < bRelease) {
                return 1;
            } else if (aRelease == bRelease) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return 1;
        }
    }

}
