package org.drools.planner.examples.mista2013.persistence.parsers.project;

import java.util.Collections;
import java.util.List;

public class Precedence {

    private final int jobNumber;
    private final int numberOfModes;
    private final int numberOfSuccessors;
    private final List<Integer> successors;

    public Precedence(final int jobNumber, final int numberOfModes, final int numberOfSuccessors,
            final List<Integer> successors) {
        if (jobNumber < 0) {
            throw new IllegalArgumentException("Job number must not be sub-zero.");
        }
        this.jobNumber = jobNumber;
        if (numberOfModes < 1) {
            throw new IllegalArgumentException("There must be at least one mode.");
        }
        this.numberOfModes = numberOfModes;
        if (numberOfSuccessors < 0) {
            throw new IllegalArgumentException("There cannot be less than zero successors.");
        }
        this.numberOfSuccessors = numberOfSuccessors;
        final int actualSuccessors = successors.size();
        if (actualSuccessors != this.numberOfSuccessors) {
            throw new IllegalArgumentException(this.numberOfSuccessors + " successors expected, but were "
                    + actualSuccessors + ".");
        }
        this.successors = Collections.unmodifiableList(successors);
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
