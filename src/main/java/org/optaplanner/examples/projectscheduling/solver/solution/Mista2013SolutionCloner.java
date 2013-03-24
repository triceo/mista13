package org.optaplanner.examples.projectscheduling.solver.solution;

import org.optaplanner.core.api.domain.solution.cloner.SolutionCloner;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

public class Mista2013SolutionCloner implements SolutionCloner<ProjectSchedule> {

    @Override
    public ProjectSchedule cloneSolution(final ProjectSchedule original) {
        final ProjectSchedule clone = new ProjectSchedule(original.getProblem());
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
