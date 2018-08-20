package autoStart;


import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.event.KeyAdapter;

public class ErrorHandlerGUI extends JFrame {

	private JPanel contentPane;
	private JLabel panText;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ErrorHandlerGUI frame = new ErrorHandlerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ErrorHandlerGUI() {
		setBackground(new Color(0, 0, 0));
		setResizable(false);
		setTitle("Fehler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 200);
		contentPane = new JPanel();
		contentPane.setFocusable(false);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(false);
		
		JButton ok = new JButton("OK");
		ok.setBackground(Color.LIGHT_GRAY);
		ok.setBounds(45, 122, 250, 25);
		contentPane.add(ok);
		
		panText = new JLabel(""); //panText
		panText.setFocusable(false);
		panText.setHorizontalAlignment(SwingConstants.CENTER);
		panText.setForeground(Color.WHITE);
		panText.setBounds(10, 11, 324, 100);
		contentPane.add(panText);
//		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{ok, panText}));
        
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				dispose();
			}
		});
		
		ok.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10 || e.getKeyCode() == 27) {
					dispose();
				}
			}
		});
	}
	
	public void showError(String ECode){
		
		panText.setText(ECode);
		setVisible(true);
	}
}
