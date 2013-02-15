package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.api.domain.solution.PlanningSolution;
import org.drools.planner.core.score.buildin.bendable.BendableScore;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.mista2013.solver.solution.Mista2013SolutionCloner;

@PlanningSolution(solutionCloner = Mista2013SolutionCloner.class)
public class Mista2013 implements Solution<BendableScore> {

    private final ProblemInstance problem;
    private final Collection<Allocation> allocations;
    /**
     * Much better for performance than HashMap.
     */
    private final Allocation[][] allocationsIndexed;
    private BendableScore score;

    public Mista2013(final ProblemInstance input) {
        this.problem = input;
        final List<Allocation> allocations = new ArrayList<Allocation>();
        this.allocationsIndexed = new Allocation[input.getProjects().size()][];
        for (final Project p : input.getProjects()) {
            this.allocationsIndexed[p.getId()] = new Allocation[p.getJobs().size()];
            for (final Job j : p.getJobs()) {
                if (j.isSink() || j.isSource()) {
                    // don't create allocations from pseudo-jobs
                    continue;
                }
                final Allocation a = new Allocation(j);
                allocations.add(a);
                this.allocationsIndexed[p.getId()][j.getId()] = a;
            }
        }
        this.allocations = Collections.unmodifiableList(allocations);
    }

    public Allocation getAllocation(final Job job) {
        final Allocation a = this.allocationsIndexed[job.getParentProject().getId()][job.getId()];
        if (a == null) {
            throw new IllegalArgumentException("Job allocation not found, this is a bug in our solution code: " + job);
        }
        return a;
    }

    @PlanningEntityCollectionProperty
    public Collection<Allocation> getAllocations() {
        return this.allocations;
    }

    public ProblemInstance getProblem() {
        return this.problem;
    }

    @Override
    public Collection<? extends Object> getProblemFacts() {
        final Collection<Object> facts = new ArrayList<Object>();
        facts.add(this.problem);
        return facts;
    }

    @Override
    public BendableScore getScore() {
        return this.score;
    }

    @Override
    public void setScore(final BendableScore score) {
        this.score = score;
    }

}
