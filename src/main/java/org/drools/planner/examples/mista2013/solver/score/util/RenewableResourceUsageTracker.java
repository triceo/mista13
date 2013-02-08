package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
        final Map<Resource, Integer> resourceRequirements = new LinkedHashMap<Resource, Integer>();
        for (final Map.Entry<Resource, Integer> entry : originalResourceRequirements.entrySet()) {
            final Resource r = entry.getKey();
            if (!r.isRenewable()) {
                // this class only tracks renewables
                continue;
            }
            final Integer resourceRequirement = entry.getValue();
            if (resourceRequirement == 0) {
                // doesn't change anything, don't track it
                continue;
            }
            resourceRequirements.put(r, resourceRequirement);
        }
        return resourceRequirements;
    }

    @SuppressWarnings("rawtypes")
    private final Map[] resourceUseInTime;
    /**
     * A cache of where jobs occur on the timeline.
     */
    private final Map<Job, IntRange> jobOccurrences = new HashMap<Job, IntRange>();

    private final int[] usageCache;

    /**
     * Times for which the {@link #countResourceOveruseInTime(int)} will need to
     * be called.
     */
    private final Set<Integer> invalidCaches = new HashSet<Integer>();

    /**
     * Result of {@link #countResourceOveruse()} excluding those times that are
     * in {@link #invalidCaches} which will need to be recalculated.
     */
    private int totalCachedResult = 0;

    public RenewableResourceUsageTracker(final int horizon) {
        this.resourceUseInTime = new HashMap[horizon];
        this.usageCache = new int[horizon];
        Arrays.fill(this.usageCache, -1);
    }

    public void add(final Allocation a) {
        final IntRange times = new IntRange(a.getStartDate().intValue(), a.getDueDate());
        this.jobOccurrences.put(a.getJob(), times);
        final Map<Resource, Integer> currentUse = RenewableResourceUsageTracker.prepareResourceRequirements(a
                .getJobMode().getResourceRequirements());
        for (int time = times.getMinimumInteger(); time <= times.getMaximumInteger(); time++) {
            @SuppressWarnings("unchecked")
            final Map<Resource, Integer> totalUse = this.resourceUseInTime[time];
            if (totalUse == null) {
                /*
                 * there is no resource use yet, use the current job mode as
                 * base
                 */
                this.resourceUseInTime[time] = new LinkedHashMap<Resource, Integer>(currentUse);
            } else {
                /*
                 * update the total resource use with the resource use of this
                 * job mode
                 */
                for (final Map.Entry<Resource, Integer> entry : currentUse.entrySet()) {
                    final Resource r = entry.getKey();
                    final Integer use = entry.getValue();
                    // avoid containsKey(r)
                    Integer newTotalUse = totalUse.get(r);
                    newTotalUse = use + ((newTotalUse == null) ? 0 : newTotalUse);
                    totalUse.put(r, newTotalUse);
                }
            }
            // re-initialize cache
            this.invalidateCache(time);
        }
    }

    public int countResourceOveruse() {
        for (final int time : this.invalidCaches) {
            final int cache = this.countResourceOveruseInTime(time);
            this.usageCache[time] = cache;
            this.totalCachedResult += cache;
        }
        this.invalidCaches.clear();
        return this.totalCachedResult;
    }

    private int countResourceOveruseInTime(final int time) {
        @SuppressWarnings("unchecked")
        final Map<Resource, Integer> resourceUsage = this.resourceUseInTime[time];
        if (resourceUsage == null) {
            return 0;
        }
        int total = 0;
        for (final Map.Entry<Resource, Integer> entry : resourceUsage.entrySet()) {
            final Resource r = entry.getKey();
            final int allocation = entry.getValue();
            total += Math.max(0, allocation - r.getCapacity());
        }
        return total;
    }

    protected void invalidateCache(final int time) {
        this.invalidCaches.add(time);
        final int cache = this.usageCache[time];
        if (cache > 0) {
            this.totalCachedResult -= cache;
        }
        this.usageCache[time] = -1;
    }

    public void remove(final Allocation a) {
        final Job j = a.getJob();
        final IntRange range = this.jobOccurrences.remove(j);
        final Map<Resource, Integer> currentUse = RenewableResourceUsageTracker.prepareResourceRequirements(a
                .getJobMode().getResourceRequirements());
        for (int time = range.getMinimumInteger(); time <= range.getMaximumInteger(); time++) {
            @SuppressWarnings("unchecked")
            final Map<Resource, Integer> totalUse = this.resourceUseInTime[time];
            // subtract the current resource use from the totals
            for (final Map.Entry<Resource, Integer> entry : currentUse.entrySet()) {
                final Resource r = entry.getKey();
                final Integer use = entry.getValue();
                totalUse.put(r, totalUse.get(r) - use);
            }
            this.invalidateCache(time);
        }
    }

}
