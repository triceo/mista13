<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>
  <parallelBenchmarkCount>2</parallelBenchmarkCount>
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
    </problemBenchmarks>
    <solver>
      <solutionClass>org.optaplanner.examples.projectscheduling.domain.ProjectSchedule</solutionClass>
      <planningEntityClass>org.optaplanner.examples.projectscheduling.domain.Allocation</planningEntityClass>
    
      <scoreDirectorFactory>
        <scoreDefinitionType>BENDABLE</scoreDefinitionType>
        <bendableHardLevelCount>1</bendableHardLevelCount>
        <bendableSoftLevelCount>2</bendableSoftLevelCount>
        <incrementalScoreCalculatorClass>org.optaplanner.examples.projectscheduling.solver.score.Mista2013IncrementalScoreCalculator</incrementalScoreCalculatorClass>
      </scoreDirectorFactory>
      <termination>
        <maximumSecondsSpend>300</maximumSecondsSpend>
      </termination>
    </solver>
  </inheritedSolverBenchmark>

<#list [8, 16, 32] as sd>
<#list [2, 4, 8] as cs>
  <solverBenchmark>
    <name>${sd}-1-${cs}</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT</constructionHeuristicType>
      </constructionHeuristic>
      <localSearch>
        <unionMoveSelector>
          <moveIteratorFactory>
            <moveIteratorFactoryClass>org.optaplanner.examples.projectscheduling.solver.move.chainshift.ChainShiftMoveIteratorFactory</moveIteratorFactoryClass>
            <fixedProbabilityWeight>${cs}.0</fixedProbabilityWeight>
          </moveIteratorFactory>
          <!-- Moves that assign various valid values to the entities -->
          <changeMoveSelector>
            <valueSelector>
              <variableName>executionMode</variableName>
            </valueSelector>
            <fixedProbabilityWeight>1.0</fixedProbabilityWeight>
          </changeMoveSelector>
          <changeMoveSelector>
            <valueSelector>
              <variableName>startDate</variableName>
            </valueSelector>
            <fixedProbabilityWeight>${sd}.0</fixedProbabilityWeight>
          </changeMoveSelector>
        </unionMoveSelector>
        <acceptor>
          <entityTabuRatio>0.2</entityTabuRatio>
          <fadingEntityTabuRatio>0.6</fadingEntityTabuRatio>
          <lateAcceptanceSize>8000</lateAcceptanceSize>
        </acceptor>
        <forager>
          <acceptedCountLimit>4</acceptedCountLimit>
        </forager>
      </localSearch>
    </solver>
  </solverBenchmark>
</#list>
</#list>
</plannerBenchmark>
