package org.drools.planner.examples.mista2013.persistence.parsers.project;

import java.util.List;

public class Precedence {

    private final int jobNumber;
    private final int numberOfModes;
    private final int numberOfSuccessors;
    private final List<Integer> successors;

    public Precedence(final int jobNumber, final int numberOfModes, final int numberOfSuccessors,
            final List<Integer> successors) {
        this.jobNumber = jobNumber;
        this.numberOfModes = numberOfModes;
        this.numberOfSuccessors = numberOfSuccessors;
        this.successors = successors;
    }

    public int getJobNumber() {
        return this.jobNumber;
    }

    public int getNumberOfModes() {
        return this.numberOfModes;
    }

    public int getNumberOfSuccessors() {
        return this.numberOfSuccessors;
    }

    public List<Integer> getSuccessors() {
        return this.successors;
    }

}
