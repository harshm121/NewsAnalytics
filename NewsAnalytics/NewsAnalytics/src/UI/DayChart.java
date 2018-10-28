package UI;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;

import Backend.NewsArticle;

public class DayChart {
	private static final String title = "DayWiseChart";
	private ChartPanel chartPanel ;
	private JPanel panel=new JPanel() ;
	public static String query;
	public static JFreeChart chart;

	public DayChart( LinkedList<NewsArticle> newsList, String startDate, String endDate, String qur) throws Exception{
		query=qur;
		chartPanel=createChart(query,startDate, endDate, newsList);
		panel.setLayout(new BorderLayout());
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setHorizontalAxisTrace(true);
        chartPanel.setVerticalAxisTrace(true);
        
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel1.add(createTrace());
        panel1.add(createDate());
        panel1.add(createZoom());
        
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.add(panel1, BorderLayout.SOUTH);
        chart=createChart(query,startDate, endDate, newsList).getChart();
		  
	}
	public static void downloadChart() throws IOException{
		 int width = 640; /* Width of the image */
	        int height = 480; /* Height of the image */ 
	        File lineChart = new File( System.getProperty("user.home")+File.separator+"Downloads"+File.separator+query+"-DayChart.jpeg" ); 
	        ChartUtilities.saveChartAsJPEG(lineChart ,chart, width ,height);
	}
	
	private JComboBox createTrace() {
        final JComboBox trace = new JComboBox();
        final String[] traceCmds = {"Enable Trace", "Disable Trace"};
        trace.setModel(new DefaultComboBoxModel(traceCmds));
        trace.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (traceCmds[0].equals(trace.getSelectedItem())) {
                    chartPanel.setHorizontalAxisTrace(true);
                    chartPanel.setVerticalAxisTrace(true);
                    chartPanel.repaint();
                } else {
                    chartPanel.setHorizontalAxisTrace(false);
                    chartPanel.setVerticalAxisTrace(false);
                    chartPanel.repaint();
                }
            }
        });
        return trace;
    }

    private JComboBox createDate() {
        final JComboBox date = new JComboBox();
        final String[] dateCmds = {"Horizontal Dates", "Vertical Dates"};
        date.setModel(new DefaultComboBoxModel(dateCmds));
        date.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFreeChart chart = chartPanel.getChart();
                XYPlot plot = (XYPlot) chart.getPlot();
                DateAxis domain = (DateAxis) plot.getDomainAxis();
                if (dateCmds[0].equals(date.getSelectedItem())) {
                    domain.setVerticalTickLabels(false);
                } else {
                    domain.setVerticalTickLabels(true);
                }
            }
        });
        return date;
    }

    private JButton createZoom() {
        final JButton auto = new JButton(new AbstractAction("Auto Zoom") {

            @Override
            public void actionPerformed(ActionEvent e) {
                chartPanel.restoreAutoBounds();
            }
        });
        return auto;
    }
	
	private ChartPanel createChart(String query,String startDate, String endDate, LinkedList<NewsArticle> newsList) {
        XYDataset roiData = createDataset(startDate, endDate, newsList);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            title, "Date", "Number of Articles", roiData, true, true, false);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer =
            (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        return new ChartPanel(chart);
    }
	
	private TimeSeries createSeries(String name, String startDate, String endDate, LinkedList<NewsArticle> newsList) {
        TimeSeries series = new TimeSeries(name);
	    NewsArticle temp;
	    Iterator<NewsArticle> articleIterator = newsList.iterator();
	    temp = articleIterator.next();
	    while(articleIterator.hasNext()){
		    int date=Integer.parseInt(temp.getDateStamp().substring(0,2));
		    int month=Integer.parseInt(temp.getDateStamp().substring(3,5));
		    int year = Integer.parseInt(temp.getDateStamp().substring(6));
		    
		    TimeSeriesDataItem temp1 = new TimeSeriesDataItem(new Day(date,month,year), 1); 
		    TimeSeriesDataItem temp2 = series.addOrUpdate(temp1);
		    if(temp2!= null){
		    	temp1.setValue(temp2.getValue().intValue()+1);
		    	series.addOrUpdate(temp1);
		    }
		    temp=articleIterator.next();
	    }
	    
	    
        return series;
    }
	private XYDataset createDataset(String startDate, String endDate, LinkedList<NewsArticle> newsList) {
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        tsc.addSeries(createSeries("Number of news per Day", startDate, endDate, newsList));
        return tsc;
    }
	public JPanel getDayPanel(){
		return panel;
	}
}
