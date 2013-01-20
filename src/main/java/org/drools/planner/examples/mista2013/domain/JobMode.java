package org.drools.planner.examples.mista2013.domain;

import java.util.Collections;
import java.util.Map;

public class JobMode {

    private final int id;
    private final int duration;
    private Job parentJob;
    private final Map<Resource, Integer> resourceRequirements;

    public JobMode(final int id, final int duration, final Map<Resource, Integer> resourceRequirements) {
        this.id = id;
        this.duration = duration;
        this.resourceRequirements = Collections.unmodifiableMap(resourceRequirements);
    }

    public int getDuration() {
        return this.duration;
    }

    public int getId() {
        return this.id;
    }

    public Job getParentJob() {
        return this.parentJob;
    }

    public int getResourceRequirement(final Resource r) {
        if (!this.resourceRequirements.containsKey(r)) {
            throw new IllegalArgumentException("Job mode " + this + " has no resource " + r);
        }
        return this.resourceRequirements.get(r);
    }

    protected void setParentJob(final Job parentJob) {
        if (parentJob == null) {
            this.parentJob = parentJob;
        } else {
            throw new IllegalStateException("Cannot override job mode's parent job.");
        }
    }

}
