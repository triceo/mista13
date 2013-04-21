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
            do {
                final int random = this.random.nextInt(allJobs.size());
                randomJob = allJobs.get(random);
                isBoundary = randomJob.isSource() || randomJob.isSink();
            } while (randomJob == null || isBoundary || randomJob.isImmediatelyBeforeSink());
            // and move the job and the ones after it by +/- CPD
            final int cpd = randomProject.getCriticalPathDuration();
            return new SubprojectShiftMove(this.project, randomJob, this.random.nextInt(2 * cpd) - cpd);
        }

        @Override
        public void remove() {
            throw new NotImplementedException();
        }

    }

    @Override
    public long getSize(final ScoreDirector scoreDirector) {
        final ProjectSchedule schedule = (ProjectSchedule) scoreDirector.getWorkingSolution();
        int total = 0;
        for (Project p: schedule.getProblem().getProjects()) {
            // -2 for source and sink
            total += 2 * (p.getJobs().size() - 2) * p.getCriticalPathDuration();
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
