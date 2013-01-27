package org.drools.planner.examples.mista2013.domain;

import java.util.Collection;
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof JobMode)) {
            return false;
        }
        final JobMode other = (JobMode) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.parentJob == null) {
            if (other.parentJob != null) {
                return false;
            }
        } else if (!this.parentJob.equals(other.parentJob)) {
            return false;
        }
        return true;
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
        return Collections.unmodifiableCollection(this.resourceRequirements.keySet());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        result = prime * result + ((this.parentJob == null) ? 0 : this.parentJob.hashCode());
        return result;
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
