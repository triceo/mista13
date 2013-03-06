package org.drools.planner.examples.mista2013.solver.score.util;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.procedure.TObjectIntProcedure;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Resource;

/**
 * Validates feasibility requirements (1), (2) and (3). Counts how many more
 * resources would we need than we have capacity for.
 */
public class ResourceUsageTracker {

    private static class ResourceDecreaser extends ResourceManager {

        public ResourceDecreaser(final Allocation a, final ResourceUsageTracker instance) {
            super(a, instance);
        }

        @Override
        public void updateCaches(final Resource r, final int requirement, final TObjectIntMap<Resource> totalUse) {
            final int currentTotalUse = totalUse.get(r);
            final int newTotalUse = currentTotalUse - requirement;
            totalUse.put(r, newTotalUse);
            final int capacity = r.getCapacity();
            if (newTotalUse > capacity) {
                // remove the decrease over the already overreached capacity
                this.overusedDifference -= currentTotalUse - newTotalUse;
            } else if (currentTotalUse > capacity) {
                // the capacity is newly idle
                this.overusedDifference -= currentTotalUse - capacity;
                this.idleDifference += capacity - newTotalUse;
            } else {
                // the capacity remains idle
                this.idleDifference += requirement;
            }
        }

    }

    private static class ResourceIncreaser extends ResourceManager {

        public ResourceIncreaser(final Allocation a, final ResourceUsageTracker instance) {
            super(a, instance);
        }

        @Override
        public void updateCaches(final Resource r, final int requirement, final TObjectIntMap<Resource> totalUse) {
            final int currentTotalUse = totalUse.get(r);
            final int newTotalUse = requirement + currentTotalUse;
            totalUse.put(r, newTotalUse);
            final int capacity = r.getCapacity();
            if (currentTotalUse > capacity) {
                // add the increase over the already overreached capacity
                this.overusedDifference += newTotalUse - currentTotalUse;
            } else if (newTotalUse > capacity) {
                // the capacity is newly overreached
                this.overusedDifference += newTotalUse - capacity;
                this.idleDifference -= capacity - currentTotalUse;
            } else {
                // the capacity remains idle
                this.idleDifference -= requirement;
            }
        }

    }

    private static abstract class ResourceManager implements TObjectIntProcedure<Resource> {

        protected int overusedDifference;

        protected int idleDifference;

        private final int totalRequirements;
        private final int startDate;

        private final int dueDate;
        private final ResourceUsageTracker instance;

        public ResourceManager(final Allocation a, final ResourceUsageTracker instance) {
            this.startDate = a.getStartDate();
            this.dueDate = a.getDueDate();
            this.instance = instance;
            this.totalRequirements = a.getJobMode().getResourceRequirements().size();
        }

        @Override
        public boolean execute(final Resource resource, final int requirement) {
            if (resource.isRenewable()) {
                for (int time = this.startDate; time <= this.dueDate; time++) {
                    this.updateCaches(resource, requirement, this.getRequirementsInTime(time));
                }
            } else {
                this.updateCaches(resource, requirement, this.instance.nonRenewableResourceUsage);
            }
            return true;
        }

        public int getIdleDifference() {
            return this.idleDifference;
        }

        public int getOverusedDifference() {
            return this.overusedDifference;
        }

        private TObjectIntMap<Resource> getRequirementsInTime(final int time) {
            TObjectIntMap<Resource> totalUse = this.instance.renewableResourceUseInTime.get(time);
            if (totalUse == null) {
                totalUse = new TObjectIntHashMap<Resource>(this.totalRequirements);
                this.instance.renewableResourceUseInTime.put(time, totalUse);
            }
            return totalUse;
        }

        public abstract void updateCaches(Resource r, int requirement, TObjectIntMap<Resource> requirements);

    }

    private final TIntObjectMap<TObjectIntMap<Resource>> renewableResourceUseInTime;

    private final TObjectIntMap<Resource> nonRenewableResourceUsage = new TObjectIntHashMap<Resource>();

    private int overused = 0;

    private int idle = 0;

    public ResourceUsageTracker(final int horizon) {
        this.renewableResourceUseInTime = new TIntObjectHashMap<TObjectIntMap<Resource>>(horizon);
    }

    public void add(final Allocation a) {
        this.process(a, new ResourceIncreaser(a, this));
    }

    public int getSumOfIdleResources() {
        return -this.idle;
    }

    public int getSumOfOverusedResources() {
        return this.overused;
    }

    private void process(final Allocation a, final ResourceManager rm) {
        a.getJobMode().getResourceRequirements().forEachEntry(rm);
        this.idle += rm.getIdleDifference();
        this.overused += rm.getOverusedDifference();
    }

    public void remove(final Allocation a) {
        this.process(a, new ResourceDecreaser(a, this));
    }

}
