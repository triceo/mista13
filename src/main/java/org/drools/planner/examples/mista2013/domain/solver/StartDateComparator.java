package org.drools.planner.examples.mista2013.domain.solver;

import java.util.Comparator;

public class StartDateComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer arg0, Integer arg1) {
        return arg0.compareTo(arg1);
    }

}
