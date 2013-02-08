package org.drools.planner.examples.mista2013.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class Project {

    private static Collection<Integer> getStartDates(final int start, final double rawEnd) {
        final int end = (int) Math.ceil(rawEnd);
        final Collection<Integer> startDates = new LinkedHashSet<Integer>();
        for (int i = start; i < end; i++) {
            startDates.add(i);
        }
        return Collections.unmodifiableCollection(startDates);
    }

    private final int id;

    private final int horizon;

    private final int releaseDate;

    private final int dueDate;

    private final int tardinessCost;

    private final List<Resource> resources;

    private final List<Job> jobs;

    private static final double HORIZON_MULTIPLIER = 2;

    private final int criticalPathDuration;
    private ProblemInstance parentInstance;

    private final Collection<Integer> startDates;

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
        this.startDates = Project.getStartDates(this.getReleaseDate(), this.getHorizon() * Project.HORIZON_MULTIPLIER);
    }

    public Collection<Integer> getAvailableJobStartDates() {
        return this.startDates;
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

    protected void setParentInstance(final ProblemInstance parent) {
        if (this.parentInstance == null) {
            this.parentInstance = parent;
        } else {
            throw new IllegalStateException("Cannot override job's parent instance.");
        }
    }

}
