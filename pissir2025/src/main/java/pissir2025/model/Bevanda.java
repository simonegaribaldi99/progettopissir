package pissir2025.model;

public class Bevanda {
	
	private String bevanda;
	private int prezzo;
	
	//si intend le tipologie di ciadle per comporre una benvanda
	private int[]cialdeBevanda;

	public Bevanda(String bevanda, int prezzo, int[] cialdeBevanda) {
		super();
		this.bevanda = bevanda;
		this.prezzo = prezzo;
		this.cialdeBevanda = cialdeBevanda;
	}
	
	
	//metodi cialde

	public String getBevanda() {
		return bevanda;
	}

	public void setBevanda(String bevanda) {
		this.bevanda = bevanda;
	}

	public int getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(int prezzo) {
		this.prezzo = prezzo;
	}

	public int[] getCialdeBevanda() {
		return cialdeBevanda;
	}

	public void setCialdeBevanda(int[] cialdeBevanda) {
		this.cialdeBevanda = cialdeBevanda;
	}
	
	

}
