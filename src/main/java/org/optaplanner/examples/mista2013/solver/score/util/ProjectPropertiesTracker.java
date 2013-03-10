package org.optaplanner.examples.mista2013.solver.score.util;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.optaplanner.examples.mista2013.domain.Allocation;
import org.optaplanner.examples.mista2013.domain.ProblemInstance;
import org.optaplanner.examples.mista2013.domain.Project;

public class ProjectPropertiesTracker {

    private static int calculateProjectDelay(final Project p, final int maxDueDate) {
        return Math.max(0, maxDueDate + 1 - p.getReleaseDate() - p.getCriticalPathDuration());
    }

    private static int getMax(final PriorityQueue<Integer> q) {
        return q.isEmpty() ? 0 : q.peek();
    }

    private static PriorityQueue<Integer> getPriorityQueue(final int size) {
        return new PriorityQueue<Integer>(size, Collections.reverseOrder());
    }

    private final ProblemInstance problem;

    private final PriorityQueue<Integer> dueDates;

    private final Map<Project, PriorityQueue<Integer>> dueDatesPerProject;

    private final TObjectIntMap<Project> projectDelays;
    private int totalProjectDelay = 0;

    private int totalMakespan;

    private static final int ESTIMATED_NUMBER_OF_JOBS_PER_PROJECT = 10;

    public ProjectPropertiesTracker(final ProblemInstance problem) {
        final int projectCount = problem.getProjects().size();
        this.problem = problem;
        this.dueDates = ProjectPropertiesTracker.getPriorityQueue(projectCount
                * ProjectPropertiesTracker.ESTIMATED_NUMBER_OF_JOBS_PER_PROJECT);
        this.dueDatesPerProject = new HashMap<Project, PriorityQueue<Integer>>(projectCount);
        this.projectDelays = new TObjectIntHashMap<Project>(projectCount);
        this.totalMakespan = this.calculateTotalMakespan(0);
    }

    public void add(final Allocation a) {
        final Project p = a.getJob().getParentProject();
        final int newMax = a.getDueDate();
        final Integer newMaxI = Integer.valueOf(newMax);
        // prepare data
        final int previousGlobalMax = ProjectPropertiesTracker.getMax(this.dueDates);
        this.dueDates.add(newMaxI);
        PriorityQueue<Integer> perProject = this.dueDatesPerProject.get(p);
        if (perProject == null) {
            perProject = ProjectPropertiesTracker.getPriorityQueue(p.getJobs().size());
            this.dueDatesPerProject.put(p, perProject);
        }
        final int previousLocalMax = ProjectPropertiesTracker.getMax(perProject);
        perProject.add(newMaxI);
        // update project properties
        if (newMax > previousLocalMax) {
            final int newDelay = ProjectPropertiesTracker.calculateProjectDelay(p, newMax);
            final int previousDelay = this.projectDelays.put(p, newDelay);
            this.totalProjectDelay += newDelay - previousDelay;
            if (newMax > previousGlobalMax) {
                this.totalMakespan = this.calculateTotalMakespan(newMax);
            }
        }
    }

    private int calculateTotalMakespan(final int maxDueDate) {
        return maxDueDate + 1 - this.problem.getMinReleaseDate();
    }

    public int getTotalMakespan() {
        return this.totalMakespan;
    }

    public int getTotalProjectDelay() {
        return this.totalProjectDelay;
    }

    public void remove(final Allocation a) {
        final Project p = a.getJob().getParentProject();
        final int previousMax = a.getDueDate();
        final Integer previousMaxI = Integer.valueOf(previousMax);
        // prepare data
        this.dueDates.remove(previousMaxI);
        final PriorityQueue<Integer> perProject = this.dueDatesPerProject.get(p);
        perProject.remove(previousMaxI);
        final int newLocalMax = ProjectPropertiesTracker.getMax(perProject);
        // update project properties
        if (previousMax > newLocalMax) {
            final int newDelay = ProjectPropertiesTracker.calculateProjectDelay(p, newLocalMax);
            final int previousDelay = this.projectDelays.put(p, newDelay);
            this.totalProjectDelay += newDelay - previousDelay;
            final int newGlobalMax = ProjectPropertiesTracker.getMax(this.dueDates);
            if (previousMax > newGlobalMax) {
                this.totalMakespan = this.calculateTotalMakespan(newGlobalMax);
            }
        }
    }

}
