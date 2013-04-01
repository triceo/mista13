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

    private static final double TMD_MULTIPLIER = 37;

    private static List<Integer> getStartDates(final int start, final double length) {
        final int actualLength = (int) Math.ceil(length);
        final List<Integer> startDates = new ArrayList<Integer>(actualLength);
        for (int i = 0; i < actualLength; i++) {
            startDates.add(i + start);
        }
        return Collections.unmodifiableList(startDates);
    }

    private static Set<Job> countSuccessorsRecursively(final Job j) {
        final Set<Job> result = new HashSet<Job>();
        for (final Job successor : j.getSuccessors()) {
            result.add(successor);
            result.addAll(Job.countSuccessorsRecursively(successor));
        }
        return Collections.unmodifiableSet(result);
    }

    private static int getCriticalPathDurationUntil(final Job until) {
        if (until.isSource()) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (final Job predecessor : until.getPredecessors()) {
            min = Math.min(min, Job.getCriticalPathDurationUntil(predecessor));
        }
        return min + until.getMinDuration();
    }

    private final int id;
    private Project parentProject;

    private final boolean isSource;
    private final boolean isSink;
    private final List<Job> successors;
    private final List<Job> recursiveSuccessors;
    private final List<ExecutionMode> executionModes;
    private List<Integer> startDates;

    private final int maxDuration;
    private final int maxResourceId;
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
        final List<ExecutionMode> executionModes = new ArrayList<ExecutionMode>(modes.size());
        for (final ExecutionMode m : modes) {
            m.setParentJob(this);
            executionModes.add(m.getId() - 1, m);
        }
        this.executionModes = Collections.unmodifiableList(executionModes);
        this.isSource = type == JobType.SOURCE;
        this.isSink = type == JobType.SINK;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (final ExecutionMode jm : this.executionModes) {
            max = Math.max(max, jm.getDuration());
            min = Math.min(min, jm.getDuration());
        }
        this.maxDuration = max;
        this.minDuration = min;
        // find the total amount of different resources
        int maxResourceId = Integer.MIN_VALUE;
        for (final ExecutionMode jm : this.getExecutionModes()) {
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
            /*
             * start date range starts no sooner than it teoretically can.
             */
            final int startDate = p.getReleaseDate() + Job.getCriticalPathDurationUntil(this);
            /*
             * and now the start dates are assembled
             */
            this.startDates = Job.getStartDates(startDate, Project.getTheoreticalMaxDurationAfter(this) * Job.TMD_MULTIPLIER);
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

    public List<Integer> getAvailableJobStartDates() {
        return this.startDates;
    }

    public List<Job> getSuccessors() {
        return this.successors;
    }

    public List<Job> getRecursiveSuccessors() {
        return this.recursiveSuccessors;
    }

    public Collection<ExecutionMode> getExecutionModes() {
        return this.executionModes;
    }

    public ExecutionMode getExecutionMode(final int id) {
        if (id < 1 || id > this.executionModes.size()) {
            throw new IllegalArgumentException("Job " + this + " has not mode #" + id);
        }
        return this.executionModes.get(id - 1);
    }

    public int getMaxDuration() {
        return this.maxDuration;
    }

    public int getMaxResourceId() {
        return this.maxResourceId;
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
