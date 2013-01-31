package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProblemInstance {

    private final List<Project> projects;
    private final int max;

    public ProblemInstance(final Collection<Project> projects) {
        final List<Project> tmp = new ArrayList<Project>();
        for (final Project p : projects) {
            p.setParentInstance(this);
            tmp.add(p);
        }
        this.projects = Collections.unmodifiableList(tmp);
        // and now find the max due date for any of the projects
        int max = Integer.MIN_VALUE;
        for (final Project p : this.projects) {
            final int horizon = p.getHorizon();
            for (final Job j : p.getJobs()) {
                int maxDuration = Integer.MIN_VALUE;
                for (final JobMode jm : j.getJobModes()) {
                    maxDuration = Math.max(maxDuration, jm.getDuration());
                }
                max = Math.max(max, maxDuration + horizon);
            }
        }
        this.max = max;
    }

    public List<Project> getProjects() {
        return this.projects;
    }

    public int getTheoreticalMaximumDueDate() {
        return this.max;
    }

}