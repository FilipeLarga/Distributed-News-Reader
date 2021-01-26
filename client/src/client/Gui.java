package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import common.News;
import common.NewsRequest;
import common.SearchRequest;
import common.SearchResult;

public class Gui {

	private Client client;

	private JTextField search;
	private JTextArea area;
	private JFrame frame;

	private DefaultListModel<SearchResult> model;
	private JList<SearchResult> jlist;

	public Gui(Client client) {
		this.client = client;
	}

	public void addComponents() {
		frame = new JFrame("Seracher");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				client.shutdown();
				close();
			}
		});

		frame.setSize(1500, 800);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((int) dimension.getWidth() / 2 - (frame.getWidth() / 2),
				(int) dimension.getHeight() / 2 - (frame.getHeight() / 2));

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1, 2));

		search = new JTextField("", 20);
		panel1.add(search);

		JButton b = new JButton("Search");
		panel1.add(b);

		frame.add(panel1, BorderLayout.NORTH);

		model = new DefaultListModel<SearchResult>();

		jlist = new JList<SearchResult>(model);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		JScrollPane scroll = new JScrollPane(jlist);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		panel2.add(scroll);

		area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);

		panel2.add(area);

		frame.add(panel2, BorderLayout.CENTER);
		frame.setVisible(true);

		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (search.getText() != null && !search.getText().equals("")) {
					model.clear();
					client.sendSearchRequest(new SearchRequest(search.getText()));
					search.setText("");
					area.setText("");
				}
			}
		});

		jlist.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && jlist.getSelectedIndex() >= 0) {
					client.sendNewsRequest(new NewsRequest(jlist.getSelectedValue().getName()));
				}
			}
		});
	}

	public void close() {
		frame.dispose();
	}

	public void updateText(News news) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				area.setText(news.getTitle() + "\n" + news.getCorpus());
			}
		});
	}

	public void updateList(List<SearchResult> results) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (SearchResult result : results) {
					model.addElement(result);
				}
			}
		});
	}

}
