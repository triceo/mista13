package org.optaplanner.examples.projectscheduling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
    private final Map<Integer, ExecutionMode> executionModes;

    private final int maxDuration;
    private final int maxRenewableResourceId;
    private final int maxNonRenewableResourceId;
    private final int minDuration;
    private List<Job> predecessors = new ArrayList<Job>();

    public Job(final int id, final Collection<ExecutionMode> modes, final Collection<Job> successors, final JobType type) {
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
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int maxRenewableResourceId = Integer.MIN_VALUE;
        int maxNonRenewableResourceId = Integer.MIN_VALUE;
        if (modes.size() < 1) {
            throw new IllegalArgumentException("Job has no execution modes!");
        }
        final Map<Integer, ExecutionMode> executionModes = new TreeMap<Integer, ExecutionMode>();
        for (final ExecutionMode m : modes) {
            m.setParentJob(this);
            executionModes.put(m.getId(), m);
            // determine additional properties
            max = Math.max(max, m.getDuration());
            min = Math.min(min, m.getDuration());
            for (final ResourceRequirement r : m.getResourceRequirements()) {
                if (r.getResource().isRenewable()) {
                    maxRenewableResourceId = Math.max(maxRenewableResourceId, r.getResource().getUniqueId());
                } else {
                    maxNonRenewableResourceId = Math.max(maxNonRenewableResourceId, r.getResource().getUniqueId());
                }
            }
        }
        this.executionModes = Collections.unmodifiableMap(executionModes);
        this.isSource = type == JobType.SOURCE;
        this.isSink = type == JobType.SINK;
        this.maxDuration = max;
        this.minDuration = min;
        this.maxRenewableResourceId = maxRenewableResourceId;
        this.maxNonRenewableResourceId = maxNonRenewableResourceId;
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

    public Collection<ExecutionMode> getExecutionModes() {
        return this.executionModes.values();
    }

    public ExecutionMode getExecutionMode(final int id) {
        if (!this.executionModes.containsKey(id)) {
            throw new IllegalArgumentException("Job " + this + " has not mode #" + id);
        }
        return this.executionModes.get(id);
    }

    public int getMaxDuration() {
        return this.maxDuration;
    }

    public int getMaxRenewableResourceId() {
        return this.maxRenewableResourceId;
    }

    public int getMaxNonRenewableResourceId() {
        return this.maxNonRenewableResourceId;
    }

    public int getMinDuration() {
        return this.minDuration;
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
