package pissir2025.model;

public class Cassa {
	//ogni machinetta e associata ad un unica sua cassa che si intende idMacchinetta = idCassa
	private int idCassa ;

	//settiamo noi di default le monete permesse
	private int[] MonetePermesse = {5, 10, 20, 50, 100, 200};

	//ogni slot di questo array permette n monete per tipo 
    private int[] MonetePresenti = {50, 40, 30, 20, 10, 10};

	//il credito in centesimi attuale dell' utente
	private int creditoAttuale=0;

	//diventa vero quando una o piu slot di un tipo moneta diventa piena o si raggiunge il 90% del valore della cassa
	private boolean cassaPiena=false;

	//per vedere in euro quanto è contenutto in cassa
	private int capienzaCassa;

	// Numero massimo di monete che la macchinetta può contenere
	private static final int MAX_MONETE=100;
	
	public Cassa(int idCassa) {
		this.idCassa = idCassa;
	}


	/**
     * Calcola l'importo totale inserito dall'utente in base alla quantità di ciascun tipo di moneta.
     *
     * @param moneteInserite Array con il numero di monete inserite per ciascun taglio.
     * @return importo totale in centesimi
     */
	public int calcolaImportoInserito(int[] moneteInserite) {
        int totale = 0;
        for (int i = 0; i < moneteInserite.length; i++) {
            totale += moneteInserite[i] * monetePermesse[i];
        }
        return totale;
    }
    

	 /**
     * Controlla se la macchinetta ha spazio sufficiente per accogliere le monete inserite.
     * Se una colonna supera la capacità massima, svuota una parte della colonna.
     *
     * @param moneteInserite Array con il numero di monete inserite per ciascun taglio
     * @return true se c'è spazio sufficiente, false altrimenti
     */
	

	//Il metodo riceve un array moneteInserite, che indica quante monete di ogni tipo vengono inserite.
	//Restituisce true se le monete vengono accettate, false se la cassa è piena.
	 public boolean accettaMonete(int[] moneteInserite) {

		//Il ciclo scorre su ogni tipo di moneta.
		//verifica se aggiungere le nuove monete supererebbe il limite massimo (MAX_MONETE = 100).
		//Se almeno uno degli slot è pieno, imposta cassaPiena = true e rifiuta tutte le monete, restituendo false.
        for (int i = 0; i < moneteInserite.length; i++) {
            if (MonetePresenti[i] + moneteInserite[i] > MAX_MONETE) {
                cassaPiena = true;
                return false; 
            }
        }

		//Dopo aver verificato che tutte le monete possono essere accettate, le aggiunge effettivamente agli slot.
        for (int i = 0; i < moneteInserite.length; i++) {
            MonetePresenti[i] += moneteInserite[i];
        }

		//Chiama il metodo calcolaImportoInserito(moneteInserite), che somma il valore totale delle monete inserite.
		//Aggiorna il credito attuale dell'utente con il valore delle monete accettate.
        creditoAttuale += calcolaImportoInserito(moneteInserite);
        return true;
    }
	
	


	//Il metodo controlla se l'utente ha inserito abbastanza credito per acquistare una bevanda.
	public boolean verificaResto(int costoBevanda) {
        return creditoAttuale >= costoBevanda;
    }


    

    public int[] erogaResto(int costoBevanda) {

		//Calcolo del resto: sottrae il costo della bevanda dal creditoAttuale.
		//Crea un array moneteDaRestituire per memorizzare il numero di monete di ciascun taglio da restituire.
        int resto = creditoAttuale - costoBevanda;
        int[] moneteDaRestituire = new int[monetePermesse.length];
        

		//Si scorre l'array monetePermesse dall'ultima posizione alla prima (cioè dalle monete di valore più alto a quelle più basse).
		//Se il valore del resto è maggiore o uguale al valore della moneta corrente e ci sono monete disponibili nello slot (capienzaTipoMonete[i] > 0), allora:
		//Si scala il valore della moneta dal resto.Si decrementa il numero di monete disponibili in cassa.
		//Si memorizza la moneta come parte del resto da restituire.
		//Il ciclo while continua fino a quando non si può più restituire la moneta corrente.
        for (int i = monetePermesse.length - 1; i >= 0; i--) {
            while (resto >= monetePermesse[i] && capienzaTipoMonete[i] > 0) {
                resto -= monetePermesse[i];
                capienzaTipoMonete[i]--;
                moneteDaRestituire[i]++;
            }
        }
        
        creditoAttuale = 0;

		//Se il resto è esattamente zero, restituisce moneteDaRestituire, 
		//altrimenti restituisce null (indica che non è stato possibile dare il resto esatto).
        return resto == 0 ? moneteDaRestituire : null;
    }
}

	//metodi get e set

	public int getCreditoAttuale() {
		return creditoAttuale;
	}

	public void setCreditoAttuale(int creditoAttuale) {
		this.creditoAttuale = creditoAttuale;
	}

	public boolean isCassaPiena() {
		return cassaPiena;
	}

	public void setCassaPiena(boolean cassaPiena) {
		this.cassaPiena = cassaPiena;
	}

	public int getIdCassa() {
		return idCassa;
	}

	public int[] getMonetePermesse() {
		return monetePermesse;
	}

	public int[] getCapienzaTipoMonete() {
		return capienzaTipoMonete;
	}
	
	
	public int getcapienzaCassa() {
		return capienzaCassa;
	}
	
	
	
	
	
	
	

}
