package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProblemInstance {

    private final List<Project> projects;

    /*
     * FIXME how does releasedate from instance data map to the same attribute
     * in project data? each have different values. currently the one from
     * instance data file overrides the one from project data file.
     */
    public ProblemInstance(final Collection<Project> projects) {
        final List<Project> tmp = new ArrayList<Project>();
        for (final Project p : projects) {
            p.setParentInstance(this);
            tmp.add(p);
        }
        this.projects = Collections.unmodifiableList(tmp);
    }

    public List<Project> getProjects() {
        return this.projects;
    }

}