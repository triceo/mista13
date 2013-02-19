package org.drools.planner.examples.mista2013.solver.score;

import java.util.Collection;

import org.drools.planner.core.score.buildin.bendable.BendableScore;
import org.drools.planner.core.score.buildin.bendable.BendableScoreDefinition;
import org.drools.planner.core.score.director.incremental.AbstractIncrementalScoreCalculator;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Mista2013;
import org.drools.planner.examples.mista2013.domain.ProblemInstance;
import org.drools.planner.examples.mista2013.solver.score.util.PrecedenceRelationsTracker;
import org.drools.planner.examples.mista2013.solver.score.util.ProjectPropertiesTracker;
import org.drools.planner.examples.mista2013.solver.score.util.ResourceUsageTracker;

public class Mista2013IncrementalScoreCalculator extends AbstractIncrementalScoreCalculator<Mista2013> {

    private ProjectPropertiesTracker properties;

    private ResourceUsageTracker resourceUse;

    private PrecedenceRelationsTracker precedenceRelations;

    private final BendableScoreDefinition scoreDefinition = new BendableScoreDefinition(1, 3);

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
        final int brokenReq1and2and3Count = this.resourceUse.getSumOfOverusedResources();
        final int brokenReq7Count = this.precedenceRelations.getBrokenPrecedenceRelationsMeasure();
        // now assemble the constraints
        final int softest = this.resourceUse.getSumOfIdleResources();
        final int soft = this.properties.getTotalMakespan();
        final int medium = this.properties.getTotalProjectDelay();
        final int hard = brokenReq1and2and3Count + brokenReq7Count;
        return BendableScore.valueOf(new int[] { -hard }, new int[] { -medium, -soft, -softest });
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
        this.precedenceRelations = new PrecedenceRelationsTracker();
        this.resourceUse = new ResourceUsageTracker(problem.getMaxAllowedDueDate());
        // insert new entities
        final Collection<Allocation> allocationsToProcess = workingSolution.getAllocations();
        for (final Allocation a : allocationsToProcess) {
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
