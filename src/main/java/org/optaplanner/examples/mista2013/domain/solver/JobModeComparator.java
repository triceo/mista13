package org.optaplanner.examples.mista2013.domain.solver;

import gnu.trove.procedure.TObjectIntProcedure;

import java.util.Comparator;

import org.optaplanner.examples.mista2013.domain.JobMode;
import org.optaplanner.examples.mista2013.domain.Resource;

public class JobModeComparator implements Comparator<JobMode> {

    private static final class ResourceSummation implements TObjectIntProcedure<Resource> {

        private final JobMode jm;
        private int total = 0;

        public ResourceSummation(final JobMode jm) {
            this.jm = jm;
        }

        @Override
        public boolean execute(final Resource resource, final int requirement) {
            if (resource.isRenewable()) {
                this.total += requirement * this.jm.getDuration();
            } else {
                this.total += requirement;
            }
            return true;
        }

        public int getTotal() {
            return this.total;
        }

    }

    private static int sumResources(final JobMode jm) {
        final ResourceSummation rs = new ResourceSummation(jm);
        jm.getResourceRequirements().forEachEntry(rs);
        return rs.getTotal();
    }

    @Override
    public int compare(final JobMode o1, final JobMode o2) {
        final int o1resourceUse = JobModeComparator.sumResources(o1);
        final int o2resourceUse = JobModeComparator.sumResources(o2);
        // less duration is stronger
        if (o1resourceUse < o2resourceUse) {
            return 1;
        } else if (o1resourceUse == o2resourceUse) {
            return 0;
        } else {
            return 1;
        }
    }

}
