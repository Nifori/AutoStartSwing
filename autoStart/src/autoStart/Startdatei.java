package autoStart;

public class Startdatei {

	
	String Name = "";
	String Pfad = "";
	int Auswahl = 0;
	
	public Startdatei(String pName, String pPfad, int pAuswahl){
		Name = pName;
		Pfad = pPfad;
		Auswahl = pAuswahl;
	}	
	public String gibName(){
		return Name;
	}
	public String gibPfad(){
		return Pfad;
	}
	public int gibAuswahl(){
		return Auswahl;
	}
	public String gibStandard(){
		if(Auswahl < 2 ){
			return "Nein";
		}
		else{
			return "Ja";
		}
	}
	public void setName(String pName){
		Name = pName;
	}
	public void setPfad(String pPfad){
		Pfad = pPfad;
	}
	public void setAuswahl(int pAuswahl){
		Auswahl = pAuswahl;
	}	
}
