package org.drools.planner.examples.mista2013.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class Project {

    private final int horizon;

    private final int releaseDate;

    private final int dueDate;

    private final int tardinessCost;

    private final int mpmTime;

    private final Map<Resource, Integer> resourceAvailabilities;

    private final Collection<Job> jobs;

    private ProblemInstance parentInstance;

    public Project(final int horizon, final int releaseDate, final int dueDate, final int tardinessCost,
            final int mpmTime, final Map<Resource, Integer> resourceAvailabilities, final Collection<Job> jobs) {
        this.horizon = horizon;
        this.releaseDate = releaseDate;
        this.dueDate = dueDate;
        this.tardinessCost = tardinessCost;
        this.mpmTime = mpmTime;
        this.resourceAvailabilities = Collections.unmodifiableMap(resourceAvailabilities);
        this.jobs = Collections.unmodifiableCollection(jobs);
        for (final Job j : jobs) {
            j.setParentProject(this);
        }
    }

    public int getAvailability(final Resource r) {
        if (!this.resourceAvailabilities.containsKey(r)) {
            // little bit of defensive programming
            throw new IllegalArgumentException("Project " + this + " doesn't have resource " + r);
        }
        return this.resourceAvailabilities.get(r);
    }

    public int getDueDate() {
        return this.dueDate;
    }

    public int getHorizon() {
        return this.horizon;
    }

    public Collection<Job> getJobs() {
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
