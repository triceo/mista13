package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resource {

    public static enum ResourceType {

        RENEWABLE, NONRENEWABLE, DOUBLE_CONSTRAINED;

    }

    private static final Map<ResourceType, List<Resource>> resources = new HashMap<ResourceType, List<Resource>>();

    public static Resource getResource(final int id, final ResourceType type) {
        if (!Resource.resources.containsKey(type)) {
            Resource.resources.put(type, new ArrayList<Resource>());
        }
        final List<Resource> resourcePerType = Resource.resources.get(type);
        if (resourcePerType.get(id) == null) {
            resourcePerType.add(id, new Resource(id, type));
        }
        return resourcePerType.get(id);
    }

    private final int id;
    private final ResourceType type;

    private Resource(final int id, final ResourceType type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public ResourceType getType() {
        return this.type;
    }

}
