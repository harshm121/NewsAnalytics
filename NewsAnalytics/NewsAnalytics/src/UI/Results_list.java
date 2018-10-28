package UI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import Backend.NewsArticle;
import Backend.parser_0;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

public class Results_list extends JFrame {

	private JPanel contentPane;
	private JTextField txtOpenInBrowser;
	private JPopupMenu rightClickMenu=new JPopupMenu();
	private JTextField textField;

	/**
	 * Launch the application.
	 
	*/
	/**
	 * Create the frame.
	 */
	/*
	public class newsArticleListCellRenderer extends DefaultListCellRenderer {
	    public Component getListCellRendererComponent(JList<?> list,
	                                 Object value,
	                                 int index,
	                                 boolean isSelected,
	                                 boolean cellHasFocus) {
	        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        if (value instanceof newsArticle) {
	            newsArticle newsArticle = (newsArticle)value;
	            setText(newsArticle.getTitle());
	            //setToolTipText(newsArticle.getDescription());
	            // setIcon(newsArticle.getIcon());
	        }
	        return this;
	    }
	}*/
	
	public class newsArticleRenderer extends JLabel implements ListCellRenderer<NewsArticle> {
		 
	    public newsArticleRenderer() {
	        setOpaque(true);
	    }
	 
	    @Override
	    public Component getListCellRendererComponent(JList<? extends NewsArticle> list, NewsArticle newsItem, int index,
	            boolean isSelected, boolean cellHasFocus) {
//	 
//	        String code = country.getCode();
//	        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + code + ".png"));
//	 
//	        setIcon(imageIcon);
	        setText(newsItem.getTitle());
	 
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
	
	
	public Results_list( LinkedList<NewsArticle> newsList, parser_0 parser, LinkedList<String> keywords) {
		Collections.sort(newsList);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(300, 100, 500, 539);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(true);
		setResizable(false);
		
		DefaultListModel<NewsArticle> listModel = new DefaultListModel<NewsArticle>();
		
	    Iterator<NewsArticle> articleIterator = newsList.iterator();
	    while(articleIterator.hasNext())
	    {
	    	listModel.addElement(articleIterator.next());
	    }
		
		JList<NewsArticle> list = new JList<>(listModel);
		
		list.setCellRenderer(new newsArticleRenderer());
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(26, 27, 439, 432);
		contentPane.add(scrollPane);
		
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(10);
		
		textField = new JTextField();
		textField.setToolTipText("Enter keywords separated by comma and a space");
		String s="";
		if(!((keywords == null) || (keywords.size()==0))){
			for(int i=0;i<keywords.size()-1;i++){
				s += keywords.get(i)+", "; 
			}
			s+= keywords.get(keywords.size()-1);
		}
		textField.setText(s);
		textField.setBounds(163, 474, 302, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Filter");
		btnNewButton.setToolTipText("Filter article containg these keywords");
		btnNewButton.setBounds(26, 471, 117, 29);
		btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			String keywordText = textField.getText();
        			String[] keywords = keywordText.split(", ");
        			LinkedList<String> keywordList = new LinkedList<>();
        			for(int i=0;i<keywords.length;i++){
        				keywordList.add(keywords[i]);
        			}
        			LinkedList<NewsArticle> newsList = parser.compileResults(keywordList, true);
        			Results_list newRl = new Results_list(newsList, parser, keywordList);
        			newRl.setVisible(true);
        			dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        });
		contentPane.add(btnNewButton);
        this.setTitle("News Articles");
        
        /*
         * Mouse click event
         * 
         */
        this.getRootPane().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	if(evt.getClickCount() == 1){
                	rightClickMenu.removeAll();
                	rightClickMenu.setVisible(false);
                }
            }
        });
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	rightClickMenu.removeAll();
            	rightClickMenu.setVisible(false);
                JList list1 = (JList)evt.getSource();
                if (evt.getButton() == MouseEvent.BUTTON1){
	                if (evt.getClickCount() == 2) {
	                	int index = list.getSelectedIndex();
	                	NewsArticle selected = listModel.get(index);
	                	HTMLdata selectedDataViewer = new HTMLdata(selected,parser);
	                    //int index = list1.locationToIndex(evt.getPoint());
	                }
	                if(evt.getClickCount() == 1){
	                	rightClickMenu.removeAll();
	                	rightClickMenu.setVisible(false);
	                }
                }
                if (evt.getButton() == MouseEvent.BUTTON3){
                	int index = list.getSelectedIndex();
    				NewsArticle selected = listModel.get(index);
    				//rightClickMenu=new JPopupMenu();
    				JMenuItem openInBrowser= new JMenuItem("Open In Browser");
    				JMenuItem copyURL = new JMenuItem("Copy URL");
    				openInBrowser.addActionListener(new ActionListener(){
    					public void actionPerformed(ActionEvent e){
    						int index = list.getSelectedIndex();    
    						NewsArticle selected = listModel.get(index);
    						// CODE to display link text for element 'selected' of type newsArticle
    						rightClickMenu.removeAll();
    		            	rightClickMenu.setVisible(false);
    						try {
    							java.awt.Desktop.getDesktop().browse(new URL(selected.getUrl()).toURI());
    						} catch (MalformedURLException e1) {
    							e1.printStackTrace();
    						} catch (IOException e1) {
    							e1.printStackTrace();
    						} catch (URISyntaxException e1) {
    							e1.printStackTrace();
    						}
    						rightClickMenu.setVisible(true);
    					}
    				});
    				copyURL.addActionListener(new ActionListener(){
    					public void actionPerformed(ActionEvent e){
    						int index = list.getSelectedIndex();
    						NewsArticle selected = listModel.get(index);
    						rightClickMenu.removeAll();
    		            	rightClickMenu.setVisible(false);
    						String url=selected.getUrl();
    						StringSelection stringSelection = new StringSelection(url);
    						Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
    						clpbrd.setContents(stringSelection, null);
    						rightClickMenu.setVisible(false);
    					}
    				});
    				rightClickMenu.add(openInBrowser);
    				rightClickMenu.add(copyURL);
    				rightClickMenu.setLocation(evt.getXOnScreen(),evt.getYOnScreen());
    				rightClickMenu.setVisible(true);
                }
            }
        });
		
	}
}
