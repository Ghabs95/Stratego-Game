package gj.stratego.player.pratesi.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



@SuppressWarnings("serial")
public class StrategoFrame extends JFrame{
	
	private JButton reset;
	private JButton start;
	private StrategoPanel boardPanel;

	public StrategoFrame() throws NumberFormatException, IOException {
		super("StrategoGame");
		setLocation(600, 150);
		setSize(800, 800);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		createGUI();
		pack();

	}
	
	private void createGUI() throws NumberFormatException, IOException {
		reset = new JButton("Reset");
		reset.addActionListener(new ResetListener());
		JPanel p = new JPanel();
		p.add(reset);
		getContentPane().add(p, BorderLayout.NORTH);
		start = new JButton("start");
		start.addActionListener(new StartListener());
		getContentPane().add(start, BorderLayout.SOUTH);
		boardPanel = new StrategoPanel(10 , 60);
		getContentPane().add(boardPanel, BorderLayout.CENTER);
	}
	
	class ResetListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			getContentPane().remove(boardPanel);
			try {
				boardPanel = new StrategoPanel(10, 50);
			} catch (NumberFormatException | IOException e1) {
				e1.printStackTrace();
			}
			getContentPane().add(boardPanel, BorderLayout.CENTER);
			pack();
		}
	}
	
	class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			boardPanel.reset();
		}
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		new StrategoFrame();
	}
}
