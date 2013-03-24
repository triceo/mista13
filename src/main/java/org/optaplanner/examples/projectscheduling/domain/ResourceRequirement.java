package org.optaplanner.examples.projectscheduling.domain;

public class ResourceRequirement {

    private final Resource resource;
    private final int requirement;

    public ResourceRequirement(final Resource resource, final int requirement) {
        this.resource = resource;
        this.requirement = requirement;
    }

    public Resource getResource() {
        return this.resource;
    }

    public int getRequirement() {
        return this.requirement;
    }

}
