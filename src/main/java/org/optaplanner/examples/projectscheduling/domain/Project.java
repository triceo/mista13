package org.optaplanner.examples.projectscheduling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Project {

    private static final double TMD_MULTIPLIER = 4;

    private static Collection<Integer> getStartDates(final int start, final double length) {
        final int actualLength = (int) Math.ceil(length);
        final Collection<Integer> startDates = new ArrayList<Integer>(actualLength);
        for (int i = 0; i < actualLength; i++) {
            startDates.add(i + start);
        }
        return Collections.unmodifiableCollection(startDates);
    }

    private ProblemInstance parentInstance;
    private final int id;

    private final int releaseDate;
    private final List<Resource> resources;
    private final List<Job> jobs;

    private final int criticalPathDuration;

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
        this.startDates = Project.getStartDates(this.getReleaseDate(), this.getTheoreticalMaxDuration()
                * Project.TMD_MULTIPLIER);
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

    public Collection<Integer> getAvailableJobStartDates() {
        return this.startDates;
    }

    private int getTheoreticalMaxDuration() {
        return this.getTheoreticalMaxDuration(this.jobs.get(0));
    }

    private int getTheoreticalMaxDuration(final Job startWith) {
        if (startWith.isSink()) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        for (final Job successor : startWith.getSuccessors()) {
            max = Math.max(max, this.getTheoreticalMaxDuration(successor));
        }
        return max + startWith.getMaxDuration();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Project [id=").append(this.id).append(", releaseDate=").append(this.releaseDate).append("]");
        return builder.toString();
    }

}
