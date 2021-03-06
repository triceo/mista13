package org.optaplanner.examples.projectscheduling.persistence;

import java.io.File;

import org.optaplanner.core.impl.solution.ProblemIO;
import org.optaplanner.core.impl.solution.Solution;

public class Mista2013ProblemIO implements ProblemIO {

    private final Mista2013SolutionImporter importer = new Mista2013SolutionImporter();
    private final Mista2013SolutionExporter exporter = new Mista2013SolutionExporter();

    @Override
    public String getFileExtension() {
        return "txt";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Solution read(final File inputSolutionFile) {
        return this.importer.readSolution(inputSolutionFile);
    }

    @Override
    public void write(@SuppressWarnings("rawtypes") final Solution solution, final File outputSolutionFile) {
        this.exporter.writeSolution(solution, outputSolutionFile);
    }

}
