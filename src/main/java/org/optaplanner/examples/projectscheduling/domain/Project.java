package org.optaplanner.examples.projectscheduling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Project {

    private static Collection<Integer> getStartDates(final int start, final double rawEnd) {
        final int end = (int) Math.ceil(rawEnd);
        final Collection<Integer> startDates = new ArrayList<Integer>(end - start);
        for (int i = start; i < end; i++) {
            startDates.add(i);
        }
        return Collections.unmodifiableCollection(startDates);
    }

    private final int id;

    private final int releaseDate;

    private final List<Resource> resources;

    private final List<Job> jobs;

    private static final double CPD_MULTIPLIER = 10;

    private final int criticalPathDuration;

    private ProblemInstance parentInstance;
    private final Collection<Integer> startDates;

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
        this.startDates = Project.getStartDates(this.getReleaseDate(), this.getReleaseDate() + this.getCriticalPathDuration()
                * Project.CPD_MULTIPLIER);
    }

    public Collection<Integer> getAvailableJobStartDates() {
        return this.startDates;
    }

    public int getCriticalPathDuration() {
        return this.criticalPathDuration;
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

    protected void setParentInstance(final ProblemInstance parent) {
        if (this.parentInstance == null) {
            this.parentInstance = parent;
        } else {
            throw new IllegalStateException("Cannot override job's parent instance.");
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Project [id=").append(this.id).append(", releaseDate=").append(this.releaseDate).append("]");
        return builder.toString();
    }

}
