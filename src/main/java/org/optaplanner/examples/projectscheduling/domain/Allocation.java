package org.optaplanner.examples.projectscheduling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.examples.projectscheduling.domain.solver.AllocationDifficultyComparator;
import org.optaplanner.examples.projectscheduling.domain.solver.ExecutionModeStrengthWeightFactory;
import org.optaplanner.examples.projectscheduling.domain.solver.StartDateStrengthComparator;

@PlanningEntity(difficultyComparatorClass = AllocationDifficultyComparator.class)
public class Allocation {

    private final Job job;

    private ExecutionMode executionMode;
    private Integer startDate;
    private int dueDate;

    private boolean isExecutionModeSet = false;
    private boolean isStartDateSet = false;

    public Allocation(final Job job) {
        this.job = job;
    }

    /**
     * The biggest time that this job is still running at.
     * 
     * @return
     */
    public int getDueDate() {
        return this.dueDate;
    }

    public Job getJob() {
        return this.job;
    }

    @PlanningVariable(valueRangeProviderRefs = {"executionModeRange"},
            strengthWeightFactoryClass = ExecutionModeStrengthWeightFactory.class)
    public ExecutionMode getExecutionMode() {
        return this.executionMode;
    }

    public void setExecutionMode(final ExecutionMode executionMode) {
        this.isExecutionModeSet = (executionMode != null);
        this.executionMode = executionMode;
        this.setDueDate(this.getStartDate(), this.executionMode);
    }

    @ValueRangeProvider(id = "executionModeRange")
    public Collection<ExecutionMode> getExecutionModeRange() {
        return this.job.getExecutionModes();
    }

    @PlanningVariable(valueRangeProviderRefs = {"startDateRange"},
            strengthComparatorClass = StartDateStrengthComparator.class)
    public Integer getStartDate() {
        return this.startDate;
    }

    public void setStartDate(final Integer startDate) {
        this.isStartDateSet = (startDate != null);
        this.startDate = startDate;
        this.setDueDate(this.getStartDate(), this.executionMode);
    }

    @ValueRangeProvider(id = "startDateRange")
    public Collection<Integer> getStartDateRange() {
        final Job job = this.getJob();
        final Project parent = job.getParentProject();
        // establish parameters; constants gathered experimentally
        final int midRangeStartDate = this.isInitialized() ? this.getStartDate() : job.getMaximalPossibleStartDateWithoutDelays();
        final int leftSidedRange = this.isInitialized() ? parent.getCriticalPathDuration() : midRangeStartDate - job.getMinimalPossibleStartDate();
        final int rightSidedRange = parent.getCriticalPathDuration();
        // infer actual range
        final int leftSpace = midRangeStartDate - leftSidedRange;
        int left = 0;
        if (this.isInitialized()) {
            /*
             * if a job starts before it possibly could, we need to make sure that we include that into the range; in
             * other words, there needs to be room for the start date to gradually get closer to the minimum in order to
             * increase the likelihood of a feasible score.
             */
            final boolean startsBeforePossible = (this.getStartDate() < job.getMinimalPossibleStartDate());
            left = Math.max(startsBeforePossible ? this.getStartDate() : job.getMinimalPossibleStartDate(), leftSpace);
        } else {
            // otherwise limit the range by the absolute minimum start date
            left = Math.max(job.getMinimalPossibleStartDate(), leftSpace);
        }
        final int right = midRangeStartDate + rightSidedRange;
        // and finally create it
        final int size = right - left;
        if (size < 1) {
            return Collections.emptyList();
        }
        final List<Integer> range = new ArrayList<Integer>(size);
        for (int i = left; i <= right; i++) {
            range.add(i);
        }
        return range;
    }

    public boolean isInitialized() {
        return this.isExecutionModeSet && this.isStartDateSet;
    }

    private void setDueDate(final Integer startDate, final ExecutionMode jm) {
        if (!this.isInitialized()) {
            return;
        }
        this.dueDate = startDate + (jm.getDuration() - 1);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Allocation [");
        if (this.job != null) {
            builder.append("job=").append(this.job).append(", ");
        }
        if (this.executionMode != null) {
            builder.append("executionMode=").append(this.executionMode.getId()).append(", ");
        }
        if (this.startDate != null) {
            builder.append("startDate=").append(this.startDate);
        }
        builder.append("]");
        return builder.toString();
    }

}
