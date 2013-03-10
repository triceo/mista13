package org.optaplanner.examples.mista2013.persistence.parsers.instance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class RawInstance {

    private final List<RawProject> projects;
    private final List<Integer> resourceCapacities;

    private static final int LINES_PER_PROJECT = 3;

    private static final int LINES_PER_RESOURCE = 2;

    public static RawInstance parse(final File input) {
        List<String> lines;
        try {
            lines = FileUtils.readLines(input);
        } catch (final IOException e) {
            throw new IllegalStateException("Instance file " + input + " cannot be read.");
        }
        // trim lines as a precaution
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, lines.get(i).trim());
        }
        // first line contains number of projects
        final int expectedNumberOfProjects = Integer.valueOf(lines.get(0));
        // each project has two lines; plus two lines for resources
        final int expectedProjectLines = expectedNumberOfProjects * RawInstance.LINES_PER_PROJECT;
        final int expectedTotalLines = 1 + expectedProjectLines + RawInstance.LINES_PER_RESOURCE;
        if (lines.size() != expectedTotalLines) {
            throw new IllegalStateException("Instance file " + input + " doesn't have the expected amount of lines: "
                    + expectedTotalLines);
        }
        final File baseFolder = input.getParentFile();
        final List<RawProject> projects = new ArrayList<RawProject>();
        // read the list of projects
        for (int projectNumber = 0; projectNumber < expectedNumberOfProjects; projectNumber++) {
            final int releaseDateLine = 1 + RawInstance.LINES_PER_PROJECT * projectNumber;
            final int releaseDate = Integer.valueOf(lines.get(releaseDateLine));
            final int criticalPathDurationLine = releaseDateLine + 1;
            final int criticalPathDuration = Integer.valueOf(lines.get(criticalPathDurationLine));
            final int projectFileLine = criticalPathDurationLine + 1;
            final File projectFile = new File(baseFolder, lines.get(projectFileLine));
            projects.add(new RawProject(projectNumber, releaseDate, criticalPathDuration, projectFile));
        }
        if (projects.size() != expectedNumberOfProjects) {
            throw new IllegalStateException("Instance file " + input + " doesn't have the stated number of projects: "
                    + expectedNumberOfProjects);
        }
        // read the list of resources
        final int resourceDescriptionStartsAtLine = 1 + expectedProjectLines;
        final int resourceCount = Integer.valueOf(lines.get(resourceDescriptionStartsAtLine));
        final String resourceList = lines.get(resourceDescriptionStartsAtLine + 1);
        final String[] resources = resourceList.split("\\s+");
        final int actualResourceCount = resources.length;
        if (actualResourceCount != resourceCount) {
            throw new IllegalStateException("Instance file " + input + " promises " + resourceCount
                    + " resources, but provides " + actualResourceCount);
        }
        final List<Integer> resourceCapacities = new ArrayList<Integer>();
        for (final String resource : resources) {
            resourceCapacities.add(Integer.valueOf(resource));
        }
        return new RawInstance(projects, resourceCapacities);
    }

    public RawInstance(final List<RawProject> projects, final List<Integer> resourceCapacities) {
        this.projects = Collections.unmodifiableList(projects);
        this.resourceCapacities = Collections.unmodifiableList(resourceCapacities);
    }

    public int getNumberOfProjects() {
        return this.projects.size();
    }

    public int getNumberOfResources() {
        return this.resourceCapacities.size();
    }

    public List<RawProject> getProjects() {
        return this.projects;
    }

    public List<Integer> getResourceCapacities() {
        return this.resourceCapacities;
    }

}
