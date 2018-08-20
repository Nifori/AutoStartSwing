package autoStart;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;



public class StartGUI extends JFrame {
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 4;
	private JPanel contentPane;
	StartoptionGUI optionUI = new StartoptionGUI();		//Optionen starten um Dateiliste zu laden	
	myList<Startdatei> pListe = new myList<Startdatei>();
	boolean pStrg = false;
	int faktor;
	private JButton btnAusfuehren;
	private JButton options;
	private JButton btnAbbrechen;
	private JLabel lblBitteGewnschteStartprogramme;
	JRadioButton[] radButton;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartGUI frame = new StartGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public StartGUI() throws Exception {
		
		setFaktor(); //aufrunden und richtung
		
		setTitle("Automatischer Start");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650-faktor, 140);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setFocusable(false);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		
		
		setUI(contentPane);
		
	}
	public void delay(){
		Calendar cal = Calendar.getInstance();
		long t = cal.getTimeInMillis();
		while(Calendar.getInstance().getTimeInMillis()<t+5000){
		}
	}
	public void refreshList() {
		myList<Startdatei> p2Liste = new myList<Startdatei>();
		p2Liste = optionUI.gibListe();						//Dateiliste anfordern
		p2Liste.toFirst();
		pListe.removeAll();
		while(p2Liste.hasAccess()){
			pListe.append(p2Liste.getContent());
			p2Liste.next();
		}
	}
	
	public void setFaktor() {
		int halfLength;

		if(optionUI.getLength()%2!=0){
			halfLength = optionUI.getLength()/2 +1;
		}
		else{
			halfLength = optionUI.getLength()/2;
		}
		if(halfLength > 4){
			halfLength = 4;
		}
		
		faktor = (4-halfLength)*85;
		
	}
	
	public void doRefresh(JPanel tmpPane) {
		tmpPane.removeAll();
		tmpPane.repaint();
		setFaktor();
		this.setBounds(100, 100, 650-faktor, 140);
		setUI(tmpPane);
		this.transferFocus();
	}
	
	public void setUI(JPanel contentPane) {
		
		refreshList();
						
		lblBitteGewnschteStartprogramme = new JLabel("Bitte gew\u00FCnschte Startprogramme w\u00E4hlen und ausf\u00FChren");
		lblBitteGewnschteStartprogramme.setFocusable(false);
		lblBitteGewnschteStartprogramme.setBounds(30, 11, 586-faktor, 14);
		contentPane.add(lblBitteGewnschteStartprogramme);
		
		pListe.toFirst();			

		JRadioButton pEins = new JRadioButton();
		JRadioButton pZwei = new JRadioButton();
		JRadioButton pDrei = new JRadioButton();
		JRadioButton pVier = new JRadioButton();
		JRadioButton pFuenf = new JRadioButton();
		JRadioButton pSechs = new JRadioButton();
		JRadioButton pSieben = new JRadioButton();
		JRadioButton pAcht = new JRadioButton();
		
		radButton  = new JRadioButton[8];
		
		radButton[0] = pEins;
		radButton[1] = pZwei;
		radButton[2] = pDrei;
		radButton[3] = pVier;
		radButton[4] = pFuenf;
		radButton[5] = pSechs;
		radButton[6] = pSieben;
		radButton[7] = pAcht;
		
		pListe.toFirst();
		
		for(int i = 0; i<radButton.length && pListe.hasAccess(); i++) {
			if(pListe.hasAccess()){
				radButton[i].setText(pListe.getContent().gibName());
				radButton[i].setBounds(30+(i-i%2)*50, 32+(i%2)*28, 100, 23);
				contentPane.add(radButton[i]);
				if(pListe.getContent().gibAuswahl() > 0){
					radButton[i].setSelected(true);
				}
			}
			else {
				radButton[i].setFocusable(false);
			}
			pListe.next();
		}
		
		pListe.toFirst();
		
		btnAusfuehren = new JButton("Start");
		btnAusfuehren.setBounds(435-faktor, 31, 79, 53);
		btnAusfuehren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				
				int a = 0;
				pListe.toFirst();
		
				for(int i = 0; i<radButton.length; i++) {
					if(radButton[i].isSelected()){ a++; }			//Anzahl der gewuenschten Programme z‰hlen f¸r Wartezeit-Abgleich
				}
				
				for(int i = 0; i<radButton.length; i++) {
					if(radButton[i].isSelected()){					
						try {
							ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", pListe.getContent().gibPfad()); //ueber CMD die Datei im Pfad aus der Liste laden
							processBuilder.start();	//und starten
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(pListe.getContent().gibAuswahl() == 0){
							pListe.getContent().setAuswahl(1);
						}
						a--;												//Rest-Start-Datei-Anzahl senken f¸r Wartezeit-Abgleich
						if( a > 0){											//Wenn noch mehr gestartet werden sollen, 5 Sek warten als Puffer
							delay();
						}
						
					}
					else{
						if(pListe.getContent().gibAuswahl() == 1){
							pListe.getContent().setAuswahl(0);
						}
					}
					pListe.next();											//In Liste zur naechsten datei wechseln
					
				}
				optionUI.setList(pListe);
				
				System.exit(0);//Fenster schlieﬂen
			}
			
		});		
		
		contentPane.add(btnAusfuehren);
		
		options = new JButton("Optionen");
		options.setBounds(524-faktor, 31, 100, 25);
		contentPane.add(options);
		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				optionUI.setVisible(true);	//Optionen-Fenster starten
				
			}
		});
		btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.setBounds(524-faktor, 59, 100, 25);
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);	//Fenster schlieﬂen
			}
		});
		contentPane.add(btnAbbrechen);		
		
		//Tab-Reihenfolge festlegen
		Component[] tabOrder = new Component[] { pEins, pZwei, pDrei, pVier, pFuenf, pSechs, pSieben, pAcht, btnAusfuehren, options, btnAbbrechen };
		contentPane.setFocusTraversalPolicyProvider(true);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(tabOrder));
		//KeyListener implementieren
		btnAusfuehren.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAusfuehren.doClick();
				}
				else {
					performKeyAction(e);
				}
				
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		btnAbbrechen.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAbbrechen.doClick();
				}
				else {
					performKeyAction(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		options.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					options.doClick();
				}
				else {
					performKeyAction(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		//Auswahlknoepfe
		pEins.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAusfuehren.doClick();
				}
				else {
					performKeyAction(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		pZwei.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAusfuehren.doClick();
				}
				else {
					performKeyAction(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		pDrei.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAusfuehren.doClick();
				}
				else {
					performKeyAction(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		pVier.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAusfuehren.doClick();
				}
				else {
					performKeyAction(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		pFuenf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAusfuehren.doClick();
				}
				else {
					performKeyAction(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		pSechs.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAusfuehren.doClick();
				}
				else {
					performKeyAction(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		pSieben.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAusfuehren.doClick();
				}
				else {
					performKeyAction(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		pAcht.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { // Enter
					btnAusfuehren.doClick();
				}
				else {
					performKeyAction(e);
				}
							}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});

	}
	public void performKeyAction(KeyEvent e) {
		
		if(e.getKeyCode() == 116) { // F5
			doRefresh(contentPane);
		}
		if(e.getKeyCode() == 27) { //ESC
			btnAbbrechen.doClick();
		}
		if(e.getKeyCode() == 17) { //STRG
			pStrg = true;
		}
		if(e.getKeyCode() == 79 || e.getKeyCode() == 123) { //O || F12
			options.doClick();
		}
		if(e.getKeyCode() == 49) {
			radButton[0].doClick();
		}
		if(e.getKeyCode() == 50) {
			radButton[1].doClick();
		}
		if(e.getKeyCode() == 51) {
			radButton[2].doClick();
		}
		if(e.getKeyCode() == 52) {
			radButton[3].doClick();
		}
		if(e.getKeyCode() == 53) {
			radButton[4].doClick();
		}
		if(e.getKeyCode() == 54) {
			radButton[5].doClick();
		}
		if(e.getKeyCode() == 55) {
			radButton[6].doClick();
		}
		if(e.getKeyCode() == 56) {
			radButton[7].doClick();
		}
	}
}
