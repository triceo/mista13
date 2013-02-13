package org.drools.planner.examples.mista2013.solver.score.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Project;

public class MaxDueDateTracker {

    private final PriorityQueue<Integer> dueDates;

    private final Map<Project, PriorityQueue<Integer>> dueDatesPerProject;

    public MaxDueDateTracker(final int projects) {
        this.dueDates = new PriorityQueue<Integer>(projects, Collections.reverseOrder());
        this.dueDatesPerProject = new HashMap<Project, PriorityQueue<Integer>>(projects);
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

    public int getMaxDueDate() {
        if (this.dueDates.isEmpty()) {
            return Integer.MIN_VALUE;
        } else {
            return this.dueDates.peek();
        }
    }

    public int getMaxDueDate(final Project p) {
        final PriorityQueue<Integer> perProject = this.dueDatesPerProject.get(p);
        if (perProject == null || perProject.isEmpty()) {
            return Integer.MIN_VALUE;
        } else {
            return perProject.peek();
        }
    }

    public void remove(final Allocation a) {
        final Project currentProject = a.getJob().getParentProject();
        final Integer dueDate = a.getDueDate();
        this.dueDatesPerProject.get(currentProject).remove(dueDate);
        this.dueDates.remove(dueDate);
    }

}
