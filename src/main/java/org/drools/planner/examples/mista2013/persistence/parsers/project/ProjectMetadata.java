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
