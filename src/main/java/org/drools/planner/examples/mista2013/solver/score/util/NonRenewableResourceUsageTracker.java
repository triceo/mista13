package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.HashMap;
import java.util.Map;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Resource;

/**
 * Validates feasibility requirements (1) and (3). Counts how many more local
 * and global renewable resources would we need than we have capacity for.
 */
public class NonRenewableResourceUsageTracker {

    private Map<Resource, Integer> nonRenewableResourceUsage = new HashMap<Resource, Integer>();
    
    public NonRenewableResourceUsageTracker() {
    }

    public void add(final Allocation a) {
        for (final Map.Entry<Resource, Integer> entry : a.getJobMode().getResourceRequirements().entrySet()) {
            final Resource r = entry.getKey();
            if (r.isRenewable()) {
                continue;
            }
            int value = entry.getValue();
            if (value == 0) {
                continue;
            }
            // slightly faster than containsKey(r)
            final Integer previousValue = this.nonRenewableResourceUsage.get(r);
            if (previousValue != null) {
                value += previousValue;
            }
            this.nonRenewableResourceUsage.put(r, value);
        }
    }

    public int getSumOfOverusedResources() {
        int total = 0;
        for (final Map.Entry<Resource, Integer> entry : this.nonRenewableResourceUsage.entrySet()) {
            final Resource r = entry.getKey();
            final int allocation = entry.getValue();
            total += Math.max(0, allocation - r.getCapacity());
        }
        return total;
    }

    public void remove(final Allocation a) {
        for (final Map.Entry<Resource, Integer> entry : a.getJobMode().getResourceRequirements().entrySet()) {
            final Resource r = entry.getKey();
            if (r.isRenewable()) {
                continue;
            }
            final int value = entry.getValue();
            if (value == 0) {
                continue;
            }
            this.nonRenewableResourceUsage.put(r, this.nonRenewableResourceUsage.get(r) - value);
        }
    }

}
