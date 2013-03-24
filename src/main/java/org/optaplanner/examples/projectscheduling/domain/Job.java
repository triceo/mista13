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
    private Project parentProject;

    private final boolean isSource;
    private final boolean isSink;
    private final List<Job> successors;
    private final List<Job> recursiveSuccessors;
    private final List<JobMode> jobModes;

    private final int maxDuration;
    private final int maxResourceId;
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
            j.isPrecededBy(this);
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
        int max = Integer.MIN_VALUE;
        for (final JobMode jm : this.jobModes) {
            max = Math.max(max, jm.getDuration());
        }
        this.maxDuration = max;
        // find the total amount of different resources
        int maxResourceId = Integer.MIN_VALUE;
        for (final JobMode jm : this.getJobModes()) {
            for (final ResourceRequirement r : jm.getResourceRequirements()) {
                maxResourceId = Math.max(maxResourceId, r.getResource().getUniqueId());
            }
        }
        this.maxResourceId = maxResourceId;
    }

    public int getId() {
        return this.id;
    }

    public Project getParentProject() {
        return this.parentProject;
    }

    protected void setParentProject(final Project p) {
        if (this.parentProject == null) {
            this.parentProject = p;
        } else {
            throw new IllegalStateException("Cannot override parent project!");
        }
    }

    public boolean isSink() {
        return this.isSink;
    }

    public boolean isSource() {
        return this.isSource;
    }

    public List<Job> getSuccessors() {
        return this.successors;
    }

    public List<Job> getRecursiveSuccessors() {
        return this.recursiveSuccessors;
    }

    public Collection<JobMode> getJobModes() {
        return this.jobModes;
    }

    public JobMode getJobMode(final int id) {
        if (id < 1 || id > this.jobModes.size()) {
            throw new IllegalArgumentException("Job " + this + " has not mode #" + id);
        }
        return this.jobModes.get(id - 1);
    }

    public int getMaxDuration() {
        return this.maxDuration;
    }

    public int getMaxResourceId() {
        return this.maxResourceId;
    }

    public List<Job> getPredecessors() {
        return this.predecessors;
    }

    private void isPrecededBy(final Job j) {
        final Set<Job> predecessors = new HashSet<Job>(this.predecessors);
        predecessors.add(j);
        this.predecessors = Collections.unmodifiableList(new ArrayList<Job>(predecessors));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Job [id=").append(this.id).append(", parentProject=").append(this.parentProject).append("]");
        return builder.toString();
    }

}
