package org.drools.planner.examples.mista2013.domain;

public class Resource {

    public static enum ResourceType {

        RENEWABLE, NONRENEWABLE, DOUBLE_CONSTRAINED;

    }

    private final int id;
    private int capacity = -1;
    private final boolean isGlobal;
    private final ResourceType type;

    public Resource(final int id) {
        this.isGlobal = true;
        this.id = id;
        this.type = ResourceType.RENEWABLE;
    }

    public Resource(final int id, final ResourceType type) {
        this.isGlobal = false;
        this.id = id;
        this.type = type;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getId() {
        return this.id;
    }

    public ResourceType getType() {
        return this.type;
    }

    public boolean isGlobal() {
        return this.isGlobal;
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

}
