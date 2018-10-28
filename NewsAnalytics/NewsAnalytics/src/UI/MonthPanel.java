package UI;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Backend.NewsArticle;
import Backend.parser_0;

public class MonthPanel extends JPanel {
	private JTextField textField;
	public String keywordText; 

	/**
	 * Create the panel.
	 * @throws Exception 
	 */
	public MonthPanel(LinkedList<NewsArticle> newsList, String startDate, String endDate, String query, parser_0 parser, List<String>keywords, GraphsFrame parentFrame) throws Exception {
		setLayout(null);
		JPanel temp =new MonthChart( newsList, startDate,endDate,query).getMonthPanel();
		temp.setBounds(6, 6, 682, 464);
		add(temp);
		
		JButton btnYearGraph = new JButton("Year Graph");
		btnYearGraph.setBounds(6, 482, 104, 29);
		btnYearGraph.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GraphsFrame.showYear();
        	}
        });
		add(btnYearGraph);
		
		JButton btnDayGraph = new JButton("Day Graph");
		btnDayGraph.setBounds(105, 482, 104, 29);
		btnDayGraph.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GraphsFrame.showDay();
        	}
        });
		add(btnDayGraph);
		
		JButton dwnldBtn1 = new JButton("Download Graphs");
		dwnldBtn1.setBounds(206, 482, 179, 29);
		dwnldBtn1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			YearChart.downloadChart();
        			MonthChart.downloadChart();
        			DayChart.downloadChart();
        			JOptionPane.showMessageDialog(null, "Graphs Downloaded in Downloads Folder");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        });
		add(dwnldBtn1);
		

		textField = new JTextField();
		textField.setBounds(397, 482, 176, 26);
		textField.setToolTipText("Write keywords separated by comman followed by a space");
		String s="";
		if(keywords.size()>0){
			for(int i=0;i<keywords.size()-1;i++){
				s += keywords.get(i) + ", ";
			}
			s+= keywords.get(keywords.size()-1);
		}
		textField.setText(s);
		add(textField);
		textField.setColumns(10);
		
		JButton btnReplot1 = new JButton("Replot");
		btnReplot1.setBounds(571, 482, 117, 29);
		btnReplot1.setToolTipText("Plot of articles containing these words");
		btnReplot1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			keywordText = textField.getText();
        			String[] keywords = keywordText.split(", ");
        			LinkedList<String> keywordList = new LinkedList<>();
        			parentFrame.setVisible(false);
        			parentFrame.myDispose();
        			for(int i=0;i<keywords.length;i++){
        				keywordList.add(keywords[i]);
        			}
        			GraphsFrame gf= new GraphsFrame(startDate, endDate, query, keywordList, parser);
        			
					gf.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        });
		add(btnReplot1);
		

	}
}
