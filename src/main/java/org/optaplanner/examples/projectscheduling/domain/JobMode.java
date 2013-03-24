package org.optaplanner.examples.projectscheduling.domain;

import java.util.Collection;
import java.util.Collections;

public class JobMode {

    private final int id;
    private Job parentJob;

    private final int duration;
    private final Collection<ResourceRequirement> resourceRequirements;

    public JobMode(final int id, final int duration, final Collection<ResourceRequirement> resourceRequirements) {
        if (id < 0) {
            throw new IllegalArgumentException("Job mode id must be >= 0.");
        }
        this.id = id;
        this.duration = duration;
        this.resourceRequirements = Collections.unmodifiableCollection(resourceRequirements);
    }

    public int getId() {
        return this.id;
    }

    public Job getParentJob() {
        return this.parentJob;
    }

    protected void setParentJob(final Job parentJob) {
        if (this.parentJob == null) {
            this.parentJob = parentJob;
        } else {
            throw new IllegalStateException("Cannot override job mode's parent job.");
        }
    }

    public int getDuration() {
        return this.duration;
    }

    public Collection<ResourceRequirement> getResourceRequirements() {
        return this.resourceRequirements;
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
