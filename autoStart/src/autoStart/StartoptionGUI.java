package autoStart;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.*;
import javax.swing.JTextPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartoptionGUI extends JFrame {

	
	private JPanel contentPane;
	private JTextField pathfield;
	private JTextField namefield;
	myList<Startdatei> Dateiliste = new myList<Startdatei>(); //Liste zur Dateiauswahl
	myList<Startdatei> tmpList = new myList<Startdatei>(); //Liste zum zwischenspeichern
	ErrorHandlerGUI ErrorHandler = new ErrorHandlerGUI();
	int length = 0;
	boolean pStrg = false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartoptionGUI frame = new StartoptionGUI();
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
	public StartoptionGUI() {

		
		setResizable(false);
		setTitle("Optionen");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 545, 350);
		contentPane = new JPanel();
		contentPane.setFocusable(false);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		namefield = new JTextField();		
		namefield.setBounds(10, 246, 160, 20);
		contentPane.add(namefield);	
		
		pathfield = new JTextField();		
		pathfield.setBounds(180, 246, 350, 20);		
		contentPane.add(pathfield);		

		JButton safe = new JButton("Speichern");
		safe.setBounds(325, 277, 100, 25);
		contentPane.add(safe);
		
		JButton abort = new JButton("Abbrechen");
		abort.setBounds(430, 277, 100, 25);
		contentPane.add(abort);
		
		JButton add = new JButton("Hinzuf\u00FCgen");
		add.setBounds(10, 277, 100, 25);
		contentPane.add(add);
		
		JButton delete = new JButton("L\u00F6schen");
		delete.setBounds(115, 277, 100, 25);
		contentPane.add(delete);
		
		JButton stdt = new JButton("Standard");
        stdt.setBounds(220, 277, 100, 25);
        contentPane.add(stdt);
		
		JLabel name = new JLabel("Name:");
		name.setFocusable(false);
		name.setFont(new Font("Tahoma", Font.PLAIN, 15));
		name.setBounds(10, 226, 104, 20);
		contentPane.add(name);
		
		JLabel path = new JLabel("Pfad:");
		path.setFocusable(false);
		path.setFont(new Font("Tahoma", Font.PLAIN, 15));
		path.setBounds(180, 226, 300, 20);
		contentPane.add(path);
		
		JLabel safed = new JLabel("bisher gespeicherte Dateien: (max: 8)");
		safed.setFocusable(false);
		safed.setFont(new Font("Tahoma", Font.PLAIN, 15));
		safed.setBounds(10, 11, 519, 18);
		contentPane.add(safed);
		
		JTextPane safedfield = new JTextPane();
		safedfield.setEditable(false);
		safedfield.setBounds(10, 40, 519, 175);
		contentPane.add(safedfield);

		load(safedfield);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				abort.doClick();
			}
		});
		
		safe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Dateiliste = tmpList;
				safen();
				namefield.setText("");
				pathfield.setText("");
				dispose();
			}
		});
		
		abort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tmpList = Dateiliste; //zuruecksetzen und abbrechen
				namefield.setText("");
				pathfield.setText("");
				dispose();
			}
		});
		add.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if(length < 8){
					if(PlausePruefung()){
						String Eingabe = pathfield.getText();		
						String Ausgabe = "";
						char c = (char) 92;
						for(int f = 0;f<Eingabe.length();f++){
							if(Eingabe.charAt(f) == c){
								if(Eingabe.charAt(f+1) == c){
									Ausgabe = Ausgabe + Eingabe.charAt(f) + Eingabe.charAt(f+1);
									f++;
								}
								else{
									Ausgabe = Ausgabe + Eingabe.charAt(f) + c;
								}
							}
							else{
								Ausgabe = Ausgabe + Eingabe.charAt(f);
							}
						}
						Startdatei tmpDatei = new Startdatei(namefield.getText(),Ausgabe, 0);
						tmpList.append(tmpDatei);
						length++;
						tmpList.toFirst();
						String AusgabeB = "";
						while(tmpList.hasAccess()){
							AusgabeB = AusgabeB + tmpList.getContent().gibName() + "	" + tmpList.getContent().gibStandard() + "	" + tmpList.getContent().gibPfad() + "\n"; //Feldausgabe aktualisieren
							tmpList.next();
						}
						safedfield.setText(AusgabeB);
					}	
				}
				else{
					ErrorHandler.showError("maximale Anzahl von Objekten erreicht");
				}
				namefield.setText("");
				pathfield.setText("");
			}
		});		
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(namefield.getText().equals("")){
					ErrorHandler.showError("bitte Namen angeben"); // kein Name in namefield
				}
				else{
					tmpList.toFirst();
					while(tmpList.hasAccess() && !tmpList.getContent().gibName().equalsIgnoreCase(namefield.getText())){ //liste durchgehen bis zielname oder platzhalterdatei gefunden
						tmpList.next();
					}
					if(!tmpList.hasAccess()){
						ErrorHandler.showError("kein passendes Objekt gefunden"); //safedfield.setText("Objekt nicht gefunden"); //wenn platzhalterdatei: Zieldatei nicht vorhanden
					}
					else{
						tmpList.remove(); //zieldatei entfernen		
						length--;
						tmpList.toFirst();
			            String AusgabeB = "";
			            while(tmpList.hasAccess()){
			            	AusgabeB = AusgabeB + tmpList.getContent().gibName() + "	" + tmpList.getContent().gibStandard() + "	" + tmpList.getContent().gibPfad() + "\n"; //Feldausgabe aktualisieren
			            	tmpList.next();
			            }
			            safedfield.setText(AusgabeB);
					}					
				}
				namefield.setText("");
				pathfield.setText("");
			}
		});		
		stdt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(namefield.getText().equals("")){
					ErrorHandler.showError("bitte einen Namen angeben");
				}
				else{
					tmpList.toFirst();
					while(tmpList.hasAccess() && !tmpList.getContent().gibName().equalsIgnoreCase(namefield.getText())){ //liste durchgehen bis zielname oder platzhalterdatei gefunden
						tmpList.next();
					}
					if(!tmpList.hasAccess()){
						ErrorHandler.showError("kein passendes Objekt gefunden"); //wenn platzhalterdatei: Zieldatei nicht vorhanden
					}
					else{
						if(tmpList.getContent().gibAuswahl()!=2){
							tmpList.getContent().setAuswahl(2);
						}
						else{
							tmpList.getContent().setAuswahl(0);
						}
					}
					tmpList.toFirst();
		            String AusgabeB = "";
		            while(tmpList.hasAccess()){
		            	AusgabeB = AusgabeB + tmpList.getContent().gibName() + "	" + tmpList.getContent().gibStandard() + "	" + tmpList.getContent().gibPfad() + "\n"; //Feldausgabe aktualisieren
		            	tmpList.next();
		            }
		            safedfield.setText(AusgabeB);
				}
			}
		});
		
		
		//keyListener anfang
		namefield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) { //ENTER
					add.doClick();
				}
				if(e.getKeyCode() == 27){ //ESC
					abort.doClick();
				}
				if(e.getKeyCode() == 17) { //STRG gedrueckt
					pStrg = true;
				}
				if(e.getKeyCode() == 83 && pStrg) { //S + STRG
					safe.doClick();
				}
				if(e.getKeyCode() == 127) { //ENTF
					delete.doClick();
				}
					
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) { //STRG losgelassen
					pStrg = false;
				}
			}
		});
		pathfield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					add.doClick();
				}
				if(e.getKeyCode() == 27){
					abort.doClick();
				}
				if(e.getKeyCode() == 17) {
					pStrg = true;
				}
				if(e.getKeyCode() == 83 && pStrg) {
					safe.doClick();
				}
				if(e.getKeyCode() == 127) {
					delete.doClick();
				}
					
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		add.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					add.doClick();
				}
				if(e.getKeyCode() == 27){
					abort.doClick();
				}
				if(e.getKeyCode() == 17) {
					pStrg = true;
				}
				if(e.getKeyCode() == 83 && pStrg) {
					safe.doClick();
				}
				if(e.getKeyCode() == 127) {
					delete.doClick();
				}
					
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		delete.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					delete.doClick();
				}
				if(e.getKeyCode() == 27){
					abort.doClick();
				}
				if(e.getKeyCode() == 17) {
					pStrg = true;
				}
				if(e.getKeyCode() == 83 && pStrg) {
					safe.doClick();
				}
				if(e.getKeyCode() == 127) {
					delete.doClick();
				}
					
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		safe.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					safe.doClick();
				}
				if(e.getKeyCode() == 27){
					abort.doClick();
				}
				if(e.getKeyCode() == 17) {
					pStrg = true;
				}
				if(e.getKeyCode() == 83 && pStrg) {
					safe.doClick();
				}
				if(e.getKeyCode() == 127) {
					delete.doClick();
				}
					
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
				if(e.getKeyCode() == 127) {
					delete.doClick();
				}
			}
		});
		abort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 127) {
					delete.doClick();
				}
				else {
					abort.doClick();
				}
				
			}
		});
		stdt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					stdt.doClick();
				}
				if(e.getKeyCode() == 27){
					abort.doClick();
				}
				if(e.getKeyCode() == 17) {
					pStrg = true;
				}
				if(e.getKeyCode() == 83 && pStrg) {
					safe.doClick();
				}
				if(e.getKeyCode() == 127) {
					delete.doClick();
				}
					
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
		safedfield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					safe.doClick();
				}
				if(e.getKeyCode() == 27){
					abort.doClick();
				}
				
				if(e.getKeyCode() == 17) {
					pStrg = true;
				}
				if(e.getKeyCode() == 83 && pStrg) {
					safe.doClick();
				}
				if(e.getKeyCode() == 127) {
					delete.doClick();
				}
					
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17) {
					pStrg = false;
				}
			}
		});
	}
	
	public void load(JTextPane safedfield){

		File file = new File(System.getProperty("user.dir") + "/safedData.txt"); //die Lokal gespeicherten Datei-Pfade laden
		
		
		if (!file.canRead() || !file.isFile()){ // wenn keine Datei geladen werden kann, abbrechen
            ErrorHandler.showError("keine Speicherdatei hinterlegt");
            dispose();
		}
            BufferedReader in = null; 
           
        try { 
            in = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/safedData.txt")); 
            
            String zeile = null;
            while((zeile = in.readLine()) != null){		//solange noch Zeilen in Datei
            	String pName = "";
            	String pPfad = "";
            	String pAuswahl = "";
            	int n = 0;
            	char c = ';';
        		while(zeile.charAt(n) != c ){			//name bis leerzeichen auslesen
        			pName = pName + zeile.charAt(n);
        			n++;
        		}
        		n++;
        		while(zeile.charAt(n) != c ){
        			pPfad = pPfad + zeile.charAt(n);	//Pfad bis zweites leerzeichen auslesen
        			n++;
        		}
        		n++;
        		while(zeile.charAt(n) != c ){
        			pAuswahl = pAuswahl + zeile.charAt(n);	//Pfad bis zweites leerzeichen auslesen
        			n++;
        		}
        		
        		Startdatei tmp = new Startdatei(pName, pPfad, Integer.parseInt(pAuswahl)); // Datei in Liste einfuegen
        		Dateiliste.append(tmp);
        		length++;
            }
            tmpList = Dateiliste;
            Dateiliste.toFirst();
            String Ausgabe = "";
            while(Dateiliste.hasAccess()){
            	Ausgabe = Ausgabe + Dateiliste.getContent().gibName() + "	" + Dateiliste.getContent().gibStandard() + "	" + Dateiliste.getContent().gibPfad() + "\n"; //in Fenster ausgeben
            	Dateiliste.next();
            }

            safedfield.setText(Ausgabe);                   
            
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally { 
            if (in != null) 
                try { 
                    in.close(); 
                } catch (IOException e) { 
                } 
        }
	}
	
	public void setList(myList<Startdatei> pList){
		Dateiliste.toFirst();
		pList.toFirst();
		while(Dateiliste.hasAccess()){
			while(pList.hasAccess()){
				if(Dateiliste.getContent().gibName().equalsIgnoreCase(pList.getContent().gibName())){
					if(Dateiliste.getContent().gibAuswahl() != 2){
						Dateiliste.getContent().setAuswahl(pList.getContent().gibAuswahl());						
					}
					break;
				}
				else{
					pList.next();
				}
			}
			Dateiliste.next();
			pList.toFirst();
		}
		safen();
	}
	
	public void safen(){
		
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/safedData.txt"));
			Dateiliste.toFirst();
			while(Dateiliste.hasAccess()){ //Liste durchgehen bis Platzhalterdatei oder keine Datei
				fw.write(Dateiliste.getContent().gibName() + ";" + Dateiliste.getContent().gibPfad() + ";" +  Dateiliste.getContent().gibAuswahl() + ";");	//name + leerzeichen + Pfad + ; in Textdatei
				fw.newLine();
				Dateiliste.next();
			}
			fw.close();
			} catch ( IOException ioex ) {}
	}

	public myList<Startdatei> gibListe(){		
		return Dateiliste;
	}
	
	public boolean PlausePruefung(){
		
		if(namefield.getText().equals("")){
			ErrorHandler.showError("bitte einen Namen angeben");
			
			return false;
		}
		if(pathfield.getText().equals("")){
			ErrorHandler.showError("bitte einen Pfad angeben");
			return false;
		}
		
		return true;
	}
	
	public int getLength(){
		return length;
	}	

}
