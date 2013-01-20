package org.drools.planner.examples.mista2013.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Job {

    private final int id;
    private final Set<Job> successors;
    private final Map<Integer, JobMode> jobModes = new HashMap<Integer, JobMode>();
    private Project parentProject;

    public Job(final int id, final Collection<JobMode> modes, final Collection<Job> successors) {
        this.id = id;
        this.successors = Collections.unmodifiableSet(new HashSet<Job>(successors));
        for (final JobMode m : modes) {
            m.setParentJob(this);
            this.jobModes.put(m.getId(), m);
        }
    }

    public int getId() {
        return this.id;
    }

    public JobMode getMode(final int id) {
        if (!this.jobModes.containsKey(id)) {
            throw new IllegalArgumentException("Job " + this + " has not mode #" + id);
        }
        return this.jobModes.get(id);
    }

    public Project getParentProject() {
        return this.parentProject;
    }

    public Set<Job> getSuccessors() {
        return this.successors;
    }

    protected void setParentProject(final Project p) {
        if (this.parentProject == null) {
            this.parentProject = p;
        } else {
            throw new IllegalStateException("Cannot override parent project!");
        }
    }

}
