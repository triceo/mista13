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
        private int durationTotal;

        public ExecutionModeStrengthWeight(final ExecutionMode executionMode) {
            this.executionMode = executionMode;
            this.durationTotal = 0;
            for (final ResourceRequirement rr : executionMode.getResourceRequirements()) {
                final int requirement = rr.getRequirement();
                final Resource resource = rr.getResource();
                if (resource.isRenewable()) {
                    this.durationTotal += requirement * this.executionMode.getDuration();
                } else {
                    this.durationTotal += requirement;
                }
            }
        }

        @Override
        public int compareTo(final ExecutionModeStrengthWeight other) {
            return new CompareToBuilder()
                    // less duration is stronger
                    .append(other.durationTotal, this.durationTotal) // Descending
                    .append(this.executionMode.getId(), other.executionMode.getId())
                    .toComparison();
        }

    }

    @Override
    public Comparable<ExecutionModeStrengthWeight> createSorterWeight(final ProjectSchedule projectSchedule, final ExecutionMode executionMode) {
        return new ExecutionModeStrengthWeight(executionMode);
    }

}
