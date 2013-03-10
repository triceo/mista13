package org.optaplanner.examples.mista2013.persistence.parsers.instance;

import java.io.File;

import org.optaplanner.examples.mista2013.persistence.parsers.project.RawProjectData;

public class RawProject {

    private final int id;
    private final int releaseDate;
    private final int criticalPathDuration;
    private final File projectFile;
    private RawProjectData data;

    public RawProject(final int id, final int releaseDate, final int criticalPathDuration, final File projectFile) {
        if (id < 0) {
            throw new IllegalArgumentException("Project id must be >= 0.");
        }
        this.id = id;
        if (releaseDate < 0) {
            throw new IllegalArgumentException("Cannot have release dates < 0: " + releaseDate);
        }
        if (criticalPathDuration < 1) {
            throw new IllegalArgumentException("Cannot have critical path shorter than 1: " + criticalPathDuration);
        }
        if (!projectFile.exists() || !projectFile.canRead()) {
            throw new IllegalStateException("Invalid project file: " + projectFile);
        }
        this.releaseDate = releaseDate;
        this.criticalPathDuration = criticalPathDuration;
        this.projectFile = projectFile;
    }

    public int getCriticalPathDuration() {
        return this.criticalPathDuration;
    }

    public int getId() {
        return this.id;
    }

    public RawProjectData getProjectData() {
        if (this.data == null) {
            this.data = RawProjectData.parse(this.projectFile);
        }
        return this.data;
    }

    public int getReleaseDate() {
        return this.releaseDate;
    }

}
