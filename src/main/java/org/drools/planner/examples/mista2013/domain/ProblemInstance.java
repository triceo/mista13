package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProblemInstance {

    private final List<Project> projects;

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