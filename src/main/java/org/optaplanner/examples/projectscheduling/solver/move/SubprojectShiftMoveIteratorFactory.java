package org.optaplanner.examples.projectscheduling.solver.move;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.NotImplementedException;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.Project;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

public class SubprojectShiftMoveIteratorFactory implements MoveIteratorFactory {

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
            final List<Project> allProjects = this.project.getProblem().getProjects();
            final Project randomProject = allProjects.get(this.random.nextInt(allProjects.size()));
            final List<Job> allJobs = randomProject.getJobs();
            Job randomJob = null;
            boolean isBoundary = false;
            boolean precedsSink = false;
            do {
                final int random = this.random.nextInt(allJobs.size());
                randomJob = allJobs.get(random);
                isBoundary = randomJob.isSource() || randomJob.isSink();
                precedsSink = !isBoundary && randomJob.getSuccessors().get(0).isSink();
            } while (randomJob == null || isBoundary || precedsSink);
            // and move the job and the ones after it by +/- 50
            return new SubprojectShiftMove(this.project, randomJob, this.random.nextInt(100) - 50);
        }

        @Override
        public void remove() {
            throw new NotImplementedException();
        }

    }

    @Override
    public long getSize(final ScoreDirector scoreDirector) {
        final ProjectSchedule schedule = (ProjectSchedule) scoreDirector.getWorkingSolution();
        long total = 0;
        for (final Allocation a : schedule.getAllocations()) {
            total += a.getStartDateRange().size();
        }
        return total;
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
