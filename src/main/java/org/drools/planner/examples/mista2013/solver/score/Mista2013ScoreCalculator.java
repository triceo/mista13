package org.drools.planner.examples.mista2013.solver.score;

import java.util.HashMap;
import java.util.Map;

import org.drools.planner.core.score.Score;
import org.drools.planner.core.score.buildin.hardmediumsoft.DefaultHardMediumSoftScore;
import org.drools.planner.core.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.drools.planner.core.score.director.simple.SimpleScoreCalculator;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.JobMode;
import org.drools.planner.examples.mista2013.domain.Mista2013;
import org.drools.planner.examples.mista2013.domain.Project;
import org.drools.planner.examples.mista2013.domain.Resource;

public class Mista2013ScoreCalculator implements SimpleScoreCalculator<Mista2013> {

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

    @Override
    public Score<HardMediumSoftScore> calculateScore(final Mista2013 solution) {
        final int plannerPlanningValueWorkaround = this.findInvalidEntityVariableValues(solution);
        final int brokenHard1 = this.getOverutilizedLocalRenewableResourcesCount(solution);
        final int brokenHard2 = this.getOverutilizedLocalNonRenewableResourcesCount(solution);
        final int brokenHard3 = this.getOverutilizedGlobalResourcesCount(solution);
        final int brokenHard4 = this.getUnassignedJobModeCount(solution);
        final int brokenHard5 = this.getHorizonOverrunCount(solution);
        // FIXME does constraint 6 need to be validated?
        final int brokenHard7 = this.getBrokenPrecedenceRelationsCount(solution);
        final int brokenTotal = plannerPlanningValueWorkaround + brokenHard1 + brokenHard2 + brokenHard3 + brokenHard4
                + brokenHard5 + brokenHard7;
        // FIXME are we interested in projects being actually ahead?
        final int medium = Math.max(0, this.getTotalProjectDelay(solution));
        final int soft = this.getTotalMakespan(solution);
        return DefaultHardMediumSoftScore.valueOf(-brokenTotal, -medium, -soft);
    }

