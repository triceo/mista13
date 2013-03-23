package org.optaplanner.examples.projectscheduling.domain.solver;

import java.util.Comparator;

import org.optaplanner.examples.projectscheduling.domain.Allocation;

public class AllocationDifficultyComparator implements Comparator<Allocation> {

    @Override
    public int compare(final Allocation a, final Allocation b) {
        final int aSize = a.getJob().getRecursiveSuccessors().size();
        final int bSize = b.getJob().getRecursiveSuccessors().size();
        if (aSize < bSize) {
            return -1;
        } else if (aSize == bSize) {
            return 0;
        } else {
            return 1;
        }
    }

}
