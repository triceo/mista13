package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProblemInstance {

    private final List<Project> projects;
    private final int max;

    private final int horizonUpperBound;

    public ProblemInstance(final Collection<Project> projects) {
        // and now find the max due date for any of the projects
        int max = Integer.MIN_VALUE;
        int horizonUpperBound = Integer.MIN_VALUE;
        for (final Project p : projects) {
            final int horizon = p.getHorizon();
            for (final Job j : p.getJobs()) {
                int maxDuration = Integer.MIN_VALUE;
                for (final JobMode jm : j.getJobModes()) {
                    maxDuration = Math.max(maxDuration, jm.getDuration());
                }
                max = Math.max(max, maxDuration + horizon);
            }
            horizonUpperBound = Math.max(horizonUpperBound, horizon);
        }
        this.horizonUpperBound = horizonUpperBound;
        this.max = max;
        // only do this after the upper bound is known
        final List<Project> tmp = new ArrayList<Project>();
        for (final Project p : projects) {
            p.setParentInstance(this);
            tmp.add(p);
        }
        this.projects = Collections.unmodifiableList(tmp);
    }

    /*
     * FIXME what's "upper bound on the time horizon of scheduling problem"?
     * here we assume that it is the maximum of horizons of all projects in a
     * problem instance.
     */
    public int getHorizonUpperBound() {
        return this.horizonUpperBound;
    }

    public List<Project> getProjects() {
        return this.projects;
    }

    public int getTheoreticalMaximumDueDate() {
        return this.max;
    }

}