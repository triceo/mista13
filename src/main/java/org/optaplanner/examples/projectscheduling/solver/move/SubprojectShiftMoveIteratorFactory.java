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
    
    private static int getLeftRangeBounds(Job j) {
        return Math.max(j.getParentProject().getReleaseDate(), Project.getTheoreticalMaxDurationUntil(j));
    }

    private static int getRightRangeBounds(Job j) {
        return Project.getTheoreticalMaxDurationAfter(j);
    }

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
            final List<Allocation> allocations = this.project.getAllocations();
            Job randomJob = null;
            do {
                final int random = this.random.nextInt(allocations.size());
                randomJob = allocations.get(random).getJob();
            } while (randomJob == null || randomJob.isImmediatelyBeforeSink());
            // and move the job and the ones after it by +/- CPD
            final int leftRangeEnd = getLeftRangeBounds(randomJob);
            final int rightRangeEnd = getRightRangeBounds(randomJob);
            return new SubprojectShiftMove(this.project, randomJob, this.random.nextInt(leftRangeEnd + rightRangeEnd) - leftRangeEnd);
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
            for (Job j: p.getJobs()) {
                if (j.isSink() || j.isSource()) {
                    continue;
                }
                final int leftRangeEnd = getLeftRangeBounds(j);
                final int rightRangeEnd = getRightRangeBounds(j);
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
