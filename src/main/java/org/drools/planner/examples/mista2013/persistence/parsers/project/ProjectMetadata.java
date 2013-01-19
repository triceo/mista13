package org.drools.planner.examples.mista2013.persistence.parsers.project;

public class ProjectMetadata {

    private final int number;
    private final int jobCount;
    private final int releaseDate;
    private final int dueDate;
    private final int tardinessCost;
    private final int mpmTime;

    public ProjectMetadata(final int number, final int jobCount, final int releaseDate, final int dueDate,
            final int tardinessCost, final int mpmTime) {
        if (number < 1) {
            throw new IllegalArgumentException("Project number too low: " + number);
        }
        if (jobCount < 1) {
            throw new IllegalArgumentException("Project is empty.");
        }
        if (releaseDate < 0) {
            throw new IllegalArgumentException("Project release date may not be in the past.");
        }
        if (dueDate < 1) {
            throw new IllegalArgumentException("Project due date must be in the future.");
        }
        if (tardinessCost < 0) {
            throw new IllegalArgumentException("Tardiness cost cannot be lower than zero.");
        }
        // FIXME what is MPM Time and how to validate it???
        this.number = number;
        this.jobCount = jobCount;
        this.releaseDate = releaseDate;
        this.dueDate = dueDate;
        this.tardinessCost = tardinessCost;
        this.mpmTime = mpmTime;
    }

    public int getDueDate() {
        return this.dueDate;
    }

    public int getJobCount() {
        return this.jobCount;
    }

    public int getMpmTime() {
        return this.mpmTime;
    }

    public int getNumber() {
        return this.number;
    }

    public int getReleaseDate() {
        return this.releaseDate;
    }

    public int getTardinessCost() {
        return this.tardinessCost;
    }

}
