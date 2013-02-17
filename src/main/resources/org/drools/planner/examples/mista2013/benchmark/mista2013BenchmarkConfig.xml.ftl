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
      <inputSolutionFile>data/mista2013/input/A-2.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-3.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-4.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-5.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-6.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-7.txt</inputSolutionFile>
      <inputSolutionFile>data/mista2013/input/A-8.txt</inputSolutionFile>
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
        <scoreDefinitionType>BENDABLE</scoreDefinitionType>
        <bendableHardLevelCount>1</bendableHardLevelCount>
        <bendableSoftLevelCount>3</bendableSoftLevelCount>
        <incrementalScoreCalculatorClass>org.drools.planner.examples.mista2013.solver.score.Mista2013IncrementalScoreCalculator</incrementalScoreCalculatorClass>
      </scoreDirectorFactory>
      <termination>
        <maximumMinutesSpend>6</maximumMinutesSpend>
      </termination>
    </solver>
  </inheritedSolverBenchmark>

<#list [160, 192, 224] as minimalAcceptedSelection>
<#list [350000, 450000, 550000, 650000] as lateAcceptance>
  <solverBenchmark>
    <name>MAS${minimalAcceptedSelection}-LAS${lateAcceptance}</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT_DECREASING</constructionHeuristicType>
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
        <moveTabuSize>7</moveTabuSize>
        <planningEntityTabuSize>7</planningEntityTabuSize>
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