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

    public int getCapacity() {
        return this.capacity;
    }

    public int getId() {
        return this.id;
    }

    public boolean isGlobal() {
        return this.isGlobal;
    }

    public boolean isRenewable() {
        return this.isRenewable;
    }

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
