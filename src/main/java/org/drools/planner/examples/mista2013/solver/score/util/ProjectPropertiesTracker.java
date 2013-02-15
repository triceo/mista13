package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.ProblemInstance;
import org.drools.planner.examples.mista2013.domain.Project;

public class ProjectPropertiesTracker {

    private final ProblemInstance problem;

    private final PriorityQueue<Integer> dueDates;

    private final Map<Project, PriorityQueue<Integer>> dueDatesPerProject;

    public ProjectPropertiesTracker(final ProblemInstance problem) {
        final int size = problem.getProjects().size();
        this.problem = problem;
        this.dueDates = new PriorityQueue<Integer>(size, Collections.reverseOrder());
        this.dueDatesPerProject = new HashMap<Project, PriorityQueue<Integer>>(size);
    }

    public void add(final Allocation a) {
        final Project currentProject = a.getJob().getParentProject();
        final Integer dueDate = a.getDueDate();
        PriorityQueue<Integer> perProject = this.dueDatesPerProject.get(currentProject);
        if (perProject == null) {
            perProject = new PriorityQueue<Integer>(currentProject.getJobs().size(), Collections.reverseOrder());
            this.dueDatesPerProject.put(currentProject, perProject);
        }
        perProject.add(dueDate);
        this.dueDates.add(dueDate);
    }

    private int getMakespan(final Project p) {
        /*
         * due date for a task is the latest time when the task is still
         * running. due date of a project is the time after the last job
         * finishes, hence +1.
         */
        return (this.getMaxDueDate(p) + 1) - p.getReleaseDate();
    }

    private int getMaxDueDate() {
        if (this.dueDates.isEmpty()) {
            return Integer.MIN_VALUE;
        } else {
            return this.dueDates.peek();
        }
    }

    private int getMaxDueDate(final Project p) {
        final PriorityQueue<Integer> perProject = this.dueDatesPerProject.get(p);
        if (perProject == null || perProject.isEmpty()) {
            return Integer.MIN_VALUE;
        } else {
            return perProject.peek();
        }
    }

    private int getProjectDelay(final Project p) {
        return this.getMakespan(p) - p.getCriticalPathDuration();
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
        int total = 0;
        for (final Project p : this.problem.getProjects()) {
            total += this.getProjectDelay(p);
        }
        return total;
    }

    public void remove(final Allocation a) {
        final Project currentProject = a.getJob().getParentProject();
        final Integer dueDate = a.getDueDate();
        this.dueDatesPerProject.get(currentProject).remove(dueDate);
        this.dueDates.remove(dueDate);
    }

}
