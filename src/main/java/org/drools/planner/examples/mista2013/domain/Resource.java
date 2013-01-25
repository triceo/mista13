package org.drools.planner.examples.mista2013.domain;

public class Resource {

    public static enum ResourceType {

        RENEWABLE, NONRENEWABLE, DOUBLE_CONSTRAINED;

    }

    private final int id;

    private int capacity = -1;
    private final boolean isGlobal;
    private final boolean isRenewable;

    public Resource(final int id) {
        this.isGlobal = true;
        this.id = id;
        this.isRenewable = true;
    }

    public Resource(final int id, final ResourceType type) {
        if (type == ResourceType.DOUBLE_CONSTRAINED) {
            throw new IllegalArgumentException("Double-constrained resources aren't yet supported.");
        }
        this.isGlobal = false;
        this.id = id;
        this.isRenewable = (type == ResourceType.RENEWABLE);
    }

    /**
     * Resources are equal when they share globality, renewability and ID.
     * Capacity is irrelevant to equality.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Resource)) {
            return false;
        }
        final Resource other = (Resource) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.isGlobal != other.isGlobal) {
            return false;
        }
        if (this.isRenewable != other.isRenewable) {
            return false;
        }
        return true;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        result = prime * result + (this.isGlobal ? 1231 : 1237);
        result = prime * result + (this.isRenewable ? 1231 : 1237);
        return result;
    }

    public boolean isGlobal() {
        return this.isGlobal;
    }

    public boolean isRenewable() {
        return this.isRenewable;
    }

    /*
     * FIXME resources seem to have a fixed and a variable capacity. which is
     * which, how are they different?
     */
    public void setCapacity(final int capacity) {
        if (this.capacity == -1) {
            this.capacity = capacity;
        } else {
            throw new IllegalStateException("Cannot override an already set resource capacity.");
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.isGlobal ? "G" : "L");
        builder.append("R");
        builder.append(this.isRenewable ? "R" : "N");
        builder.append(this.id);
        builder.append("-");
        builder.append(this.capacity);
        return builder.toString();
    }

}
