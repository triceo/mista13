package org.optaplanner.examples.projectscheduling.domain.solver;

import java.util.Comparator;

import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.Project;

/**
 * Allocations are ordered by project, to prevent fragementation. Then by approximate release date.
 */
public class AllocationDifficultyComparator implements Comparator<Allocation> {

    @Override
    public int compare(final Allocation a, final Allocation b) {
        final Job aJob = a.getJob();
        final Job bJob = b.getJob();
        final int aSize = aJob.getParentProject().getId();
        final int bSize = bJob.getParentProject().getId();
        if (aSize < bSize) {
            return 1;
        } else if (aSize == bSize) {
            final int aRelease = aJob.getParentProject().getReleaseDate() + Project.getCriticalPathDurationUntil(aJob);
            final int bRelease = bJob.getParentProject().getReleaseDate() + Project.getCriticalPathDurationUntil(bJob);
            if (aRelease < bRelease) {
                return 1;
            } else if (aRelease == bRelease) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

}
