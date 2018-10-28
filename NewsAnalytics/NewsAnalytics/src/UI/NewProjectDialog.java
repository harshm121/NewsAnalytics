package UI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Backend.ProjectFile;
import Backend.main;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class NewProjectDialog extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6491039900895342268L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	public static String projectDirectory;
	public static String projectName;


	/**
	 * Create the frame.
	 */
	public NewProjectDialog(JFrame parentScreen, boolean showWarning) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 492, 322);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 6, 492, 294);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblProjectName = new JLabel("Project Name :");
		lblProjectName.setBounds(6, 52, 99, 22);
		panel.add(lblProjectName);
		
		textField = new JTextField();
		textField.setBounds(117, 50, 230, 26);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Project Location :");
		lblNewLabel.setBounds(6, 86, 110, 26);
		panel.add(lblNewLabel);
		
		textField_1 = new JTextField();
		textField_1.setBounds(117, 86, 230, 26);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Folder Already Exists!");
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setBounds(157, 7, 136, 16);
		lblNewLabel_1.setVisible(showWarning);
		panel.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Browse");
		btnNewButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			            projectDirectory=chooser.getSelectedFile().getAbsolutePath();
			            textField_1.setText(projectDirectory);
			            
			            
			    }
			}
		});
		btnNewButton.setBounds(359, 87, 82, 29);
		panel.add(btnNewButton);
		
		JButton btnNext = new JButton("Next");
		btnNext.setBounds(322, 231, 110, 29);
		btnNext.addActionListener(new ActionListener(){
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e){
				projectName=textField.getText();
				String date = ""+ Calendar.getInstance().get(Calendar.DATE)+"/"+ Calendar.getInstance().get(Calendar.MONTH)+"/"+ Calendar.getInstance().get(Calendar.YEAR);
				File newProjectFile = new File(projectDirectory+File.separator+projectName);
				if(newProjectFile.exists()){
					dispose();
					NewProjectDialog newp = new NewProjectDialog(parentScreen, true);
					newp.setVisible(true);
				}else{
					ProjectFile newProject = new ProjectFile(projectName, date, projectDirectory);
					main mainObject =null;
					mainObject = new main(newProject.getProjectTitle(), true , newProject.getLocation());
					MainFrame queryScreen = null;
					dispose();
					parentScreen.dispose();
					SplashScreen s=new SplashScreen();
			        s.setVisible(true);
					try {
						queryScreen = new MainFrame(newProject, mainObject);
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					
					File projectListFile=new File(Constants.getData()+File.separator+"projectList.ser");
					LinkedList<ProjectFile> projectList;
					if(!projectListFile.exists()){
						projectList=new LinkedList<ProjectFile>();
						projectList.add(newProject);
						main.serialize(projectList,projectListFile.getPath());
						
					}
					else{
						projectList = (LinkedList<ProjectFile>)main.deserialize(projectListFile.getPath());
						projectList.add(newProject);
						main.serialize(projectList,projectListFile.getPath());
					}
					s.dispose();
					queryScreen.setVisible(true);
				}
			}
		});
		panel.add(btnNext);
		JFrame frame=this;
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(210, 231, 110, 29);
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.dispose();
			}
		});
		panel.add(btnCancel);
		
	}
}
