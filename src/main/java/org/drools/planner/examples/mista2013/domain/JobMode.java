package org.drools.planner.examples.mista2013.domain;

import java.util.Collections;
import java.util.Map;

public class JobMode {

    private final int id;

    private final int duration;

    private Job parentJob;
    private final Map<Resource, Integer> resourceRequirements;

    public JobMode(final int id, final int duration, final Map<Resource, Integer> resourceRequirements) {
        if (id < 0) {
            throw new IllegalArgumentException("Job mode id must be >= 0.");
        }
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
    
    public Map<Resource, Integer> getResourceRequirements() {
        return this.resourceRequirements;
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
