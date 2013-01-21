package org.drools.planner.examples.mista2013.domain;

import java.util.Collections;
import java.util.List;

public class Project {

    private final int horizon;

    private final int releaseDate;

    private final int dueDate;

    private final int tardinessCost;

    private final int mpmTime;

    private final List<Resource> resources;

    private final List<Job> jobs;

    private final int criticalPathDuration;

    private ProblemInstance parentInstance;

    public Project(final int criticalPathDuration, final int horizon, final int releaseDate, final int dueDate,
            final int tardinessCost, final int mpmTime, final List<Resource> resources, final List<Job> jobs) {
        this.criticalPathDuration = criticalPathDuration;
        this.horizon = horizon;
        this.releaseDate = releaseDate;
        this.dueDate = dueDate;
        this.tardinessCost = tardinessCost;
        this.mpmTime = mpmTime;
        this.resources = Collections.unmodifiableList(resources);
        this.jobs = Collections.unmodifiableList(jobs);
        for (final Job j : jobs) {
            j.setParentProject(this);
        }
    }

    public int getCriticalPathDuration() {
        return this.criticalPathDuration;
    }

    public int getDueDate() {
        return this.dueDate;
    }

    public int getHorizon() {
        return this.horizon;
    }

    public List<Job> getJobs() {
        // FIXME return also source and sink? currently yes, but undecided.
        return this.jobs;
    }

    public int getMpmTime() {
        return this.mpmTime;
    }

    public ProblemInstance getParentInstance() {
        return this.parentInstance;
    }

    public int getReleaseDate() {
        return this.releaseDate;
    }

    public List<Resource> getResources() {
        return this.resources;
    }

    public int getTardinessCost() {
        return this.tardinessCost;
    }

    protected void setParentInstance(final ProblemInstance parent) {
        if (parent == null) {
            this.parentInstance = parent;
        } else {
            throw new IllegalStateException("Cannot override job's parent instance.");
        }
    }

}
