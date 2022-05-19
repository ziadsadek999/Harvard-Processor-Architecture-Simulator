package views;

import java.awt.*;

import javax.swing.*;
public class MainFrame extends JFrame {
	
	JPanel mainPanel;
	TextArea code;
	public MainFrame() {
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(500,500));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,2));
		code = new TextArea();
		code.setFont(new Font("Berlin Sans FB Demi", 2, 40));
		mainPanel.add(code);
		JPanel right = new JPanel();
		right.setLayout(new GridLayout(2,1));
		Label log = new Label("Log");
		log.setBackground(Color.black);
		JButton nextLine = new JButton("Next Line");
		JButton runALL = new JButton("Run ALL");
		JPanel rightBottom = new JPanel();
		rightBottom.add(nextLine);
		rightBottom.add(runALL);
		right.add(log);
		right.add(rightBottom);
		mainPanel.add(right);
		add(mainPanel);
		this.revalidate();
		this.repaint();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}
}
