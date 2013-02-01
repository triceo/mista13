package org.drools.planner.examples.mista2013.domain.solver;

import java.util.Comparator;

import org.drools.planner.examples.mista2013.domain.JobMode;

public class JobModeComparator implements Comparator<JobMode> {

    @Override
    public int compare(JobMode o1, JobMode o2) {
        // less duration is stronger
        if (o1.getDuration() < o2.getDuration()) {
            return 1;
        } else if (o1.getDuration() > o2.getDuration()) {
            return -1;
        } else {
            return 0;
        }
    }

}
