package pissir2025.model;

/**
 * La classe `Macchinetta` rappresenta una macchina da caff√® con un identificativo unico e un nome.
 * Questa classe fornisce metodi per accedere e modificare queste informazioni.
 */

public class Macchinetta {
	private int machinettaId;
	private String nomeMacchinetta;
	private boolean guasto;
	private String guastoDescrizione;
	private int idIstituto;
	
	
	//creazione machinetta
	public Macchinetta(int machinettaId,String nomeMachinetta,boolean guasto,String descrizioneGuasto,int  idIstituto) {
		super();
		this.nomeMacchinetta=nomeMachinetta;
		this.machinettaId = machinettaId;
		this.guasto=guasto;
		this.guastoDescrizione=descrizioneGuasto;
		this.idIstituto = idIstituto;
	}
	
	
	//get id machinetta
	public int getMachinettaId() {
		return machinettaId;
	}
	//metodo per vedere se si Ë verificato un guasto
	public boolean isGuasto() {
		return guasto;
	}
	
	
	public void setGuasto(boolean guasto) {
		this.guasto = guasto;
	}
	//metodo per descrivere il tipo di guasto 
	public String getGuastoDescrizione() {
		return guastoDescrizione;
	}
	public void setGuastoDescrizione(String guastoDescrizione) {
		this.guastoDescrizione = guastoDescrizione;
	}


	public int getIdIstituto() {
		return idIstituto;
	}
	
	
	

}
