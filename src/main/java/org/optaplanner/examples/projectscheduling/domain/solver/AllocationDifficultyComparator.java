package org.optaplanner.examples.projectscheduling.domain.solver;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.Project;

public class AllocationDifficultyComparator implements Comparator<Allocation> {

    @Override
    public int compare(final Allocation a, final Allocation b) {
        final Job aJob = a.getJob();
        final Project aParent = aJob.getParentProject();
        final Job bJob = b.getJob();
        final Project bParent = bJob.getParentProject();
        return new CompareToBuilder()
                .append(bParent.getReleaseDate(), aParent.getReleaseDate())
                .append(aParent.getCriticalPathDuration(), bParent.getCriticalPathDuration())
                .append(aJob.getMinimalPossibleStartDate(), bJob.getMinimalPossibleStartDate())
                .toComparison();
    }

}
