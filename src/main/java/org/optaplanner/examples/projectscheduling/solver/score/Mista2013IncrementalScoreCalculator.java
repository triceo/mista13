package org.optaplanner.examples.projectscheduling.solver.score;

import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.impl.score.director.incremental.AbstractIncrementalScoreCalculator;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.ProblemInstance;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;
import org.optaplanner.examples.projectscheduling.solver.score.util.CapacityTracker;
import org.optaplanner.examples.projectscheduling.solver.score.util.PrecedenceRelationsTracker;
import org.optaplanner.examples.projectscheduling.solver.score.util.ProjectPropertiesTracker;

public class Mista2013IncrementalScoreCalculator extends AbstractIncrementalScoreCalculator<ProjectSchedule> {

    private ProjectPropertiesTracker projectPropertiesTracker;

    private CapacityTracker capacityTracker;

    private PrecedenceRelationsTracker precedenceRelationsTracker;

    @Override
    public void resetWorkingSolution(final ProjectSchedule workingSolution) {
        // change to the new problem
        final ProblemInstance problem = workingSolution.getProblem();
        this.projectPropertiesTracker = new ProjectPropertiesTracker(problem);
        this.precedenceRelationsTracker = new PrecedenceRelationsTracker(workingSolution);
        this.capacityTracker = new CapacityTracker(problem);
        // insert new entities
        for (final Allocation allocation : workingSolution.getAllocations()) {
            this.insert(allocation);
        }
    }

    @Override
    public void beforeEntityAdded(final Object entity) {
        // Do nothing
    }

    @Override
    public void afterEntityAdded(final Object entity) {
        // TODO the maps/trackers should probably be adjusted
        this.insert((Allocation) entity);
    }

    @Override
    public void beforeVariableChanged(final Object entity, final String variableName) {
        this.retract((Allocation) entity);
    }

    @Override
    public void afterVariableChanged(final Object entity, final String variableName) {
        this.insert((Allocation) entity);
    }

    @Override
    public void beforeEntityRemoved(final Object entity) {
        this.retract((Allocation) entity);
    }

    @Override
    public void afterEntityRemoved(final Object entity) {
        // Do nothing
        // TODO the maps/trackers should probably be adjusted
    }

    private void insert(final Allocation entity) {
        if (!entity.isInitialized()) {
            return;
        }
        this.capacityTracker.add(entity);
        this.precedenceRelationsTracker.add(entity);
        this.projectPropertiesTracker.add(entity);
    }

    private void retract(final Allocation entity) {
        if (!entity.isInitialized()) {
            return;
        }
        this.capacityTracker.remove(entity);
        this.precedenceRelationsTracker.remove(entity);
        this.projectPropertiesTracker.remove(entity);
    }

    @Override
    public BendableScore calculateScore() {
        /*
         * validate MISTA requirements. Requirements (4, 5, 6) won't be
         * validated, as planner does that for us.
         */
        final int brokenReq1and2and3Count = this.capacityTracker.getOverusedCapacity();
        final int brokenReq7Count = this.precedenceRelationsTracker.getBrokenPrecedenceRelationsMeasure();
        // now assemble the constraints
        final int hard = brokenReq1and2and3Count + brokenReq7Count;
        final int medium = this.projectPropertiesTracker.getTotalProjectDelay();
        final int soft = this.projectPropertiesTracker.getTotalMakespan();
        return BendableScore.valueOf(new int[]{-hard}, new int[]{-medium, -soft});
    }

}
