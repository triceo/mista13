package org.optaplanner.examples.mista2013.persistence.parsers.project;

import java.util.Collections;
import java.util.List;

public class Request {

    private final int jobNumber;
    private final int mode;
    private final int duration;
    private final List<Integer> resources;

    public Request(final int jobNumber, final int mode, final int duration, final List<Integer> resources) {
        if (jobNumber < 0) {
            throw new IllegalArgumentException("Job number must not be sub-zero.");
        }
        this.jobNumber = jobNumber;
        if (mode < 1) {
            throw new IllegalArgumentException("Mode number must not be lower than one.");
        }
        this.mode = mode;
        if (duration < 0) {
            throw new IllegalArgumentException("Job duration must not be lower than zero.");
        }
        this.duration = duration;
        this.resources = Collections.unmodifiableList(resources);
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
