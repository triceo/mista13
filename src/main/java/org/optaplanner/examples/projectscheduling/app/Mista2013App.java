package org.optaplanner.examples.projectscheduling.app;

import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.XmlSolverFactory;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.examples.common.app.CommonApp;
import org.optaplanner.examples.common.persistence.AbstractSolutionExporter;
import org.optaplanner.examples.common.persistence.AbstractSolutionImporter;
import org.optaplanner.examples.common.persistence.SolutionDao;
import org.optaplanner.examples.common.swingui.SolutionPanel;
import org.optaplanner.examples.projectscheduling.persistence.Mista2013DaoImpl;
import org.optaplanner.examples.projectscheduling.persistence.Mista2013SolutionExporter;
import org.optaplanner.examples.projectscheduling.persistence.Mista2013SolutionImporter;
import org.optaplanner.examples.projectscheduling.swingui.GanttPanel;

public class Mista2013App extends CommonApp {

    public static final String SOLVER_CONFIG = "/org/optaplanner/examples/projectscheduling/solver/mista2013SolverConfig.xml";

    public static void main(final String[] args) {
        CommonApp.fixateLookAndFeel();
        new Mista2013App().init();
    }

    @Override
    protected SolutionDao createSolutionDao() {
        return new Mista2013DaoImpl();
    }

    @Override
    protected AbstractSolutionExporter createSolutionExporter() {
        return new Mista2013SolutionExporter();
    }

    @Override
    protected AbstractSolutionImporter createSolutionImporter() {
        return new Mista2013SolutionImporter();
    }

    @Override
    protected SolutionPanel createSolutionPanel() {
        return new GanttPanel();
    }

    @Override
    protected Solver createSolver() {
        final SolverFactory solverFactory = new XmlSolverFactory(Mista2013App.SOLVER_CONFIG);
        return solverFactory.buildSolver();
    }

}
