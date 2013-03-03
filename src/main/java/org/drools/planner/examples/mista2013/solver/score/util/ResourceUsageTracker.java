package org.drools.planner.examples.mista2013.solver.score.util;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.HashMap;
import java.util.Map;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.JobMode;
import org.drools.planner.examples.mista2013.domain.Resource;

/**
 * Validates feasibility requirements (1), (2) and (3). Counts how many more
 * resources would we need than we have capacity for.
 */
public class ResourceUsageTracker {

    private final Map<JobMode, TObjectIntMap<Resource>> renewableResourceRequirementCache = new HashMap<JobMode, TObjectIntMap<Resource>>();
    private final Map<JobMode, TObjectIntMap<Resource>> nonRenewableResourceRequirementCache = new HashMap<JobMode, TObjectIntMap<Resource>>();

    private final TIntObjectMap<TObjectIntMap<Resource>> renewableResourceUseInTime;

    private int overused = 0;
    private int idle = 0;

    private final TObjectIntMap<Resource> nonRenewableResourceUsage = new TObjectIntHashMap<Resource>();

    public ResourceUsageTracker(final int horizon) {
        this.renewableResourceUseInTime = new TIntObjectHashMap<TObjectIntMap<Resource>>();
    }

    public void add(final Allocation a) {
        this.addRenewable(a);
        this.addNonRenewable(a);
    }

    private void addNonRenewable(final Allocation a) {
        final TObjectIntMap<Resource> nonRenewables = this.prepareResourceRequirements(a.getJobMode(), false);
        for (final Resource resource : nonRenewables.keySet()) {
            this.updateCachesForAddition(resource, nonRenewables.get(resource), this.nonRenewableResourceUsage);
        }
    }

    private void addRenewable(final Allocation a) {
        final TObjectIntMap<Resource> renewables = this.prepareResourceRequirements(a.getJobMode(), true);
        final int dueDate = a.getDueDate();
        final int startDate = a.getStartDate();
        for (final Resource resource : renewables.keySet()) {
            final int requirement = renewables.get(resource);
            for (int time = startDate; time <= dueDate; time++) {
                TObjectIntMap<Resource> totalUse = this.renewableResourceUseInTime.get(time);
                if (totalUse == null) {
                    totalUse = new TObjectIntHashMap<Resource>(renewables.size());
                    this.renewableResourceUseInTime.put(time, totalUse);
                }
                this.updateCachesForAddition(resource, requirement, totalUse);
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
     * @param needsRenewable
     *            Whether to return only renewables. When false, only
     *            non-renewables will be returned.
     * @return Resource requirements without those useless for the purposes of
     *         this class.
     */
    private TObjectIntMap<Resource> prepareResourceRequirements(final JobMode jobMode, final boolean needsRenewable) {
        TObjectIntMap<Resource> requirements = needsRenewable ? this.renewableResourceRequirementCache.get(jobMode)
                : this.nonRenewableResourceRequirementCache.get(jobMode);
        if (requirements == null) {
            // prepare the data
            final TObjectIntMap<Resource> original = jobMode.getResourceRequirements();
            requirements = new TObjectIntHashMap<Resource>(original.size());
            for (final Resource resource : original.keySet()) {
                final int requirement = original.get(resource);
                if (resource.isRenewable() == needsRenewable && requirement > 0) {
                    requirements.put(resource, requirement);
                }
            }
            // store them in the cache
            if (needsRenewable) {
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
        final TObjectIntMap<Resource> nonRenewables = this.prepareResourceRequirements(a.getJobMode(), false);
        for (final Resource resource : nonRenewables.keySet()) {
            this.updateCachesForRemoval(resource, nonRenewables.get(resource), this.nonRenewableResourceUsage);
        }
    }

    private void removeRenewable(final Allocation a) {
        final TObjectIntMap<Resource> renewables = this.prepareResourceRequirements(a.getJobMode(), true);
        final int dueDate = a.getDueDate();
        final int startDate = a.getStartDate();
        for (final Resource resource : renewables.keySet()) {
            final int requirement = renewables.get(resource);
            for (int time = startDate; time <= dueDate; time++) {
                final TObjectIntMap<Resource> totalUse = this.renewableResourceUseInTime.get(time);
                this.updateCachesForRemoval(resource, requirement, totalUse);
            }
        }
    }

    private void updateCachesForAddition(final Resource r, final int requirement, final TObjectIntMap<Resource> totalUse) {
        final int currentTotalUse = totalUse.get(r);
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

    private void updateCachesForRemoval(final Resource r, final int requirement, final TObjectIntMap<Resource> totalUse) {
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
