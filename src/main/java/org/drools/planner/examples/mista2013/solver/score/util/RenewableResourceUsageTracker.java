package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.HashMap;
import java.util.Map;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.Resource;

/**
 * Validates feasibility requirements (1) and (3). Counts how many more local
 * and global renewable resources would we need than we have capacity for.
 */
public class RenewableResourceUsageTracker {

    private int minTime = Integer.MAX_VALUE;
    private int maxTime = Integer.MIN_VALUE;

    @SuppressWarnings("rawtypes")
    private final Map[] resourceUsagesInTime;
    private final int[] usageCache;

    public RenewableResourceUsageTracker(final int horizon) {
        this.resourceUsagesInTime = new HashMap[horizon];
        this.usageCache = new int[horizon];
    }

    protected void cache(final int time, final int value) {
        this.usageCache[time] = value;
    }

    public int countResourceOveruse() {
        int total = 0;
        for (int time = this.minTime; time <= this.maxTime; time++) {
            int cache = this.getCached(time);
            if (cache < 0) {
                cache = this.countResourceOveruseInTime(time);
                this.cache(time, cache);
            }
            total += cache;
        }
        return total;
    }

    private int countResourceOveruseInTime(final int time) {
        @SuppressWarnings("unchecked")
        final Map<Job, Map<Resource, Integer>> resourceUsage = this.resourceUsagesInTime[time];
        if (resourceUsage == null || resourceUsage.size() == 0) {
            return 0;
        }
        int total = 0;
        final Map<Resource, Integer> renewableAllocations = new HashMap<Resource, Integer>();
        for (final Map<Resource, Integer> resources : resourceUsage.values()) {
            for (final Map.Entry<Resource, Integer> entry : resources.entrySet()) {
                final Resource r = entry.getKey();
                if (!r.isRenewable()) {
                    // this class only tracks renewables
                    continue;
                }
                final int resourceRequirement = entry.getValue();
                if (resourceRequirement == 0) {
                    // doesn't change anything
                    continue;
                }
                Integer totalAllocation = renewableAllocations.get(r);
                totalAllocation = resourceRequirement + ((totalAllocation == null) ? 0 : totalAllocation);
                renewableAllocations.put(r, totalAllocation);
            }
        }
        for (final Map.Entry<Resource, Integer> entry : renewableAllocations.entrySet()) {
            final Resource r = entry.getKey();
            final int allocation = entry.getValue();
            total += Math.max(0, allocation - r.getCapacity());
        }
        return total;
    }

    protected int getCached(final int time) {
        return this.usageCache[time];
    }

    @SuppressWarnings("unchecked")
    protected Map<Job, Map<Resource, Integer>> getResourceUsage(final int time) {
        if (time >= this.resourceUsagesInTime.length || time < 0) {
            throw new IllegalArgumentException("Invalid time value: " + time);
        }
        return this.resourceUsagesInTime[time];
    }

    protected void invalidateCache(final int time) {
        this.usageCache[time] = -1;
    }

    public void removeAllocation(final Allocation a) {
        final Job j = a.getJob();
        for (int time = this.minTime; time <= this.maxTime; time++) {
            @SuppressWarnings("unchecked")
            final Map<Job, Map<Resource, Integer>> resourceUsage = this.resourceUsagesInTime[time];
            if (resourceUsage == null) {
                continue;
            }
            final Map<Resource, Integer> previous = resourceUsage.remove(j);
            if (previous != null && previous.size() > 0) {
                this.invalidateCache(time);
            }
        }
    }

    public void updateAllocation(final Allocation a) {
        if (!a.isInitialized()) {
            this.removeAllocation(a);
            return;
        }
        this.minTime = Math.min(this.minTime, a.getStartDate());
        this.maxTime = Math.max(this.maxTime, a.getDueDate());
        for (int time = a.getStartDate(); time <= a.getDueDate(); time++) {
            Map<Job, Map<Resource, Integer>> resourceUsagePerJob = this.getResourceUsage(time);
            if (resourceUsagePerJob == null) { // new time information
                resourceUsagePerJob = new HashMap<Job, Map<Resource, Integer>>();
                this.resourceUsagesInTime[time] = resourceUsagePerJob;
            }
            resourceUsagePerJob.put(a.getJob(), a.getJobMode().getResourceRequirements());
            this.invalidateCache(time);
        }
    }

}
