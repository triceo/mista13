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

    private static int getRightRangeBounds(final Job j) {
        return j.getParentProject().getCriticalPathDuration();
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
            final Job job = jobs.get(this.random.nextInt(jobs.size()));
            int startDate = Integer.MAX_VALUE;
            if (job.isSink()) {
                return this.next();
            } else if (job.isSource()) {
                startDate = project.getReleaseDate();
                for (Job succ: job.getSuccessors()) {
                    startDate = Math.min(startDate, this.schedule.getAllocation(succ).getStartDate());
                }
            } else {
                startDate = this.schedule.getAllocation(job).getStartDate();
            }
            final int leftRangeEnd = Math.max(0, startDate - job.getParentProject().getReleaseDate());
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
        throw new UnsupportedOperationException();
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
