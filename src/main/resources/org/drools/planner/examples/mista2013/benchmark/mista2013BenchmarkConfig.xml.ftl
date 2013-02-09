<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>
  <parallelBenchmarkCount>3</parallelBenchmarkCount>
  <benchmarkDirectory>data/benchmark/</benchmarkDirectory>
  <warmUpSecondsSpend>30</warmUpSecondsSpend>
  <solverBenchmarkRankingType>TOTAL_RANKING</solverBenchmarkRankingType>
  <inheritedSolverBenchmark>
    <problemBenchmarks>
      <problemIOClass>org.drools.planner.examples.mista2013.persistence.Mista2013ProblemIO</problemIOClass>
      <inputSolutionFile>data/mista2013/input/A-1.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-3.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-6.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-7.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-9.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-10.txt</inputSolutionFile>
      <writeOutputSolutionEnabled>true</writeOutputSolutionEnabled>
      <problemStatisticType>BEST_SOLUTION_CHANGED</problemStatisticType>
      <problemStatisticType>CALCULATE_COUNT_PER_SECOND</problemStatisticType>
    </problemBenchmarks>
    <solver>
      <solutionClass>org.drools.planner.examples.mista2013.domain.Mista2013</solutionClass>
      <planningEntityClass>org.drools.planner.examples.mista2013.domain.Allocation</planningEntityClass>
    
      <scoreDirectorFactory>
        <scoreDefinitionType>HARD_MEDIUM_SOFT</scoreDefinitionType>
        <incrementalScoreCalculatorClass>org.drools.planner.examples.mista2013.solver.score.Mista2013IncrementalScoreCalculator</incrementalScoreCalculatorClass>
      </scoreDirectorFactory>
      <termination>
        <maximumMinutesSpend>5</maximumMinutesSpend>
      </termination>
    </solver>
  </inheritedSolverBenchmark>

<#list [32, 64, 96] as minimalAcceptedSelection>
<#list [5000, 25000, 50000, 75000, 100000] as lateAcceptance>
  <solverBenchmark>
    <name>MAS${minimalAcceptedSelection}-LAS${lateAcceptance}</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>BEST_FIT</constructionHeuristicType>
      </constructionHeuristic>
    <localSearch>
      <unionMoveSelector>
        <changeMoveSelector>
          <valueSelector>
            <variableName>jobMode</variableName>
          </valueSelector>
        </changeMoveSelector>
        <changeMoveSelector>
          <valueSelector>
            <variableName>startDate</variableName>
          </valueSelector>
        </changeMoveSelector>
        <swapMoveSelector>
          <variableNameInclude>startDate</variableNameInclude>
        </swapMoveSelector>
      </unionMoveSelector>
      <acceptor>
        <lateAcceptanceSize>${lateAcceptance}</lateAcceptanceSize>
      </acceptor>
      <forager>
        <minimalAcceptedSelection>${minimalAcceptedSelection}</minimalAcceptedSelection>
      </forager>
    </localSearch>
    </solver>
  </solverBenchmark>
</#list>
</#list>
</plannerBenchmark>