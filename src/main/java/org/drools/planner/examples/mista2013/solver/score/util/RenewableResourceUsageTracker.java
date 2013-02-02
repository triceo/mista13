package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.IntRange;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.JobMode;
import org.drools.planner.examples.mista2013.domain.Resource;

/**
 * Validates feasibility requirements (1) and (3). Counts how many more local
 * and global renewable resources would we need than we have capacity for.
 */
public class RenewableResourceUsageTracker {

    /**
     * Of all the resource requirements provided by the job, we don't care about
     * non-renewables and also we don't care about resources with 0 consumption.
     * They wouldn't have changed anything anyway. We exclude these resources,
     * so that there's less iteration later when we're counting the resource
     * use.
     * 
     * @param originalResourceRequirements
     *            {@link JobMode#getResourceRequirements()}.
     * @return Resource requirements without those useless for the purposes of
     *         this class.
     */
    private static Map<Resource, Integer> prepareResourceRequirements(
            final Map<Resource, Integer> originalResourceRequirements) {
        final Map<Resource, Integer> resourceRequirements = new HashMap<Resource, Integer>();
        for (final Map.Entry<Resource, Integer> entry : originalResourceRequirements.entrySet()) {
            final Resource r = entry.getKey();
            if (!r.isRenewable()) {
                // this class only tracks renewables
                continue;
            }
            final int resourceRequirement = entry.getValue();
            if (resourceRequirement == 0) {
                // doesn't change anything, don't track it
                continue;
            }
            resourceRequirements.put(r, resourceRequirement);
        }
        return resourceRequirements;
    }

    /**
     * Minimal time for which we have any resource usage tracked.
     */
    private int minTime = Integer.MAX_VALUE;

    /**
     * Maximal time for which we have any resource usage tracked.
     */
    private int maxTime = Integer.MIN_VALUE;
    @SuppressWarnings("rawtypes")
    private final Map[] resourceUsagesInTime;
    /**
     * A cache of where jobs occur on the timeline.
     */
    private final Map<Job, IntRange> jobOccurrences = new HashMap<Job, IntRange>();

    private final int[] usageCache;

    public RenewableResourceUsageTracker(final int horizon) {
        this.resourceUsagesInTime = new HashMap[horizon];
        this.usageCache = new int[horizon];
    }

    public void add(final Allocation a) {
        final IntRange times = new IntRange(a.getStartDate().intValue(), a.getDueDate());
        this.minTime = Math.min(this.minTime, times.getMinimumInteger());
        this.maxTime = Math.max(this.maxTime, times.getMaximumInteger());
        final Map<Resource, Integer> resourceRequirements = RenewableResourceUsageTracker.prepareResourceRequirements(a
                .getJobMode().getResourceRequirements());
        this.jobOccurrences.put(a.getJob(), times);
        for (int time = times.getMinimumInteger(); time <= times.getMaximumInteger(); time++) {
            @SuppressWarnings("unchecked")
            Map<Job, Map<Resource, Integer>> resourceUsagePerJob = this.resourceUsagesInTime[time];
            if (resourceUsagePerJob == null) { // new time information
                resourceUsagePerJob = new HashMap<Job, Map<Resource, Integer>>();
                this.resourceUsagesInTime[time] = resourceUsagePerJob;
            }
            resourceUsagePerJob.put(a.getJob(), resourceRequirements);
            // re-initialize cache
            this.invalidateCache(time);
        }
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
                final int resourceRequirement = entry.getValue();
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

    protected void invalidateCache(final int time) {
        this.usageCache[time] = -1;
    }

    public void remove(final Allocation a) {
        final Job j = a.getJob();
        final IntRange r = this.jobOccurrences.remove(j);
        for (int time = r.getMinimumInteger(); time <= r.getMaximumInteger(); time++) {
            @SuppressWarnings("unchecked")
            final Map<Job, Map<Resource, Integer>> resourceUsage = this.resourceUsagesInTime[time];
            final Map<Resource, Integer> previous = resourceUsage.remove(j);
            if (previous != null && previous.size() > 0) {
                this.invalidateCache(time);
            }
        }
    }

}
