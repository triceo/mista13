<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>
  <parallelBenchmarkCount>3</parallelBenchmarkCount>
  <benchmarkDirectory>local/data/projectscheduling</benchmarkDirectory>
  <warmUpSecondsSpend>30</warmUpSecondsSpend>
  <solverBenchmarkRankingType>TOTAL_RANKING</solverBenchmarkRankingType>
  <inheritedSolverBenchmark>
    <problemBenchmarks>
      <problemIOClass>org.optaplanner.examples.projectscheduling.persistence.Mista2013ProblemIO</problemIOClass>
      <inputSolutionFile>data/projectscheduling/input/A-1.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/A-2.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/A-3.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/A-4.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/A-5.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/A-6.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/A-7.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/A-8.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/A-9.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/A-10.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-1.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-2.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-3.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-4.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-5.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-6.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-7.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-8.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-9.txt</inputSolutionFile>
      <inputSolutionFile>data/projectscheduling/input/B-10.txt</inputSolutionFile>
      <writeOutputSolutionEnabled>true</writeOutputSolutionEnabled>
      <problemStatisticType>BEST_SOLUTION_CHANGED</problemStatisticType>
      <problemStatisticType>CALCULATE_COUNT_PER_SECOND</problemStatisticType>
    </problemBenchmarks>
    <solver>
      <solutionClass>org.optaplanner.examples.projectscheduling.domain.ProjectSchedule</solutionClass>
      <planningEntityClass>org.optaplanner.examples.projectscheduling.domain.Allocation</planningEntityClass>
    
      <scoreDirectorFactory>
        <scoreDefinitionType>BENDABLE</scoreDefinitionType>
        <bendableHardLevelCount>1</bendableHardLevelCount>
        <bendableSoftLevelCount>4</bendableSoftLevelCount>
        <incrementalScoreCalculatorClass>org.optaplanner.examples.projectscheduling.solver.score.Mista2013IncrementalScoreCalculator</incrementalScoreCalculatorClass>
      </scoreDirectorFactory>
      <termination>
        <maximumMinutesSpend>6</maximumMinutesSpend>
      </termination>
    </solver>
  </inheritedSolverBenchmark>

<#list [24, 32, 40] as minimalAcceptedSelection>
<#list [50000, 500000] as lateAcceptance>
<#list [5, 11] as planningEntityTabuSize>
  <solverBenchmark>
    <name>MAS${minimalAcceptedSelection}-LAS${lateAcceptance}-PETS${planningEntityTabuSize}</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>BEST_FIT_DECREASING</constructionHeuristicType>
      </constructionHeuristic>
    <localSearch>
      <unionMoveSelector>
        <moveIteratorFactory>
          <moveIteratorFactoryClass>org.optaplanner.examples.projectscheduling.solver.move.SubprojectShiftMoveIteratorFactory</moveIteratorFactoryClass>
          <fixedProbabilityWeight>2.0</fixedProbabilityWeight>
        </moveIteratorFactory>
        <changeMoveSelector>
          <valueSelector>
            <variableName>executionMode</variableName>
          </valueSelector>
          <fixedProbabilityWeight>2.0</fixedProbabilityWeight>
        </changeMoveSelector>
        <changeMoveSelector>
          <valueSelector>
            <variableName>startDate</variableName>
          </valueSelector>
          <fixedProbabilityWeight>2.0</fixedProbabilityWeight>
        </changeMoveSelector>
        <swapMoveSelector>
          <variableNameInclude>startDate</variableNameInclude>
          <filterClass>org.optaplanner.examples.projectscheduling.solver.move.filter.SwapMoveFilter</filterClass>
          <fixedProbabilityWeight>1.0</fixedProbabilityWeight>
        </swapMoveSelector>
      </unionMoveSelector>
      <acceptor>
        <planningEntityTabuSize>${planningEntityTabuSize}</planningEntityTabuSize>
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
</#list>
</plannerBenchmark>
