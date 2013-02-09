package org.drools.planner.examples.mista2013.swingui;

import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;

import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.common.swingui.SolutionPanel;
import org.drools.planner.examples.common.swingui.TangoColorFactory;
import org.drools.planner.examples.mista2013.domain.Allocation;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.Mista2013;
import org.drools.planner.examples.mista2013.domain.Project;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.joda.time.DateTime;

public class GanttPanel extends SolutionPanel {

    private static final long serialVersionUID = -1962577861486577643L;

    public static IntervalCategoryDataset createDataset(final Mista2013 solution) {
        final TaskSeriesCollection collection = new TaskSeriesCollection();
        for (final Project p : solution.getProblem().getProjects()) {
            final TaskSeries series = new TaskSeries("Proj. " + p.getId());
            for (final Job j : p.getJobs()) {
                if (j.isSink() || j.isSource()) {
                    continue;
                }
                final Allocation a = solution.getAllocation(j);
                if (!a.isInitialized()) {
                    continue;
                }
                series.add(new Task("Job " + j.getId(), new SimpleTimePeriod(GanttPanel.date(a.getStartDate()),
                        GanttPanel.date(a.getDueDate()))));
            }
            collection.add(series);
        }
        return collection;
    }

    private static Date date(final int day) {
        final DateTime dt = new DateTime().plusDays(day);
        final Calendar c = Calendar.getInstance();
        c.set(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
        return c.getTime();
    }

    public GanttPanel() {
    }
    
    private final TangoColorFactory tango = new TangoColorFactory();
    
    private JFreeChart createChart(final IntervalCategoryDataset dataset) {
        JFreeChart c = ChartFactory.createGanttChart(null, null, null, dataset, true, false, false);
        CategoryPlot plot = (CategoryPlot) c.getPlot();
        for (int i = 0; i < dataset.getRowCount(); i++) {
            plot.getRenderer().setSeriesPaint(i, tango.pickColor(i));
        }
        return c;
    }

    @Override
    public void resetPanel(@SuppressWarnings("rawtypes") final Solution solution) {
        final IntervalCategoryDataset dataset = GanttPanel.createDataset((Mista2013) solution);
        final JFreeChart chart = this.createChart(dataset);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1280, 720));

        // add panel to the component
        this.removeAll();
        final BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(bl);
        this.add(chartPanel);
    }

}