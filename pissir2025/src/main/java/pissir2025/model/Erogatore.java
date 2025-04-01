package pissir2025.model;

public class Erogatore {
	
	
	////ogni machinetta e associata ad un unico suo erogatore si intende idMacchinetta = idErogatore
	private int idErogatore;
	//indicia le quantity di ogni tipo di cialda
	private int [] nCialde= {20, 20, 20, 20};;
	//indicia le quantity di ogni tipo di bicchieri
	private int nBicchieri=20;
	//indicia la quanita bicchieri
	private int dosiZucchero=20;

	
	public Erogatore(int idErogatore) {
		this.idErogatore= idErogatore;
	}

	public boolean erogaBevanda(int[] cialdeRichieste, boolean zucchero) {
        for (int i = 0; i < cialdeRichieste.length; i++) {
            if (nCialde[i] < cialdeRichieste[i]) {
                return false;
            }
        }
        if (nBicchieri == 0 || (zucchero && dosiZucchero == 0)) {
            return false;
        }
        for (int i = 0; i < cialdeRichieste.length; i++) {
            nCialde[i] -= cialdeRichieste[i];
        }
        nBicchieri--;
        if (zucchero) dosiZucchero--;
        return true;
    }

	

	//metodi get e set
	public int[] getnCialde() {
		return nCialde;
	}

	public void setnCialde(int[] nCialde) {
		this.nCialde = nCialde;
	}

	public int getnBicchieri() {
		return nBicchieri;
	}

	public void setnBicchieri(int nBicchieri) {
		this.nBicchieri = nBicchieri;
	}

	public int getDosiZucchero() {
		return dosiZucchero;
	}

	public void setDosiZucchero(int dosiZucchero) {
		this.dosiZucchero = dosiZucchero;
	}

	public int getIdErogatore() {
		return idErogatore;
	}
	
	
}
