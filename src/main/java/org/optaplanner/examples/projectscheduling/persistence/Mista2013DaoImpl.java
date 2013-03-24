package org.optaplanner.examples.projectscheduling.persistence;

import org.optaplanner.examples.common.persistence.XStreamSolutionDaoImpl;
import org.optaplanner.examples.projectscheduling.domain.ProjectSchedule;

public class Mista2013DaoImpl extends XStreamSolutionDaoImpl {

    public Mista2013DaoImpl() {
        super("projectscheduling", ProjectSchedule.class);
    }

}
