package org.drools.planner.examples.mista2013.solver.score;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.drools.planner.core.score.buildin.hardmediumsoft.DefaultHardMediumSoftScore;
import org.drools.planner.core.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.drools.planner.core.score.director.incremental.AbstractIncrementalScoreCalculator;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.JobMode;
import org.drools.planner.examples.mista2013.domain.Mista2013;
import org.drools.planner.examples.mista2013.domain.ProblemInstance;
import org.drools.planner.examples.mista2013.domain.Project;
import org.drools.planner.examples.mista2013.domain.Resource;

public class Mista2013IncrementalScoreCalculator extends AbstractIncrementalScoreCalculator<Mista2013> {

    private static interface Filter<T> {

        public boolean accept(T what);

    }

    private static final Filter<Resource> GLOBALS_ONLY = new Filter<Resource>() {

        @Override
        public boolean accept(final Resource what) {
            return (what.isGlobal());
        }

    };

    private static final Filter<Resource> LOCAL_RENEWABLES = new Filter<Resource>() {

        @Override
        public boolean accept(final Resource what) {
            return (!what.isGlobal() && what.isRenewable());
        }

    };

    private static final Filter<Resource> LOCAL_NONRENEWABLES = new Filter<Resource>() {

        @Override
        public boolean accept(final Resource what) {
            return !(what.isGlobal() || what.isRenewable());
        }

    };

    /*
     * TODO remove when Planner 6.0-SNAPSHOT fixes constr heur and selectors
     * wrt. planning values
     */
    private static int findInvalidEntityVariableValues(final Allocation a) {
        int total = 0;
        if (!a.isInitialized()) {
            total++;
        } else {
            if (!a.getJobModes().contains(a.getJobMode())) {
                total++;
            }
            if (!a.getStartDates().contains(a.getStartDate())) {
                total++;
            }
        }
        return total * 1000000;
    }

    private ProblemInstance problem = null;

    private final Set<Allocation> allocations = new LinkedHashSet<Allocation>();

    private final Map<Job, Allocation> allocationsPerJob = new LinkedHashMap<Job, Allocation>();

    private final Map<Project, Integer> maxDueDateCache = new HashMap<Project, Integer>();

    /**
     * Validates feasibility requirement (4).
     * 
     * @return How many jobs haven't picked a job mode or start date.
     */
    private int unassignedJobModeCount = 0;

    private int invalidValuesAssignedToEntityVariableCount = 0;

    @Override
    public void afterAllVariablesChanged(final Object entity) {
        this.insert((Allocation) entity);
    }

    @Override
    public void afterEntityAdded(final Object entity) {
        // TODO the maps should probably be adjusted
        this.insert((Allocation) entity);
    }

    @Override
    public void afterEntityRemoved(final Object entity) {
        // Do nothing
        // TODO the maps should probably be adjusted
    }

    @Override
    public void afterVariableChanged(final Object entity, final String variableName) {
        this.insert((Allocation) entity);
    }

    @Override
    public void beforeAllVariablesChanged(final Object entity) {
        this.retract((Allocation) entity);
    }

    @Override
    public void beforeEntityAdded(final Object entity) {
        // Do nothing
    }

    @Override
    public void beforeEntityRemoved(final Object entity) {
        this.retract((Allocation) entity);
    }

    @Override
    public void beforeVariableChanged(final Object entity, final String variableName) {
        this.retract((Allocation) entity);
    }

    @Override
    public HardMediumSoftScore calculateScore() {
        final int plannerPlanningValueWorkaround = this.invalidValuesAssignedToEntityVariableCount;
        final int brokenHard1 = this.getOverutilizedLocalRenewableResourcesCount();
        final int brokenHard2 = this.getOverutilizedLocalNonRenewableResourcesCount();
        final int brokenHard3 = this.getOverutilizedGlobalResourcesCount();
        final int brokenHard4 = this.unassignedJobModeCount;
        final int brokenHard5 = this.getHorizonOverrunCount();
        // FIXME does constraint 6 need to be validated?
        final int brokenHard7 = this.getBrokenPrecedenceRelationsCount();
        final int brokenTotal = plannerPlanningValueWorkaround + brokenHard1 + brokenHard2 + brokenHard3 + brokenHard4
                + brokenHard5 + brokenHard7;
        // FIXME are we interested in projects being actually ahead?
        final int medium = Math.max(0, this.getTotalProjectDelay());
        final int soft = this.getTotalMakespan();
        return DefaultHardMediumSoftScore.valueOf(-brokenTotal, -medium, -soft);
    }

