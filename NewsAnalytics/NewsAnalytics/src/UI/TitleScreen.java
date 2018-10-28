package UI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.UIManager;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.Component;

import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import Backend.GoogleConfig;
import Backend.ProjectFile;
import Backend.QueryDetails;
import Backend.main;
import Backend.proxy;

public class TitleScreen extends JFrame {

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JPanel ProjectDisplay = new JPanel();
	

	/**
	 * Create the frame.
	 */
	
	public class projectListRenderer extends JLabel implements ListCellRenderer<ProjectFile> {
		 
	    /**
		 * 
		 */
		private static final long serialVersionUID = 5710762749198526728L;
		
		public projectListRenderer() {
	        setOpaque(true);
	    }
	 
	    @Override
	    public Component getListCellRendererComponent(JList<? extends ProjectFile> list, ProjectFile projectItem, int index,
	            boolean isSelected, boolean cellHasFocus) {
//	 
//	        String code = country.getCode();
//	        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + code + ".png"));
//	 
//	        setIcon(imageIcon);
	        setText(projectItem.getProjectTitle());
	 
	        if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
	 
	        return this;
	    }
	} 
	
	
	public TitleScreen() {
		javax.swing.ToolTipManager.sharedInstance().setInitialDelay(0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 560, 363);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		ProjectDisplay.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		
		ProjectDisplay.setBounds(0, 32, 225, 303);
		contentPane.add(ProjectDisplay);
		ProjectDisplay.setLayout(null);
		
		JLabel lblExistingProjects = new JLabel("Existing Projects");
		lblExistingProjects.setBounds(56, 6, 105, 21);
		ProjectDisplay.add(lblExistingProjects);
		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		mainPanel.setBounds(226, 32, 328, 303);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);
		
		JButton btnCreate = new JButton("Create New");
		btnCreate.setBounds(113, 105, 117, 29);
		TitleScreen ts=this;
		mainPanel.add(btnCreate);
		btnCreate.setToolTipText("Each Project can have multiple search queries");
		btnCreate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				NewProjectDialog newProjectD=new NewProjectDialog(ts, false);
				newProjectD.setVisible(true);
			}
		});
		
		JButton btnImport = new JButton("Import");
		btnImport.setBounds(113, 140, 117, 29);
		btnImport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("ZIP","zip");
				    chooser.setFileFilter(filter);
				chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	  main.importProject(chooser.getSelectedFile().getPath());
			    	  try {
			    		  	dispose();
							TitleScreen first = new TitleScreen();
							first.setVisible(true);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
			    }
			}
		});
		mainPanel.add(btnImport);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBounds(0, 0, 554, 31);
		contentPane.add(titlePanel);
		titlePanel.setLayout(null);
		
		JLabel lblNewsAnalytics = new JLabel("News Analytics");
		lblNewsAnalytics.setBounds(209, 6, 133, 20);
		lblNewsAnalytics.setForeground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		lblNewsAnalytics.setFont(new Font("Heiti TC", Font.PLAIN, 16));
		titlePanel.add(lblNewsAnalytics);
		File projectListFile=new File(Constants.getData()+File.separator+"projectList.ser");
		LinkedList<ProjectFile> projectList;
		if(!projectListFile.exists()){
			projectList=new LinkedList<ProjectFile>();
		}
		else{
			projectList = (LinkedList<ProjectFile>)main.deserialize(projectListFile.getAbsolutePath());
			projectList = main.checkForProjects(projectList);
		}
		
		
		
		
		DefaultListModel<ProjectFile> listModel = new DefaultListModel<ProjectFile>();
		
	    Iterator<ProjectFile> projectIterator = projectList.iterator();	
	    while(projectIterator.hasNext())
	    {
	    	ProjectFile temp = projectIterator.next();
	    	listModel.addElement(temp);
	    }
		
		JList<ProjectFile> list = new JList<>(listModel);
		
		list.setCellRenderer(new projectListRenderer());
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(6, 31, 213, 266);
		ProjectDisplay.add(scrollPane);
		
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(25);
		
		JFrame tsFrame=this;
		list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
//                JList list1 = (JList)evt.getSource();
                if (evt.getButton() == MouseEvent.BUTTON1){
	                if (evt.getClickCount() == 2) {
	                	int index = list.getSelectedIndex();
	                	ProjectFile selected = listModel.get(index);
//	                	JPanel loadingPanel=new Loading(selected, selected.queryList.getLast());
	                	try {
	                		main mainObj = new main(selected.getProjectTitle(), false, selected.getLocation());
							MainFrame fs=new MainFrame(selected, mainObj);
							fs.setVisible(true);
							tsFrame.dispose();
							LinkedList<QueryDetails> list = selected.getQueries();
							if((list != null )&&( list.size()!=0 )){
								File querySer =  new File(selected.getLocation()+File.separator+selected.getProjectTitle()+File.separator+
													"queries"+File.separator+list.getLast().queryDirectoryName()+File.separator+list.getLast().getQuery()+".nq");
								QueryDetails qd = (QueryDetails) main.deserialize(querySer.getAbsolutePath());
								fs.goToLoading(qd);
							}

							
							
						}catch (ClassNotFoundException | IOException  e) {
							e.printStackTrace();
						}
	                }
                }
            }
        });
		
		
	}

	public static void main(String[] args) {
		 File tempFolder = new File(Constants.getData());
			if(!tempFolder.exists()){
				tempFolder.mkdirs();
			}
		
			File errorFile = new File(Constants.getData()+File.separator+"logs.txt");
			FileOutputStream fos = null;
			try {
				errorFile.createNewFile();
				fos = new FileOutputStream(errorFile);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			PrintStream ps = new PrintStream(fos);
			System.setErr(ps);
			try{
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		TitleScreen first = new TitleScreen();
		SplashScreen s=new SplashScreen();
        s.setVisible(true);
		
        File defGC = new File(Constants.getData()+File.separator+"googleConfig.ser");
        
        if(!defGC.exists()){
	        GoogleConfig defaultGC = new GoogleConfig();
	        defaultGC.setUrlTag1("l _PMs");
	        defaultGC.setUrlTag2("_pJs");
	        defaultGC.setDateTag1("f nsa _QHs");
	        defaultGC.setDateTag2("nsa _QHs f");
	        defaultGC.setPublisherTag1("_OHs _PHs");
	        defaultGC.setPublisherTag2("nsa _OHs f _PHs");
	        defaultGC.setDateFormat("dd/MM/yyyy");
	        main.serialize(defaultGC, defGC.getAbsolutePath());
        }
        File proxyConfigs = new File(Constants.getData()+File.separator+"proxyConfig.ser");
        if(proxyConfigs.exists()){
        	ProxyConfig p = (ProxyConfig) main.deserialize(proxyConfigs.getAbsolutePath());
        	proxy.enableProxy(p.getProxyNameHTTP(), p.getProxyPortHTTP(), p.getProxyNameHTTPS(), p.getProxyPortHTTPS());
        }
        Thread t=Thread.currentThread();
        try {
        	t.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        s.dispose();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					File DataFolder = new File(Constants.getData());
					if(!DataFolder.exists())
						DataFolder.mkdir();
					first.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

