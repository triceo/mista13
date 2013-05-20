package org.optaplanner.examples.projectscheduling.domain.solver;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;
import org.optaplanner.examples.projectscheduling.domain.ExecutionMode;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;
import org.optaplanner.examples.projectscheduling.domain.Resource;
import org.optaplanner.examples.projectscheduling.domain.ResourceRequirement;

public class ExecutionModeStrengthWeightFactory implements SelectionSorterWeightFactory<ProjectSchedule, ExecutionMode> {

    private static class ExecutionModeStrengthWeight implements Comparable<ExecutionModeStrengthWeight> {

        private final ExecutionMode executionMode;
        private int averageRatio;

        public ExecutionModeStrengthWeight(final ExecutionMode executionMode) {
            this.executionMode = executionMode;
            double totalRatio = 0;
            for (final ResourceRequirement rr : executionMode.getResourceRequirements()) {
                final double requirement = rr.getRequirement();
                final Resource resource = rr.getResource();
                totalRatio += requirement * 1000 / resource.getCapacity();
            }
            this.averageRatio = (int)Math.round(totalRatio / executionMode.getResourceRequirements().size());
        }

        @Override
        public int compareTo(final ExecutionModeStrengthWeight other) {
            return new CompareToBuilder()
                    .append(other.averageRatio, this.averageRatio)
                    .append(other.executionMode.getDuration(), this.executionMode.getDuration())
                    .append(other.executionMode.getId(), this.executionMode.getId())
                    .toComparison();
        }

    }

    @Override
    public Comparable<ExecutionModeStrengthWeight> createSorterWeight(final ProjectSchedule projectSchedule, final ExecutionMode executionMode) {
        return new ExecutionModeStrengthWeight(executionMode);
    }

}
