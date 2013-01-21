package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resource {

    public static enum ResourceType {

        RENEWABLE, NONRENEWABLE, DOUBLE_CONSTRAINED;

    }

    // TODO needs to be fixed for the situation where local resources are
    // created before global resources, thus not being overriden
    private static final Map<ResourceType, List<Resource>> localResources = new HashMap<ResourceType, List<Resource>>();
    private static final Map<Integer, Resource> globalResources = new HashMap<Integer, Resource>();

    public static Resource getGlobalResource(final int id) {
        if (!Resource.globalResources.containsKey(id)) {
            Resource.globalResources.put(id, new Resource(id, ResourceType.RENEWABLE, true));
        }
        return Resource.globalResources.get(id);
    }

    public static Resource getLocalResource(final int id, final ResourceType type) {
        if (type == ResourceType.RENEWABLE && Resource.globalResources.containsKey(id)) {
            // global renewable resource of the same id already exists;
            throw new IllegalArgumentException("Cannot override global resource.");
        }
        if (!Resource.localResources.containsKey(type)) {
            Resource.localResources.put(type, new ArrayList<Resource>());
        }
        final List<Resource> resourcePerType = Resource.localResources.get(type);
        if (resourcePerType.get(id) == null) {
            resourcePerType.add(id, new Resource(id, type, false));
        }
        return resourcePerType.get(id);
    }

    private final int id;
    private final boolean isGlobal;
    private final ResourceType type;

    private Resource(final int id, final ResourceType type, final boolean isGlobal) {
        this.isGlobal = isGlobal;
        this.id = id;
        this.type = type;
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

}
