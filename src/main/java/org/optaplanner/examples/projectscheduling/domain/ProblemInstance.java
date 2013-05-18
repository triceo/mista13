package org.optaplanner.examples.projectscheduling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProblemInstance {

    private final List<Project> projects;

    private final int minReleaseDate;
    private final int totalJobCount;
    private final int maxRenewableResourceId;
    private final int maxNonRenewableResourceId;

    public ProblemInstance(final Collection<Project> projects) {
        // and now find the max due date for any of the projects
        int minReleaseDate = Integer.MAX_VALUE;
        int maxRenewableResourceId = Integer.MIN_VALUE;
        int maxNonRenewableResourceId = Integer.MIN_VALUE;
        int tmpJobCount = 0;
        final List<Project> tmp = new ArrayList<Project>(projects.size());
        for (final Project p : projects) {
            p.setParentInstance(this);
            minReleaseDate = Math.min(minReleaseDate, p.getReleaseDate());
            for (final Job j : p.getJobs()) {
                maxRenewableResourceId = Math.max(maxRenewableResourceId, j.getMaxRenewableResourceId());
                maxNonRenewableResourceId = Math.max(maxNonRenewableResourceId, j.getMaxNonRenewableResourceId());
                if (j.isSink() || j.isSource()) {
                    continue;
                }
            }
            tmpJobCount += p.getJobs().size();
            tmp.add(p);
        }
        this.minReleaseDate = minReleaseDate;
        this.maxRenewableResourceId = maxRenewableResourceId;
        this.maxNonRenewableResourceId = maxNonRenewableResourceId;
        this.totalJobCount = tmpJobCount;
        this.projects = Collections.unmodifiableList(tmp);
    }

    public List<Project> getProjects() {
        return this.projects;
    }

    public int getMinReleaseDate() {
        return this.minReleaseDate;
    }

    public int getTotalNumberOfJobs() {
        return this.totalJobCount;
    }

    public int getMaxRenewableResourceId() {
        return this.maxRenewableResourceId;
    }

    public int getMaxNonRenewableResourceId() {
        return this.maxNonRenewableResourceId;
    }

}
