package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.api.domain.solution.PlanningSolution;
import org.drools.planner.core.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.mista2013.solver.solution.Mista2013SolutionCloner;

@PlanningSolution(solutionCloner = Mista2013SolutionCloner.class)
public class Mista2013 implements Solution<HardMediumSoftScore> {

    private final ProblemInstance problem;
    private final Collection<Allocation> allocations;
    private final Map<Job, Allocation> allocationsPerJob;

    private HardMediumSoftScore score;

    public Mista2013(final ProblemInstance input) {
        this.problem = input;
        final Collection<Allocation> allocations = new HashSet<Allocation>();
        final Map<Job, Allocation> allocationsPerJob = new HashMap<Job, Allocation>();
        for (final Project p : input.getProjects()) {
            for (final Job j : p.getJobs()) {
                final Allocation a = new Allocation(j);
                allocations.add(a);
                allocationsPerJob.put(j, a);
            }
        }
        this.allocationsPerJob = Collections.unmodifiableMap(allocationsPerJob);
        this.allocations = Collections.unmodifiableCollection(allocations);
    }

    public Allocation getAllocation(final Job job) {
        if (!this.allocationsPerJob.containsKey(job)) {
            throw new IllegalArgumentException("Job allocation not found, this is a bug in our solution code: " + job);
        }
        return this.allocationsPerJob.get(job);
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
    public HardMediumSoftScore getScore() {
        return this.score;
    }

    @Override
    public void setScore(final HardMediumSoftScore score) {
        this.score = score;
    }

}