    /**
     * Find maximum due date for any of the activities in a problem instance.
     * This date (since we ignore project sink) is effectively equivalent to the
     * end of last of the projects.
     * 
     * @param solution
     * @param p
     * @return
     */
    private int findMaxDueDate() {
        int maxDueDate = Integer.MIN_VALUE;
        for (final Project p : this.problem.getProjects()) {
            maxDueDate = Math.max(maxDueDate, this.findMaxDueDate(p));
        }
        return maxDueDate;
    }

    /**
     * Find maximum due date for any of the activities in a given project. This
     * date (since we ignore project sink) is effectively equivalent to the end
     * of the project. This is very heavily used throughout the calculator,
     * hence it includes result cache.
     * 
     * @param solution
     * @param p
     * @return
     */
    private int findMaxDueDate(final Project p) {
        if (!this.maxDueDateCache.containsKey(p)) {
            int maxDueDate = Integer.MIN_VALUE;
            for (final Job j : p.getJobs()) {
                if (j.isSource() || j.isSink()) {
                    continue;
                }
                final Allocation a = this.allocationsPerJob.get(j);
                if (!a.isInitialized()) {
                    continue;
                }
                maxDueDate = Math.max(maxDueDate, a.getStartDate() + a.getJobMode().getDuration());
            }
            this.maxDueDateCache.put(p, maxDueDate);
        }
        return this.maxDueDateCache.get(p);
    }

    private int findMinReleaseDate() {
        int minReleaseDate = Integer.MAX_VALUE;
        for (final Project p : this.problem.getProjects()) {
            minReleaseDate = Math.min(minReleaseDate, p.getReleaseDate());
        }
        return minReleaseDate;
    }

    /**
     * Validates feasibility requirement (7).
     * 
     * @return How many precedence relations are broken
     */
    private int getBrokenPrecedenceRelationsCount() {
        int total = 0;
        for (final Allocation currentJobAllocation : this.allocations) {
            if (!currentJobAllocation.isInitialized()) {
                continue;
            }
            final Job currentJob = currentJobAllocation.getJob();
            final Project p = currentJob.getParentProject();
            final JobMode currentMode = currentJobAllocation.getJobMode();
            if (currentJobAllocation.getStartDate() < p.getReleaseDate()) {
                // make sure we never start before we're allowed to
                total++;
            }
            final int currentDoneBy = currentJobAllocation.getStartDate() + currentMode.getDuration();
            for (final Job succeedingJob : currentJob.getSuccessors()) {
                if (succeedingJob.isSink()) {
                    continue;
                }
                // find its successors
                final Allocation succeedingJobAllocation = this.allocationsPerJob.get(succeedingJob);
                if (!succeedingJobAllocation.isInitialized()) {
                    continue;
                }
                final Integer nextStartedAt = succeedingJobAllocation.getStartDate();
                if (nextStartedAt <= currentDoneBy) {
                    /*
                     * successor starts before its predecessor ends
                     */
                    total++;
                }
            }
        }
        return total * 1000;
    }

    /**
     * Validates feasibility requirement (5).
     * 
     * FIXME will need to be re-checked; what's the
     * "upper bound on the time horizon of scheduling problem"? this method
     * assumes that it is the maximum of horizons of all projects in a problem
     * instance.
     * 
     * @return How many projects have overrun their horizon.
     */
    private int getHorizonOverrunCount() {
        // find what we think is the upper bound
        int total = 0;
        int upperBound = Integer.MIN_VALUE;
        for (final Project p : this.problem.getProjects()) {
            upperBound = Math.max(upperBound, p.getHorizon());
        }
        // and now find out the number of projects that went over it
        for (final Project p : this.problem.getProjects()) {
            if (this.findMaxDueDate(p) > upperBound) {
                total++;
            }
        }
        return total;
    }

    private int getMakespan(final Project p) {
        return this.findMaxDueDate(p) - p.getReleaseDate();
    }

    /**
     * Validates feasibility requirement (3).
     * 
     * @return How many more global resources would we need than we have
     *         capacity for.
     */
    private int getOverutilizedGlobalResourcesCount() {
        return this.getOverutilizedRenewableResourceCount(Mista2013IncrementalScoreCalculator.GLOBALS_ONLY);
    }

