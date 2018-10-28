package UI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import Backend.ProjectFile;
import Backend.QueryDetails;
import jaco.swing.autocomplete.Autocomplete;

public class QueryInputPanel extends JPanel {
	
	JPanel panel;
	private static JTextField textField;
	public static String startDate;
	public static String endDate;
	LinkedList<String> entries = new LinkedList<String>();
	public ProjectFile project;
	public QueryDetails currentQuery;
	public MainFrame parentFrame;
	public static JLabel lblInternetStatus;
	
	public QueryInputPanel(ProjectFile proj, MainFrame frame) {
		this.project = proj;
		this.parentFrame = frame;
		panel = this;
		panel.setLayout(null);
		setLayout(null);
		
		javax.swing.ToolTipManager.sharedInstance().setInitialDelay(0);
		
		JLabel lblEnterDetails = new JLabel("Enter Details");
		lblEnterDetails.setFont(new Font("Menlo", Font.PLAIN, 17));
		lblEnterDetails.setBounds(274, 46, 186, 33);
		panel.add(lblEnterDetails);
		
		JLabel lblSearchQuery = new JLabel("Search Query : ");
		lblSearchQuery.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblSearchQuery.setBounds(213, 109, 117, 21);
		panel.add(lblSearchQuery);
		
		textField = new JTextField();
		textField.setEditable(true);
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setBounds(342, 109, 177, 21);
		panel.add(textField);
		textField.setColumns(10);
		/*
		 * 
		 Suggestion!
		 * 
		 */
		File outputDir = new File(Constants.getData());
		try{
			FileInputStream FIn = new FileInputStream(outputDir + File.separator + "prevSearches.ser");
			ObjectInputStream OIn = new ObjectInputStream(FIn);
			entries = (LinkedList) OIn.readObject();
	        OIn.close();
	        FIn.close();
		}catch(Exception e){
			
		}
		Autocomplete<String> autocomplete = new Autocomplete<String>(entries, true);
		autocomplete.setMaximumRowCount(4);
		autocomplete.setDelimiter(Autocomplete.LINE_START_PATTERN);
		autocomplete.setCompletionBoundary(Autocomplete.CompletionBoundary.NEWLINE);
		autocomplete.setTextComponent(textField);
		
		
		
		JLabel lblStartYear = new JLabel("Start Date : ");
		lblStartYear.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblStartYear.setBounds(57, 201, 99, 24);
		panel.add(lblStartYear);
		
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,new DateLabelFormatter());
		datePicker.setBounds(146, 201, 165, 33);
		panel.add(datePicker);
		
		
		JLabel lblEndYear = new JLabel("End Date : ");
		lblEndYear.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblEndYear.setBounds(338, 204, 89, 19);
		panel.add(lblEndYear);
		
		UtilDateModel endModel = new UtilDateModel();
		Properties p1 = new Properties();
		p1.put("text.today", "Today");
		p1.put("text.month", "Month");
		p1.put("text.year", "Year");
		JDatePanelImpl endDatePanel = new JDatePanelImpl(endModel,p1);
		JDatePickerImpl endDatePicker = new JDatePickerImpl(endDatePanel,new DateLabelFormatter());
		endDatePicker.setBounds(427, 201, 165, 33);
		panel.add(endDatePicker);
		
		JTextField trial =new JTextField();
		trial.setBounds(0, 0, 0, 0);
		trial.setEditable(true);
		panel.add(trial);
		
		
		JButton btnGo = new JButton("Go!");
		btnGo.setBounds(298, 382, 117, 29);
		btnGo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				updateInternetStatus(true);
				if(!MainFrame.isInternetReachable(false)){
					try {
						throw new InternetNotConnectedException(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else{
					if((textField.getText().trim()==null)||(textField.getText().equals(""))){
						JOptionPane.showMessageDialog(null, "Query Field is Mandatory", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else if(datePicker.getModel().getValue()==null){
						JOptionPane.showMessageDialog(null, "Start Date is Mandatory", "Error", JOptionPane.ERROR_MESSAGE);
					}else if(endDatePicker.getModel().getValue()==null){
						JOptionPane.showMessageDialog(null, "End Date Field is Mandatory", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else{
						boolean alreadyExists=false;
						for(String str : entries){
							if(str.equals(textField.getText())){
								alreadyExists=true;
							}
						}
						if(!alreadyExists)
						entries.add(textField.getText());
						FileOutputStream FOut;
						try{
							FOut = new FileOutputStream(outputDir + File.separator + "prevSearches.ser");
							ObjectOutputStream OOut = new ObjectOutputStream(FOut);
						    OOut.writeObject(entries);
						}catch(Exception e4){
							e4.printStackTrace();
						}
						Date startSelectedDate = (Date) datePicker.getModel().getValue();
						Date endSelectedDate = (Date) endDatePicker.getModel().getValue();
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						startDate = df.format(startSelectedDate);
						endDate  = df.format(endSelectedDate);
						currentQuery=new QueryDetails(project,textField.getText(),startDate, endDate );
						currentQuery.setParentProject(project);
						project.addQuery(currentQuery);
						parentFrame.goToLoading(currentQuery);
					}	
			}
			}
		});
		panel.add(btnGo);
		
		lblInternetStatus = new JLabel("Internet Status:");
		lblInternetStatus.setBounds(20, 543, 239, 16);
		panel.add(lblInternetStatus);
		File questionImg = new File(Constants.getData()+File.separator+"question.png");
		ImageIcon img = new ImageIcon(questionImg.getPath());
		JLabel lblNewLabel = new JLabel(img);
		lblNewLabel.setBounds(531, 106, 24, 24);
		lblNewLabel.setToolTipText("<html>To force Google to search for a particular term,<br> enclose the term between quotation marks <b>\" \"</b></html>");
		add(lblNewLabel);
		updateInternetStatus(true);
		Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	updateInternetStatus(false);
            }
        }, 0, 5000);

	}
	public static void updateInternetStatus(boolean showPane){
		boolean isConnected = MainFrame.isInternetReachable(showPane);
		if(isConnected){
			lblInternetStatus.setText("Internet Status: Connected");
		}
		else{
			lblInternetStatus.setText("Internet Status: Not Connected!");
		}
	}
}
