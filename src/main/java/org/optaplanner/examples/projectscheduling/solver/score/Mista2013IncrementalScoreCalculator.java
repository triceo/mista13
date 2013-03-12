package org.optaplanner.examples.projectscheduling.solver.score;

import org.optaplanner.core.score.buildin.bendable.BendableScore;
import org.optaplanner.core.score.director.incremental.AbstractIncrementalScoreCalculator;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Mista2013;
import org.optaplanner.examples.projectscheduling.domain.ProblemInstance;
import org.optaplanner.examples.projectscheduling.solver.score.util.PrecedenceRelationsTracker;
import org.optaplanner.examples.projectscheduling.solver.score.util.ProjectPropertiesTracker;
import org.optaplanner.examples.projectscheduling.solver.score.util.CapacityTracker;

public class Mista2013IncrementalScoreCalculator extends AbstractIncrementalScoreCalculator<Mista2013> {

    private ProjectPropertiesTracker properties;

    private CapacityTracker resourceUse;

    private PrecedenceRelationsTracker precedenceRelations;

    @Override
    public void afterAllVariablesChanged(final Object entity) {
        this.insert((Allocation) entity);
    }

    @Override
    public void afterEntityAdded(final Object entity) {
        this.insert((Allocation) entity);
    }

    @Override
    public void afterEntityRemoved(final Object entity) {
        // Do nothing
    }

    @Override
    public void afterVariableChanged(final Object entity, final String variableName) {
        this.insert((Allocation) entity);
    }

    @Override
    public void beforeAllVariablesChanged(final Object entity) {
        this.retract((Allocation) entity);
    }

    @Override
    public void beforeEntityAdded(final Object entity) {
        // Do nothing
    }

    @Override
    public void beforeEntityRemoved(final Object entity) {
        this.retract((Allocation) entity);
    }

    @Override
    public void beforeVariableChanged(final Object entity, final String variableName) {
        this.retract((Allocation) entity);
    }
    
    @Override
    public BendableScore calculateScore() {
        /*
         * validate MISTA requirements. Requirements (4, 5, 6) won't be
         * validated, as planner does that for us.
         */
        final int brokenReq1and2and3Count = this.resourceUse.getOverusedCapacity();
        final int brokenReq7Count = this.precedenceRelations.getBrokenPrecedenceRelationsMeasure();
        // find out how many resources are used
        final int idle = this.resourceUse.getIdleCapacity();
        final int total = this.resourceUse.getTotalCapacity();
        final int ratio = (int)((100000d * idle) / total);
        // now assemble the constraints
        final int soft = this.properties.getTotalMakespan();
        final int medium = this.properties.getTotalProjectDelay();
        final int hard = brokenReq1and2and3Count + brokenReq7Count;
        return BendableScore.valueOf(new int[] { -hard }, new int[] { -medium, -soft, -ratio });
    }

    private void insert(final Allocation entity) {
        if (!entity.isInitialized()) {
            return;
        }
        this.resourceUse.add(entity);
        this.precedenceRelations.add(entity);
        this.properties.add(entity);
    }

    @Override
    public void resetWorkingSolution(final Mista2013 workingSolution) {
        // change to the new problem
        final ProblemInstance problem = workingSolution.getProblem();
        this.properties = new ProjectPropertiesTracker(problem);
        this.precedenceRelations = new PrecedenceRelationsTracker(workingSolution);
        this.resourceUse = new CapacityTracker(problem.getMaxAllowedDueDate());
        // insert new entities
        for (final Allocation a : workingSolution.getAllocations()) {
            this.insert(a);
        }
    }

    private void retract(final Allocation entity) {
        if (!entity.isInitialized()) {
            return;
        }
        this.resourceUse.remove(entity);
        this.precedenceRelations.remove(entity);
        this.properties.remove(entity);
    }
}
