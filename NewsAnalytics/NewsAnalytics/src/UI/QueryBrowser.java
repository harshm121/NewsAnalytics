package UI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Backend.ProjectFile;
import Backend.QueryDetails;
import Backend.main;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;

class queryListRenderer extends JLabel implements ListCellRenderer<QueryDetails> {
	 
    public queryListRenderer() {
        setOpaque(true);
    }
 
    @Override
    public Component getListCellRendererComponent(JList<? extends QueryDetails> list, QueryDetails query, int index,
            boolean isSelected, boolean cellHasFocus) {
// 
//        String code = country.getCode();
//        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + code + ".png"));
// 
//        setIcon(imageIcon);
        setText(query.getQuery());
 
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

public class QueryBrowser extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public QueryBrowser(ProjectFile project, MainFrame parentFrame, main mainObj) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 427, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane.setLayout(null);
		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 415, 384);
		contentPane.add(panel);
		File desrProjFile = new File(project.getLocation()+File.separator+project.getProjectTitle()+File.separator+project.getProjectTitle()+".ser");
		if(desrProjFile.exists()){
			project = (ProjectFile) main.deserialize(desrProjFile.getAbsolutePath());
		}
		LinkedList<QueryDetails> queryList=project.getQueries();
		DefaultListModel<QueryDetails> listModel = new DefaultListModel<QueryDetails>();
	    Iterator<QueryDetails> iterator = queryList.iterator();
	    while(iterator.hasNext())
	    {
	    	listModel.addElement(iterator.next());
	    }
		panel.setLayout(null);
		
		JList<QueryDetails> list = new JList<>(listModel);
		list.setCellRenderer(new queryListRenderer());
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(20, 41, 371, 322);
		panel.add(scrollPane);
		
		JLabel lblQueries = new JLabel("Queries");
		lblQueries.setBounds(158, 13, 61, 16);
		panel.add(lblQueries);
		
		
		list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list1 = (JList)evt.getSource();
                if (evt.getButton() == MouseEvent.BUTTON1){
	                if (evt.getClickCount() == 2) {
	                	int index = list.getSelectedIndex();
	                	QueryDetails selected = listModel.get(index);
	                    File querySelected = new File(selected.getLocation()+File.separator+selected.getQuery()+".nq");
	                    QueryDetails qs = (QueryDetails) main.deserialize(querySelected.getAbsolutePath());
	                    try{
	                    	Loading p = (Loading) parentFrame.panel;
	                    	p.pause();
	                    }catch(Exception e){
	                    	e.printStackTrace();
	                    }
 	                    parentFrame.removeExistingPanel();
	                    parentFrame.goToLoading(qs);
	                    
	                }
                }
               
            }
        });
	}
}
