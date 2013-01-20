package org.drools.planner.examples.mista2013.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProblemInstance {

    private final int releaseDate;
    private final int criticalPathDuration;
    private final Set<Project> projects;

    public ProblemInstance(final int releaseDate, final int criticalPathDuration, final Collection<Project> projects) {
        this.releaseDate = releaseDate;
        this.criticalPathDuration = criticalPathDuration;
        final Set<Project> tmp = new HashSet<Project>();
        for (final Project p : projects) {
            p.setParentInstance(this);
            tmp.add(p);
        }
        this.projects = Collections.unmodifiableSet(tmp);
    }

    public int getCriticalPathDuration() {
        return this.criticalPathDuration;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public int getReleaseDate() {
        return this.releaseDate;
    }

}
