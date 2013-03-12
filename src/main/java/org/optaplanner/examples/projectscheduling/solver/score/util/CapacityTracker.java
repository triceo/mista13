package org.optaplanner.examples.projectscheduling.solver.score.util;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TObjectIntProcedure;

import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Resource;

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
            if (newTotalUse == 0) {
                this.totalCapacityDifference -= capacity;
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
            if (currentTotalUse == 0) {
                this.totalCapacityDifference += capacity;
            }
            return newTotalUse;
        }

    }

    private static abstract class ResourceManager implements TObjectIntProcedure<Resource> {

        protected int overusedDifference;
        protected int idleDifference;
        protected int totalCapacityDifference;

        private final int startDate;
        private final int dueDate;
        private final CapacityTracker instance;

        public ResourceManager(final Allocation a, final CapacityTracker instance) {
            this.startDate = a.getStartDate();
            this.dueDate = a.getDueDate();
            this.instance = instance;
        }

        @Override
        public boolean execute(final Resource resource, final int requirement) {
            if (resource.isRenewable()) {
                for (int time = this.startDate; time++ <= this.dueDate;) {
                    this.processRequirementChange(resource, requirement, this.getRequirementsInTime(time));
                }
            } else {
                this.processRequirementChange(resource, requirement, this.instance.nonRenewableResourceUsage);
            }
            return true;
        }

        public int getIdleDifference() {
            return this.idleDifference;
        }

        public int getOverusedDifference() {
            return this.overusedDifference;
        }

        private TIntIntMap getRequirementsInTime(final int time) {
            TIntIntMap totalUse = this.instance.renewableResourceUseInTime.get(time);
            if (totalUse == null) {
                totalUse = new TIntIntHashMap();
                this.instance.renewableResourceUseInTime.put(time, totalUse);
            }
            return totalUse;
        }

        public int getTotalCapacityDifference() {
            return this.totalCapacityDifference;
        }

        /*
         * FIXME better algo or data type??? the map is a huge perf bottleneck.
         */
        private void processRequirementChange(final Resource r, final int newRequirement,
                final TIntIntMap overallRequirements) {
            final int resourceId = r.getUniqueId();
            overallRequirements.put(resourceId,
                    this.recalculateRequirements(overallRequirements.get(resourceId), newRequirement, r.getCapacity()));
        }

        protected abstract int recalculateRequirements(int currentTotalUse, int resourceRequirement,
                int resourceCapacity);

    }

    /**
     * Key is the time at which the resource use is registered. Value has the
     * same meaning as {@link #nonRenewableResourceUsage}, only for renewable
     * resources.
     */
    private final TIntObjectMap<TIntIntMap> renewableResourceUseInTime;

    /**
     * Key is the {@link Resource#getUniqueId()} of a resource and value is the
     * resource usage currently registered.
     */
    private final TIntIntMap nonRenewableResourceUsage = new TIntIntHashMap();

    private int overused = 0;

    private int total = 0;

    private int idle = 0;

    public CapacityTracker(final int horizon) {
        this.renewableResourceUseInTime = new TIntObjectHashMap<TIntIntMap>(horizon);
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

    public int getTotalCapacity() {
        return this.total;
    }

    private void process(final Allocation a, final ResourceManager rm) {
        a.getJobMode().getResourceRequirements().forEachEntry(rm);
        this.idle += rm.getIdleDifference();
        this.overused += rm.getOverusedDifference();
        this.total += rm.getTotalCapacityDifference();
    }

    public void remove(final Allocation a) {
        this.process(a, new ResourceDecreaser(a, this));
    }

}
