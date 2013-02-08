package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProblemInstance {

    private final List<Project> projects;
    private final int maxDuration;
    private final int maxStartDate;
    private final int minReleaseDate;

    private final int horizonUpperBound;

    public ProblemInstance(final Collection<Project> projects) {
        // and now find the max due date for any of the projects
        int horizonUpperBound = Integer.MIN_VALUE;
        int maxDuration = Integer.MIN_VALUE;
        int maxStartDate = Integer.MIN_VALUE;
        for (final Project p : projects) {
            final int horizon = p.getHorizon();
            for (final Job j : p.getJobs()) {
                for (final JobMode jm : j.getJobModes()) {
                    maxDuration = Math.max(maxDuration, jm.getDuration());
                }
            }
            maxStartDate = Math.max(maxStartDate,
                    Collections.max(p.getAvailableJobStartDates(), new Comparator<Integer>() {

                        @Override
                        public int compare(final Integer arg0, final Integer arg1) {
                            return arg0.compareTo(arg1);
                        }

                    }));
            horizonUpperBound = Math.max(horizonUpperBound, horizon);
        }
        this.maxStartDate = maxStartDate;
        this.maxDuration = maxDuration;
        this.horizonUpperBound = horizonUpperBound;
        // find minimum release date
        int minReleaseDate = Integer.MAX_VALUE;
        for (final Project p : projects) {
            minReleaseDate = Math.min(minReleaseDate, p.getReleaseDate());
        }
        this.minReleaseDate = minReleaseDate;
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

    public int getMaxAllowedDueDate() {
        return this.maxStartDate + this.maxDuration;
    }

    public int getMinReleaseDate() {
        return this.minReleaseDate;
    }

    public List<Project> getProjects() {
        return this.projects;
    }

}