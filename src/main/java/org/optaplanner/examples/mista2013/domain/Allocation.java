package org.optaplanner.examples.mista2013.domain;

import java.util.Collection;

import org.optaplanner.api.domain.entity.PlanningEntity;
import org.optaplanner.api.domain.value.ValueRange;
import org.optaplanner.api.domain.value.ValueRangeType;
import org.optaplanner.api.domain.variable.PlanningVariable;
import org.optaplanner.examples.mista2013.domain.solver.AllocationComparator;
import org.optaplanner.examples.mista2013.domain.solver.JobModeComparator;
import org.optaplanner.examples.mista2013.domain.solver.StartDateComparator;

@PlanningEntity(difficultyComparatorClass = AllocationComparator.class)
public class Allocation {

    private final Job job;
    private JobMode jobMode;
    private Integer startDate;
    private int dueDate;

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

    private void setDueDate(final Integer startDate, final JobMode jm) {
        if (!this.isInitialized()) {
            return;
        }
        this.dueDate = startDate + (jm.getDuration() - 1);
    }

    public void setJobMode(final JobMode jobMode) {
        this.isJobModeSet = (jobMode != null);
        this.jobMode = jobMode;
        this.setDueDate(this.getStartDate(), this.jobMode);
    }

    public void setStartDate(final Integer startDate) {
        this.isStartDateSet = (startDate != null);
        this.startDate = startDate;
        this.setDueDate(this.getStartDate(), this.jobMode);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Allocation [");
        if (job != null) {
            builder.append("job=").append(job).append(", ");
        }
        if (jobMode != null) {
            builder.append("jobMode=").append(jobMode.getId()).append(", ");
        }
        if (startDate != null) {
            builder.append("startDate=").append(startDate);
        }
        builder.append("]");
        return builder.toString();
    }

}
