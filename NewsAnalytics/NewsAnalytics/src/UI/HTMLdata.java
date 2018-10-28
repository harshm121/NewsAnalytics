package UI;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Backend.NewsArticle;
import Backend.parser_0;

import javax.swing.JScrollPane;

public class HTMLdata extends JFrame {

	/**
	 * Create the panel.
	 */
	public HTMLdata(NewsArticle selected, parser_0 parser) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setBounds(300, 100, 696, 609);
		setVisible(true);
		setResizable(false);
		
		JLabel textAreaTitle = new JLabel();
		textAreaTitle.setHorizontalAlignment(SwingConstants.CENTER);
		textAreaTitle.setText(selected.getTitle());
		textAreaTitle.setBounds(82, 15, 530, 30);
		getContentPane().add(textAreaTitle);
		
		JTextArea textAreaData = new JTextArea();
		textAreaData.setBounds(6, 57, 438, 215);
		textAreaData.setEditable(false);
		//System.out.println(parser.getContent(selected));
		JScrollPane scrollPane = new JScrollPane(textAreaData);
		scrollPane.setBounds(17, 84, 658, 484);
		getContentPane().add(scrollPane);
		textAreaData.setText(parser.getContent(selected));

	}
}
