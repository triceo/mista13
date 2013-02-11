package org.drools.planner.examples.mista2013.domain.solver;

import java.util.Comparator;
import java.util.Map;

import org.drools.planner.examples.mista2013.domain.JobMode;
import org.drools.planner.examples.mista2013.domain.Resource;

public class JobModeComparator implements Comparator<JobMode> {

    private static int sumResources(final JobMode jm) {
        int total = 0;
        for (final Map.Entry<Resource, Integer> entry : jm.getResourceRequirements().entrySet()) {
            final Resource resource = entry.getKey();
            if (resource.isRenewable()) {
                total += entry.getValue() * jm.getDuration();
            } else {
                total += entry.getValue();
            }
        }
        return total;
    }

    @Override
    public int compare(final JobMode o1, final JobMode o2) {
        final int o1resourceUse = JobModeComparator.sumResources(o1);
        final int o2resourceUse = JobModeComparator.sumResources(o2);
        // less duration is stronger
        if (o1resourceUse < o2resourceUse) {
            return 1;
        } else if (o1resourceUse == o2resourceUse) {
            return 0;
        } else {
            return 1;
        }
    }

}
