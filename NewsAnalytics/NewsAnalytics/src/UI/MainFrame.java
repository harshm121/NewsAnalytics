package UI;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


import Backend.ProjectFile;
import Backend.QueryDetails;
import Backend.main;


public class MainFrame extends JFrame {
	
	public String savedProxyHost;
	public String savedProxyPort;	
	public static MainFrame frame;
	public static JPanel panel;
	public File userDirectory;
	public ProjectFile project;
	public main mainObj;

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public MainFrame(ProjectFile proj, main mainObj) throws ClassNotFoundException, IOException {
		frame=this;
		this.project=proj;
		this.mainObj = mainObj;
		setType(Type.NORMAL);
		setTitle("NewsStory Lifeline - "+project.getProjectTitle());
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBounds(300, 100, 696, 632);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window?", "We will miss you", JOptionPane.YES_NO_OPTION, 
		        		JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            System.exit(0);
		        }
		    }
		});
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		MainFrame fs=this;
		JMenuItem mnNewProject = new JMenuItem("New Project");
		mnNewProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewProjectDialog np=new NewProjectDialog(fs, false);
				np.setVisible(true);
		    }
		});
		JMenuItem mnOpenProject = new JMenuItem("Open Project");
		mnOpenProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TitleScreen ts=new TitleScreen();
				ts.setVisible(true);
		        }
		});
		JMenuItem mnCloseProject = new JMenuItem("Close Project");
		mnCloseProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TitleScreen ts=new TitleScreen();
				ts.setVisible(true);
				frame.dispose();
		    }
		});
		
		JMenuItem mnImport = new JMenuItem("Import");
		mnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("ZIP","zip");
				    chooser.setFileFilter(filter);
				chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	  main.importProject(chooser.getSelectedFile().getPath());
			    	  //ProjectFile projectimport=(ProjectFile) main.deserialize(chooser.getSelectedFile().getPath().replace(".zip", "")+File.separator+"FinalProject.ser");
			    	  //TODO
			    	  ProjectFile projectImport = null;
			          frame.dispose();
			          MainFrame fs = null;
					try {
 
						fs = new MainFrame(projectImport,mainObj);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			          fs.setVisible(true);
			          fs.goToLoading(projectImport.getQueries().getLast());
			            
			            
			    }
			}
		});
		
		
		JMenuItem mnExport = new JMenuItem("Export");
		mnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.serialize(project, project.getLocation()+File.separator+project.getProjectTitle()+File.separator+project.getProjectTitle()+".ser");
				main.exportProject(project.getLocation()+File.separator+project.getProjectTitle(), project.getLocation()+File.separator+project.getProjectTitle()+"-zipped.zip", project.getProjectTitle());
				JOptionPane.showMessageDialog(frame, project.getProjectTitle()+" Exported Successfully");
		    }
		});
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window?", "We will miss you", JOptionPane.YES_NO_OPTION, 
		        		JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            System.exit(0);
		        }
			}
		});
		
		ProxyDialog dialog = new ProxyDialog();
		dialog.setVisible(false);
		
		JMenuItem mntmProxySettings = new JMenuItem("Proxy Settings");
		mntmProxySettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dialog.setVisible(true);
			}
		});
		
		mnFile.add(mnNewProject);
		mnFile.add(mnOpenProject);
		mnFile.add(mnCloseProject);
		mnFile.add(mnExport);
		mnFile.add(mnImport);
		mnFile.add(mntmProxySettings);
		
		JMenuItem mntmGoogleConfigurations = new JMenuItem("Google Configurations");
		mntmGoogleConfigurations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GoogleConfigurationFrame gcf = new GoogleConfigurationFrame();
				gcf.setVisible(true);
			}
		});
		mnFile.add(mntmGoogleConfigurations);
		
		mnFile.add(mntmExit);
		
		JMenu mnView=new JMenu("View");
		
		JMenuItem mnTodo=new JMenuItem("ToDo");
		mnTodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File myToDo = new File(Constants.getData()+File.separator+"TODO.txt");
				try {
					if(myToDo.createNewFile()){
						 FileWriter fstream = new FileWriter(myToDo);
						  BufferedWriter out = new BufferedWriter(fstream);
						  out.write("Your Personalised ToDo. Pen down your pending tasks: \n");
						  out.close();
					}
					Desktop.getDesktop().edit(myToDo);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
//		JMenuItem mnEventLog=new JMenuItem("Event Log");
//		mnEventLog.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		});
		mnView.add(mnTodo);
//		mnView.add(mnEventLog);
		menuBar.add(mnView);
		
		JMenu mnQuery=new JMenu ("Query");
		JMenuItem mnNewQuery= new JMenuItem("New Query");
		mnNewQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					Loading p = (Loading) panel;
					p.pause();
				}catch(Exception ez){
					ez.printStackTrace();
				}
				frame.removeExistingPanel();
				panel = new QueryInputPanel(project, frame);
				frame.getContentPane().add(panel, BorderLayout.CENTER);
				panel.setVisible(true);
				frame.revalidate();
				frame.repaint();
			}
		});
		JMenuItem mnBrowseQueries =new JMenuItem("Browse Queries");
		mnBrowseQueries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryBrowser qb=new QueryBrowser(project,frame, mainObj);
				qb.setVisible(true);
			}
		});
		mnQuery.add(mnNewQuery);
		mnQuery.add(mnBrowseQueries);
		menuBar.add(mnQuery);
		
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
	
		
		JMenuItem mntmHowToUse = new JMenuItem("How to Use");
		mntmHowToUse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						java.awt.Desktop.getDesktop().browse(new URL("https://drive.google.com/open?id=1sLRErDO2D7hQyd14HdTe3awSeeeVwgpA2YUNViXIjZ0").toURI());
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			});
		mnHelp.add(mntmHowToUse);
		
		JMenuItem mntmContactDevelopers = new JMenuItem("Contact Developers");
		mntmContactDevelopers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().browse(new URL("mailto:harshm121@gmail.com").toURI());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmContactDevelopers);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		
		JMenuItem mntmKnowMoreAbout = new JMenuItem("Know more about the application");
		mntmKnowMoreAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame about =  new AboutFrame();
				about.setVisible(true);
			}
		});
		mnAbout.add(mntmKnowMoreAbout);
		panel = new QueryInputPanel(project, frame);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setVisible(true);
		frame.revalidate();
		frame.repaint();


	}
	public void goToLoading(QueryDetails query){
		query.printQueryDetails();
		this.removeExistingPanel();
		panel=new Loading(project,query);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		File projectListFile=new File(Constants.getData()+File.separator+"projectList.ser");
		LinkedList<ProjectFile> projectList = (LinkedList<ProjectFile>)main.deserialize(projectListFile.getPath());
		Iterator<ProjectFile> it = projectList.iterator();
		while(it.hasNext()){
			ProjectFile temp =it.next();
			if(temp.getProjectTitle().equals(project.getProjectTitle()))
				it.remove();
		}
		
		projectList.add(project);
		main.serialize(projectList,projectListFile.getPath());
		panel.setVisible(true);
		frame.revalidate();
		frame.repaint();
	}
	
	public static boolean isInternetReachable(boolean showPane){
		LinkedList<String> domains=new LinkedList<String>();
		domains.add("https://www.bing.com");
		domains.add("https://www.stackoverflow.com");
		domains.add("https://www.yahoo.com");
		Random randomUserAgentIndex = new Random();
			
        try {
            URL url = new URL(domains.get(randomUserAgentIndex.nextInt(domains.size())));
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
            Object objData = urlConnect.getContent();

        } catch (UnknownHostException e) {
        	if(showPane)
        		JOptionPane.showMessageDialog(null, "Error in internet Connection", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        catch (IOException e) {
        	if(showPane)
        		JOptionPane.showMessageDialog(null, "Error in internet Connection", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
	public void removeExistingPanel(){
		panel.setEnabled(false);
		this.remove(panel);
		this.revalidate();
		this.repaint();
		frame.remove(panel);
		frame.revalidate();
		frame.repaint();
	}
}
class DateLabelFormatter extends AbstractFormatter {

    private String datePattern = "dd/MM/yyyy";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }

}
class InternetNotConnectedException extends Exception{
	public InternetNotConnectedException(boolean showPane){
		if(showPane)
    		JOptionPane.showMessageDialog(null, "Error in internet Connection", "Error", JOptionPane.ERROR_MESSAGE);
    	
	}
	
}
