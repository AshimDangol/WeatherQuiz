package com.perisic.tomato.peripherals;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;

import com.perisic.tomato.engine.GameEngine;

/**
 * A Simple Graphical User Interface for the Six Equation Game.
 * 
 * @author Marc Conrad
 *
 */
public class GameGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = -107785653906635L;
	

	/**
	 * Method that is called when a button has been pressed.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("logout")) {
			new LoginData().clearSession();
			LoginGUI login = new LoginGUI();
			login.setVisible(true);
			dispose();
			return;
		}
		int solution = Integer.parseInt(cmd);
		checkAnswer(solution);
	}

	void checkAnswer(int solution) {
		boolean correct = myGame.checkSolution(solution);
		int score = myGame.getScore();
		if (correct) {
			System.out.println("Correct solution entered!");
			currentGame = myGame.nextGame();
			if (currentGame != null) {
				ImageIcon ii = new ImageIcon(currentGame);
				questArea.setIcon(ii);
			}
			infoArea.setText("Good " + myGame.getPlayer() + "!  Score: " + score
					+ " tries: " + myGame.getTries());
		} else {
			System.out.println("Not Correct");
			infoArea.setText("Oops. Try again!  Score: " + score
					+ " tries: " + myGame.getTries());
		}
	}

	JLabel questArea = null;
	GameEngine myGame = null;
	BufferedImage currentGame = null;
	JTextArea infoArea = null;
	JLabel topLabel = null;
	int seconds = 0;
/**
 * Initializes the game. 
 * @param player
 */
	private void initGame(String player) {
		setSize(700, 560);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("What is the missing value? - " + player);
		JPanel panel = new JPanel();

		myGame = new GameEngine(player);
		currentGame = myGame.nextGame();

		topLabel = new JLabel("player: " + player + "  0s");
		panel.add(topLabel);

		JButton logout = new JButton("logout");
		logout.setActionCommand("logout");
		logout.addActionListener(this);
		panel.add(logout);

		infoArea = new JTextArea(1, 40);
		
		infoArea.setEditable(false);
		infoArea.setText("What is the value of the tomato?   Score: 0");
		
		JScrollPane infoPane = new JScrollPane(infoArea);
		panel.add(infoPane);

		ImageIcon ii = new ImageIcon(currentGame);
		questArea = new JLabel(ii);
	    questArea.setSize(330, 600);
	    
		JScrollPane questPane = new JScrollPane(questArea);
		panel.add(questPane);

		for (int i = 0; i < 10; i++) {
			JButton btn = new JButton(String.valueOf(i));
			panel.add(btn);
			btn.addActionListener(this);
		}

		getContentPane().add(panel);
		panel.repaint();

		// keyboard 0-9 also answers, handy for demo
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent k) {
				char c = k.getKeyChar();
				if (c >= '0' && c <= '9') {
					checkAnswer(c - '0');
				}
			}
		});
		setFocusable(true);

		// simple timer event, updates label each second
		Timer timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seconds++;
				topLabel.setText("player: " + myGame.getPlayer() + "  " + seconds + "s");
			}
		});
		timer.start();

	}
/**
 * Default player is null. 
 */
	public GameGUI() {
		super();
		initGame(null);
	}

	/**
	 * Use this to start GUI, e.g., after login.
	 * 
	 * @param player
	 */
	public GameGUI(String player) {
		super();
		initGame(player);
	}

	/**
	 * Main entry point into the equation game. Can be used without login for testing. 
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		GameGUI myGUI = new GameGUI();
		myGUI.setVisible(true);

	}
}