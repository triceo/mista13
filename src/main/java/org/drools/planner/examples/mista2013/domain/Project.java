package org.drools.planner.examples.mista2013.domain;

import java.util.Collections;
import java.util.List;

public class Project {

    private final int id;

    private final int horizon;

    private final int releaseDate;

    private final int dueDate;

    private final int tardinessCost;

    private final List<Resource> resources;

    private final List<Job> jobs;

    private final int criticalPathDuration;

    private ProblemInstance parentInstance;

    public Project(final int id, final int criticalPathDuration, final int horizon, final int releaseDate,
            final int dueDate, final int tardinessCost, final List<Resource> resources, final List<Job> jobs) {
        this.id = id;
        this.criticalPathDuration = criticalPathDuration;
        this.horizon = horizon;
        this.releaseDate = releaseDate;
        this.dueDate = dueDate;
        this.tardinessCost = tardinessCost;
        this.resources = Collections.unmodifiableList(resources);
        this.jobs = Collections.unmodifiableList(jobs);
        for (final Job j : jobs) {
            j.setParentProject(this);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Project)) {
            return false;
        }
        final Project other = (Project) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.parentInstance == null) {
            if (other.parentInstance != null) {
                return false;
            }
        } else if (!this.parentInstance.equals(other.parentInstance)) {
            return false;
        }
        return true;
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

    public int getId() {
        return this.id;
    }

    public List<Job> getJobs() {
        return this.jobs;
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

    public Job getSink() {
        for (final Job j : this.jobs) {
            if (j.isSink()) {
                return j;
            }
        }
        throw new IllegalStateException("Project has no sink!");
    }

    public Job getSource() {
        for (final Job j : this.jobs) {
            if (j.isSource()) {
                return j;
            }
        }
        throw new IllegalStateException("Project has no source!");
    }

    public int getTardinessCost() {
        return this.tardinessCost;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        result = prime * result + ((this.parentInstance == null) ? 0 : this.parentInstance.hashCode());
        return result;
    }

    protected void setParentInstance(final ProblemInstance parent) {
        if (this.parentInstance == null) {
            this.parentInstance = parent;
        } else {
            throw new IllegalStateException("Cannot override job's parent instance.");
        }
    }

}
