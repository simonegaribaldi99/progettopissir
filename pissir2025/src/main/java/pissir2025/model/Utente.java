package pissir2025.model;

public class Utente {
	private int id;
	private String nome;
	private String password;
	private boolean ruolo;
	
	
	public Utente(int id, String nome, String password, boolean ruolo) {
		super();
		this.id = id;
		this.nome = nome;
		this.password = password;
		this.ruolo = ruolo;
	}


	public int getId() {
		return id;
	}


	public String getNome() {
		return nome;
	}


	public String getPassword() {
		return password;
	}


	public boolean getRuolo() {
		return ruolo;
	}
	
	

	
}
