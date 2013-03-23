package org.optaplanner.examples.projectscheduling.domain.solver;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;
import org.optaplanner.examples.projectscheduling.domain.JobMode;
import org.optaplanner.examples.projectscheduling.domain.Mista2013;
import org.optaplanner.examples.projectscheduling.domain.Resource;
import org.optaplanner.examples.projectscheduling.domain.ResourceRequirement;

public class JobModeStrengthWeightFactory implements SelectionSorterWeightFactory<Mista2013, JobMode> {

    private static class JobModeStrengthWeight implements Comparable<JobModeStrengthWeight> {

        private final JobMode jobMode;
        private int durationTotal;

        public JobModeStrengthWeight(final JobMode jobMode) {
            this.jobMode = jobMode;
            this.durationTotal = 0;
            for (final ResourceRequirement rr : jobMode.getResourceRequirements()) {
                final int requirement = rr.getRequirement();
                final Resource resource = rr.getResource();
                if (resource.isRenewable()) {
                    this.durationTotal += requirement * this.jobMode.getDuration();
                } else {
                    this.durationTotal += requirement;
                }
            }
        }

        @Override
        public int compareTo(final JobModeStrengthWeight other) {
            return new CompareToBuilder()
                    // less duration is stronger
                    .append(other.durationTotal, this.durationTotal) // Descending
                    .append(this.jobMode.getId(), other.jobMode.getId())
                    .toComparison();
        }

    }

    @Override
    public Comparable<JobModeStrengthWeight> createSorterWeight(final Mista2013 mista2013, final JobMode jobMode) {
        return new JobModeStrengthWeight(jobMode);
    }

}
