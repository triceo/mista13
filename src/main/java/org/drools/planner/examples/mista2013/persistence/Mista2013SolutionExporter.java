package org.drools.planner.examples.mista2013.persistence;

import org.drools.planner.examples.common.persistence.AbstractTxtSolutionExporter;

// TODO needs to be implemented
public class Mista2013SolutionExporter extends AbstractTxtSolutionExporter {

    public Mista2013SolutionExporter() {
        super(new Mista2013DaoImpl());
    }

    @Override
    public TxtOutputBuilder createTxtOutputBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

}
