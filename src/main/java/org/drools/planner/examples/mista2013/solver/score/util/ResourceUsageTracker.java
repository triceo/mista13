package org.drools.planner.examples.mista2013.solver.score.util;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Resource;

/**
 * Validates feasibility requirements (1), (2) and (3). Counts how many more
 * resources would we need than we have capacity for.
 */
public class ResourceUsageTracker {

    private final TIntObjectMap<TObjectIntMap<Resource>> renewableResourceUseInTime;
    private final TObjectIntMap<Resource> nonRenewableResourceUsage = new TObjectIntHashMap<Resource>();

    private int overused = 0;
    private int idle = 0;

    public ResourceUsageTracker(final int horizon) {
        this.renewableResourceUseInTime = new TIntObjectHashMap<TObjectIntMap<Resource>>();
    }

    public void add(final Allocation a) {
        final int dueDate = a.getDueDate();
        final int startDate = a.getStartDate();
        final TObjectIntMap<Resource> requirements = a.getJobMode().getResourceRequirements();
        for (final Resource resource : requirements.keySet()) {
            final int requirement = requirements.get(resource);
            if (requirement == 0) {
                // doesn't change anything
                continue;
            }
            if (resource.isRenewable()) {
                for (int time = startDate; time <= dueDate; time++) {
                    TObjectIntMap<Resource> totalUse = this.renewableResourceUseInTime.get(time);
                    if (totalUse == null) {
                        totalUse = new TObjectIntHashMap<Resource>(requirements.size());
                        this.renewableResourceUseInTime.put(time, totalUse);
                    }
                    this.updateCachesForAddition(resource, requirement, totalUse);
                }
            } else {
                this.updateCachesForAddition(resource, requirement, this.nonRenewableResourceUsage);
            }
        }
    }

    public int getSumOfIdleResources() {
        return -this.idle;
    }

    public int getSumOfOverusedResources() {
        return this.overused;
    }

    public void remove(final Allocation a) {
        final int dueDate = a.getDueDate();
        final int startDate = a.getStartDate();
        final TObjectIntMap<Resource> requirements = a.getJobMode().getResourceRequirements();
        for (final Resource resource : requirements.keySet()) {
            final int requirement = requirements.get(resource);
            if (requirement == 0) {
                // doesn't change anything
                continue;
            }
            if (resource.isRenewable()) {
                for (int time = startDate; time <= dueDate; time++) {
                    this.updateCachesForRemoval(resource, requirement, this.renewableResourceUseInTime.get(time));
                }
            } else {
                this.updateCachesForRemoval(resource, requirement, this.nonRenewableResourceUsage);
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
