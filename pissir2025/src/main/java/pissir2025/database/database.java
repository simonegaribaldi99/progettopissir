package pissir2025.database;

//import java.awt.List;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pissir2025.model.Cassa;
import pissir2025.model.Istituto;
import pissir2025.model.Macchinetta;

/**
 * Classe di utilit� per l'accesso al database.
 * Fornisce metodi statici per eseguire operazioni CRUD (Create, Read, Update, Delete)
 * sulle tabelle: Istituto, Macchinetta, Cassa, Amministratore e Impiegato.
 *
 * Questa classe semplifica l'interazione con il database, centralizzando le operazioni
 * comuni e migliorando la manutenibilit� del codice.
 */
public class database <JdbcTemplate>{
	
	 // Costante per l'URL del database SQLite
	// da verificare
		private static final String DATABASE_URL="jdbc:sqlite:C:/Users/samis/Desktop/pissir2025/src/main/java/pissir2025/database/Database.db";
		
		
		
		
		//------------------------METODI ISTITUTO-----------------------------------------
		//creare istituto
		public static String creaIstituto(String nome) {
		    Gson gson = new Gson();

		    if (nome == null || nome.trim().isEmpty()) {
		        JsonObject risultato = new JsonObject();
		        risultato.addProperty("messaggio", "Il nome dell'istituto non pu� essere nullo o vuoto.");
		        return gson.toJson(risultato);
		    }

		    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		         PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Istituto (nome) VALUES (?)")) {

		        pstmt.setString(1, nome);

		        int rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            JsonObject risultato = new JsonObject();
		            risultato.addProperty("messaggio", "Istituto inserito con successo");
		            return gson.toJson(risultato);
		        } else {
		            JsonObject risultato = new JsonObject();
		            risultato.addProperty("messaggio", "Impossibile inserire l'istituto");
		            return gson.toJson(risultato);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        JsonObject risultato = new JsonObject();
		        risultato.addProperty("messaggio", "Errore durante l'inserimento dell'istituto: " + e.getMessage());
		        return gson.toJson(risultato);
		    }
		}
		
		
		//rimuovere Istituto
		public static String rimuoviIstituto(int idIstituto) {
		    Gson gson = new Gson();

		    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		         PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Istituto WHERE idIstituto = ?")) {

		        pstmt.setInt(1, idIstituto);

		        int rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            JsonObject risultato = new JsonObject();
		            risultato.addProperty("messaggio", "Istituto rimosso con successo");
		            return gson.toJson(risultato);
		        } else {
		            JsonObject risultato = new JsonObject();
		            risultato.addProperty("messaggio", "Impossibile rimuovere l'istituto. Verifica l'ID.");
		            return gson.toJson(risultato);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        JsonObject risultato = new JsonObject();
		        risultato.addProperty("messaggio", "Errore durante la rimozione dell'istituto: " + e.getMessage());
		        return gson.toJson(risultato);
		    }
		}
		
		//visualizzare il singolo istituto
		public static String visualizzaIstituto(int idIstituto) {
	        Gson gson = new Gson();
	        JsonObject risultato = new JsonObject();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement("SELECT idIstituto, nome FROM istituto WHERE idIstituto = ?")) {

	            pstmt.setInt(1, idIstituto);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                int id = rs.getInt("idIstituto");
	                String nome = rs.getString("nome");
	                Istituto istituto = new Istituto(id, nome);
	                return gson.toJson(istituto);
	            } else {
	                risultato.addProperty("messaggio", "Istituto non trovato.");
	                return gson.toJson(risultato);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            risultato.addProperty("messaggio", "Errore durante l'operazione sul database: " + e.getMessage());
	            return gson.toJson(risultato);
	        }
	    }
		
		
		
		//vedere tutti gli istituti 
		public static String visualizzaIstituti() {
			List<Istituto>istituto = new ArrayList<>();
			Gson gson = new Gson();
			
			try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		             PreparedStatement pstmt = conn.prepareStatement("SELECT idIstituto,nome FROM istituto")) {

		            ResultSet rs = pstmt.executeQuery();

		            while (rs.next()) {
		            	
		            	int idIstituto = rs.getInt("idIstituto");
		                String nome = rs.getString("nome");
		                Istituto  i = new Istituto(idIstituto,nome);
		                
		                istituto.add(i);
		            }

		        } catch (SQLException e) {
		            e.printStackTrace();
		            return gson.toJson("Error during database operation");
		        }

		        return gson.toJson(istituto);
		}
		//------------------------FINE ISTITUTO--------------------------------------------------------------------
	
		//----------METODI MACCHINETTA----------------------------------------------
		
		//vede una singola machinetta
		public static String visualizzaMachinetta(int idMacchinetta) {
		    Gson gson = new Gson();

		    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		         PreparedStatement pstmt = conn.prepareStatement("SELECT IdMacchinetta, nome, guasto, descrizioneGuasto, idIstituto FROM Macchinetta WHERE IdMacchinetta = ?")) {

		        pstmt.setInt(1, idMacchinetta);
		        ResultSet rs = pstmt.executeQuery();

		        if (rs.next()) {
		            int idMacchinetteResult = rs.getInt("IdMacchinetta");
		            String nome = rs.getString("nome");
		            boolean guasto = rs.getBoolean("guasto");
		            String descrizioneGuasto = rs.getString("descrizioneGuasto");
		            int idIstituto = rs.getInt("idIstituto");

		            Macchinetta macchinetta = new Macchinetta(idMacchinetteResult, nome, guasto, descrizioneGuasto, idIstituto);
		            return gson.toJson(macchinetta); // Restituisce un singolo oggetto Macchinetta
		        } else {
		            return gson.toJson("Macchinetta non trovata"); // Gestisce il caso in cui la macchinetta non esiste
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        return gson.toJson("Error during database operation");
		    }
		}
		
		//visulizzare tutte le machinette di un certo istituto
		//ho un dubbio se restituire logetto in stringo o in altro modo 
		
		public static String visualizzaMachinetteIstituto(int IdIstituto) {
	        List<Macchinetta> macchinette = new ArrayList<>();
	        Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement("SELECT IdMacchinetta, nome, guasto, descrizioneGuasto, idIstituto FROM Macchinetta WHERE idIstituto = ?")) {

	            pstmt.setInt(1, IdIstituto);
	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                int idMacchinette = rs.getInt("IdMacchinetta");
	                String nome = rs.getString("nome");
	                boolean guasto = rs.getBoolean("guasto");
	                String descrizioneGuasto = rs.getString("descrizioneGuasto");
	                int idIstituto = rs.getInt("idIstituto");

	                Macchinetta macchinetta = new Macchinetta(idMacchinette, nome, guasto, descrizioneGuasto, idIstituto);
	                macchinette.add(macchinetta);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Error during database operation");
	        }

	        return gson.toJson(macchinette);
	    }
		

		//Visualizza tutte le machinette 
		public static String visualizzaMachinette() { 
		    List<Macchinetta> macchinette = new ArrayList<>();
		    Gson gson = new Gson();

		    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		         PreparedStatement pstmt = conn.prepareStatement("SELECT IdMacchinetta, nome, guasto, descrizioneGuasto, idIstituto FROM Macchinetta")) { // Rimossa la clausola WHERE

		        ResultSet rs = pstmt.executeQuery();

		        while (rs.next()) {
		            int idMacchinette = rs.getInt("IdMacchinetta");
		            String nome = rs.getString("nome");
		            boolean guasto = rs.getBoolean("guasto");
		            String descrizioneGuasto = rs.getString("descrizioneGuasto");
		            int idIstituto = rs.getInt("idIstituto");

		            Macchinetta macchinetta = new Macchinetta(idMacchinette, nome, guasto, descrizioneGuasto, idIstituto);
		            macchinette.add(macchinetta);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        return gson.toJson("Error during database operation");
		    }

		    return gson.toJson(macchinette);
		}
	
		// Aggiunta di una macchinetta in un istituto
		public static String inserisciMacchinetta(String nomeMacchinetta, int idIstituto) {
		    Gson gson = new Gson();

		    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		         PreparedStatement pstmtMacchinetta = conn.prepareStatement("INSERT INTO Macchinetta (nome, guasto, descrizioneGuasto, idIstituto, idErogatore, idCassa) VALUES (?, ?, ?, ?, ?, ?)")) {

		        // Inserisci la cassa e recupera l'id generato
		        String risultatoCassa = inserisciCassa();
		        JsonObject jsonCassa = gson.fromJson(risultatoCassa, JsonObject.class);
		        if (!jsonCassa.has("idCassa")) {
		            return gson.toJson("Errore inserimento cassa: " + jsonCassa.get("messaggio").getAsString());
		        }
		        int idCassa = jsonCassa.get("idCassa").getAsInt();

		        // Inserisci l'erogatore e recupera l'id generato
		        String risultatoErogatore = inserisciErogatore();
		        JsonObject jsonErogatore = gson.fromJson(risultatoErogatore, JsonObject.class);
		        if (!jsonErogatore.has("idErogatore")) {
		            return gson.toJson("Errore inserimento erogatore: " + jsonErogatore.get("messaggio").getAsString());
		        }
		        int idErogatore = jsonErogatore.get("idErogatore").getAsInt();

		        // Inserisci la macchinetta con le chiavi esterne corrette
		        pstmtMacchinetta.setString(1, nomeMacchinetta);
		        pstmtMacchinetta.setInt(2, 0); // guasto = 0 (false)
		        pstmtMacchinetta.setString(3, "nessun guasto");
		        pstmtMacchinetta.setInt(4, idIstituto);
		        pstmtMacchinetta.setInt(5, idErogatore);
		        pstmtMacchinetta.setInt(6, idCassa);

		        int rowsAffected = pstmtMacchinetta.executeUpdate();

		        if (rowsAffected > 0) {
		            return gson.toJson("Macchinetta inserita con successo");
		        } else {
		            return gson.toJson("Impossibile inserire la macchinetta");
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        return gson.toJson("Errore durante l'inserimento della macchinetta: " + e.getMessage());
		    }
		}
	    
	    
	   
	    
	    
	   
	     // Rimozione di una macchinetta di un istituto 	DA VERE PER ISTITUTOOOOOOOOO
	    public static String rimuoviMachinetta(int idMacchinetta) {
	        Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Macchinetta WHERE IdMacchinetta = ?")) {

	            pstmt.setInt(1, idMacchinetta);
	            //mi indica il nuemro di tabelle in quella riga 
	            int rowsAffected = pstmt.executeUpdate();
	            rimuoviCassa(idMacchinetta);
	            rimuoviErogatore(idMacchinetta);

	            if (rowsAffected > 0) {
	                return gson.toJson("Macchinetta rimossa con successo");
	            } else {
	                return gson.toJson("Impossibile rimuovere la macchinetta. Verifica ID e Istituto.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Errore durante la rimozione: " + e.getMessage());
	        }
	    }
	   
	    
	   //vede tutte  machinette con un guasto
	    public static String getGuasto() {
	        Gson gson = new Gson();
	        List<Macchinetta> macchinetteGuaste = new ArrayList<>();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Macchinetta WHERE guasto = 1");
	             ResultSet rs = pstmt.executeQuery()) {

	            while (rs.next()) {
	            	
	            	String descrizioneGuasto = rs.getString("descrizioneGuasto");
	            	boolean guasto = rs.getInt("guasto") == 1; // o mi da 1 se e vero senno me lo imposta a 0 
	                if (rs.wasNull()) {
	                    descrizioneGuasto = "nessun guasto"; 
	                }
	            	
	                Macchinetta macchinetta = new Macchinetta(
	                    rs.getInt("idMacchinetta"),
	                    rs.getString("nome"),
	                    guasto,
	                    descrizioneGuasto,
	                    rs.getInt("idIstituto")
	       
	                );
	                macchinetteGuaste.add(macchinetta);
	            }

	            if (macchinetteGuaste.isEmpty()) {
	                return gson.toJson("Nessuna macchinetta guasta trovata");
	            } else {
	                return gson.toJson(macchinetteGuaste);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Errore durante la ricerca: " + e.getMessage());
	        }
	    }
	    
	    public static String guastoRisolto(int idMacchinetta) {
	    	Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement(
	                     "UPDATE Macchinetta SET guasto = ?, descrizioneGuasto = ?  WHERE idMacchinetta = ?"
	             )) {

	            pstmt.setInt(1, 0); // reimposto il guasto a zero 
	            pstmt.setString(2, "Nessun guasto presente"); // reimposto la descrizione una vuota o forse meglio scrivere nessun guasto presente
	           

	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected == 1) {
	                return gson.toJson("guasto della Macchina: " + idMacchinetta + " risolto");
	            } else {
	                return gson.toJson("Problema per mandare il tecnico a risolvere il guasto . Verifica ID o se esiste");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Errore durante il risolvimento del guasto: " + e.getMessage());
	        }
	    	
	    }
	    
	    //------FINE METODI EROGATORE--------------------------------------------------
	    
	  
	    //----------------METODI EROGATORE------------------------------------------
	    
	    
	    public static String inserisciErogatore() {
	        Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Erogatore (caff�, latte, th�, cioccolato, bicchieri, zucchero) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

	            pstmt.setInt(1, 20);
	            pstmt.setInt(2, 20);
	            pstmt.setInt(3, 20);
	            pstmt.setInt(4, 20);
	            pstmt.setInt(5, 20);
	            pstmt.setInt(6, 20);

	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected > 0) {
	                ResultSet generatedKeys = pstmt.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    int idGenerato = generatedKeys.getInt(1);
	                    JsonObject risultato = new JsonObject();
	                    risultato.addProperty("messaggio", "Erogatore inserito con successo");
	                    risultato.addProperty("idErogatore", idGenerato); // Cambiato "ID" in "idErogatore"
	                    return gson.toJson(risultato);
	                } else {
	                    JsonObject risultato = new JsonObject();
	                    risultato.addProperty("messaggio", "Erogatore inserito con successo, ma ID non recuperabile");
	                    return gson.toJson(risultato);
	                }
	            } else {
	                JsonObject risultato = new JsonObject();
	                risultato.addProperty("messaggio", "Impossibile inserire l'erogatore");
	                return gson.toJson(risultato);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            JsonObject risultato = new JsonObject();
	            risultato.addProperty("messaggio", "Errore durante l'inserimento dell'erogatore: " + e.getMessage());
	            return gson.toJson(risultato);
	        }
	    }
	    
	    
	    
	    //rimuovi erogatore di una macchinetta 
	    public static String rimuoviErogatore(int idMacchinetta) {
	    	Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Erogatore WHERE IdErogatore = ?")) {

	            pstmt.setInt(1, idMacchinetta);
	            //mi indica il nuemro di tabelle in quella riga 
	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected > 0) {
	            	//nel messagigio stampo per controllare id rimosso
	                return gson.toJson("Erogatore di Macchina:"+Integer.toString(idMacchinetta)+" rimossa con successo");
	            } else {
	                return gson.toJson("Impossibile rimuovere l'erogatore della macchinetta. Verifica ID");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Errore durante la rimozione: " + e.getMessage());
	        }
	    	
	    }
	    
	    
	    
	    
	    
	  //metodi che resettano  erogatori a default dopo il tecnico
	    public static String riempiCapsule(int idMacchinetta) {
	        Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement(
	                     "UPDATE Erogatore SET caff� = ?, latte = ?, th� = ?, cioccolato = ?, bicchieri = ?, zucchero = ? WHERE idErogatore = ?"
	             )) {

	            pstmt.setInt(1, 20); // caff�
	            pstmt.setInt(2, 20); // latte
	            pstmt.setInt(3, 20); // th�
	            pstmt.setInt(4, 20); // cioccolato
	            pstmt.setInt(5, 20); // bicchieri
	            pstmt.setInt(6, 20); // zucchero
	            pstmt.setInt(7, idMacchinetta); // idErogatore

	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected == 1) {
	                return gson.toJson("Capsule della Macchina: " + idMacchinetta + " caricate con successo");
	            } else {
	                return gson.toJson("Problema per il riempimento capsule. Verifica ID o se esiste");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Errore durante il riempimento capsule: " + e.getMessage());
	        }
	    }
	    
	  //aggiorno la cassa di una machinetta dopo un erogazione
	  	public static String aggiornaErogatore(int idErogatore, int []nCialde,int zucchero,int bicchieri) {
	  			
	  			 Gson gson = new Gson();

	  		        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	  		             PreparedStatement pstmt = conn.prepareStatement(
	  		                     "UPDATE Erogatore SET caffè = ?, latte = ?, thè = ?, cioccolato = ?, bicchieri = ?, zucchero = ? WHERE idErogatore = ?"
	  		             )) {

	  		            pstmt.setInt(1, nCialde[0]); // caffè
	  		            pstmt.setInt(2, nCialde[1]); // latte
	  		            pstmt.setInt(3, nCialde[2]); // thè
	  		            pstmt.setInt(4, nCialde[3]); // cioccolato
	  		            pstmt.setInt(5, nCialde[4]); // bicchieri
	  		            pstmt.setInt(6, nCialde[5]); // zucchero
	  		            pstmt.setInt(7, idErogatore); // idCassa

	  		            int rowsAffected = pstmt.executeUpdate();

	  		            if (rowsAffected == 1) {
	  		                return gson.toJson("capsule Macchina: " + idErogatore + " aggiornate con successo");
	  		            } else {
	  		                return gson.toJson("Problema aggiornamento capsule macchientta. Verifica ID o se esiste");
	  		            }

	  		        } catch (SQLException e) {
	  		            e.printStackTrace();
	  		            return gson.toJson("Errore durante aggiornamento capsule capsule: " + e.getMessage());
	  		        }
	  		        
	  		}
	  		
	  		//visualizza una erogatore di una machinetta
	  		public static String visualizzaErogatore(int idErogatore) {
	  				    Gson gson = new Gson();

	  				    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	  				         PreparedStatement pstmt = conn.prepareStatement("SELECT caffè, latte, thè, cioccolato, bicchieri, zucchero FROM Erogatore WHERE idErogatore = ?")) {

	  				        pstmt.setInt(1, idErogatore);
	  				        ResultSet rs = pstmt.executeQuery();

	  				        if (rs.next()) {
	  				        	int cialdePresenti[]= {rs.getInt("caffè"),rs.getInt("latte"),rs.getInt("thè"),rs.getInt("cioccolato")};
	  				        	int bicchieri = rs.getInt("bicchieri");
	  				        	int zucchero = rs.getInt("zucchero");
	  				            Erogatore er = new Erogatore(idErogatore, cialdePresenti, bicchieri, zucchero);
	  				           
	  				            return gson.toJson(er);
	  				        } else {
	  				            return gson.toJson("Erogatore non trovata");
	  				        }

	  				    } catch (SQLException e) {
	  				        e.printStackTrace();
	  				        return gson.toJson("Errore durante la visualizzazione dell Erogatore: " + e.getMessage());
	  				    }
	  				}

	    
	    //-----------FINE METODI EROGATORE------------------------------------------------------
	    
	    
	  //----------------------METODI CASSA----------------------------------------------------------------------------------------------------------------------------------------  
	    
	    
	    // Aggiunta di una cassa
	    public static String inserisciCassa() {
	        Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Cassa (5_centesimi, 10_centesimi, 20_centesimi, 50_centesimi, 1_euro, 2_euro) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

	            pstmt.setInt(1, 100);
	            pstmt.setInt(2, 100);
	            pstmt.setInt(3, 100);
	            pstmt.setInt(4, 100);
	            pstmt.setInt(5, 100);
	            pstmt.setInt(6, 100);

	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected > 0) {
	                ResultSet generatedKeys = pstmt.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    int idCassa = generatedKeys.getInt(1);
	                    JsonObject risultato = new JsonObject();
	                    risultato.addProperty("messaggio", "Cassa inserita con successo");
	                    risultato.addProperty("idCassa", idCassa);
	                    return gson.toJson(risultato);
	                } else {
	                    JsonObject risultato = new JsonObject();
	                    risultato.addProperty("messaggio", "Cassa inserita con successo, ma ID non recuperabile");
	                    return gson.toJson(risultato);
	                }
	            } else {
	                JsonObject risultato = new JsonObject();
	                risultato.addProperty("messaggio", "Impossibile inserire la cassa");
	                return gson.toJson(risultato);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            JsonObject risultato = new JsonObject();
	            risultato.addProperty("messaggio", "Errore durante l'inserimento della cassa: " + e.getMessage());
	            return gson.toJson(risultato);
	        }
	    }
	    
	    
	    
	    //rimuovi cassa di una macchinetta
	    public static String rimuoviCassa(int idCassa) {
	    	Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Cassa WHERE IdCassa = ?")) {

	            pstmt.setInt(1, idCassa);
	            //mi indica il nuemro di tabelle in quella riga 
	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected > 0) {
	                return gson.toJson("Cassa di Macchina:"+Integer.toString(idCassa)+" rimossa con successo");
	            } else {
	                return gson.toJson("Impossibile rimuovere la macchinetta. Verifica ID");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Errore durante la rimozione: " + e.getMessage());
	        }
	    }
	    
	  //svuotamento casse 
	    public static String svuotaCassa(int idCassa) {
	        Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement(
	                     "UPDATE Cassa SET 5_centesimi = ?, 10_centesimi = ?, 20_centesimi = ?, 50_centesimi = ?, 1_euro = ?, 2_euro = ? WHERE idCassa = ?"
	             )) {

	            pstmt.setInt(1, 100); // caff�
	            pstmt.setInt(2, 100); // latte
	            pstmt.setInt(3, 100); // th�
	            pstmt.setInt(4, 100); // cioccolato
	            pstmt.setInt(5, 100); // bicchieri
	            pstmt.setInt(6, 100); // zucchero
	            pstmt.setInt(7, idCassa); // idErogatore

	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected == 1) {
	                return gson.toJson("monente Macchina: " + idCassa + " prese con successo");
	            } else {
	                return gson.toJson("Problema per il ritiro moneta. Verifica ID o se esiste");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Errore durante il riempimento capsule: " + e.getMessage());
	        }
	    }
	    
		
		
	    //aggiorno la cassa di una machinetta dopo un erogazione
		public static String aggiornaCassa(int idCassa, int []monete) {
			
			 Gson gson = new Gson();

		        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		             PreparedStatement pstmt = conn.prepareStatement(
		                     "UPDATE Cassa SET 5_centesimi = ?, 10_centesimi = ?, 20_centesimi = ?, 50_centesimi = ?, 1_euro = ?, 2_euro = ? WHERE idCassa = ?"
		             )) {

		            pstmt.setInt(1, monete[0]); // 5
		            pstmt.setInt(2, monete[1]); // 10
		            pstmt.setInt(3, monete[2]); // 20
		            pstmt.setInt(4, monete[3]); // 50
		            pstmt.setInt(5, monete[4]); // 100
		            pstmt.setInt(6, monete[5]); // 200
		            pstmt.setInt(7, idCassa); // idCassa

		            int rowsAffected = pstmt.executeUpdate();

		            if (rowsAffected == 1) {
		                return gson.toJson("monente Macchina: " + idCassa + " prese con successo");
		            } else {
		                return gson.toJson("Problema per il ritiro moneta. Verifica ID o se esiste");
		            }

		        } catch (SQLException e) {
		            e.printStackTrace();
		            return gson.toJson("Errore durante il riempimento capsule: " + e.getMessage());
		        }
		        
		}
		
		//visualizza una cassa di una machinetta
		public static String visualizzaCassa(int idCassa) {
		    Gson gson = new Gson();

		    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		         PreparedStatement pstmt = conn.prepareStatement("SELECT 5_centesimi, 10_centesimi, 20_centesimi, 50_centesimi, 1_euro, 2_euro FROM Cassa WHERE idCassa = ?")) {

		        pstmt.setInt(1, idCassa);
		        ResultSet rs = pstmt.executeQuery();

		        if (rs.next()) {
		        	int monetePresenti[];
		            Cassa cassa = new Cassa(idCassa,rs.getInt("5_centesimi"),rs.getInt("10_centesimi"));
		            cassa.addProperty("5_centesimi", rs.getInt("5_centesimi"));
		            cassa.addProperty("10_centesimi", rs.getInt("10_centesimi"));
		            cassa.addProperty("20_centesimi", rs.getInt("20_centesimi"));
		            cassa.addProperty("50_centesimi", rs.getInt("50_centesimi"));
		            cassa.addProperty("1_euro", rs.getInt("1_euro"));
		            cassa.addProperty("2_euro", rs.getInt("2_euro"));
		            return gson.toJson(cassa);
		        } else {
		            return gson.toJson("Cassa non trovata");
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        return gson.toJson("Errore durante la visualizzazione della cassa: " + e.getMessage());
		    }
		}
		
		//visualizza tutte le casse 
		public static String visualizzaCasse() {
		    Gson gson = new Gson();
		    List<Cassa> casse = new ArrayList<>();

		    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		         PreparedStatement pstmt = conn.prepareStatement("SELECT idCassa, 5_centesimi, 10_centesimi, 20_centesimi, 50_centesimi, 1_euro, 2_euro FROM Cassa")) {

		        ResultSet rs = pstmt.executeQuery();

		        while (rs.next()) {
		            Cassa cassa = new Cassa(0);
		            cassa.addProperty("idCassa", rs.getInt("idCassa"));
		            cassa.addProperty("5_centesimi", rs.getInt("5_centesimi"));
		            cassa.addProperty("10_centesimi", rs.getInt("10_centesimi"));
		            cassa.addProperty("20_centesimi", rs.getInt("20_centesimi"));
		            cassa.addProperty("50_centesimi", rs.getInt("50_centesimi"));
		            cassa.addProperty("1_euro", rs.getInt("1_euro"));
		            cassa.addProperty("2_euro", rs.getInt("2_euro"));
		            casse.add(cassa);
		        }

		        return gson.toJson(casse);

		    } catch (SQLException e) {
		        e.printStackTrace();
		        JsonObject risultato = new JsonObject();
		        risultato.addProperty("messaggio", "Errore durante la visualizzazione delle casse: " + e.getMessage());
		        return gson.toJson(risultato);
		    }
		}
		
		 //fine metodi cassa----------------------------------------------------------------------------------------------------------------------------------------  
		
		//metodi ricavi----------------------------------------------------------------------------------------------------------------------------------------
		
		public static String creaRicavo(int idMacchinetta, double totale) {
	        Gson gson = new Gson();

	        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Ricavo (data, idMacchinetta, totale) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

	            // Ottieni la data corrente nel formato AAAA-MM-GG
	            LocalDate dataCorrente = LocalDate.now();
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            String dataFormattata = dataCorrente.format(formatter);
	            pstmt.setString(1, dataFormattata);

	            // Imposta l'idMacchinetta
	            pstmt.setInt(2, idMacchinetta);

	            // Imposta il totale
	            pstmt.setDouble(3, totale);

	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected > 0) {
	                ResultSet generatedKeys = pstmt.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    int idRicavo = generatedKeys.getInt(1);
	                    JsonObject risultato = new JsonObject();
	                    risultato.addProperty("messaggio", "Ricavo creato con successo");
	                    risultato.addProperty("idRicavo", idRicavo);
	                    return gson.toJson(risultato);
	                } else {
	                    JsonObject risultato = new JsonObject();
	                    risultato.addProperty("messaggio", "Ricavo creato con successo, ma ID non recuperabile");
	                    return gson.toJson(risultato);
	                }
	            } else {
	                JsonObject risultato = new JsonObject();
	                risultato.addProperty("messaggio", "Impossibile creare il ricavo");
	                return gson.toJson(risultato);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            JsonObject risultato = new JsonObject();
	            risultato.addProperty("messaggio", "Errore durante la creazione del ricavo: " + e.getMessage());
	            return gson.toJson(risultato);
	        }
	    }
		
		//aggiunge un ricavo di una macchinetta 
		public static String aggiungiRicavo(int idMacchinetta, int ricavo) {
	        Gson gson = new Gson();
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        try {
	            conn = DriverManager.getConnection(DATABASE_URL);
	            pstmt = conn.prepareStatement("INSERT INTO Ricavo (idMacchinetta, totale, data) VALUES (?, ?, ?)");

	            pstmt.setInt(1, idMacchinetta);
	            pstmt.setInt(2, ricavo);
	            pstmt.setDate(3, Date.valueOf(LocalDate.now())); // Correzione qui

	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected > 0) {
	                return gson.toJson("Ricavo inserito con successo");
	            } else {
	                return gson.toJson("Impossibile inserire il ricavo");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Errore durante l'inserimento del ricavo: " + e.getMessage());
	            
	        } finally {
	            try {
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		
		//elimina tutti i ricavi di una macchina
		public static String eliminaRicavoCassa(int idMacchinetta) {
			        Gson gson = new Gson();
			        Connection conn = null;
			        PreparedStatement pstmt = null;
			        try {
			            conn = DriverManager.getConnection(DATABASE_URL);
			            pstmt = conn.prepareStatement("Delete from Ricavo where idMacchinetta=?");

			            pstmt.setInt(1, idMacchinetta);
			       

			            int rowsAffected = pstmt.executeUpdate();

			            if (rowsAffected > 0) {
			                return gson.toJson("Ricavi della macchina " + idMacchinetta + " eliminati con successo");
			            } else {
			                return gson.toJson("Impossibile inserire il ricavo");
			            }

			        } catch (SQLException e) {
			            e.printStackTrace();
			            return gson.toJson("Errore durante l'inserimento del ricavo: " + e.getMessage());
			            
			        } finally {
			            try {
			                if (pstmt != null) {
			                    pstmt.close();
			                }
			                if (conn != null) {
			                    conn.close();
			                }
			            } catch (SQLException e) {
			                e.printStackTrace();
			            }
			        }
			    }
		
		//visualizzo tutti i ricavi 
		public static String visualizzaRicavi() {
		    Gson gson = new Gson();
		    List<JsonObject> ricavi = new ArrayList<>();

		    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		         PreparedStatement pstmt = conn.prepareStatement("SELECT idRicavo, data, idMacchinetta, totale FROM Ricavo")) {

		        ResultSet rs = pstmt.executeQuery();

		        while (rs.next()) {
		            JsonObject ricavo = new JsonObject();
		            ricavo.addProperty("idRicavo", rs.getInt("idRicavo"));

		            // Formatta la data nel formato AAAA-MM-GG
		            LocalDate data = rs.getDate("data").toLocalDate();
		            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		            String dataFormattata = data.format(formatter);
		            ricavo.addProperty("data", dataFormattata);

		            ricavo.addProperty("idMacchinetta", rs.getInt("idMacchinetta"));
		            ricavo.addProperty("totale", rs.getDouble("totale"));
		            ricavi.add(ricavo);
		        }

		        return gson.toJson(ricavi);

		    } catch (SQLException e) {
		        e.printStackTrace();
		        JsonObject risultato = new JsonObject();
		        risultato.addProperty("messaggio", "Errore durante la visualizzazione dei ricavi: " + e.getMessage());
		        return gson.toJson(risultato);
		    }
		}
		
		
		//visualizza tutti i ricavi di una machinetta 
		public static String visualizzaRicaviMacchinetta(int idMacchinetta) {
		    Gson gson = new Gson();
		    List<JsonObject> ricavi = new ArrayList<>();

		    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
		         PreparedStatement pstmt = conn.prepareStatement("SELECT idRicavo, data, totale FROM Ricavo WHERE idMacchinetta = ?")) {

		        pstmt.setInt(1, idMacchinetta);
		        ResultSet rs = pstmt.executeQuery();

		        while (rs.next()) {
		            JsonObject ricavo = new JsonObject();
		            ricavo.addProperty("idRicavo", rs.getInt("idRicavo"));

		            // Formatta la data nel formato AAAA-MM-GG
		            LocalDate data = rs.getDate("data").toLocalDate();
		            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		            String dataFormattata = data.format(formatter);
		            ricavo.addProperty("data", dataFormattata);

		            ricavo.addProperty("totale", rs.getDouble("totale"));
		            ricavi.add(ricavo);
		        }

		        return gson.toJson(ricavi);

		    } catch (SQLException e) {
		        e.printStackTrace();
		        JsonObject risultato = new JsonObject();
		        risultato.addProperty("messaggio", "Errore durante la visualizzazione dei ricavi della macchinetta: " + e.getMessage());
		        return gson.toJson(risultato);
		    }
		}
		
		
				
		//--------------------FINE METODI RICAVI----------------------------------------------------------------------------------------------------------------------------------------
		
		
		
		
		//-------------------------METODI UTENTE--------------------------------------------------------------------------------------------------
		public static String creaUtente(String username, String password, boolean ruolo) {
		    Gson gson = new Gson();
		    Connection conn = null;
		    PreparedStatement pstmt = null;
		    try {
		        conn = DriverManager.getConnection(DATABASE_URL);
		        pstmt = conn.prepareStatement("INSERT INTO Utente (username, password, ruolo) VALUES (?, ?, ?)");

		        pstmt.setString(1, username);
		        pstmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt())); // Hash della password con BCrypt
		        if (ruolo) {
		            pstmt.setInt(3, 1); // amministratore
		        } else {
		            pstmt.setInt(3, 0); // impiegato
		        }

		        int rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            return gson.toJson("Utente creato con successo");
		        } else {
		            return gson.toJson("Impossibile creare l'utente");
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        return gson.toJson("Errore durante la creazione dell'utente: " + e.getMessage());
		    } finally {
		        try {
		            if (pstmt != null) {
		                pstmt.close();
		            }
		            if (conn != null) {
		                conn.close();
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}
		
		public static String eliminaUtente(String username) {
		    Gson gson = new Gson();
		    Connection conn = null;
		    PreparedStatement pstmt = null;

		    try {
		        conn = DriverManager.getConnection(DATABASE_URL);
		        pstmt = conn.prepareStatement("DELETE FROM Utente WHERE username = ?");
		        pstmt.setString(1, username);

		        int rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            return gson.toJson("Utente eliminato con successo");
		        } else {
		            return gson.toJson("Utente non trovato");
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        return gson.toJson("Errore durante l'eliminazione dell'utente: " + e.getMessage());

		    } finally {
		        try {
		            if (pstmt != null) {
		                pstmt.close();
		            }
		            if (conn != null) {
		                conn.close();
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}

	    // Funzione per l'hash della password con BCrypt
	    private String hashPassword(String password) {
	        return BCrypt.hashpw(password, BCrypt.gensalt());
	    }
		
	    //get utente con username e password
	    public static String getUtente(String username, String password) {
	        Gson gson = new Gson();
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;

	        try {
	            conn = DriverManager.getConnection(DATABASE_URL);
	            pstmt = conn.prepareStatement("SELECT username, password, ruolo FROM Utente WHERE username = ?");
	            pstmt.setString(1, username);
	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	                String passwordHash = rs.getString("password");
	                if (BCrypt.checkpw(password, passwordHash)) {
	                    JsonObject utente = new JsonObject();
	                    utente.addProperty("username", rs.getString("username"));
	                    utente.addProperty("ruolo", rs.getBoolean("ruolo"));
	                    return gson.toJson(utente);
	                } else {
	                    return gson.toJson("Password errata");
	                }
	            } else {
	                return gson.toJson("Utente non trovato");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return gson.toJson("Errore durante la ricerca dell'utente: " + e.getMessage());
	        } finally {
	            try {
	                if (rs != null) rs.close();
	                if (pstmt != null) pstmt.close();
	                if (conn != null) conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	   
	    
	  
	    

		//-----FINE METODI UTENTE------------------------------------
		

	    
	    
	

}
