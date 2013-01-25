package org.drools.planner.examples.mista2013.solver.solution;

import org.drools.planner.api.domain.solution.cloner.SolutionCloner;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.Mista2013;
import org.drools.planner.examples.mista2013.domain.Project;

public class Mista2013SolutionCloner implements SolutionCloner<Mista2013> {

    @Override
    public Mista2013 cloneSolution(final Mista2013 original) {
        final Mista2013 clone = new Mista2013(original.getProblem());
        clone.setScore(original.getScore());
        for (final Project p : clone.getProblem().getProjects()) {
            for (final Job j : p.getJobs()) {
                final Allocation originalAllocation = original.getAllocation(j);
                final Allocation clonedAllocation = clone.getAllocation(j);
                clonedAllocation.setJobMode(originalAllocation.getJobMode());
                clonedAllocation.setStartDate(originalAllocation.getStartDate());
            }
        }
        return clone;
    }

}
