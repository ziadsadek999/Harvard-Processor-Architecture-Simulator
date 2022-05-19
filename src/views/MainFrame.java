package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

import components.CPU;
import utils.*;

public class MainFrame extends JFrame implements ActionListener {

	JPanel mainPanel;
	TextArea code;
	JButton runAll;
	JButton nextLine;
	boolean codeRead;
	JButton reset;
	public TextArea log;
	public TextArea[] registers;

	public MainFrame() {
		codeRead = false;
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(500, 500));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1, 2));
		code = new TextArea();
		code.setFont(new Font("Berlin Sans FB Demi", 2, 40));
		mainPanel.add(code);
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		log = new TextArea(20, 200);
		log.setEditable(false);
		log.setBackground(Color.black);
		log.setForeground(Color.RED);
		right.add(log);
		nextLine = new JButton("Next Line");
		runAll = new JButton("Run ALL");
		reset = new JButton("Reset");
		nextLine.addActionListener(this);
		runAll.addActionListener(this);
		reset.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(nextLine);
		buttonPanel.add(runAll);
		buttonPanel.add(reset);
		right.add(buttonPanel);
		TextArea title = new TextArea("REGISTERS", 1, 20, TextArea.SCROLLBARS_NONE);

		title.setEditable(false);
		right.add(title);
		JPanel rightBottom = new JPanel();
		rightBottom.setLayout(new GridLayout(16, 4));
		registers = new TextArea[64];
		for (int i = 0; i < 16; i++) {
			JPanel quadrable = new JPanel();
			quadrable.setLayout(new GridLayout(1, 4));
			registers[i] = new TextArea("", 1, 20, TextArea.SCROLLBARS_NONE);
			registers[i].setText("R" + i + ": 0b00000000/0");
			registers[i + 16] = new TextArea("", 1, 20, TextArea.SCROLLBARS_NONE);
			registers[i + 16].setText("R" + (i + 16) + ": 0b00000000/0");
			registers[i].setEditable(false);
			registers[i + 16].setEditable(false);
			registers[i + 48] = new TextArea("", 1, 20, TextArea.SCROLLBARS_NONE);
			registers[i + 48].setText("R" + (i + 48) + ": 0b00000000/0");
			registers[i + 32] = new TextArea("", 1, 20, TextArea.SCROLLBARS_NONE);
			registers[i + 32].setText("R" + (i + 32) + ": 0b00000000/0");
			registers[i + 48].setEditable(false);
			registers[i + 32].setEditable(false);
			quadrable.add(registers[i]);
			quadrable.add(registers[i + 16]);
			quadrable.add(registers[i + 32]);
			quadrable.add(registers[i + 48]);
			rightBottom.add(quadrable);
		}
		JScrollPane scrollable = new JScrollPane();
		scrollable.setViewportView(rightBottom);
		right.add(scrollable);
		mainPanel.add(right);
		add(mainPanel);
		this.revalidate();
		this.repaint();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == runAll) {
			code.setEditable(false);
			try {
				if (!codeRead) {
					PrintWriter pw = new PrintWriter(new File("input.txt"));
					String s = code.getText();
					pw.println(s);
					pw.flush();
					pw.close();
					Parser.parseProgram("input.txt");
					codeRead = true;
				}

				CPU.getInstance().runAll();
				runAll.setEnabled(false);
				nextLine.setEnabled(false);
			} catch (Exception ex) {
				// System.out.println(ex.getMessage());
			}
		} else if (e.getSource() == nextLine) {
			code.setEditable(false);
			try {
				if (!codeRead) {
					PrintWriter pw = new PrintWriter(new File("input.txt"));
					String s = code.getText();
					pw.println(s);
					pw.flush();
					pw.close();
					Parser.parseProgram("input.txt");
					codeRead = true;
				}
				boolean finished = CPU.getInstance().runNextInstruction();
				if (finished) {
					runAll.setEnabled(false);
					nextLine.setEnabled(false);
				}
			} catch (Exception ex) {

			}
		} else {
			code.setText("");
			log.setText("");
			runAll.setEnabled(true);
			nextLine.setEnabled(true);
			code.setEditable(true);
			codeRead = false;
			
		}

	}
}
