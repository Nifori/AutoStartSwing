package autoStart;


public class myList<ContentType> extends List<ContentType>{

	public myList() {
		super();
		
	}

	/**
	* Setzt alle direkten Verweise aus NULL, wodurch der GarbageCollector die Liste an sich "entsorgt"
	* 
	*/	
	public void removeAll() {
		  first = null;
		  last = null;
		  current = null;
	  }
}
