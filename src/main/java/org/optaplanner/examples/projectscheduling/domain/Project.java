package org.optaplanner.examples.projectscheduling.domain;

import java.util.Collections;
import java.util.List;

public class Project {

    public static int getCriticalPathDurationUntil(final Job until) {
        if (until.isSource()) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (final Job predecessor : until.getPredecessors()) {
            min = Math.min(min, Project.getCriticalPathDurationUntil(predecessor));
        }
        return min + until.getMinDuration();
    }

    protected static int getMaxDurationUntil(final Job until) {
        if (until.isSource()) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        for (final Job predecessor : until.getPredecessors()) {
            max = Math.max(max, Project.getMaxDurationUntil(predecessor));
        }
        return max + until.getMaxDuration();
    }

    private ProblemInstance parentInstance;
    private final int id;

    private final int releaseDate;
    private final List<Resource> resources;
    private final List<Job> jobs;

    private final int criticalPathDuration;

    public Project(final int id, final int criticalPathDuration, final int releaseDate, final List<Resource> resources,
            final List<Job> jobs) {
        this.id = id;
        this.criticalPathDuration = criticalPathDuration;
        this.releaseDate = releaseDate;
        this.resources = Collections.unmodifiableList(resources);
        this.jobs = Collections.unmodifiableList(jobs);
        for (final Job j : jobs) {
            j.setParentProject(this);
        }
    }

    public ProblemInstance getParentInstance() {
        return this.parentInstance;
    }

    protected void setParentInstance(final ProblemInstance parent) {
        if (this.parentInstance == null) {
            this.parentInstance = parent;
        } else {
            throw new IllegalStateException("Cannot override job's parent instance.");
        }
    }

    public int getId() {
        return this.id;
    }

    public int getReleaseDate() {
        return this.releaseDate;
    }

    public List<Job> getJobs() {
        return this.jobs;
    }

    public List<Resource> getResources() {
        return this.resources;
    }

    public int getCriticalPathDuration() {
        return this.criticalPathDuration;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Project [id=").append(this.id).append(", releaseDate=").append(this.releaseDate).append("]");
        return builder.toString();
    }

}
