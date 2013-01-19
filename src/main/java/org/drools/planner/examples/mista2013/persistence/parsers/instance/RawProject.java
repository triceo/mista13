package org.drools.planner.examples.mista2013.persistence.parsers.instance;

import java.io.File;

public class RawProject {
    
    private final int releaseDate;
    private final int criticalPathDuration;
    private final File projectFile;

    public int getReleaseDate() {
        return releaseDate;
    }

    public int getCriticalPathDuration() {
        return criticalPathDuration;
    }

    public File getProjectFile() {
        return projectFile;
    }

    public RawProject(final int releaseDate, final int criticalPathDuration, final File projectFile) {
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
    
}
