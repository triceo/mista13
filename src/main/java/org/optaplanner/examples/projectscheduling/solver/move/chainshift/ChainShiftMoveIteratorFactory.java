package org.optaplanner.examples.projectscheduling.solver.move.chainshift;

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

public class ChainShiftMoveIteratorFactory implements MoveIteratorFactory {

    private static int getLeftRangeBounds(final Job j) {
        return Project.getTheoreticalMaxDurationUntil(j);
    }

    private static int getRightRangeBounds(final Job j) {
        return Project.getCriticalPathDurationAfter(j);
    }

    private static final class RandomIterator implements Iterator<Move> {

        private final Random random;
        private final ProjectSchedule schedule;

        public RandomIterator(final ProjectSchedule schedule, final Random random) {
            this.schedule = schedule;
            this.random = random;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Move next() {
            final List<Project> projects = this.schedule.getProblem().getProjects();
            final Project project = projects.get(this.random.nextInt(projects.size()));
            final List<Job> jobs = project.getJobs();
            final Job job = jobs.get(this.random.nextInt(jobs.size() - 1)); // -1 to ignore sink
            final int leftRangeEnd = ChainShiftMoveIteratorFactory.getLeftRangeBounds(job);
            final int rightRangeEnd = ChainShiftMoveIteratorFactory.getRightRangeBounds(job);
            return new ChainShiftMove(this.schedule, job, this.random.nextInt(leftRangeEnd + rightRangeEnd) - leftRangeEnd);
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
        for (final Project p : schedule.getProblem().getProjects()) {
            for (final Job j : p.getJobs()) {
                if (j.isSink() || j.isSource()) {
                    continue;
                }
                final int leftRangeEnd = ChainShiftMoveIteratorFactory.getLeftRangeBounds(j);
                final int rightRangeEnd = ChainShiftMoveIteratorFactory.getRightRangeBounds(j);
                total += leftRangeEnd + rightRangeEnd;
            }
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
