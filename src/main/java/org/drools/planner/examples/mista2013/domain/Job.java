package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private final Map<Integer, JobMode> jobModes = new HashMap<Integer, JobMode>();
    private Project parentProject;
    private final boolean isSource;

    private final boolean isSink;

    private final Collection<Job> predecessors = new HashSet<Job>();

    public Job(final int id, final Collection<JobMode> modes, final Collection<Job> successors) {
        this(id, modes, successors, JobType.STANDARD);
    }

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
        for (final Job j : successors) {
            j.isPreceededBy(this);
        }
        // prepare job modes
        for (final JobMode m : modes) {
            m.setParentJob(this);
            this.jobModes.put(m.getId(), m);
        }
        this.isSource = type == JobType.SOURCE;
        this.isSink = type == JobType.SINK;
    }

    public int getId() {
        return this.id;
    }

    public JobMode getJobMode(final int id) {
        if (!this.jobModes.containsKey(id)) {
            throw new IllegalArgumentException("Job " + this + " has not mode #" + id);
        }
        return this.jobModes.get(id);
    }

    public Collection<JobMode> getJobModes() {
        return Collections.unmodifiableCollection(this.jobModes.values());
    }

    public Project getParentProject() {
        return this.parentProject;
    }

    public Collection<Job> getPredecessors() {
        return Collections.unmodifiableCollection(this.predecessors);
    }

    public List<Job> getRecursiveSuccessors() {
        return this.recursiveSuccessors;
    }

    public List<Job> getSuccessors() {
        return this.successors;
    }

    private void isPreceededBy(final Job j) {
        this.predecessors.add(j);
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
