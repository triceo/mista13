package org.drools.planner.examples.mista2013.domain.solver;

import java.util.Comparator;

import org.drools.planner.examples.mista2013.domain.Allocation;

public class AllocationComparator implements Comparator<Allocation> {

    @Override
    public int compare(Allocation o1, Allocation o2) {
        int s1 = o1.getJob().getRecursiveSuccessors().size();
        int s2 = o1.getJob().getRecursiveSuccessors().size();
        if (s1 < s2) {
            return -1;
        } else if (s1 > s2) {
            return 1;
        } else {
            return 0;
        }
    }

}