    /**
     * Validates feasibility requirement (2).
     * 
     * @return How many more local non-renewable resources would we need than we
     *         have capacity for.
     */
    private int getOverutilizedLocalNonRenewableResourcesCount() {
        int total = 0;
        final Map<Resource, Integer> totalAllocations = new HashMap<Resource, Integer>();
        // sum up all the resource consumptions that we track
        for (final Allocation a : this.allocations) {
            if (!a.isInitialized()) {
                continue;
            }
            final JobMode jm = a.getJobMode();
            for (final Resource r : jm.getResources()) {
                if (!Mista2013IncrementalScoreCalculator.LOCAL_NONRENEWABLES.accept(r)) {
                    // not the type of resource we're interested in
                    continue;
                }
                final int resourceRequirement = jm.getResourceRequirement(r);
                if (resourceRequirement == 0) {
                    // doesn't change anything
                    continue;
                }
                Integer totalAllocation = totalAllocations.get(r);
                totalAllocation = resourceRequirement + ((totalAllocation == null) ? 0 : totalAllocation);
                totalAllocations.put(r, totalAllocation);
            }
        }
        // and now find out how many more we have than we should
        for (final Map.Entry<Resource, Integer> entry : totalAllocations.entrySet()) {
            final Resource r = entry.getKey();
            final int allocation = entry.getValue();
            total += Math.max(0, allocation - r.getCapacity());
        }
        return total;
    }

    /**
     * Validates feasibility requirement (1).
     * 
     * @return How many more local renewable resources would we need than we
     *         have capacity for.
     */
    private int getOverutilizedLocalRenewableResourcesCount() {
        return this.getOverutilizedRenewableResourceCount(Mista2013IncrementalScoreCalculator.LOCAL_RENEWABLES);
    }

    private int getOverutilizedRenewableResourceCount(final Filter<Resource> filter) {
        int total = 0;
        for (int time = this.findMinReleaseDate(); time <= this.findMaxDueDate(); time++) {
            final Map<Resource, Integer> totalAllocations = new HashMap<Resource, Integer>();
            for (final Allocation a : this.allocations) {
                // activity not yet initialized
                if (!a.isInitialized()) {
                    continue;
                }
                // activity not running at the time
                final JobMode jm = a.getJobMode();
                final int dueDate = a.getStartDate() + jm.getDuration();
                if (a.getStartDate() > time || dueDate < time) {
                    continue;
                }
                for (final Resource r : jm.getResources()) {
                    if (!filter.accept(r)) {
                        // not the type of resource we're interested in
                        continue;
                    }
                    final int resourceRequirement = jm.getResourceRequirement(r);
                    if (resourceRequirement == 0) {
                        // doesn't change anything
                        continue;
                    }
                    Integer totalAllocation = totalAllocations.get(r);
                    totalAllocation = resourceRequirement + ((totalAllocation == null) ? 0 : totalAllocation);
                    totalAllocations.put(r, totalAllocation);
                }
            }
            for (final Map.Entry<Resource, Integer> entry : totalAllocations.entrySet()) {
                final Resource r = entry.getKey();
                final int allocation = entry.getValue();
                total += Math.max(0, allocation - r.getCapacity());
            }
        }
        return total;
    }

    private int getProjectDelay(final Project p) {
        return this.getMakespan(p) - p.getCriticalPathDuration();
    }

    private int getTotalMakespan() {
        return this.findMaxDueDate() - this.findMinReleaseDate();
    }

    private int getTotalProjectDelay() {
        int total = 0;
        for (final Project p : this.problem.getProjects()) {
            total += this.getProjectDelay(p);
        }
        return total;
    }

    private void insert(final Allocation entity) {
        this.maxDueDateCache.remove(entity.getJob().getParentProject());
        this.allocations.add(entity);
        this.allocationsPerJob.put(entity.getJob(), entity);
        if (!entity.isInitialized()) {
            this.unassignedJobModeCount += 1;
        }
        this.invalidValuesAssignedToEntityVariableCount += Mista2013IncrementalScoreCalculator
                .findInvalidEntityVariableValues(entity);
    }

    @Override
    public void resetWorkingSolution(final Mista2013 workingSolution) {
        this.unassignedJobModeCount = 0;
        this.invalidValuesAssignedToEntityVariableCount = 0;
        this.allocations.clear();
        this.allocationsPerJob.clear();
        this.maxDueDateCache.clear();
        // change to the new problem
        this.problem = workingSolution.getProblem();
        for (final Allocation a : workingSolution.getAllocations()) {
            this.insert(a);
        }
    }

    private void retract(final Allocation entity) {
        this.maxDueDateCache.remove(entity.getJob().getParentProject());
        this.allocations.remove(entity);
        this.allocationsPerJob.remove(entity);
        if (!entity.isInitialized()) {
            this.unassignedJobModeCount -= 1;
        }
        this.invalidValuesAssignedToEntityVariableCount -= Mista2013IncrementalScoreCalculator
                .findInvalidEntityVariableValues(entity);
    }
}
