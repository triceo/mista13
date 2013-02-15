package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.JobMode;
import org.drools.planner.examples.mista2013.domain.Resource;

/**
 * Validates feasibility requirements (1) and (3). Counts how many more local
 * and global renewable resources would we need than we have capacity for.
 */
public class RenewableResourceUsageTracker {

    private final Map<JobMode, Map<Resource, Integer>> resourceRequirementCache = new HashMap<JobMode, Map<Resource, Integer>>();

    @SuppressWarnings("rawtypes")
    private final Map[] resourceUseInTime;

    private int totalCachedResult = 0;

    public RenewableResourceUsageTracker(final int horizon) {
        this.resourceUseInTime = new HashMap[horizon];
    }

    public void add(final Allocation a) {
        final Map<Resource, Integer> currentUse = this.prepareResourceRequirements(a.getJobMode());
        final int dueDate = a.getDueDate();
        final int startDate = a.getStartDate();
        for (final Map.Entry<Resource, Integer> entry : currentUse.entrySet()) {
            final Resource r = entry.getKey();
            final int use = entry.getValue();
            for (int time = startDate; time <= dueDate; time++) {
                // fetch the total use of resources
                @SuppressWarnings("unchecked")
                Map<Resource, Integer> totalUse = this.resourceUseInTime[time];
                if (totalUse == null) {
                    totalUse = new LinkedHashMap<Resource, Integer>();
                    this.resourceUseInTime[time] = totalUse;
                }
                // avoid containsKey(r)
                Integer currentTotalUseTmp = totalUse.get(r);
                currentTotalUseTmp = (currentTotalUseTmp == null) ? Integer.valueOf(0) : currentTotalUseTmp;
                final int currentTotalUse = currentTotalUseTmp.intValue();
                final int newTotalUse = use + currentTotalUse;
                // and update the tracker
                totalUse.put(r, newTotalUse);
                if (currentTotalUse > r.getCapacity()) {
                    // add the increase over the already overreached capacity
                    this.totalCachedResult += newTotalUse - currentTotalUse;
                } else {
                    // the capacity is newly overreached
                    this.totalCachedResult += Math.max(0, newTotalUse - r.getCapacity());
                }
            }
        }
    }

    public int getSumOfOverusedResources() {
        return this.totalCachedResult;
    }

    /**
     * Of all the resource requirements provided by the job, we don't care about
     * non-renewables and also we don't care about resources with 0 consumption.
     * They wouldn't have changed anything anyway. We exclude these resources,
     * so that there's less iteration later when we're counting the resource
     * use.
     * 
     * @param jobMode
     *            The job mode of which the requirements are preprocessed.
     * @return Resource requirements without those useless for the purposes of
     *         this class.
     */
    private Map<Resource, Integer> prepareResourceRequirements(final JobMode jobMode) {
        Map<Resource, Integer> requirements = this.resourceRequirementCache.get(jobMode);
        if (requirements == null) {
            requirements = new LinkedHashMap<Resource, Integer>();
            for (final Map.Entry<Resource, Integer> entry : jobMode.getResourceRequirements().entrySet()) {
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
                requirements.put(r, resourceRequirement);
            }
            this.resourceRequirementCache.put(jobMode, requirements);
        }
        return requirements;
    }

    public void remove(final Allocation a) {
        final Map<Resource, Integer> currentUse = this.prepareResourceRequirements(a.getJobMode());
        final int dueDate = a.getDueDate();
        final int startDate = a.getStartDate();
        // subtract the current resource use from the totals
        for (final Map.Entry<Resource, Integer> entry : currentUse.entrySet()) {
            final Resource r = entry.getKey();
            final int use = entry.getValue();
            for (int time = startDate; time <= dueDate; time++) {
                // fetch the total use of resources
                @SuppressWarnings("unchecked")
                final Map<Resource, Integer> totalUse = this.resourceUseInTime[time];
                final int total = totalUse.get(r);
                final int result = total - use;
                // update the tracker
                totalUse.put(r, result);
                if (result > r.getCapacity()) {
                    // remove the decrease over the already overreached capacity
                    this.totalCachedResult -= total - result;
                } else {
                    // the capacity is newly satisfied
                    this.totalCachedResult -= Math.max(0, total - r.getCapacity());
                }
            }
        }
    }

}
