package main;

public class Sensor {

	private int letzterWert;
	private int aktuellerWert;
	private String name = "";
	private boolean mailVerschickt = false;
	private boolean wertSchlecht = false;
	private boolean wertWiederGut = false;
	private boolean rasanterWechsel = false;
	private String down = "";
	private String upAgain = "";
	private int counter = 0;
	private int toleranz;
	
	public Sensor(String name, int toleranz){
		this.name = name;
		this.toleranz = toleranz;
	}

	public int getLetzterWert() {
		return letzterWert;
	}

	public void setLetzterWert(int letzterWert) {
		this.letzterWert = letzterWert;
	}

	public int getAktuellerWert() {
		return aktuellerWert;
	}

	public void setAktuellerWert(int aktuellerWert) {
		this.aktuellerWert = aktuellerWert;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMailVerschickt() {
		return mailVerschickt;
	}

	public void setMailVerschickt(boolean mailVerschickt) {
		this.mailVerschickt = mailVerschickt;
	}

	public boolean isWertSchlecht() {
		return wertSchlecht;
	}

	public void setWertSchlecht(boolean wertSchlecht) {
		this.wertSchlecht = wertSchlecht;
	}

	public boolean isWertWiederGut() {
		return wertWiederGut;
	}

	public void setWertWiederGut(boolean wertWiederGut) {
		this.wertWiederGut = wertWiederGut;
	}

	public boolean isRasanterWechsel() {
		return rasanterWechsel;
	}

	public void setRasanterWechsel(boolean rasanterWechsel) {
		this.rasanterWechsel = rasanterWechsel;
	}

	public String getDown() {
		return down;
	}

	public void setDown(String down) {
		this.down = down;
	}

	public String getUpAgain() {
		return upAgain;
	}

	public void setUpAgain(String upAgain) {
		this.upAgain = upAgain;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getToleranz() {
		return toleranz;
	}

	public void setToleranz(int toleranz) {
		this.toleranz = toleranz;
	}
	
	
	
	
}
