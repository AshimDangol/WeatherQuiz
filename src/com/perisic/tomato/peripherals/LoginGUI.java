package com.perisic.tomato.peripherals;
/*
 * Code adapted from from https://best-programming-tricks.blogspot.com/2011/07/how-to-make-login-form-with-java-gui.html
 */

import javax.swing.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6921462126880570161L;

	public static void main(String[] args) {
		LoginGUI frameTabel = new LoginGUI();
	}

	JButton blogin = new JButton("Login");
	JButton bsignup = new JButton("Sign up");
	JCheckBox remember = new JCheckBox("remember me", true);
	JPanel panel = new JPanel();
	JTextField txuser = new JTextField(15);
	JPasswordField pass = new JPasswordField(15);
	
	LoginData ldata = new LoginData(); 

	LoginGUI() {
		super("Login Autentification");
		setSize(300, 250);
		setLocation(500, 280);
		panel.setLayout(null);

		txuser.setBounds(70, 30, 150, 20);
		pass.setBounds(70, 65, 150, 20);
		blogin.setBounds(40, 100, 80, 20);
		bsignup.setBounds(140, 100, 90, 20);
		remember.setBounds(70, 130, 150, 20);

		panel.add(blogin);
		panel.add(bsignup);
		panel.add(txuser);
		panel.add(pass);
		panel.add(remember);

		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		String saved = ldata.checkSession();
		if (saved != null) {
			openGame(saved);
			return;
		}

		actionlogin();
	}

	void openGame(String user) {
		GameGUI theGame = new GameGUI(user);
		theGame.setVisible(true);
		dispose();
	}

	public void actionlogin() {
		blogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String puname = txuser.getText().trim();
				String ppaswd = String.valueOf(pass.getPassword()); // https://stackoverflow.com/questions/10443308/why-gettext-in-jpasswordfield-was-deprecated
				if (ldata.checkPassword(puname, ppaswd)) {
					if (remember.isSelected()) {
						ldata.createSession(puname);
					} else {
						ldata.clearSession();
					}
					openGame(puname);
				} else {

					JOptionPane.showMessageDialog(null, "Wrong Password / Username");
					txuser.setText("");
					pass.setText("");
					txuser.requestFocus();
				}

			}
		});
		bsignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String puname = txuser.getText().trim();
				String ppaswd = String.valueOf(pass.getPassword());
				if (puname.equals("") || ppaswd.length() < 4) {
					JOptionPane.showMessageDialog(null, "pick a name and 4+ char password");
					return;
				}
				try {
					boolean ok = ldata.addUser(puname, ppaswd);
					if (ok) {
						JOptionPane.showMessageDialog(null, "account made, you can log in now");
					} else {
						JOptionPane.showMessageDialog(null, "name taken or bad input");
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "error making account");
				}
			}
		});
	}
}
