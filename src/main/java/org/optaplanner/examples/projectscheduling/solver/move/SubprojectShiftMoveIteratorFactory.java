package org.optaplanner.examples.projectscheduling.solver.move;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.NotImplementedException;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Job;
import org.optaplanner.examples.projectscheduling.domain.Project;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

public class SubprojectShiftMoveIteratorFactory implements MoveIteratorFactory {

    private static final int ALLOWED_OFFSET = 25;

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
            // and move the job and the ones after it by +/- ALLOWED_OFFSET
            return new SubprojectShiftMove(this.project, randomJob, this.random.nextInt(2 * SubprojectShiftMoveIteratorFactory.ALLOWED_OFFSET) - SubprojectShiftMoveIteratorFactory.ALLOWED_OFFSET);
        }

        @Override
        public void remove() {
            throw new NotImplementedException();
        }

    }

    @Override
    public long getSize(final ScoreDirector scoreDirector) {
        final ProjectSchedule schedule = (ProjectSchedule) scoreDirector.getWorkingSolution();
        return 2 * SubprojectShiftMoveIteratorFactory.ALLOWED_OFFSET * schedule.getAllocations().size();
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
