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
      <problemStatisticType>BEST_SCORE</problemStatisticType>
      <problemStatisticType>CALCULATE_COUNT_PER_SECOND</problemStatisticType>
      <problemStatisticType>BEST_SOLUTION_MUTATION</problemStatisticType>
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
        <maximumMinutesSpend>1</maximumMinutesSpend>
      </termination>
    </solver>
  </inheritedSolverBenchmark>

<#list ['BEST_FIT', 'BEST_FIT_DECREASING', 'FIRST_FIT', 'FIRST_FIT_DECREASING'] as ch>
  <solverBenchmark>
    <name>${ch}</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>${ch}</constructionHeuristicType>
      </constructionHeuristic>
    </solver>
  </solverBenchmark>
</#list>
</plannerBenchmark>
