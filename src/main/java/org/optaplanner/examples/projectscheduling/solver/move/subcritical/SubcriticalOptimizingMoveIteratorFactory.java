package org.optaplanner.examples.projectscheduling.solver.move.subcritical;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.NotImplementedException;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Project;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

public class SubcriticalOptimizingMoveIteratorFactory implements MoveIteratorFactory {

    private static final class RandomIterator implements Iterator<Move> {

        private final Random random;
        private final ProjectSchedule project;

        public RandomIterator(final ProjectSchedule project, final Random random) {
            this.project = project;
            this.random = random;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Move next() {
            final List<Project> projects = this.project.getProblem().getProjects();
            final Project randomProject = projects.get(this.random.nextInt(projects.size()));
            return new SubcriticalOptimizingMove(this.project, randomProject, this.random);
        }

        @Override
        public void remove() {
            throw new NotImplementedException();
        }

    }

    @Override
    public long getSize(final ScoreDirector scoreDirector) {
        final ProjectSchedule schedule = (ProjectSchedule) scoreDirector.getWorkingSolution();
        return schedule.getProblem().getProjects().size();
    }

    @Override
    public Iterator<Move> createOriginalMoveIterator(final ScoreDirector scoreDirector) {
        throw new NotImplementedException();
    }

    @Override
    public Iterator<Move> createRandomMoveIterator(final ScoreDirector scoreDirector, final Random workingRandom) {
        final ProjectSchedule schedule = (ProjectSchedule) scoreDirector.getWorkingSolution();
        return new RandomIterator(schedule, workingRandom);
    }

}
