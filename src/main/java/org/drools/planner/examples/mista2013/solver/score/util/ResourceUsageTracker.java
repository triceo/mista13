package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.JobMode;
import org.drools.planner.examples.mista2013.domain.Resource;

/**
 * Validates feasibility requirements (1), (2) and (3). Counts how many more
 * resources would we need than we have capacity for.
 */
public class ResourceUsageTracker {

    private final Map<JobMode, Map<Resource, Integer>> renewableResourceRequirementCache = new HashMap<JobMode, Map<Resource, Integer>>();
    private final Map<JobMode, Map<Resource, Integer>> nonRenewableResourceRequirementCache = new HashMap<JobMode, Map<Resource, Integer>>();

    @SuppressWarnings("rawtypes")
    private final Map[] renewableResourceUseInTime;

    private int overused = 0;
    private int idle = 0;

    private final Map<Resource, Integer> nonRenewableResourceUsage = new HashMap<Resource, Integer>();

    public ResourceUsageTracker(final int horizon) {
        this.renewableResourceUseInTime = new HashMap[horizon];
    }

    public void add(final Allocation a) {
        this.addRenewable(a);
        this.addNonRenewable(a);
    }

    private void addNonRenewable(final Allocation a) {
        final Map<Resource, Integer> nonRenewables = this.prepareResourceRequirements(a.getJobMode(), false);
        for (final Resource r : nonRenewables.keySet()) {
            this.updateCachesForAddition(r, nonRenewables.get(r), this.nonRenewableResourceUsage);
        }
    }

    private void addRenewable(final Allocation a) {
        final Map<Resource, Integer> renewables = this.prepareResourceRequirements(a.getJobMode(), true);
        final int dueDate = a.getDueDate();
        final int startDate = a.getStartDate();
        for (final Resource r : renewables.keySet()) {
            final int requirement = renewables.get(r);
            for (int time = startDate; time <= dueDate; time++) {
                @SuppressWarnings("unchecked")
                Map<Resource, Integer> totalUse = this.renewableResourceUseInTime[time];
                if (totalUse == null) {
                    totalUse = new LinkedHashMap<Resource, Integer>(renewables.size());
                    this.renewableResourceUseInTime[time] = totalUse;
                }
                this.updateCachesForAddition(r, requirement, totalUse);
            }
        }
    }

    public int getSumOfIdleResources() {
        return -this.idle;
    }

    public int getSumOfOverusedResources() {
        return this.overused;
    }

    /**
     * Of all the resource requirements provided by the job, we sometimes don't
     * care about particular types and also we don't care about resources with 0
     * consumption. They wouldn't have changed anything anyway. We exclude these
     * resources, so that there's less iteration later when we're counting the
     * resource use.
     * 
     * @param jobMode
     *            The job mode of which the requirements are to be preprocessed.
     * @param renewable
     *            Whether to return only renewables. When false, only
     *            non-renewables will be returned.
     * @return Resource requirements without those useless for the purposes of
     *         this class.
     */
    private Map<Resource, Integer> prepareResourceRequirements(final JobMode jobMode, final boolean renewable) {
        Map<Resource, Integer> requirements = renewable ? this.renewableResourceRequirementCache.get(jobMode)
                : this.nonRenewableResourceRequirementCache.get(jobMode);
        if (requirements == null) {
            // prepare the data
            final Map<Resource, Integer> original = jobMode.getResourceRequirements();
            requirements = new LinkedHashMap<Resource, Integer>(original.size());
            for (final Resource r : original.keySet()) {
                if (r.isRenewable() != renewable) {
                    continue;
                }
                final Integer resourceRequirement = original.get(r);
                if (resourceRequirement == 0) {
                    // doesn't change anything, don't track it
                    continue;
                }
                requirements.put(r, resourceRequirement);
            }
            // store them in the cache
            if (renewable) {
                this.renewableResourceRequirementCache.put(jobMode, requirements);
            } else {
                this.nonRenewableResourceRequirementCache.put(jobMode, requirements);
            }
        }
        return requirements;
    }

    public void remove(final Allocation a) {
        this.removeRenewable(a);
        this.removeNonRenewable(a);
    }

    private void removeNonRenewable(final Allocation a) {
        final Map<Resource, Integer> nonRenewables = this.prepareResourceRequirements(a.getJobMode(), false);
        for (final Resource r : nonRenewables.keySet()) {
            this.updateCachesForRemoval(r, nonRenewables.get(r), this.nonRenewableResourceUsage);
        }
    }

    private void removeRenewable(final Allocation a) {
        final Map<Resource, Integer> renewables = this.prepareResourceRequirements(a.getJobMode(), true);
        final int dueDate = a.getDueDate();
        final int startDate = a.getStartDate();
        for (final Resource r : renewables.keySet()) {
            final int requirement = renewables.get(r);
            for (int time = startDate; time <= dueDate; time++) {
                @SuppressWarnings("unchecked")
                final Map<Resource, Integer> totalUse = this.renewableResourceUseInTime[time];
                this.updateCachesForRemoval(r, requirement, totalUse);
            }
        }
    }

    private void updateCachesForAddition(final Resource r, final int requirement, final Map<Resource, Integer> totalUse) {
        Integer currentTotalUseTmp = totalUse.get(r);
        currentTotalUseTmp = (currentTotalUseTmp == null) ? Integer.valueOf(0) : currentTotalUseTmp;
        final int currentTotalUse = currentTotalUseTmp.intValue();
        final int newTotalUse = requirement + currentTotalUse;
        totalUse.put(r, newTotalUse);
        final int capacity = r.getCapacity();
        if (currentTotalUse > capacity) {
            // add the increase over the already overreached capacity
            this.overused += newTotalUse - currentTotalUse;
        } else if (newTotalUse > capacity) {
            // the capacity is newly overreached
            this.overused += newTotalUse - capacity;
            this.idle -= capacity - currentTotalUse;
        } else {
            // the capacity remains idle
            this.idle -= requirement;
        }
    }

    private void updateCachesForRemoval(final Resource r, final int requirement, final Map<Resource, Integer> totalUse) {
        final int currentTotalUse = totalUse.get(r);
        final int newTotalUse = currentTotalUse - requirement;
        totalUse.put(r, newTotalUse);
        final int capacity = r.getCapacity();
        if (newTotalUse > capacity) {
            // remove the decrease over the already overreached capacity
            this.overused -= currentTotalUse - newTotalUse;
        } else if (currentTotalUse > capacity) {
            // the capacity is newly idle
            this.overused -= currentTotalUse - capacity;
            this.idle += capacity - newTotalUse;
        } else {
            // the capacity remains idle
            this.idle += requirement;
        }
    }

}
