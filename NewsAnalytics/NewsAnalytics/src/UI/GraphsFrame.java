package UI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import Backend.NewsArticle;
import Backend.parser_0;

public class GraphsFrame {
	static JFrame graphFrame=new JFrame("Graphs");
	static JPanel mainPanel=new JPanel();
	JPanel monthPanel;
	JPanel yearPanel; 
	JPanel dayPanel;
//	JButton nxtBtn1=new JButton();
//	JButton prevBtn1= new JButton();
//	JButton midBtn1= new JButton();
//	JButton midBtn2= new JButton();
//	JButton nxtBtn2=new JButton();
//	JButton prevBtn2= new JButton();
//	static JButton replotBtn1 =new JButton();
//	static JButton replotBtn2 =new JButton();
//	static JButton dwnldBtn1 =new JButton();
//	static JButton dwnldBtn2 =new JButton();
	JPanel monthGraphPanel = new JPanel();
	JPanel yearGraphPanel = new JPanel();
	JPanel dayGraphPanel = new JPanel();
	static CardLayout cl =new CardLayout();
	
	public GraphsFrame(String startDate, String endDate, String query, LinkedList<String> keywords, parser_0 parser) throws Exception{
		LinkedList<NewsArticle> newsList = new LinkedList<NewsArticle>();
		//LinkedList<String> keywords = new LinkedList<>();
		newsList = parser.compileResults(keywords, true);
		if(newsList.size()==0 || newsList==null){
			JOptionPane.showMessageDialog(null, "No articles Found", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else{
			Collections.sort(newsList);
			mainPanel.setLayout(cl);
			monthPanel =new MonthPanel(newsList, startDate, endDate, query, parser, keywords, this);
			yearPanel = new YearPanel(newsList, startDate, endDate, query, parser, keywords, this);
			dayPanel = new DayPanel(newsList, startDate, endDate, query, parser, keywords, this);
			
			mainPanel.add(monthPanel, "MonthPanel");
			mainPanel.add(yearPanel, "YearPanel");
			mainPanel.add(dayPanel,"DayPanel");
			cl.show(mainPanel, "MonthPanel");
//			prevBtn2.addActionListener(new ActionListener(){
//				@Override
//				public void actionPerformed(ActionEvent e){
//					showMonth();
//				}
//			});
			graphFrame.getContentPane().add(mainPanel);
			graphFrame.pack();
			graphFrame.setBounds(200, 75, 696, 609);
			graphFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			graphFrame.setResizable(false);
			graphFrame.setVisible(true);
		}
	}

	public static void showYear(){
		cl.show(mainPanel, "YearPanel");
	}
	public static void showMonth(){
		cl.show(mainPanel, "MonthPanel");
	}
	public static void showDay(){
		cl.show(mainPanel, "DayPanel");
	}
	public JPanel getPanel(){
		return mainPanel;
	}

	public void myDispose(){
		graphFrame.dispose();
	}
	public void setVisible(boolean a){
		graphFrame.setVisible(a);
	}

}
