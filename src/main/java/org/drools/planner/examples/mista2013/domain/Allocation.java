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
        for (int i = 1; i <= job.countModes(); i++) {
            // FIXME = 0 or 1???
            jobModes.add(job.getMode(i));
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

    @PlanningVariable
    @ValueRange(planningEntityProperty = "jobModes", type = ValueRangeType.FROM_PLANNING_ENTITY_PROPERTY)
    public JobMode getJobMode() {
        return this.jobMode;
    }

    public Collection<JobMode> getJobModes() {
        return this.jobModes;
    }

    @PlanningVariable
    @ValueRange(planningEntityProperty = "startDates", type = ValueRangeType.FROM_PLANNING_ENTITY_PROPERTY)
    public Integer getStartDate() {
        return this.startDate;
    }

    public Collection<Integer> getStartDates() {
        return this.startDates;
    }

    public void setJobMode(final JobMode jobMode) {
        this.jobMode = jobMode;
    }

    public void setStartDate(final Integer startDate) {
        this.startDate = startDate;
    }

}
