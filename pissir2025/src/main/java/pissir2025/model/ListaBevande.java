package pissir2025.model;

import java.util.ArrayList;
import java.util.List;


public class ListaBevande {
	
	 // Lista delle bevande disponibili nella macchinetta
    private List<Bevanda> bevandeDisponibili;

	public ListaBevande() {
		super();
		bevandeDisponibili=new ArrayList<>();
		
		// Inizializzazione delle bevande con nomi, prezzi e quantità di cialde necessarie
        bevandeDisponibili.add(new Bevanda("Caffè", 45, new int[]{2, 0, 0, 0}));
        bevandeDisponibili.add(new Bevanda("Macchiato", 70, new int[]{2, 0, 1, 0}));
        bevandeDisponibili.add(new Bevanda("Cioccolata", 80, new int[]{0, 0, 2, 2}));
        bevandeDisponibili.add(new Bevanda("Mocaccino", 80, new int[]{2, 0, 1, 1}));
        bevandeDisponibili.add(new Bevanda("Tè", 60, new int[]{0, 2, 0, 0}));
		
	}
	
	// Restituisce l'elenco delle bevande disponibili
    public List<Bevanda> getBevandeDisponibili() {
        return bevandeDisponibili;
    }
 

    // Restituisce il nome della bevanda all'indice specificato
    public String getNomeBevanda(int indice) {
        return bevandeDisponibili.get(indice).getBevanda();
    }

    // Restituisce il prezzo della bevanda all'indice specificato
    public int getPrezzoBevanda(int indice) {
        return bevandeDisponibili.get(indice).getPrezzo();
    }

    // Restituisce l'array delle cialde necessarie per una specifica bevanda
    public int[] getCialdeBevanda(int indice) {
        return bevandeDisponibili.get(indice).getCialdeBevanda();
    }

    // Restituisce il numero totale di bevande disponibili
    public int getNumeroBevande() {
        return bevandeDisponibili.size();
    }
    
    public Bevanda getBevanda(int index) {
        return bevandeDisponibili.get(index);
    }
}

	

	
	

