package UI;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;

import Backend.NewsArticle;

public class MonthChart {
	private static final String title = "MonthWiseChart";
	private ChartPanel chartPanel ;
	private JPanel panel=new JPanel() ;
	public static String query;
	public static JFreeChart chart;

	public MonthChart( LinkedList<NewsArticle> newsList, String startDate, String endDate, String qur) throws Exception{
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
	        File lineChart = new File(System.getProperty("user.home")+File.separator+"Downloads"+File.separator+ query+"-MonthChart.jpeg" ); 
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
        int startDateInteger = Integer.parseInt(startDate.substring(6));
        int startMonth = Integer.parseInt(startDate.subSequence(3,5).toString());
        int endMonth = Integer.parseInt(endDate.subSequence(3,5).toString());
	    int endDateInteger = Integer.parseInt(endDate.substring(6));
	    int[] countForMonth= new int[12];
	    NewsArticle temp;
	    Iterator<NewsArticle> articleIterator = newsList.iterator();
	    temp = articleIterator.next();
	    
	    for(int i = startDateInteger ; i <= endDateInteger ; i++){
	    	for(int k=0;k<12;k++){
	    		countForMonth[k]=0;
	    	}
	    	while(articleIterator.hasNext()){
	    		
	    		if(Integer.parseInt( temp.getDateStamp().subSequence(6,10).toString()) == i){
	    			countForMonth[ ( Integer.parseInt( temp.getDateStamp().subSequence(3,5).toString() ) ) - 1]++;
	    			temp = articleIterator.next();
	    		}
	    		else break;
	    	}
	    	for(int k=0;k<12;k++){
	    		if((i!=startDateInteger)&&(i!=endDateInteger)){
	    			series.add(new Month(k+1, i),countForMonth[k] );
	    		}
	    		else if(i==startDateInteger){
	    			if(k>=startMonth) series.add(new Month(k+1, i),countForMonth[k]);
	    		}
	    		else if(i==endDateInteger){
	    			if(k<=endMonth) series.add(new Month(k+1, i),countForMonth[k]);
	    		}
	    	}
	    }
        return series;
    }
	private XYDataset createDataset(String startDate, String endDate, LinkedList<NewsArticle> newsList) {
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        tsc.addSeries(createSeries("Number of news per Month", startDate, endDate, newsList));
        return tsc;
    }
	public JPanel getMonthPanel(){
		return panel;
	}
}