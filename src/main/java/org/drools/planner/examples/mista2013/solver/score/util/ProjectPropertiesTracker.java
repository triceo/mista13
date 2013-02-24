package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.ProblemInstance;
import org.drools.planner.examples.mista2013.domain.Project;

public class ProjectPropertiesTracker {

    private static final PriorityQueue<Integer> getPriorityQueue(final int size) {
        return new PriorityQueue<Integer>(size, Collections.reverseOrder());
    }

    private final ProblemInstance problem;

    private final PriorityQueue<Integer> dueDates;

    private final Map<Project, PriorityQueue<Integer>> dueDatesPerProject;

    private final Map<Project, Integer> projectDelays;

    private int totalProjectDelay = 0;

    private static final int ESTIMATED_NUMBER_OF_JOBS_PER_PROJECT = 10;

    public ProjectPropertiesTracker(final ProblemInstance problem) {
        final int projectCount = problem.getProjects().size();
        this.problem = problem;
        this.dueDates = ProjectPropertiesTracker.getPriorityQueue(projectCount
                * ProjectPropertiesTracker.ESTIMATED_NUMBER_OF_JOBS_PER_PROJECT);
        this.dueDatesPerProject = new HashMap<Project, PriorityQueue<Integer>>(projectCount);
        this.projectDelays = new HashMap<Project, Integer>(projectCount);
    }

    public void add(final Allocation a) {
        final Project currentProject = a.getJob().getParentProject();
        final Integer dueDate = a.getDueDate();
        PriorityQueue<Integer> perProject = this.dueDatesPerProject.get(currentProject);
        if (perProject == null) {
            perProject = ProjectPropertiesTracker.getPriorityQueue(currentProject.getJobs().size());
            this.dueDatesPerProject.put(currentProject, perProject);
        }
        perProject.add(dueDate);
        this.dueDates.add(dueDate);
        final int newDelay = this.calculateProjectDelay(currentProject);
        final Integer previousDelay = this.projectDelays.put(currentProject, newDelay);
        if (previousDelay == null) {
            this.totalProjectDelay += newDelay;
        } else {
            this.totalProjectDelay += newDelay - previousDelay;
        }
    }

    private int calculateMakespan(final Project p) {
        /*
         * due date for a task is the latest time when the task is still
         * running. due date of a project is the time after the last job
         * finishes, hence +1.
         */
        return (this.getMaxDueDate(p) + 1) - p.getReleaseDate();
    }

    private int calculateProjectDelay(final Project p) {
        return Math.max(0, this.calculateMakespan(p) - p.getCriticalPathDuration());
    }

    private int getMaxDueDate() {
        if (this.dueDates.isEmpty()) {
            return 0;
        } else {
            return this.dueDates.peek();
        }
    }

    private int getMaxDueDate(final Project p) {
        final PriorityQueue<Integer> perProject = this.dueDatesPerProject.get(p);
        if (perProject == null || perProject.isEmpty()) {
            return 0;
        } else {
            return perProject.peek();
        }
    }

    public int getTotalMakespan() {
        /*
         * due date for a task is the latest time when the task is still
         * running. due date of a project is the time after the last job
         * finishes, hence +1.
         */
        return (this.getMaxDueDate() + 1) - this.problem.getMinReleaseDate();
    }

    public int getTotalProjectDelay() {
        return this.totalProjectDelay;
    }

    public void remove(final Allocation a) {
        final Project currentProject = a.getJob().getParentProject();
        // update due dates
        final Integer dueDate = a.getDueDate();
        this.dueDatesPerProject.get(currentProject).remove(dueDate);
        this.dueDates.remove(dueDate);
        // recalculate project delay
        final int newDelay = this.calculateProjectDelay(currentProject);
        final int previousDelay = this.projectDelays.put(currentProject, newDelay);
        this.totalProjectDelay += newDelay - previousDelay;
    }

}
