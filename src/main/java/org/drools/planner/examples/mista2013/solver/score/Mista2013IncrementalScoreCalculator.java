package org.drools.planner.examples.mista2013.solver.score;

import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * Find maximum due date for any of the activities in a problem instance.
     * This date (since we ignore project sink) is effectively equivalent to the
     * end of last of the projects.
     * 
     * @return
     */
    private static int findMaxDueDate(final ProblemInstance problem, final Map<Project, Integer> maxDueDatesPerProject) {
        int maxDueDate = Integer.MIN_VALUE;
        for (final Project p : problem.getProjects()) {
            maxDueDate = Math.max(maxDueDate, maxDueDatesPerProject.get(p));
        }
        return maxDueDate;
    }

    /**
     * Find maximum due date for any of the activities in a given project. This
     * date (since we ignore project sink) is effectively equivalent to the end
     * of the project. This is very heavily used throughout the calculator,
     * hence it includes result cache.
     * 
     * @return
     */
    private static int findMaxDueDate(final Project p, final Map<Job, Allocation> allocations) {
        int maxDueDate = Integer.MIN_VALUE;
        for (final Job j : p.getJobs()) {
            if (j.isSource() || j.isSink()) {
                continue;
            }
            final Allocation a = allocations.get(j);
            if (a == null || !a.isInitialized()) {
                continue;
            }
            maxDueDate = Math.max(maxDueDate, a.getDueDate());
        }
        return maxDueDate;
    }

    private static int findMinReleaseDate(final ProblemInstance instance) {
        int minReleaseDate = Integer.MAX_VALUE;
        for (final Project p : instance.getProjects()) {
            minReleaseDate = Math.min(minReleaseDate, p.getReleaseDate());
        }
        return minReleaseDate;
    }

    private ProblemInstance problem = null;

    private final Set<Allocation> allocations = new LinkedHashSet<Allocation>();

    private final Map<Job, Allocation> allocationsPerJob = new LinkedHashMap<Job, Allocation>();

    /**
     * Validates feasibility requirement (4).
     * 
     * @return How many jobs haven't picked a job mode or start date.
     */
    private int unassignedJobModeCount = 0;

    private int invalidValuesAssignedToEntityVariableCount = 0;

    /**
     * Cached minimal release date for all the problem instance's projects. This
     * only changes when the problem instance changes.
     */
    private int minReleaseDate = 0;

    private int upperBound = Integer.MIN_VALUE;

    private int maxDueDateGlobal = Integer.MIN_VALUE;

    private final Map<Project, Integer> maxDueDatesPerProject = new HashMap<Project, Integer>();

    @Override
    public void afterAllVariablesChanged(final Object entity) {
        this.insert((Allocation) entity);
    }

    @Override
    public void afterEntityAdded(final Object entity) {
        this.insert((Allocation) entity);
    }

    @Override
    public void afterEntityRemoved(final Object entity) {
        // Do nothing
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
        final int brokenHard1and3 = this.getOverutilizedRenewableResourcesCount();
        final int brokenHard2 = this.getOverutilizedNonRenewableResourcesCount();
        final int brokenHard4 = this.unassignedJobModeCount;
        // FIXME does constraint 6 need to be validated?
        final int brokenHard7 = this.getBrokenPrecedenceRelationsCount();
        /*
         * the following vars are always recalculated; but they come from cached
         * values, so it brings near zero overhead.
         */
        final int brokenHard5 = this.getHorizonOverrunCount();
        final int medium = this.getTotalProjectDelay();
        final int soft = this.getTotalMakespan();
        final int brokenTotal = plannerPlanningValueWorkaround + brokenHard1and3 + brokenHard2 + brokenHard4
                + brokenHard5 + brokenHard7;
        return DefaultHardMediumSoftScore.valueOf(-brokenTotal, -medium, -soft);
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
            if (currentJobAllocation.getStartDate() < p.getReleaseDate()) {
                // make sure we never start before we're allowed to
                total++;
            }
            final int currentDoneBy = currentJobAllocation.getDueDate();
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
     * @return How many projects have overrun their horizon.
     */
    private int getHorizonOverrunCount() {
        // find what we think is the upper bound
        int total = 0;
        // and now find out the number of projects that went over it
        for (final Project p : this.problem.getProjects()) {
            if (this.maxDueDatesPerProject.get(p) > this.upperBound) {
                total++;
            }
        }
        return total;
    }

    private int getMakespan(final Project p) {
        return this.maxDueDatesPerProject.get(p) - p.getReleaseDate();
    }

    /**
     * Validates feasibility requirement (2).
     * 
     * @return How many more non-renewable resources would we need than we have
     *         capacity for.
     */
    private int getOverutilizedNonRenewableResourcesCount() {
        int total = 0;
        final Map<Resource, Integer> totalAllocations = new HashMap<Resource, Integer>();
        // sum up all the resource consumptions that we track
        for (final Allocation a : this.allocations) {
            if (!a.isInitialized()) {
                continue;
            }
            final JobMode jm = a.getJobMode();
            for (final Resource r : jm.getResources()) {
                if (r.isRenewable()) {
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
     * Validates feasibility requirements (1) and (3).
     * 
     * @return How many more local and global non-renewable resources would we
     *         need than we have capacity for.
     */
    private int getOverutilizedRenewableResourcesCount() {
        int total = 0;
        // don't check for initialized entities in the inner cycle
        final Collection<Allocation> initializedEntities = new ArrayList<Allocation>();
        for (final Allocation a : this.allocations) {
            if (!a.isInitialized()) {
                continue;
            }
            initializedEntities.add(a);
        }
        // terminate early
        if (initializedEntities.size() == 0) {
            return 0;
        }
        final int dueDate = this.maxDueDateGlobal;
        for (int time = this.minReleaseDate; time <= dueDate; time++) {
            final Map<Resource, Integer> totalAllocations = new HashMap<Resource, Integer>();
            for (final Allocation a : initializedEntities) {
                // activity not running at the time
                if (a.getStartDate() > time || a.getDueDate() < time) {
                    continue;
                }
                final JobMode jm = a.getJobMode();
                for (final Resource r : jm.getResources()) {
                    if (!r.isRenewable()) {
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
        return this.maxDueDateGlobal - this.minReleaseDate;
    }

    private int getTotalProjectDelay() {
        int total = 0;
        for (final Project p : this.problem.getProjects()) {
            total += this.getProjectDelay(p);
        }
        return total;
    }

    private void insert(final Allocation entity) {
        this.allocations.add(entity);
        this.allocationsPerJob.put(entity.getJob(), entity);
        this.invalidValuesAssignedToEntityVariableCount += Mista2013IncrementalScoreCalculator
                .findInvalidEntityVariableValues(entity);
        if (!entity.isInitialized()) {
            this.unassignedJobModeCount += 1;
            return;
        }
        /*
         * following operations can not be performed on uninitialized entities
         */
        // find new max due dates
        final int newDueDate = entity.getDueDate();
        final Project currentProject = entity.getJob().getParentProject();
        final int currentProjectMaxDueDate = this.maxDueDatesPerProject.get(currentProject);
        if (newDueDate > currentProjectMaxDueDate) {
            this.maxDueDatesPerProject.put(currentProject, newDueDate);
            if (newDueDate > this.maxDueDateGlobal) {
                this.maxDueDateGlobal = newDueDate;
            }
        }
    }

    @Override
    public void resetWorkingSolution(final Mista2013 workingSolution) {
        this.unassignedJobModeCount = 0;
        this.invalidValuesAssignedToEntityVariableCount = 0;
        this.allocations.clear();
        this.allocationsPerJob.clear();
        // change to the new problem
        this.problem = workingSolution.getProblem();
        this.maxDueDateGlobal = Integer.MIN_VALUE;
        for (final Project p : this.problem.getProjects()) {
            this.maxDueDatesPerProject.put(p, this.maxDueDateGlobal);
        }
        /*
         * FIXME what's "upper bound on the time horizon of scheduling problem"?
         * here we assume that it is the maximum of horizons of all projects in
         * a problem instance.
         */
        this.upperBound = Integer.MIN_VALUE;
        for (final Project p : this.problem.getProjects()) {
            this.upperBound = Math.max(this.upperBound, p.getHorizon());
        }
        this.minReleaseDate = Mista2013IncrementalScoreCalculator.findMinReleaseDate(this.problem);
        // insert new entities
        for (final Allocation a : workingSolution.getAllocations()) {
            this.insert(a);
        }
    }

    private void retract(final Allocation entity) {
        this.allocations.remove(entity);
        this.allocationsPerJob.remove(entity.getJob());
        this.invalidValuesAssignedToEntityVariableCount -= Mista2013IncrementalScoreCalculator
                .findInvalidEntityVariableValues(entity);
        if (!entity.isInitialized()) {
            this.unassignedJobModeCount -= 1;
            return;
        }
        /*
         * following operations can not be performed on uninitialized entities
         */
        // find new due dates
        final int currentDueDate = entity.getDueDate();
        final Project currentProject = entity.getJob().getParentProject();
        if (currentDueDate == this.maxDueDatesPerProject.get(currentProject)) {
            this.maxDueDatesPerProject.put(currentProject,
                    Mista2013IncrementalScoreCalculator.findMaxDueDate(currentProject, this.allocationsPerJob));
            if (currentDueDate == this.maxDueDateGlobal) {
                this.maxDueDateGlobal = Mista2013IncrementalScoreCalculator.findMaxDueDate(this.problem,
                        this.maxDueDatesPerProject);
            }
        }
    }
}
