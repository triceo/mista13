package org.drools.planner.examples.mista2013.persistence.parsers.project;

public class SituationMetadata {

    private final int projectCount;
    private final int jobCount;
    private final int horizon;
    private final int renewableResourceCount;
    private final int nonRenewableResourceCount;
    private final int doublyConstrainedResourceCount;

    public SituationMetadata(final int projectCount, final int jobCount, final int horizon,
            final int renewableResourceCount, final int nonRenewableResourceCount,
            final int doubleConstrainedResourceCount) {
        if (projectCount != 1) {
            throw new IllegalArgumentException("Unsupported project count: " + projectCount);
        }
        if (jobCount < 1) {
            throw new IllegalArgumentException("The project is empty!");
        }
        if (horizon < 1) {
            throw new IllegalArgumentException("Project horizon must be larger than zero!");
        }
        if (renewableResourceCount < 0 || nonRenewableResourceCount < 0 || doubleConstrainedResourceCount < 0) {
            throw new IllegalArgumentException("Number of resources can never be sub-zero.");
        }
        this.projectCount = projectCount;
        this.jobCount = jobCount;
        this.horizon = horizon;
        this.renewableResourceCount = renewableResourceCount;
        this.nonRenewableResourceCount = nonRenewableResourceCount;
        this.doublyConstrainedResourceCount = doubleConstrainedResourceCount;
    }

    public int getDoublyConstrainedResourceCount() {
        return this.doublyConstrainedResourceCount;
    }

    public int getHorizon() {
        return this.horizon;
    }

    public int getJobCount() {
        return this.jobCount;
    }

    public int getNonRenewableResourceCount() {
        return this.nonRenewableResourceCount;
    }

    public int getProjectCount() {
        return this.projectCount;
    }

    public int getRenewableResourceCount() {
        return this.renewableResourceCount;
    }

}
