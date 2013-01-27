package org.drools.planner.examples.mista2013.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;

import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.variable.PlanningVariable;
import org.drools.planner.api.domain.variable.ValueRange;
import org.drools.planner.api.domain.variable.ValueRangeType;

@PlanningEntity
public class Allocation {

    private final Job job;
    private JobMode jobMode;
    private Integer startDate;

    private final Collection<JobMode> jobModes;

    private final Collection<Integer> startDates;

    public Allocation(final Job job) {
        this.job = job;
        // list available job modes
        final Collection<JobMode> jobModes = new HashSet<JobMode>();
        for (final JobMode jm : job.getJobModes()) {
            jobModes.add(jm);
        }
        this.jobModes = Collections.unmodifiableCollection(jobModes);
        // list available start dates
        final Collection<Integer> startDates = new TreeSet<Integer>();
        for (int i = job.getParentProject().getReleaseDate(); i < job.getParentProject().getHorizon(); i++) {
            startDates.add(i);
        }
        this.startDates = Collections.unmodifiableCollection(startDates);
    }

    public Job getJob() {
        return this.job;
    }

    // FIXME convert to from_entity when it works
    @PlanningVariable
    @ValueRange(solutionProperty = "jobModes", type = ValueRangeType.FROM_SOLUTION_PROPERTY)
    public JobMode getJobMode() {
        return this.jobMode;
    }

    public Collection<JobMode> getJobModes() {
        return this.jobModes;
    }

    // FIXME convert to from_entity when it works
    @PlanningVariable
    @ValueRange(solutionProperty = "startDates", type = ValueRangeType.FROM_SOLUTION_PROPERTY)
    public Integer getStartDate() {
        return this.startDate;
    }

    public Collection<Integer> getStartDates() {
        return this.startDates;
    }

    public boolean isInitialized() {
        return !(this.jobMode == null || this.startDate == null);
    }

    public void setJobMode(final JobMode jobMode) {
        this.jobMode = jobMode;
    }

    public void setStartDate(final Integer startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Allocation [job=").append(this.job).append(", jobMode=").append(this.jobMode)
                .append(", startDate=").append(this.startDate).append("]");
        return builder.toString();
    }

}
