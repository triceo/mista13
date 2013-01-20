package org.drools.planner.examples.mista2013.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProblemInstance {

    private final Set<Project> projects;

    /*
     * FIXME how do duedate/criticalPathDuration from instance data map to 
     * attributes in project data? both have duedate, each have different values.
     */
    public ProblemInstance(final Collection<Project> projects) {
        final Set<Project> tmp = new HashSet<Project>();
        for (final Project p : projects) {
            p.setParentInstance(this);
            tmp.add(p);
        }
        this.projects = Collections.unmodifiableSet(tmp);
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

}
