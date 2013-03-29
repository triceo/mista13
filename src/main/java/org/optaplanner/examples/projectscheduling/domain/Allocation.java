package org.optaplanner.examples.projectscheduling.domain;

import java.util.Collection;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.value.ValueRange;
import org.optaplanner.core.api.domain.value.ValueRangeType;
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

    @PlanningVariable(strengthWeightFactoryClass = ExecutionModeStrengthWeightFactory.class)
    @ValueRange(planningEntityProperty = "executionModeRange", type = ValueRangeType.FROM_PLANNING_ENTITY_PROPERTY)
    public ExecutionMode getExecutionMode() {
        return this.executionMode;
    }

    public void setExecutionMode(final ExecutionMode executionMode) {
        this.isExecutionModeSet = (executionMode != null);
        this.executionMode = executionMode;
        this.setDueDate(this.getStartDate(), this.executionMode);
    }

    public Collection<ExecutionMode> getExecutionModeRange() {
        return this.job.getExecutionModes();
    }

    @PlanningVariable(strengthComparatorClass = StartDateStrengthComparator.class)
    @ValueRange(planningEntityProperty = "startDateRange", type = ValueRangeType.FROM_PLANNING_ENTITY_PROPERTY)
    public Integer getStartDate() {
        return this.startDate;
    }

    public void setStartDate(final Integer startDate) {
        this.isStartDateSet = (startDate != null);
        this.startDate = startDate;
        this.setDueDate(this.getStartDate(), this.executionMode);
    }

    public Collection<Integer> getStartDateRange() {
        return this.job.getAvailableJobStartDates();
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
