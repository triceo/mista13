package org.optaplanner.examples.projectscheduling.domain.solver;

import gnu.trove.procedure.TObjectIntProcedure;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;
import org.optaplanner.examples.projectscheduling.domain.JobMode;
import org.optaplanner.examples.projectscheduling.domain.Mista2013;
import org.optaplanner.examples.projectscheduling.domain.Resource;

public class JobModeStrengthWeightFactory implements SelectionSorterWeightFactory<Mista2013, JobMode> {

    public Comparable createSorterWeight(Mista2013 mista2013, JobMode jobMode) {
        return new JobModeStrengthWeight(jobMode);
    }

    private static class JobModeStrengthWeight implements Comparable<JobModeStrengthWeight> {

        private final JobMode jobMode;
        private int durationTotal;

        public JobModeStrengthWeight(JobMode jobMode) {
            this.jobMode = jobMode;
            durationTotal = 0;
            jobMode.getResourceRequirements().forEachEntry(new TObjectIntProcedure<Resource>() {
                public boolean execute(Resource resource, int requirement) {
                    if (resource.isRenewable()) {
                        durationTotal += requirement * JobModeStrengthWeight.this.jobMode.getDuration();
                    } else {
                        durationTotal += requirement;
                    }
                    return true;
                }
            });
        }

        public int compareTo(JobModeStrengthWeight other) {
            return new CompareToBuilder()
                    // less duration is stronger
                    .append(other.durationTotal, durationTotal) // Descending
                    .append(jobMode.getId(), other.jobMode.getId())
                    .toComparison();
        }

    }

}
