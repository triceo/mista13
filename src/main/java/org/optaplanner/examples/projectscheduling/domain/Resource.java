package org.optaplanner.examples.projectscheduling.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class Resource {

    private static final AtomicInteger renewableIdGenerator = new AtomicInteger(0);
    private static final AtomicInteger nonRenewableIdGenerator = new AtomicInteger(0);

    public static enum ResourceType {
        RENEWABLE,
        NONRENEWABLE,
        DOUBLE_CONSTRAINED;
    }

    private final int id;
    private final int uniqueId;

    private final boolean global;
    private final boolean renewable;
    private int capacity = -1;

    public Resource(final int id) {
        this.global = true;
        this.id = id;
        this.uniqueId = Resource.renewableIdGenerator.getAndIncrement();
        this.renewable = true;
    }

    public Resource(final int id, final ResourceType type) {
        if (type == ResourceType.DOUBLE_CONSTRAINED) {
            throw new IllegalArgumentException("Double-constrained resources aren't yet supported.");
        }
        this.global = false;
        this.id = id;
        this.renewable = (type == ResourceType.RENEWABLE);
        this.uniqueId = this.renewable ? Resource.renewableIdGenerator.getAndIncrement() : Resource.nonRenewableIdGenerator.getAndIncrement();
    }

    public int getId() {
        return this.id;
    }

    /**
     * To be used as a key in maps or arrays for super-fast indexing by
     * resource.
     * 
     * @return Unique number, generated when the object instance is constructed.
     *         No two resources in the same JVM ever get the same.
     */
    public int getUniqueId() {
        return this.uniqueId;
    }

    public boolean isGlobal() {
        return this.global;
    }

    public boolean isRenewable() {
        return this.renewable;
    }

    public int getCapacity() {
        return this.capacity;
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
        builder.append(this.renewable ? "R" : "N");
        builder.append(this.global ? "G" : "L");
        builder.append(this.id);
        builder.append("-");
        builder.append(this.capacity);
        return builder.toString();
    }

}