    // TODO remove when Planner 6.0-SNAPSHOT fixes contr heur and selectors wrt.
    // planning values
    private int findInvalidEntityVariableValues(final Mista2013 solution) {
        int total = 0;
        for (final Allocation a : solution.getAllocations()) {
            if (a.getJobMode() == null || !a.getJobModes().contains(a.getJobMode())) {
                total++;
            }
            if (a.getStartDate() == null || !a.getStartDates().contains(a.getStartDate())) {
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
     * @param solution
     * @param p
     * @return
     */
    private int findMaxDueDate(final Mista2013 solution) {
        int maxDueDate = Integer.MIN_VALUE;
        for (final Project p : solution.getProblem().getProjects()) {
            maxDueDate = Math.max(maxDueDate, this.findMaxDueDate(solution, p));
        }
        return maxDueDate;
    }

    /**
     * Find maximum due date for any of the activities in a given project. This
     * date (since we ignore project sink) is effectively equivalent to the end
     * of the project.
     * 
     * @param solution
     * @param p
     * @return
     */
    private int findMaxDueDate(final Mista2013 solution, final Project p) {
        int maxDueDate = Integer.MIN_VALUE;
        for (final Allocation a : solution.getAllocations()) {
            if (a.getJob().getParentProject() != p) {
                continue;
            }
            if (a.getStartDate() == null || a.getJobMode() == null) {
                continue;
            }
            maxDueDate = Math.max(maxDueDate, a.getStartDate() + a.getJobMode().getDuration());
        }
        return maxDueDate;
    }

    private int findMinReleaseDate(final Mista2013 solution) {
        int minReleaseDate = Integer.MAX_VALUE;
        for (final Project p : solution.getProblem().getProjects()) {
            minReleaseDate = Math.min(minReleaseDate, p.getReleaseDate());
        }
        return minReleaseDate;
    }

    /**
     * Validates feasibility requirement (7).
     * 
     * @return How many precedence relations are broken
     */
    private int getBrokenPrecedenceRelationsCount(final Mista2013 solution) {
        int total = 0;
        for (final Allocation currentJobAllocation : solution.getAllocations()) {
            final Job currentJob = currentJobAllocation.getJob();
            final Project p = currentJob.getParentProject();
            final JobMode currentMode = currentJobAllocation.getJobMode();
            if (currentMode == null) {
                continue;
            }
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
                final Allocation succeedingJobAllocation = solution.getAllocation(succeedingJob);
                final Integer nextStartedAt = succeedingJobAllocation.getStartDate();
                if (nextStartedAt == null) {
                    continue;
                }
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
    private int getHorizonOverrunCount(final Mista2013 solution) {
        // find what we think is the upper bound
        int total = 0;
        int upperBound = Integer.MIN_VALUE;
        for (final Project p : solution.getProblem().getProjects()) {
            upperBound = Math.max(upperBound, p.getHorizon());
        }
        // and now find out the number of projects that went over it
        for (final Project p : solution.getProblem().getProjects()) {
            if (this.findMaxDueDate(solution, p) > upperBound) {
                total++;
            }
        }
        return total;
    }

    private int getMakespan(final Mista2013 solution, final Project p) {
        return this.findMaxDueDate(solution, p) - p.getReleaseDate();
    }

    /**
     * Validates feasibility requirement (3).
     * 
     * @return How many more global resources would we need than we have
     *         capacity for.
     */
    private int getOverutilizedGlobalResourcesCount(final Mista2013 solution) {
        return this.getOverutilizedRenewableResourceCount(solution, Mista2013ScoreCalculator.GLOBALS_ONLY);
    }

    /**
     * Validates feasibility requirement (2).
     * 
     * @return How many more local non-renewable resources would we need than we
     *         have capacity for.
     */
    private int getOverutilizedLocalNonRenewableResourcesCount(final Mista2013 solution) {
        int total = 0;
        final Map<Resource, Integer> totalAllocations = new HashMap<Resource, Integer>();
        // sum up all the resource consumptions that we track
        for (final Allocation a : solution.getAllocations()) {
            final JobMode jm = a.getJobMode();
            if (jm == null) {
                continue;
            }
            for (final Resource r : jm.getResources()) {
                if (!Mista2013ScoreCalculator.LOCAL_NONRENEWABLES.accept(r)) {
                    // not the type of resource we're interested in
                    continue;
                }
                final int totalAllocation = totalAllocations.containsKey(r) ? totalAllocations.get(r) : 0;
                totalAllocations.put(r, totalAllocation + jm.getResourceRequirement(r));
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
    private int getOverutilizedLocalRenewableResourcesCount(final Mista2013 solution) {
        return this.getOverutilizedRenewableResourceCount(solution, Mista2013ScoreCalculator.LOCAL_RENEWABLES);
    }

    private int getOverutilizedRenewableResourceCount(final Mista2013 solution, final Filter<Resource> filter) {
        int total = 0;
        for (int time = this.findMinReleaseDate(solution); time <= this.findMaxDueDate(solution); time++) {
            final Map<Resource, Integer> totalAllocations = new HashMap<Resource, Integer>();
            for (final Allocation a : solution.getAllocations()) {
                // activity not yet initialized
                final JobMode jm = a.getJobMode();
                if (a.getStartDate() == null || jm == null) {
                    continue;
                }
                // activity not running at the time
                final int dueDate = a.getStartDate() + jm.getDuration();
                if (a.getStartDate() > time || dueDate < time) {
                    continue;
                }
                for (final Resource r : jm.getResources()) {
                    if (!filter.accept(r)) {
                        // not the type of resource we're interested in
                        continue;
                    }
                    final int totalAllocation = totalAllocations.containsKey(r) ? totalAllocations.get(r) : 0;
                    totalAllocations.put(r, totalAllocation + jm.getResourceRequirement(r));
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

    private int getProjectDelay(final Mista2013 solution, final Project p) {
        return this.getMakespan(solution, p) - p.getCriticalPathDuration();
    }

    private int getTotalMakespan(final Mista2013 solution) {
        return this.findMaxDueDate(solution) - this.findMinReleaseDate(solution);
    }

    private int getTotalProjectDelay(final Mista2013 solution) {
        int total = 0;
        for (final Project p : solution.getProblem().getProjects()) {
            total += this.getProjectDelay(solution, p);
        }
        return total;
    }

    /**
     * Validates feasibility requirement (4).
     * 
     * @return How many jobs haven't picked a job mode.
     */
    private int getUnassignedJobModeCount(final Mista2013 solution) {
        int total = 0;
        for (final Allocation a : solution.getAllocations()) {
            if (a.getJobMode() == null) {
                total++;
            }
        }
        return total;
    }
}
