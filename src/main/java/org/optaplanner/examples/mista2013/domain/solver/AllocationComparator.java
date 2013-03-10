package org.optaplanner.examples.mista2013.domain.solver;

import java.util.Comparator;

import org.optaplanner.examples.mista2013.domain.Allocation;

public class AllocationComparator implements Comparator<Allocation> {

    @Override
    public int compare(final Allocation o1, final Allocation o2) {
        final int s1 = o1.getJob().getRecursiveSuccessors().size();
        final int s2 = o2.getJob().getRecursiveSuccessors().size();
        if (s1 < s2) {
            return -1;
        } else if (s1 == s2) {
            return 0;
        } else {
            return 1;
        }
    }

}
