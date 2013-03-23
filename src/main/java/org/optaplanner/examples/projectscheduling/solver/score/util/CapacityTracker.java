package org.optaplanner.examples.projectscheduling.solver.score.util;

import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.ProblemInstance;
import org.optaplanner.examples.projectscheduling.domain.Resource;
import org.optaplanner.examples.projectscheduling.domain.ResourceRequirement;

/**
 * Validates feasibility requirements (1), (2) and (3). Counts how many more
 * resources would we need than we have capacity for.
 */
public class CapacityTracker {

    private static class ResourceDecreaser extends ResourceManager {

        public ResourceDecreaser(final Allocation a, final CapacityTracker instance) {
            super(a, instance);
        }

        @Override
        public int recalculateRequirements(final int currentTotalUse, final int requirement, final int capacity) {
            final int newTotalUse = currentTotalUse - requirement;
            if (newTotalUse > capacity) {
                // remove the decrease over the already overreached capacity
                this.overusedDifference -= requirement;
            } else if (currentTotalUse > capacity) {
                // the capacity is newly idle
                this.overusedDifference -= currentTotalUse - capacity;
                this.idleDifference += capacity - newTotalUse;
            } else {
                // the capacity remains idle
                this.idleDifference += requirement;
            }
            return newTotalUse;
        }

    }

    private static class ResourceIncreaser extends ResourceManager {

        public ResourceIncreaser(final Allocation a, final CapacityTracker instance) {
            super(a, instance);
        }

        @Override
        public int recalculateRequirements(final int currentTotalUse, final int requirement, final int capacity) {
            final int newTotalUse = requirement + currentTotalUse;
            if (currentTotalUse > capacity) {
                // add the increase over the already overreached capacity
                this.overusedDifference += requirement;
            } else if (newTotalUse > capacity) {
                // the capacity is newly overreached
                this.overusedDifference += newTotalUse - capacity;
                this.idleDifference -= capacity - currentTotalUse;
            } else {
                // the capacity remains idle
                this.idleDifference -= requirement;
            }
            return newTotalUse;
        }

    }

    private static abstract class ResourceManager {

        protected int overusedDifference;
        protected int idleDifference;

        private final int startDate;
        private final int dueDate;
        private final CapacityTracker instance;

        public ResourceManager(final Allocation a, final CapacityTracker instance) {
            this.startDate = a.getStartDate();
            this.dueDate = a.getDueDate();
            this.instance = instance;
        }

        public boolean execute(final Resource resource, final int requirement) {
            final int resourceId = resource.getUniqueId();
            final int resourceCapacity = resource.getCapacity();
            if (resource.isRenewable()) {
                for (int time = this.startDate; time++ <= this.dueDate;) {
                    this.processRequirementChange(resourceId, resourceCapacity, requirement, this.getRequirementsInTime(time));
                }
            } else {
                this.processRequirementChange(resourceId, resourceCapacity, requirement, this.instance.nonRenewableResourceUsage);
            }
            return true;
        }

        public int getIdleDifference() {
            return this.idleDifference;
        }

        public int getOverusedDifference() {
            return this.overusedDifference;
        }

        private int[] getRequirementsInTime(final int time) {
            final int[] totalUse = this.instance.renewableResourceUseInTime[time];
            if (totalUse == null) {
                /*
                 * this array needs to have room for all resources in the 
                 * problem, but will only have occupied a few of them. however, 
                 * this is drammatically faster than having a properly sized 
                 * collection on which we put()/get() all the time.
                 */
                return this.instance.renewableResourceUseInTime[time] = new int[this.instance.maxResourceId + 1];
            } else {
                return totalUse;
            }
        }

        private void processRequirementChange(final int resourceId, final int resourceCapacity, final int newRequirement,
                final int[] overallRequirements) {
            overallRequirements[resourceId] = this.recalculateRequirements(overallRequirements[resourceId], newRequirement, resourceCapacity);
        }

        protected abstract int recalculateRequirements(int currentTotalUse, int resourceRequirement,
                int resourceCapacity);

    }

    /**
     * Key is the time at which the resource use is registered. Value has the
     * same meaning as {@link #nonRenewableResourceUsage}, only for renewable
     * resources.
     */
    private final int[][] renewableResourceUseInTime;

    /**
     * Key is the {@link Resource#getUniqueId()} of a resource and value is the
     * resource usage currently registered.
     */
    private final int[] nonRenewableResourceUsage;

    private final int maxResourceId;

    private int overused = 0;

    private int idle = 0;

    public CapacityTracker(final ProblemInstance problem) {
        this.maxResourceId = problem.getMaxResourceId();
        this.renewableResourceUseInTime = new int[problem.getMaxAllowedDueDate() + 1][];
        this.nonRenewableResourceUsage = new int[this.maxResourceId + 1];
    }

    public void add(final Allocation a) {
        this.process(a, new ResourceIncreaser(a, this));
    }

    public int getIdleCapacity() {
        return -this.idle;
    }

    public int getOverusedCapacity() {
        return this.overused;
    }

    private void process(final Allocation a, final ResourceManager rm) {
        for (final ResourceRequirement rr : a.getJobMode().getResourceRequirements()) {
            rm.execute(rr.getResource(), rr.getRequirement());
        }
        this.idle += rm.getIdleDifference();
        this.overused += rm.getOverusedDifference();
    }

    public void remove(final Allocation a) {
        this.process(a, new ResourceDecreaser(a, this));
    }

}
