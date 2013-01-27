package org.drools.planner.examples.mista2013.persistence;

import java.io.IOException;

import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.common.persistence.AbstractTxtSolutionExporter;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.JobMode;
import org.drools.planner.examples.mista2013.domain.Mista2013;
import org.drools.planner.examples.mista2013.domain.Project;

// TODO needs to be implemented
public class Mista2013SolutionExporter extends AbstractTxtSolutionExporter {

    private final class Mista2013TxtOutputBuilder extends TxtOutputBuilder {

        private Mista2013 solution;

        private int getMaxDueDate(final Mista2013 solution, Project p) {
            int max = Integer.MIN_VALUE;
            for (final Allocation a : solution.getAllocations()) {
                if (a.getJob().getParentProject() != p) {
                    continue;
                }
                max = Math.max(max, a.getStartDate() + a.getJobMode().getDuration());
            }
            return max;
        }

        private int getMinStartDate(final Mista2013 solution, Project p) {
            int min = Integer.MAX_VALUE;
            for (final Allocation a : solution.getAllocations()) {
                if (a.getJob().getParentProject() != p) {
                    continue;
                }
                min = Math.min(min, a.getStartDate());
            }
            return min;
        }

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
                    int startDate = -1;
                    JobMode jobMode = j.getMode(1);
                    if (j.isSource()) {
                        startDate = this.getMinStartDate(this.solution, p);
                    } else if (j.isSink()) {
                        startDate = this.getMaxDueDate(this.solution, p);
                    } else {
                        final Allocation a = this.solution.getAllocation(j);
                        startDate = a.getStartDate();
                        jobMode = a.getJobMode();
                    }
                    this.bufferedWriter.write(String.valueOf(id));
                    this.bufferedWriter.write(" ");
                    this.bufferedWriter.write(String.valueOf(j.getId() - 1));
                    this.bufferedWriter.write(" ");
                    this.bufferedWriter.write(String.valueOf(jobMode.getId() - 1));
                    this.bufferedWriter.write(" ");
                    this.bufferedWriter.write(String.valueOf(startDate));
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