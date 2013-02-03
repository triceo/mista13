package org.drools.planner.examples.mista2013.app;

import org.drools.planner.examples.common.app.CommonBenchmarkApp;

public class Mista2013BenchmarkApp extends CommonBenchmarkApp {

    public static final String BENCHMARK_CONFIG_TEMPLATE = "/org/drools/planner/examples/mista2013/benchmark/mista2013BenchmarkConfig.xml.ftl";

    public static void main(final String[] args) {
        String benchmarkConfig;
        benchmarkConfig = Mista2013BenchmarkApp.BENCHMARK_CONFIG_TEMPLATE;
        new Mista2013BenchmarkApp().buildFromTemplateAndBenchmark(benchmarkConfig);
        return;
    }
}