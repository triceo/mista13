package org.optaplanner.examples.projectscheduling.solver.score.util;

import java.util.Arrays;

import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.ProblemInstance;
import org.optaplanner.examples.projectscheduling.domain.Resource;
import org.optaplanner.examples.projectscheduling.domain.ResourceRequirement;

/**
 * Validates feasibility requirements (1), (2) and (3). Counts how many more
 * resources would we need than we have capacity for.
 */
public class CapacityTracker {

    private final static int DEFAULT_TIME_COUNT = 100;

    /**
     * Key is the time at which the resource use is registered. Value has the
     * same meaning as {@link #nonRenewableResourceConsumption}, only for renewable
     * resources.
     */
    private int[][] renewableResourceConsumptionInTime;

    /**
     * Key is the {@link Resource#getUniqueId()} of a resource and value is the
     * resource usage currently registered.
     */
    private final int[] nonRenewableResourceConsumption;

    private final int maxResourceId;

    private int overused = 0;

    public CapacityTracker(final ProblemInstance problem) {
        this.maxResourceId = problem.getMaxRenewableResourceId();
        this.renewableResourceConsumptionInTime = new int[CapacityTracker.DEFAULT_TIME_COUNT][];
        this.nonRenewableResourceConsumption = new int[problem.getMaxNonRenewableResourceId() + 1];
    }

    public void add(final Allocation a) {
        this.process(a, true);
    }

    private void resizeArray(final int newMinimumLength) {
        this.renewableResourceConsumptionInTime = Arrays.copyOf(this.renewableResourceConsumptionInTime, (int) (newMinimumLength * 1.1));
    }

    /**
     * Retrieve resource consumption existing at a given time.
     * 
     * @param time
     *            Time in question, 0 is the beginning of the world.
     * @return Key is the {@link Resource#getUniqueId()}, value is the consumption.
     */
    private int[] getConsumptionInTime(final int time) {
        final int length = this.renewableResourceConsumptionInTime.length;
        if (time >= length) {
            this.resizeArray(time + 1);
        }
        final int[] totalUse = this.renewableResourceConsumptionInTime[time];
        if (totalUse == null) {
            /*
             * this array needs to have room for all resources in the
             * problem, but will only have occupied a few of them. however,
             * this is dramatically faster than having a properly sized
             * collection on which we put()/get() all the time.
             */
            return this.renewableResourceConsumptionInTime[time] = new int[this.maxResourceId + 1];
        } else {
            return totalUse;
        }
    }

    public int getOverusedCapacity() {
        return this.overused;
    }

    private int recalculateConsumption(final int consumption, final int newRequirement, final int resourceCapacity, final boolean isAdding) {
        return isAdding ? 
                this.recalculateConsumptionOnAddition(consumption, newRequirement, resourceCapacity) :
                    this.recalculateConsumptionOnRemoval(consumption, newRequirement, resourceCapacity);
    }

    /**
     * Update capacity information based on the addition/removal of the
     * supplied allocation.
     * 
     * @param a
     *            The allocation in question.
     * @param isAdding
     *            Whether or not the allocation is being added or removed.
     */
    private void process(final Allocation a, final boolean isAdding) {
        final int startDate = a.getStartDate();
        final int dueDate = a.getDueDate();
        for (final ResourceRequirement rr : a.getExecutionMode().getResourceRequirements()) {
            this.processRequirement(startDate, dueDate, rr, isAdding);
        }
    }

    private void processRequirement(final int startDate, final int dueDate, final ResourceRequirement rr, final boolean isAdding) {
        final Resource resource = rr.getResource();
        final int requirement = rr.getRequirement();
        final int resourceId = resource.getUniqueId();
        final int resourceCapacity = resource.getCapacity();
        if (resource.isRenewable()) {
            for (int time = startDate; time++ <= dueDate;) {
                this.modifyConsumption(resourceId, requirement, resourceCapacity, this.getConsumptionInTime(time), isAdding);
            }
        } else {
            this.modifyConsumption(resourceId, requirement, resourceCapacity, this.nonRenewableResourceConsumption, isAdding);
        }
    }

    private void modifyConsumption(final int resourceId, final int requirement, final int resourceCapacity, final int[] consumptionInTime, final boolean isAdding) {
        consumptionInTime[resourceId] = this.recalculateConsumption(consumptionInTime[resourceId], requirement, resourceCapacity, isAdding);
    }

    private int recalculateConsumptionOnAddition(final int currentTotalUse, final int requirement, final int capacity) {
        final int newTotalUse = requirement + currentTotalUse;
        if (currentTotalUse > capacity) {
            // add the increase over the already overreached capacity
            this.overused += requirement;
        } else if (newTotalUse > capacity) {
            // the capacity is newly overreached
            this.overused += newTotalUse - capacity;
        } else {
            // the capacity remains idle
        }
        return newTotalUse;
    }

    private int recalculateConsumptionOnRemoval(final int currentTotalUse, final int requirement, final int capacity) {
        final int newTotalUse = currentTotalUse - requirement;
        if (newTotalUse > capacity) {
            // remove the decrease over the already overreached capacity
            this.overused -= requirement;
        } else if (currentTotalUse > capacity) {
            // the capacity is newly idle
            this.overused -= currentTotalUse - capacity;
        } else {
            // the capacity remains idle
        }
        return newTotalUse;
    }

    public void remove(final Allocation a) {
        this.process(a, false);
    }

}
