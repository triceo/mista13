package org.optaplanner.examples.mista2013.app;

import org.optaplanner.config.XmlSolverFactory;
import org.optaplanner.core.Solver;
import org.optaplanner.examples.common.app.CommonApp;
import org.optaplanner.examples.common.persistence.AbstractSolutionExporter;
import org.optaplanner.examples.common.persistence.AbstractSolutionImporter;
import org.optaplanner.examples.common.persistence.SolutionDao;
import org.optaplanner.examples.common.swingui.SolutionPanel;
import org.optaplanner.examples.mista2013.persistence.Mista2013DaoImpl;
import org.optaplanner.examples.mista2013.persistence.Mista2013SolutionExporter;
import org.optaplanner.examples.mista2013.persistence.Mista2013SolutionImporter;
import org.optaplanner.examples.mista2013.swingui.GanttPanel;

public class Mista2013App extends CommonApp {

    public static final String SOLVER_CONFIG = "/org/optaplanner/examples/mista2013/solver/mista2013SolverConfig.xml";

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
        final XmlSolverFactory solverFactory = new XmlSolverFactory();
        solverFactory.configure(Mista2013App.SOLVER_CONFIG);
        return solverFactory.buildSolver();
    }
}
