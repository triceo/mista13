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
        final int brokenHard1 = this.getOverutilizedLocalRenewableResourcesCount(solution);
        final int brokenHard2 = this.getOverutilizedLocalNonRenewableResourcesCount(solution);
        final int brokenHard3 = this.getOverutilizedGlobalResourcesCount(solution);
        final int brokenHard4 = this.getUnassignedJobModeCount(solution);
        final int brokenHard5 = this.getHorizonOverrunCount(solution);
        // FIXME does constraint 6 need to be validated?
        final int brokenHard7 = this.getBrokenPrecedenceRelationsCount(solution);
        final int brokenTotal = brokenHard1 + brokenHard2 + brokenHard3 + brokenHard4 + brokenHard5 + brokenHard7;
        // FIXME are we interested in projects being actually ahead?
        final int medium = Math.max(0, this.getTotalProjectDelay(solution));
        final int soft = this.getTotalMakespan(solution);
        return DefaultHardMediumSoftScore.valueOf(-brokenTotal, -medium, -soft);
    }

    /**
     * Finds the maximum starting date for any of the activities in the problem
     * instance.
     * 
     * @param solution
     * @return
     */
    private int findMaxStartDate(final Mista2013 solution) {
        int maxStartDate = Integer.MIN_VALUE;
        for (final Project p : solution.getProblem().getProjects()) {
            maxStartDate = Math.max(maxStartDate, this.findMaxStartDate(solution, p));
        }
        return maxStartDate;
    }

    /**
     * Finds the maximum starting date for any of the activities of a project.
     * This will look up the starting date of the sink of each project,
     * effectively location the end of the project.
     * 
     * @param solution
     * @return
     */
    private int findMaxStartDate(final Mista2013 solution, final Project p) {
        int maxStartDate = Integer.MIN_VALUE;
        for (final Allocation a : solution.getAllocations()) {
            if (a.getStartDate() == null) {
                continue;
            }
            maxStartDate = Math.max(maxStartDate, a.getStartDate());
        }
        return maxStartDate;
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
        for (final Project p : solution.getProblem().getProjects()) {
            // for each job in each project
            for (final Job j : p.getJobs()) {
                final Allocation a = solution.getAllocation(j);
                for (final Job successor : j.getSuccessors()) {
                    if (a.getJobMode() == null) {
                        // not yet initialized
                        total++;
                        continue;
                    }
                    // find its successors
                    final Allocation successing = solution.getAllocation(successor);
                    if (successing.getStartDate() == null) {
                        // not yet initialized
                        total++;
                        continue;
                    }
                    final int previousJobDoneBy = a.getStartDate() + a.getJobMode().getDuration();
                    /*
                     * FIXME > or >= ?
                     */
                    if (previousJobDoneBy > successing.getStartDate()) {
                        // and mark them if they don't actually succeed
                        total++;
                    }
                }
            }
        }
        return total;
    }

    private int getCriticalPathDuration(final Project p) {
        /*
         * FIXME footnote on page 3 of competition description says this is OK.
         * is it? or should we calculate it, as the competition describes?
         */
        return p.getCriticalPathDuration();
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
            if (this.findMaxStartDate(solution, p) > upperBound) {
                total++;
            }
        }
        return total;
    }

    private int getMakespan(final Mista2013 solution, final Project p) {
        return this.findMaxStartDate(solution, p) - p.getReleaseDate();
    }

    /**
     * Validates feasibility requirement (3).
     * 
     * FIXME is my understanding correct? if not, the whole method is wrong.
     * 
     * @return How many more global resources would we need than we have
     *         capacity for.
     */
    private int getOverutilizedGlobalResourcesCount(final Mista2013 solution) {
        return this.getOverutilizedResourceCount(solution, Mista2013ScoreCalculator.GLOBALS_ONLY);
    }

    /**
     * Validates feasibility requirement (2).
     * 
     * FIXME is my understanding correct? if not, the whole method is wrong.
     * 
     * @return How many more local non-renewable resources would we need than we
     *         have capacity for.
     */
    private int getOverutilizedLocalNonRenewableResourcesCount(final Mista2013 solution) {
        return this.getOverutilizedResourceCount(solution, Mista2013ScoreCalculator.LOCAL_NONRENEWABLES);
    }

    /**
     * Validates feasibility requirement (1).
     * 
     * FIXME is my understanding correct? if not, the whole method is wrong.
     * 
     * @return How many more local renewable resources would we need than we
     *         have capacity for.
     */
    private int getOverutilizedLocalRenewableResourcesCount(final Mista2013 solution) {
        return this.getOverutilizedResourceCount(solution, Mista2013ScoreCalculator.LOCAL_RENEWABLES);
    }

    private int getOverutilizedResourceCount(final Mista2013 solution, final Filter<Resource> filter) {
        int total = 0;
        final Map<Resource, Integer> totalAllocations = new HashMap<Resource, Integer>();
        // sum up all the resource consumptions that we track
        for (final Project p : solution.getProblem().getProjects()) {
            for (final Job j : p.getJobs()) {
                final Allocation a = solution.getAllocation(j);
                final JobMode jm = a.getJobMode();
                if (jm == null) {
                    continue;
                }
                for (final Resource r : p.getResources()) {
                    if (!filter.accept(r)) {
                        // not the type of resource we're interested in
                        continue;
                    }
                    final int totalAllocation = totalAllocations.containsKey(r) ? totalAllocations.get(r) : 0;
                    totalAllocations.put(r, totalAllocation + jm.getResourceRequirement(r));
                }
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

    private int getProjectDelay(final Mista2013 solution, final Project p) {
        return this.getMakespan(solution, p) - this.getCriticalPathDuration(p);
    }

    private int getTotalMakespan(final Mista2013 solution) {
        return this.findMaxStartDate(solution) - this.findMinReleaseDate(solution);
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
