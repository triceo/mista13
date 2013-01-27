package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JobMode {

    private final int id;

    private final int duration;

    private Job parentJob;
    private final Map<Resource, Integer> resourceRequirements;
    private final List<Resource> resources; // much better performance than
                                            // resourceRequirements.keySet()

    public JobMode(final int id, final int duration, final Map<Resource, Integer> resourceRequirements) {
        if (id < 0) {
            throw new IllegalArgumentException("Job mode id must be >= 0.");
        }
        this.id = id;
        this.duration = duration;
        this.resourceRequirements = Collections.unmodifiableMap(resourceRequirements);
        this.resources = Collections.unmodifiableList(new ArrayList<Resource>(resourceRequirements.keySet()));
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

    public Collection<Resource> getResources() {
        return this.resources;
    }

    protected void setParentJob(final Job parentJob) {
        if (this.parentJob == null) {
            this.parentJob = parentJob;
        } else {
            throw new IllegalStateException("Cannot override job mode's parent job.");
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("JobMode [id=").append(this.id).append(", duration=").append(this.duration)
                .append(", parentJob=").append(this.parentJob.getId()).append(", resourceRequirements=")
                .append(this.resourceRequirements).append("]");
        return builder.toString();
    }

}
