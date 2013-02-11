package org.drools.planner.examples.mista2013.solver.score;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.drools.planner.core.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.drools.planner.core.score.director.incremental.AbstractIncrementalScoreCalculator;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.Mista2013;
import org.drools.planner.examples.mista2013.domain.ProblemInstance;
import org.drools.planner.examples.mista2013.domain.Project;
import org.drools.planner.examples.mista2013.domain.Resource;
import org.drools.planner.examples.mista2013.solver.score.util.PrecedenceRelationsTracker;
import org.drools.planner.examples.mista2013.solver.score.util.RenewableResourceUsageTracker;

public class Mista2013IncrementalScoreCalculator extends AbstractIncrementalScoreCalculator<Mista2013> {

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
            if (a == null) {
                continue;
            }
            maxDueDate = Math.max(maxDueDate, a.getDueDate());
        }
        return maxDueDate;
    }

    private ProblemInstance problem = null;

    private Map<Job, Allocation> allocationsPerJob;

    private int maxDueDateGlobal = Integer.MIN_VALUE;

    private final Map<Project, Integer> maxDueDatesPerProject = new HashMap<Project, Integer>();

    private RenewableResourceUsageTracker renewableResourceUsage;
    private Map<Resource, Integer> nonRenewableResourceUsage;

    private PrecedenceRelationsTracker precedenceRelations;

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
        /*
         * validate MISTA requirements. Requirements (4, 5, 6) won't be
         * validated, as planner does that for us.
         */
        final int brokenReq1and3Count = this.renewableResourceUsage.countResourceOveruse();
        final int brokenReq2Count = this.getOverutilizedNonRenewableResourcesCount();
        final int brokenReq7Count = this.precedenceRelations.countBrokenPrecedenceRelations();
        // now assemble the constraints
        final int soft = this.getTotalMakespan();
        final int medium = this.getTotalProjectDelay();
        final int hard = brokenReq1and3Count + brokenReq2Count + brokenReq7Count;
        return HardMediumSoftScore.valueOf(-hard, -medium, -soft);
    }

    private void decreaseNonRenewableResourceUsage(final Allocation a) {
        for (final Map.Entry<Resource, Integer> entry : a.getJobMode().getResourceRequirements().entrySet()) {
            final Resource r = entry.getKey();
            if (r.isRenewable()) {
                continue;
            }
            final int value = entry.getValue();
            if (value == 0) {
                continue;
            }
            this.nonRenewableResourceUsage.put(r, this.nonRenewableResourceUsage.get(r) - value);
        }
    }

    private int getMakespan(final Project p) {
        /*
         * due date for a task is the latest time when the task is still
         * running. due date of a project is the time after the last job
         * finishes, hence +1.
         */
        return (this.maxDueDatesPerProject.get(p) + 1) - p.getReleaseDate();
    }

    /**
     * Validates feasibility requirement (2).
     * 
     * @return How many more non-renewable resources would we need than we have
     *         capacity for.
     */
    private int getOverutilizedNonRenewableResourcesCount() {
        int total = 0;
        for (final Map.Entry<Resource, Integer> entry : this.nonRenewableResourceUsage.entrySet()) {
            final Resource r = entry.getKey();
            final int allocation = entry.getValue();
            total += Math.max(0, allocation - r.getCapacity());
        }
        return total;
    }

    private int getProjectDelay(final Project p) {
        return this.getMakespan(p) - p.getCriticalPathDuration();
    }

    private int getTotalMakespan() {
        /*
         * due date for a task is the latest time when the task is still
         * running. due date of a project is the time after the last job
         * finishes, hence +1.
         */
        return (this.maxDueDateGlobal + 1) - this.problem.getMinReleaseDate();
    }

    private int getTotalProjectDelay() {
        int total = 0;
        for (final Project p : this.problem.getProjects()) {
            total += this.getProjectDelay(p);
        }
        return total;
    }

    private void increaseNonRenewableResourceUsage(final Allocation a) {
        for (final Map.Entry<Resource, Integer> entry : a.getJobMode().getResourceRequirements().entrySet()) {
            final Resource r = entry.getKey();
            if (r.isRenewable()) {
                continue;
            }
            int value = entry.getValue();
            if (value == 0) {
                continue;
            }
            // slightly faster than containsKey(r)
            final Integer previousValue = this.nonRenewableResourceUsage.get(r);
            if (previousValue != null) {
                value += previousValue;
            }
            this.nonRenewableResourceUsage.put(r, value);
        }
    }

    private void insert(final Allocation entity) {
        if (!entity.isInitialized()) {
            return;
        }
        this.allocationsPerJob.put(entity.getJob(), entity);
        this.renewableResourceUsage.add(entity);
        this.precedenceRelations.add(entity);
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
        // cache non-renewable resource use
        this.increaseNonRenewableResourceUsage(entity);
    }

    @Override
    public void resetWorkingSolution(final Mista2013 workingSolution) {
        // change to the new problem
        this.problem = workingSolution.getProblem();
        final Collection<Project> projects = this.problem.getProjects();
        this.maxDueDateGlobal = Integer.MIN_VALUE;
        for (final Project p : projects) {
            this.maxDueDatesPerProject.put(p, this.maxDueDateGlobal);
        }
        this.renewableResourceUsage = new RenewableResourceUsageTracker(this.problem.getMaxAllowedDueDate());
        this.nonRenewableResourceUsage = new HashMap<Resource, Integer>(projects.size() * 4);
        this.precedenceRelations = new PrecedenceRelationsTracker(projects);
        // insert new entities
        final Collection<Allocation> allocationsToProcess = workingSolution.getAllocations();
        final int size = allocationsToProcess.size();
        this.allocationsPerJob = new HashMap<Job, Allocation>(size);
        for (final Allocation a : allocationsToProcess) {
            this.insert(a);
        }
    }

    private void retract(final Allocation entity) {
        if (!entity.isInitialized()) {
            return;
        }
        this.allocationsPerJob.remove(entity.getJob());
        this.renewableResourceUsage.remove(entity);
        this.precedenceRelations.remove(entity);
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
        // update cache of non-renewable resource use
        this.decreaseNonRenewableResourceUsage(entity);
    }
}
