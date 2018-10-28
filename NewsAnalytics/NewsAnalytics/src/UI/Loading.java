package UI;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import Backend.main;
import Backend.NewsArticle;
import Backend.ProjectFile;
import Backend.QueryDetails;
import Backend.parser_0;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Loading extends JPanel {
	private JTextField textField_1;
	private JTextField textField_2;
	private static JLabel lblInternetStatus;
	private static JLabel loadingGif;
	public main mainThread;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField;

	public static JButton btnPause;
	public static JButton btnResume;
	public static JButton btnPlot;
	public static JButton btnViewArticles;
	public static File userDirectory;
	public LinkedList<String> keywords = new LinkedList<String>();
	public Rectangle bounds=MainFrame.panel.getBounds();
	public parser_0 parser;
	/**
	 * Create the panel.
	 */
	public Loading(ProjectFile project,QueryDetails currentQuery) {
		userDirectory = new File(project.getLocation());
		setLayout(null);
		setBounds(bounds);
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane ( textArea );
	    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    scroll.setBounds(51, 250, 595, 234);
	    add ( scroll );
	    
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		System.setOut(printStream);
		
        JLabel lblNewLabel = new JLabel("Searching");
		lblNewLabel.setBounds(289, 20, 109, 16);
		Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	if(lblNewLabel.getText().equals("Searching"))
                	lblNewLabel.setText("Searching.");
            	else if(lblNewLabel.getText().equals("Searching."))
            	lblNewLabel.setText("Searching..");
            	else if(lblNewLabel.getText().equals("Searching.."))
                	lblNewLabel.setText("Searching...");
            	else if(lblNewLabel.getText().equals("Searching..."))
                	lblNewLabel.setText("Searching");
            }
        }, 0,2000);
		add(lblNewLabel);
		JLabel lblNewLabel_1 = new JLabel("Query :");
		lblNewLabel_1.setBounds(241, 58, 61, 16);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Start Date : ");
		lblNewLabel_2.setBounds(51, 105, 90, 16);
		add(lblNewLabel_2);
		Color bg= MainFrame.panel.getBackground();
		textField_4 = new JTextField();
		textField_4.setBounds(152, 100, 130, 26);
		textField_4.setText(currentQuery.getStartDate());
		textField_4.setBackground(bg);
		textField_4.setEditable(false);
		add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("End Date :");
		lblNewLabel_3.setBounds(355, 105, 72, 16);
		add(lblNewLabel_3);
		
		textField_5 = new JTextField();
		textField_5.setBounds(453, 100, 130, 26);
		textField_5.setText(currentQuery.getEndDate());
		textField_5.setBackground(bg);
		textField_5.setEditable(false);
		add(textField_5);
		textField_5.setColumns(10);
		textField_2 = new JTextField();
		textField_2.setText(currentQuery.getQuery());
		textField_2.setBackground(bg);
		textField_2.setBounds(314, 53, 130, 26);
		textField_2.setEditable(false);
		add(textField_2);
		textField_2.setColumns(10);
        
        JLabel lblNewLabel_4 = new JLabel("Level 0 links Parsed : ");
        lblNewLabel_4.setBounds(157, 168, 145, 16);
        add(lblNewLabel_4);
        
        
        textField = new JTextField();
        textField.setBounds(301, 148, 97, 49);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        Font font =new Font("MonoSpaced",Font.BOLD,35);
        textField.setFont(font);
        textField.setText("0");
        
        textField.setEditable(false);
        textField.setBackground(this.getBackground());
        add(textField);
        textField.setColumns(10);
        
        String path = new File(Constants.getData()+ File.separator + "loading.gif").getPath();
        ImageIcon img = new ImageIcon(path);
        loadingGif = new JLabel(img);
        loadingGif.setBounds(51, 212, 595, 26);
        add(loadingGif);
        btnPause= new JButton("Pause");
        btnResume = new JButton("Resume");
        btnPlot = new JButton("Plot");
        btnViewArticles = new JButton("View Articles");
        
        btnPause.setBounds(61, 505, 117, 29);
        btnResume.setBounds(252, 505, 117, 29);
        btnPlot.setBounds(399, 505, 117, 29);
        
        btnPause.setBounds(123, 505, 117, 29);
        btnPause.setEnabled(true);
        btnPause.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		parser.pause();
        		btnPause.setEnabled(false);
        		btnResume.setEnabled(true);
        		btnPlot.setEnabled(true);
        		btnViewArticles.setEnabled(true);
        		loadingGif.setVisible(false);
        	}
        });
        btnResume.setEnabled(false);
        btnResume.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		parser.resume();
        		btnResume.setEnabled(false);
        		btnPause.setEnabled(true);
        		btnPlot.setEnabled(false);
        		btnViewArticles.setEnabled(false);
        		loadingGif.setVisible(true);
        	}
        });
        btnPlot.setEnabled(false);
        btnViewArticles.setEnabled(false);
        btnPlot.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			LinkedList<String> emptyList=new LinkedList<>();
					GraphsFrame gf = new GraphsFrame(currentQuery.startDate, currentQuery.endDate, currentQuery.query, emptyList, parser);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        });
        add(btnPause);
        add(btnResume);
        add(btnPlot);
        lblInternetStatus = new JLabel("Internet Status:");
        lblInternetStatus.setBounds(6, 547, 248, 16);
        add(lblInternetStatus);
        updateInternetStatus(true);
        
        btnViewArticles.setBounds(528, 505, 117, 29);
        add(btnViewArticles);
        btnViewArticles.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		LinkedList<NewsArticle> newsList = parser.compileResults(new LinkedList<String>(), true);
        		Results_list resultList= new Results_list(newsList, parser, new LinkedList<String>());
        		resultList.setVisible(true);
        	}
        });
        this.parser = new parser_0(currentQuery, currentQuery.getLocation());
        Thread parserThread = new Thread(parser);
        parserThread.start();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	textField.setText(Integer.toString(parser.compileResults(keywords, true).size())); 
            	while(parser.isPaused){
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            	if(!parserThread.isAlive()){
            		onCompletion();
            	}
            }
        }, 0, 1500);
	}
	public static void onCompletion(){
	  	   btnPlot.setEnabled(true);
	  	   btnViewArticles.setEnabled(true);
	  	   btnPause.setEnabled(false);
	  	   btnResume.setEnabled(false);
	  	   loadingGif.setVisible(false);
	     }
	public static void updateInternetStatus(boolean showPane){
		boolean isConnected = MainFrame.isInternetReachable(showPane);
		if(isConnected){
			lblInternetStatus.setText("Internet Status: Connected");
		}
		else{
			lblInternetStatus.setText("Internet Status: Not Connected");
		}
	}
	public void pause(){
		parser.pause();
		btnPause.setEnabled(false);
		btnResume.setEnabled(true);
		btnPlot.setEnabled(true);
		btnViewArticles.setEnabled(true);
		loadingGif.setVisible(false);
	}
	
}
