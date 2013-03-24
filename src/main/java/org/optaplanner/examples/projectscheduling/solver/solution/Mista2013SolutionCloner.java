package org.optaplanner.examples.projectscheduling.solver.solution;

import org.optaplanner.core.api.domain.solution.cloner.SolutionCloner;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.Mista2013;

public class Mista2013SolutionCloner implements SolutionCloner<Mista2013> {

    @Override
    public Mista2013 cloneSolution(final Mista2013 original) {
        final Mista2013 clone = new Mista2013(original.getProblem());
        clone.setScore(original.getScore());
        for (final Allocation a : original.getAllocations()) {
            final Job j = a.getJob();
            final Allocation originalAllocation = original.getAllocation(j);
            final Allocation clonedAllocation = clone.getAllocation(j);
            clonedAllocation.setExecutionMode(originalAllocation.getExecutionMode());
            clonedAllocation.setStartDate(originalAllocation.getStartDate());
        }
        return clone;
    }

}
