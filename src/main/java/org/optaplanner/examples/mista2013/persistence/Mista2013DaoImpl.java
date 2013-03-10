package org.optaplanner.examples.mista2013.persistence;

import org.drools.planner.examples.common.persistence.XStreamSolutionDaoImpl;
import org.optaplanner.examples.mista2013.domain.Mista2013;

public class Mista2013DaoImpl extends XStreamSolutionDaoImpl {

    public Mista2013DaoImpl() {
        super("mista2013", Mista2013.class);
    }

}
