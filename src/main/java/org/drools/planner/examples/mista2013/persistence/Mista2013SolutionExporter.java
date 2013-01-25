package org.drools.planner.examples.mista2013.persistence;

import java.io.IOException;

import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.common.persistence.AbstractTxtSolutionExporter;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.Mista2013;
import org.drools.planner.examples.mista2013.domain.Project;

// TODO needs to be implemented
public class Mista2013SolutionExporter extends AbstractTxtSolutionExporter {

    private final class Mista2013TxtOutputBuilder extends TxtOutputBuilder {

        private Mista2013 solution;

        @Override
        public void setSolution(final Solution solution) {
            if (!(solution instanceof Mista2013)) {
                throw new IllegalArgumentException("Please provide a valid Mista2013 solution instance.");
            }
            this.solution = (Mista2013) solution;
        }

        @Override
        public void writeSolution() throws IOException {
            for (final Project p : this.solution.getProblem().getProjects()) {
                final int id = p.getId();
                for (final Job j : p.getJobs()) {
                    final Allocation a = this.solution.getAllocation(j);
                    this.bufferedWriter.write(String.valueOf(id));
                    this.bufferedWriter.write(" ");
                    this.bufferedWriter.write(String.valueOf(j.getId() - 1));
                    this.bufferedWriter.write(" ");
                    this.bufferedWriter.write(String.valueOf(a.getJobMode().getId() - 1));
                    this.bufferedWriter.write(" ");
                    this.bufferedWriter.write(String.valueOf(a.getStartDate()));
                    this.bufferedWriter.newLine();
                }
            }

        }

    }

    public Mista2013SolutionExporter() {
        super(new Mista2013DaoImpl());
    }

    @Override
    public TxtOutputBuilder createTxtOutputBuilder() {
        return new Mista2013TxtOutputBuilder();
    }

}
