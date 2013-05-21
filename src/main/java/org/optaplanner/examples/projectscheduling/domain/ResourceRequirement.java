package org.optaplanner.examples.projectscheduling.domain;

public class ResourceRequirement {

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResourceRequirement [resource=" + resource + ", requirement=" + requirement + "]";
    }

    private final Resource resource;
    private final int requirement;

    public ResourceRequirement(final Resource resource, final int requirement) {
        if (requirement < 1) {
            throw new IllegalArgumentException("Zero requirements are useless, you needn't create those.");
        }
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
