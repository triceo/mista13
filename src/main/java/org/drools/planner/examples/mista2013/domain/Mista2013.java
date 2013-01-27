package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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
        final Collection<Allocation> allocations = new LinkedHashSet<Allocation>();
        final Map<Job, Allocation> allocationsPerJob = new HashMap<Job, Allocation>();
        for (final Project p : input.getProjects()) {
            for (final Job j : p.getJobs()) {
                if (j.isSink() || j.isSource()) {
                    // don't create allocations from pseudo-jobs
                    continue;
                }
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

    public Collection<JobMode> getJobModes() {
        final List<JobMode> s = new ArrayList<JobMode>();
        for (final Project p : this.getProblem().getProjects()) {
            for (final Job j : p.getJobs()) {
                for (final JobMode jm : j.getJobModes()) {
                    s.add(jm);
                }
            }
        }
        return Collections.unmodifiableCollection(s);
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

    public Collection<Integer> getStartDates() {
        final SortedSet<Integer> s = new TreeSet<Integer>();
        for (final Project p : this.getProblem().getProjects()) {
            for (int i = p.getReleaseDate(); i < p.getHorizon(); i++) {
                s.add(i);
            }
        }
        return Collections.unmodifiableSortedSet(s);
    }

    @Override
    public void setScore(final HardMediumSoftScore score) {
        this.score = score;
    }

}
