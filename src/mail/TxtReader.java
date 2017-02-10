package mail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TxtReader {
	
	/**
	  * Enthaelt das gesamte Dokument als verkettete String-Liste 
	  */
	private List<String> text = new LinkedList<String>();
	
	public List<String> getText() {
		return text;
	}
	public void setText(List<String> text) {
		this.text = text;
	}

	
	public TxtReader(String path){
		FileReader fileReader;
		BufferedReader bufferedReader;
		try {
			fileReader = new FileReader(path);
			bufferedReader = new BufferedReader(fileReader);
			String zeile = "";
			
			while ( (zeile = bufferedReader.readLine()) != null) {
				text.add(zeile);
			}
		} catch(IOException e) {
			System.out.println("Fehler beim Auslesen der Datei");
		}
	}
	
//	public static void main(String[] args){
//		
//	}
	

}
