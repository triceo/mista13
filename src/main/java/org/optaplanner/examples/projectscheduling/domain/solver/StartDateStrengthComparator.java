package org.optaplanner.examples.projectscheduling.domain.solver;

import java.util.Comparator;

public class StartDateStrengthComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer a, Integer b) {
        return a.compareTo(b);
    }

}
