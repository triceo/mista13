package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProblemInstance {

    private static class IntegerComparator implements Comparator<Integer> {

        @Override
        public int compare(final Integer arg0, final Integer arg1) {
            return arg0.compareTo(arg1);
        }

    }

    private final List<Project> projects;
    private final int maxDuration;
    private final int maxStartDate;
    private final int minReleaseDate;

    public ProblemInstance(final Collection<Project> projects) {
        // and now find the max due date for any of the projects
        int maxDuration = Integer.MIN_VALUE;
        int maxStartDate = Integer.MIN_VALUE;
        for (final Project p : projects) {
            for (final Job j : p.getJobs()) {
                for (final JobMode jm : j.getJobModes()) {
                    maxDuration = Math.max(maxDuration, jm.getDuration());
                }
            }
            maxStartDate = Math.max(maxStartDate,
                    Collections.max(p.getAvailableJobStartDates(), new IntegerComparator()));
        }
        this.maxStartDate = maxStartDate;
        this.maxDuration = maxDuration;
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