package org.optaplanner.examples.projectscheduling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.projectscheduling.solver.solution.Mista2013SolutionCloner;

@PlanningSolution(solutionCloner = Mista2013SolutionCloner.class)
public class ProjectSchedule implements Solution<BendableScore> {

    private final ProblemInstance problem;
    private final List<Allocation> allocations;
    private final Map<Job, Allocation> allocationsPerJob;
    private BendableScore score;

    public ProjectSchedule(final ProblemInstance input) {
        this.problem = input;
        this.allocationsPerJob = new HashMap<Job, Allocation>(input.getTotalNumberOfJobs());
        this.allocations = new ArrayList<Allocation>(input.getTotalNumberOfJobs());
        for (final Project p : input.getProjects()) {
            for (final Job j : p.getJobs()) {
                if (j.isSink() || j.isSource()) {
                    // don't create allocations from pseudo-jobs
                    continue;
                }
                final Allocation a = new Allocation(j);
                this.allocations.add(a);
                this.allocationsPerJob.put(j, a);
            }
        }
    }

    public Allocation getAllocation(final Job job) {
        final Allocation a = this.allocationsPerJob.get(job);
        if (a == null) {
            throw new IllegalArgumentException("Job allocation not found, this is a bug in our solution code: " + job);
        }
        return a;
    }

    @PlanningEntityCollectionProperty
    public List<Allocation> getAllocations() {
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
