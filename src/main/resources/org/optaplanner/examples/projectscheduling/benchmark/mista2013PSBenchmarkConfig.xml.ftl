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
        <maximumSecondsSpend>30</maximumSecondsSpend>
      </termination>
    </solver>
  </inheritedSolverBenchmark>

<#list [0, 1, 2, 4] as subprojectMove>
<#list [1, 2, 4] as executionChangeMove> <#-- no other move changes execution modes; 0 is not possible -->
<#list [0, 1, 2, 4] as startChangeMove>
<#list [0, 1, 2, 4] as swapMove>
  <#-- 2, 2, 2, 2 is the same as 1, 1, 1, 1; don't repeat; this works as long as the values are always powers of 2 -->
  <#if subprojectMove % 2 == 1 || executionChangeMove % 2 == 1 || startChangeMove % 2 == 1 || swapMove % 2 == 1>
  <solverBenchmark>
    <name>${subprojectMove}-${executionChangeMove}-${startChangeMove}-${swapMove}</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT_DECREASING</constructionHeuristicType>
      </constructionHeuristic>
    <localSearch>
      <unionMoveSelector>
        <moveIteratorFactory>
          <moveIteratorFactoryClass>org.optaplanner.examples.projectscheduling.solver.move.SubprojectShiftMoveIteratorFactory</moveIteratorFactoryClass>
          <fixedProbabilityWeight>${subprojectMove}.0</fixedProbabilityWeight>
        </moveIteratorFactory>
        <changeMoveSelector>
          <valueSelector>
            <variableName>executionMode</variableName>
          </valueSelector>
          <fixedProbabilityWeight>${executionChangeMove}.0</fixedProbabilityWeight>
        </changeMoveSelector>
        <changeMoveSelector>
          <valueSelector>
            <variableName>startDate</variableName>
          </valueSelector>
          <fixedProbabilityWeight>${startChangeMove}.0</fixedProbabilityWeight>
        </changeMoveSelector>
        <swapMoveSelector>
          <variableNameInclude>startDate</variableNameInclude>
          <filterClass>org.optaplanner.examples.projectscheduling.solver.move.filter.SwapMoveFilter</filterClass>
          <fixedProbabilityWeight>${swapMove}.0</fixedProbabilityWeight>
        </swapMoveSelector>
      </unionMoveSelector>
      <acceptor>
        <planningEntityTabuSize>5</planningEntityTabuSize>
        <lateAcceptanceSize>50000</lateAcceptanceSize>
      </acceptor>
      <forager>
        <minimalAcceptedSelection>32</minimalAcceptedSelection>
      </forager>
    </localSearch>
    </solver>
  </solverBenchmark>
  </#if>
</#list>
</#list>
</#list>
</#list>
</plannerBenchmark>
