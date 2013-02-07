package org.drools.planner.examples.mista2013.domain;

import java.util.Collection;

import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.value.ValueRange;
import org.drools.planner.api.domain.value.ValueRangeType;
import org.drools.planner.api.domain.variable.PlanningVariable;
import org.drools.planner.examples.mista2013.domain.solver.AllocationComparator;
import org.drools.planner.examples.mista2013.domain.solver.JobModeComparator;
import org.drools.planner.examples.mista2013.domain.solver.StartDateComparator;

@PlanningEntity(difficultyComparatorClass = AllocationComparator.class)
public class Allocation {

    private final Job job;
    private JobMode jobMode;
    private Integer startDate;
    private Integer dueDate;

    private boolean isJobModeSet = false;

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
        if (!this.isInitialized()) {
            throw new IllegalStateException("Planning entity not yet initialized.");
        }
        return this.dueDate;
    }

    public Job getJob() {
        return this.job;
    }

    @PlanningVariable(strengthComparatorClass = JobModeComparator.class)
    @ValueRange(planningEntityProperty = "jobModes", type = ValueRangeType.FROM_PLANNING_ENTITY_PROPERTY)
    public JobMode getJobMode() {
        return this.jobMode;
    }

    public Collection<JobMode> getJobModes() {
        return this.job.getJobModes();
    }

    @PlanningVariable(strengthComparatorClass = StartDateComparator.class)
    @ValueRange(planningEntityProperty = "startDates", type = ValueRangeType.FROM_PLANNING_ENTITY_PROPERTY)
    public Integer getStartDate() {
        return this.startDate;
    }

    public Collection<Integer> getStartDates() {
        return this.job.getParentProject().getAvailableJobStartDates();
    }

    public boolean isInitialized() {
        return this.isJobModeSet && this.isStartDateSet;
    }

    public void setJobMode(final JobMode jobMode) {
        this.isJobModeSet = (jobMode != null);
        this.jobMode = jobMode;
        if (this.isInitialized()) {
            this.dueDate = this.getStartDate() + this.jobMode.getDuration() - 1;
        }
    }

    public void setStartDate(final Integer startDate) {
        this.isStartDateSet = (startDate != null);
        this.startDate = startDate;
        if (this.isInitialized()) {
            this.dueDate = this.getStartDate() + this.jobMode.getDuration() - 1;
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Allocation [job=").append(this.job).append(", jobMode=").append(this.jobMode)
                .append(", startDate=").append(this.startDate).append("]");
        return builder.toString();
    }

}
