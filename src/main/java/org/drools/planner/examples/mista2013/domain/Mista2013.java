package org.drools.planner.examples.mista2013.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.drools.planner.api.domain.solution.PlanningSolution;
import org.drools.planner.core.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.drools.planner.core.solution.Solution;

@PlanningSolution()
public class Mista2013 implements Solution<HardMediumSoftScore> {

    private final ProblemInstance problem;

    public Mista2013(final ProblemInstance input) {
        this.problem = input;
    }

    @Override
    public Collection<? extends Object> getProblemFacts() {
        final Collection<Object> facts = new ArrayList<Object>();
        facts.add(this.problem);
        return facts;
    }

    @Override
    public HardMediumSoftScore getScore() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setScore(final HardMediumSoftScore score) {
        // TODO Auto-generated method stub

    }

}
