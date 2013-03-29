package org.optaplanner.examples.projectscheduling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProblemInstance {

    private final List<Project> projects;

    private final int minReleaseDate;
    private final int maxDueDate;
    private final int totalJobCount;
    private final int maxResourceId;

    public ProblemInstance(final Collection<Project> projects) {
        // and now find the max due date for any of the projects
        int minReleaseDate = Integer.MAX_VALUE;
        int maxDueDate = Integer.MIN_VALUE;
        int maxResourceId = Integer.MIN_VALUE;
        int tmpJobCount = 0;
        final List<Project> tmp = new ArrayList<Project>(projects.size());
        for (final Project p : projects) {
            p.setParentInstance(this);
            minReleaseDate = Math.min(minReleaseDate, p.getReleaseDate());
            for (final Job j : p.getJobs()) {
                maxResourceId = Math.max(maxResourceId, j.getMaxResourceId());
                if (j.isSink() || j.isSource()) {
                    continue;
                }
                final int dueDate = j.getMaxDuration() + Collections.max(j.getAvailableJobStartDates());
                maxDueDate = Math.max(maxDueDate, dueDate);
            }
            tmpJobCount += p.getJobs().size();
            tmp.add(p);
        }
        this.minReleaseDate = minReleaseDate;
        this.maxDueDate = maxDueDate;
        this.maxResourceId = maxResourceId;
        this.totalJobCount = tmpJobCount;
        this.projects = Collections.unmodifiableList(tmp);
    }

    public List<Project> getProjects() {
        return this.projects;
    }

    public int getMinReleaseDate() {
        return this.minReleaseDate;
    }

    public int getMaxAllowedDueDate() {
        return this.maxDueDate;
    }

    public int getTotalNumberOfJobs() {
        return this.totalJobCount;
    }

    public int getMaxResourceId() {
        return this.maxResourceId;
    }

}
