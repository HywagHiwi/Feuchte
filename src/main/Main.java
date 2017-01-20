package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import mail.Mail;
import mail.MailAccounts;
import website.Reader;

public class Main {

	/**
     * Liste zum Speichern aller e-mail-Adressen, die informiert werden sollen.
     */
	static List<String> empfaenger = new LinkedList<String>(); 
	
	public static void main(String[] args) throws AddressException, MessagingException, IOException {
		
		LinkedList<Sensor> sensors = new LinkedList<Sensor>();
		
		GregorianCalendar now; 
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
		
		Reader r = new Reader();

		//sensors.add( new Sensor("rel. Luftfeuchte", 40));
		
		
	    InputStreamReader isr = new InputStreamReader(System.in);
	    BufferedReader br = new BufferedReader(isr);
	    boolean finished = false;
	    while(finished != true){
		    System.out.println("Bitte gib eine Sensor, Toleranzkombination ein, H (Hilfe) oder F (Fertig)");
			String eingabe = br.readLine();
			if (eingabe.toLowerCase().equals("h")) {
				System.out.println(
						"Eingabe: \t durch Bindestrich getrennter Sensortyp und den Toleranzwert");
				System.out.println(
						"Sensoren: \t Windrichtung \n \t Windgeschwindigkeit \n \t Niederschlag \n \t Temperatur \n \t Windchill \n \t rel. Luftfeuchte \n \t abs. Luftdruck \n \t Taupunkt");
			} else if(eingabe.toLowerCase().equals("f")){
				if(sensors.isEmpty()){
					System.out.println("Zuerst einen zu beobachtenden Sensor hinzufuegen. Sonst ist das Programm ziemlich sinnlos :^)");
				} else {
					finished = true;
				}
			} else{
				try{
					String[] inputvalues = eingabe.split("-");
					sensors.add( new Sensor(inputvalues[0], Integer.parseInt(inputvalues[1])));
					System.out.println("Sensor hinzugefügt: " + inputvalues[0] + " mit Toleranzwert: " + inputvalues[1]);
				} catch(Exception e){
					
				}
			}
		}
		for(Sensor s : sensors){
			s.setLetzterWert(r.find(s.getName()));
		}

		
		System.out.print("Programm wird gestartet.");
		for(int i = 0; i < 10; i++){
			try {
				Thread.sleep(500);
				System.out.print(".");
			} catch (InterruptedException e) {
			}
		}
		System.out.println("\n");
		
		
		while(true)
		{
			
			now = new GregorianCalendar();
			String time = df.format(now.getTime());

	    	System.out.println(df.format(now.getTime()) + ": Das Programm läuft.");
			
			for(Sensor sen : sensors){
				checkDiscrepancy(sen, r, time);
				printStatus(sen, r);
			}
			
			try {
				Thread.sleep(300000); //5 Minuten
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static void printStatus(Sensor s, Reader r){
		System.out.println("Sensorstatus: " + s.getName() + " aktueller Wert: " + s.getAktuellerWert());
	}
	
	private static void checkDiscrepancy(Sensor s, Reader r, String time) throws AddressException, MessagingException{
		
		int aktuellerWert = r.find(s.getName());
		int letzterWert   = s.getLetzterWert();
		int toleranz      = s.getToleranz();
		int counter       = s.getCounter();
		
		s.setAktuellerWert(aktuellerWert);
		
		
		if((aktuellerWert - toleranz > letzterWert || aktuellerWert + toleranz < letzterWert)){
			s.setRasanterWechsel(true);
			sendMail(s.getName(), s.isRasanterWechsel() , s.getDown(), s.getUpAgain(), s.getLetzterWert(), s.getAktuellerWert());
		}
		
		if(aktuellerWert == 0 && counter <= 9){
			if(counter == 0){
				s.setDown(time);
			}
			s.setCounter(counter + 1);
		} else if(aktuellerWert != 0 && counter > 5){
			s.setWertWiederGut(true);
			sendMail(s.getName(), s.isRasanterWechsel() , s.getDown(), time, s.getLetzterWert(), s.getAktuellerWert());
			s.setCounter(0);
		} else if(aktuellerWert == 0 && counter == 10){
			sendMail(s.getName(), s.isRasanterWechsel() , s.getDown(), null, s.getLetzterWert(), s.getAktuellerWert());
		}
				
		s.setLetzterWert(aktuellerWert);
	}
	
	/**
	 * Methode zum Senden einer e-mail
	 * 
	 * @param value
	 * @throws AddressException
	 * @throws MessagingException
	 */
	private static void sendMail(String sensor, boolean rasant, String down, String up, int letzterWert, int aktuellerWert) throws AddressException, MessagingException {
	    	    
	    empfaenger.add(new String("s.nikelski@tu-braunschweig.de"));
	    //empfaenger.add(new String("tim.mueller@tu-braunschweig.de"));
	    
	    String subject = "";
	    String text = "";
	    
	    if(rasant == true){
	    	subject = "Sensoränderung: " + sensor;
	    	text = "Hallo, \n der Sensor \"" + sensor + "\" ist im letzten Intervall (" + down + ")von " + letzterWert + "% auf " + aktuellerWert + "% gesprungen."  ;
	    } else if (rasant == false){
	    	subject = "Sensor \"" + sensor + "\" bei 0.";
	    	if(up != null){
	    		text = "Hallo, \n der Sensor \"" + sensor + "\" maß von " + down + " an 0. Fing sich aber um " + up + "wieder;" ;
	    	} else {
	    		text = "Hallo, \n der Sensor \"" + sensor + "\" ist seit " + down + " bei 0.";
	    	}
	    }
	    
	    
	    /**
	     * For-Each-Schleife.
	     * Sprich: "Fuer jede Adresse in empfaenger sende eine mail an die entsprechende Person"
	     */
	    for(String s : empfaenger){
	    	Mail.send(MailAccounts.TU, s, subject, text);    	
	    }
	    System.out.println(subject);
	    
	}

}
