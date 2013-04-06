package org.optaplanner.examples.projectscheduling.app;

import java.io.File;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.XmlSolverFactory;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.projectscheduling.persistence.Mista2013ProblemIO;

public class Mista2013CompetitionApp {

    private static boolean solve(final File in, final File out, final long timeLimit, final long randomSeed) {
        // get initial configuration
        final XmlSolverFactory f = new XmlSolverFactory();
        f.configure(Mista2013CompetitionApp.class.getResourceAsStream("/org/optaplanner/examples/projectscheduling/solver/mista2013SolverConfig.xml"));
        final SolverConfig c = f.getSolverConfig();
        // make sure some of the settings are always what we need them to be
        c.setEnvironmentMode(EnvironmentMode.REPRODUCIBLE);
        c.setRandomSeed(randomSeed);
        c.getTerminationConfig().setMaximumSecondsSpend(timeLimit);
        // read the problem
        final Mista2013ProblemIO io = new Mista2013ProblemIO();
        @SuppressWarnings("rawtypes")
        final Solution problem = io.read(in);
        // solve
        final Solver s = f.buildSolver();
        s.setPlanningProblem(problem);
        s.solve();
        // write result
        io.write(s.getBestSolution(), out);
        return true;
    }

    public static void main(final String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("The program expects 4 arguments.");
        }
        final File globalProblemFile = new File(args[0]);
        if (!globalProblemFile.canRead()) {
            throw new IllegalArgumentException("Global problem file cannot be read: " + globalProblemFile);
        }
        final File scheduleFile = new File(args[1]);
        final long timeLimit = Long.valueOf(args[2]);
        final long randomSeed = Long.valueOf(args[3]);
        Mista2013CompetitionApp.solve(globalProblemFile, scheduleFile, timeLimit, randomSeed);
    }

}
