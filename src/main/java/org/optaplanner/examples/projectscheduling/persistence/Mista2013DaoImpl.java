package org.optaplanner.examples.projectscheduling.persistence;

import org.optaplanner.examples.common.persistence.XStreamSolutionDao;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

public class Mista2013DaoImpl extends XStreamSolutionDao {

    public Mista2013DaoImpl() {
        super("projectscheduling", ProjectSchedule.class);
    }

}
