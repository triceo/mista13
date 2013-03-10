package org.optaplanner.examples.projectscheduling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Job {

    public static enum JobType {
        SOURCE, SINK, STANDARD
    };

    private static Set<Job> countSuccessorsRecursively(final Job j) {
        final Set<Job> result = new HashSet<Job>();
        for (final Job successor : j.getSuccessors()) {
            result.add(successor);
            result.addAll(Job.countSuccessorsRecursively(successor));
        }
        return Collections.unmodifiableSet(result);
    }

    private final int id;
    private final List<Job> successors;
    private final List<Job> recursiveSuccessors;
    private final List<JobMode> jobModes;
    private Project parentProject;
    private final boolean isSource;

    private final boolean isSink;

    private List<Job> predecessors = new ArrayList<Job>();

    public Job(final int id, final Collection<JobMode> modes, final Collection<Job> successors, final JobType type) {
        this.id = id;
        // update successor info
        this.successors = Collections.unmodifiableList(new ArrayList<Job>(successors));
        if (this.successors.contains(null)) {
            throw new IllegalStateException("Cannot have null as a successor!");
        }
        this.recursiveSuccessors = Collections
                .unmodifiableList(new ArrayList<Job>(Job.countSuccessorsRecursively(this)));
        // update predecessor info
        for (final Job j : this.successors) {
            j.isPreceededBy(this);
        }
        // prepare job modes
        final List<JobMode> jobModes = new ArrayList<JobMode>(modes.size());
        for (final JobMode m : modes) {
            m.setParentJob(this);
            jobModes.add(m.getId() - 1, m);
        }
        this.jobModes = Collections.unmodifiableList(jobModes);
        this.isSource = type == JobType.SOURCE;
        this.isSink = type == JobType.SINK;
    }

    public int getId() {
        return this.id;
    }

    public JobMode getJobMode(final int id) {
        if (id < 1 || id > this.jobModes.size()) {
            throw new IllegalArgumentException("Job " + this + " has not mode #" + id);
        }
        return this.jobModes.get(id - 1);
    }

    public Collection<JobMode> getJobModes() {
        return this.jobModes;
    }

    public Project getParentProject() {
        return this.parentProject;
    }

    public List<Job> getPredecessors() {
        return this.predecessors;
    }

    public List<Job> getRecursiveSuccessors() {
        return this.recursiveSuccessors;
    }

    public List<Job> getSuccessors() {
        return this.successors;
    }

    private void isPreceededBy(final Job j) {
        final Set<Job> predecessors = new HashSet<Job>(this.predecessors);
        predecessors.add(j);
        this.predecessors = Collections.unmodifiableList(new ArrayList<Job>(predecessors));
    }

    public boolean isSink() {
        return this.isSink;
    }

    public boolean isSource() {
        return this.isSource;
    }

    protected void setParentProject(final Project p) {
        if (this.parentProject == null) {
            this.parentProject = p;
        } else {
            throw new IllegalStateException("Cannot override parent project!");
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Job [id=").append(this.id).append(", parentProject=").append(this.parentProject).append("]");
        return builder.toString();
    }

}
