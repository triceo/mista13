package org.optaplanner.examples.projectscheduling.domain;

public class ResourceRequirement {

    private final Resource resource;
    private final int requirement;

    public ResourceRequirement(final Resource r, final int requirement) {
        this.resource = r;
        this.requirement = requirement;
    }

    public int getRequirement() {
        return this.requirement;
    }

    public Resource getResource() {
        return this.resource;
    }

}
