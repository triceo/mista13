package org.optaplanner.examples.projectscheduling.solver.move.gapremover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.math.IntRange;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.projectscheduling.domain.Allocation;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

// FIXME proof of concept; terrible performance-wise
public class GapRemovingMoveIteratorFactory implements MoveIteratorFactory {

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
            // find out what is occupied
            boolean[] containsSomethingAtTime = new boolean[1]; // will be extended
            Arrays.fill(containsSomethingAtTime, false);
            for (final Allocation a : this.schedule.getAllocations()) {
                for (int i = a.getStartDate(); i <= a.getDueDate(); i++) {
                    if (containsSomethingAtTime.length <= i) {
                        containsSomethingAtTime = Arrays.copyOf(containsSomethingAtTime, i + 1);
                    }
                    containsSomethingAtTime[i] = true;
                }
            }
            // split out the unoccupied ranges
            int gapStart = -1;
            final List<IntRange> list = new ArrayList<IntRange>();
            for (int i = 0; i < containsSomethingAtTime.length; i++) {
                final boolean hasSomething = containsSomethingAtTime[i];
                if (!hasSomething && gapStart < 0) {
                    gapStart = i;
                } else if (hasSomething && gapStart >= 0) {
                    list.add(new IntRange(gapStart, i - 1));
                    gapStart = -1;
                }
            }
            // pick one at random
            if (list.size() == 0) {
                return new GapRemovingMove();
            } else {
                final IntRange range = list.get(this.random.nextInt(list.size()));
                return new GapRemovingMove(this.schedule, range);
            }
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
