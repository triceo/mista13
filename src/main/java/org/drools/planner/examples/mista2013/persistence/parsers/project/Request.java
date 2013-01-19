package org.drools.planner.examples.mista2013.persistence.parsers.project;

import java.util.List;

public class Request {

    private final int jobNumber;
    private final int mode;
    private final int duration;
    private final List<Integer> resources;

    public Request(final int jobNumber, final int mode, final int duration, final List<Integer> resources) {
        this.jobNumber = jobNumber;
        this.mode = mode;
        this.duration = duration;
        this.resources = resources;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getJobNumber() {
        return this.jobNumber;
    }

    public int getMode() {
        return this.mode;
    }

    public List<Integer> getResources() {
        return this.resources;
    }

}
