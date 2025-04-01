package pissir2025.manager;

import static spark.Spark.*;
import java.sql.SQLException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import pissir2025.database.database;
/**
 * Ci sono da implementare ancora le classi per far funzionare questi comandi
 */

public class Serverhttp implements MqttCallbackExtended {
	public static void start() throws SQLException {
		
		//percorso per le risorse statiche per Spark
		staticFiles.location("frontend/html");
	
		/**
		 * Avvia il server sulla pagina prinicipale del sito internet che gestisce il tutto
		 */
		get("/",(req,res)->{
		res.redirect("pagina-login.html");
		return null;
	});
		
		/*
		 * Visualizza una sola macchinetta
		 */
		get("/macchinetta",(req,res)->{
			String idMacchinetta = req.queryParams("idMacchinetta");
		    int id = Integer.parseInt(idMacchinetta);
		    res.type("application/json"); // Imposta il tipo di contenuto della risposta come JSON
		    try {
		    // Recupera il risultato della visualizzazione della macchinetta
			String result = database.visualizzaMachinetta(id);
			// Restituisci il risultato in formato JSON
			return result;
		    } catch (Exception e) {
		        // In caso di errore (qualsiasi tipo di eccezione), gestisci l'errore
		        e.printStackTrace();
		        res.status(500);
		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero della macchinetta: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Visualizza tutte le macchinette di un istituto
		 */
		get("/macchinetteIstituto",(req,res)->{
			String idIstituto = req.queryParams("idIstituto");
			int id = Integer.parseInt(idIstituto);
			res.type("application/json");
			try {
				String result = database.visualizzaMachinetteIstituto(id);
				return result;
			} catch (Exception e) {
		        // In caso di errore (qualsiasi tipo di eccezione), gestisci l'errore
		        e.printStackTrace();
		        res.status(500);
		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero delle macchinette di un istituto: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Visualizza tutte le macchinette
		 */
		get("/macchinette",(req,res)-> {
			res.type("application/json");
			try {
				String result = database.visualizzaMachinette();
				return result;
			} catch (Exception e) {
		        // In caso di errore (qualsiasi tipo di eccezione), gestisci l'errore
		        e.printStackTrace();
		        res.status(500);
		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la visualizzazione di tutte le macchinette: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 *  Visualizza un istituto
		 */
		get("/istituto",(req,res)->{
			String idIstituto = req.queryParams("idIstituto");
			int id = Integer.parseInt(idIstituto);
			res.type("application/json");
			try {
				String result = database.visualizzaIstituto(id);
				return result;
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la visualizzazione di un istituto: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Visualizza tutti gli istituti
		 */
		get("/istituti",(req,res)->{
			res.type("application/json");
			try {
				String result = database.visualizzaIstituti();
				return result;
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la visualizzazione di tutti gli istituti: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Visualizza il singolo amministratore
		 */
		get("/amministratore",(req,res)->{
			res.type("application/json");
			String username = req.queryParams("username");
            String password = req.queryParams("password");
            if (username == null || password == null) {
                res.status(400);
                return new Gson().toJson("Errore: Parametri mancanti");
            }
            try {
                String result = database.getUtente(username, password);
                return result;
            } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la visualizzazione di un amministratore: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 *  Visualizza amministratori
		 
		 
		get("/amministratori",(req,res)-> {
			res.type("application/json");
			try {
				return database.
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero delle macchinette di un istituto: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});*/
		
		/*
		 * Visualizza il singolo impiegato
		 * CAPIRE COME IMPLEMENTARE LA PARTE ISADMIN
		 */
		get("/impiegato",(req,res)->{
			res.type("application/json");
			String username = req.queryParams("username");
            String password = req.queryParams("password");
            if (username == null || password == null) {
                res.status(400);
                return new Gson().toJson("Errore: Parametri mancanti");
            }

            try {
                String result = database.getUtente(username, password);
                return result;
            } catch (Exception e) {
                res.status(500);
                return new Gson().toJson("Errore del server: " + e.getMessage());
            }
		});
		
		/*
		 * Visualizza impiegati
		 * isAdmin deve essere falso (da capire come implementarla)
		 
		get("/impiegati",(req,res,boolean isAdmin())-> {
			try {
				//return database.funzione che ti da tutti gli impiegati
			} catch(SQLException e) {
				e.printStackTrace();
				res.status(500);
				return "Errore recupero dati";
			}
		});
		*/
		
		/*
		 * Creare un nuovo amministratore
		 * in caso non funzionasse "/createAdmin/:idUtente"
		 */
		post("/createAdmin/:idAdmin",(req,res)->{
			res.type("application/json");
			try {
				String nome = req.queryParams("nome");
				String password = req.queryParams("password");
				database.creaUtente(nome, password, true);
				res.status(201);
				return "Amministratore creato";
				
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la creazione di un amministratore: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Creare un nuovo Impiegato
		 * in caso non funzionasse "/createAdmin/:idUtente"
		 */
		post("/createImpiegato/:idImpiegato",(req,res)->{
			res.type("application/json");
			try {
				String nome = req.queryParams("nome");
				String password = req.queryParams("password");
				database.creaUtente(nome, password, false);
				res.status(201);
				return "Impiegato creato";
				
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la creazione di un impiegato: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Elimina un amministatore
		 */
		post("/delete/:idAdmin",(req,res)->{
			res.type("application/json");
			String username = req.queryParams("username"); 
			try {
				String result = database.eliminaUtente(username);
			if(result.contains("Utente eliminato con successo")) {
				res.status(200);
				return result;
			}else {
				res.status(404);
				return result;
			}
			
		}catch (Exception e) {
	        e.printStackTrace();
	        res.status(500);
	        JsonObject errorJson = new JsonObject();
	        errorJson.addProperty("error", "Errore durante l'eliminazione di un amministratore: " + e.getMessage());
	        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
	    }
		});
		
		/*
		 * Elimina un impiegato
		 */
		post("/delete/:idImpiegato",(req,res)->{
			res.type("application/json");
			String username = req.queryParams("username"); 
			try {
				String result = database.eliminaUtente(username);
			if(result.contains("Utente eliminato con successo")) {
				res.status(200);
				return result;
			}else {
				res.status(404);
				return result;
			}
			
		}catch (Exception e) {
	        e.printStackTrace();
	        res.status(500);
	        JsonObject errorJson = new JsonObject();
	        errorJson.addProperty("error", "Errore durante l'eliminazione di un impiegato: " + e.getMessage());
	        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
	    }
		});
		
		/*
		 * Creare una macchinetta
		 */
		
		post("/create/macchinetta", (req,res)->{
			res.type("application/json");
			try {
				String nomeMacchinetta = req.queryParams("nomeMacchinetta");
				String idIstituto = req.queryParams("idIstituto");
				int id = Integer.parseInt(idIstituto);
				database.inserisciMacchinetta(nomeMacchinetta, id);
				res.status(201);
				return "Macchinetta creata";
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la creazione di una macchinetta: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Vedere tutte le macchinette con guasti
		 */
		
		get("/guasti", (req, res) -> {
		    res.type("application/json"); // Imposta il tipo di contenuto della risposta come JSON

		    try {
		        String result = database.getGuasto(); // Recupera i dati delle macchinette guaste
		        return result;
		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero dei guasti: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});
		
		/*
		 * Risolvere il guasto
		 */
		post("/guasti", (req,res) -> {
			res.type("application/json");
			try {
				String idMacchinetta = req.queryParams("idMacchinetta");
				int id = Integer.parseInt(idMacchinetta);
				database.guastoRisolto(id);
				res.status(201);
				return "Guasto risolto o mai esistito";
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero dei guasti: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});
		
		/*
		 * Creare un istituto
		 */
		
		post("/createIstituto/:idAdmin",(req,res)->{
			res.type("application/json");
			try {
				String nome = req.queryParams("nome");
				database.creaIstituto(nome);
				res.status(201);
				return "Istituto creato";
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la creazione di un istituto: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Eliminare una macchinetta
		 */
		
		post("/delete/:idMacchinetta",(req,res)->{
			res.type("application/json");
			String idMacchinetta = req.queryParams("idMacchinetta");
		    int id = Integer.parseInt(idMacchinetta);
		    try {
		        // Funzione del database per rimuovere la macchinetta
		        String result = database.rimuoviMachinetta(id);

		        // Controllo sulla stringa restituita dalla funzione del database
		        if (result.contains("Macchinetta rimossa con successo")) {
		            res.status(200);
		            return result; // Restituisci il risultato
		        } else {
		            res.status(404);
		            return result; // Restituisci il messaggio di errore
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la cancellazione di una macchinetta: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Eliminare un istituto
		 */
		
		post("/delete/:idIstituto",(req,res)->{
			res.type("application/json");
		    String idIstituto = req.queryParams("idIstituto");
		    int id = Integer.parseInt(idIstituto);
		    try {
		        // Funzione del database per rimuovere l'istituto
		        String result = database.rimuoviIstituto(id);

		        // Controllo sul risultato restituito dalla funzione del database
		        if (result.contains("Istituto rimosso con successo")) {
		            res.status(200);
		            return result; // Restituisci il risultato
		        } else {
		            res.status(404);
		            return result; // Restituisci il messaggio di errore
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la cancellazione di un istituto: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Inserisci un erogatore
		 */
		post("/erogatore", (req, res) -> {
		    res.type("application/json"); // Imposta la risposta come JSON

		    try {
		        String result = database.inserisciErogatore(); // Chiama la funzione del database
		        res.status(201); // Codice HTTP 201: Creazione avvenuta con successo
		        return result;
		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante l'inserimento dell'erogatore: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});
		
		/*
		 * Rimuovi un erogatore di una macchinetta
		 */
		post("/delete/erogatore",(req,res)->{
			res.type("application/json");
			String idMacchinetta = req.queryParams("idMacchinetta");
		    int id = Integer.parseInt(idMacchinetta);
		    try {
		    	// Funzione del database per rimuovere la macchinetta
		        String result = database.rimuoviErogatore(id);

		        // Controllo sulla stringa restituita dalla funzione del database
		        if (result.contains("Macchinetta rimossa con successo")) {
		            res.status(200);
		            return result; // Restituisci il risultato
		        } else {
		            res.status(404);
		            return result; // Restituisci il messaggio di errore
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la cancellazione di un erogatore: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Resetta gli erogatori a default
		 */
		post("/riempiCapsule", (req, res) -> {
		    res.type("application/json"); // Imposta il tipo di risposta come JSON

		    // Recupero dell'ID dalla richiesta
		    String idMacchinettaStr = req.queryParams("idMacchinetta");
		    if (idMacchinettaStr == null) {
		        res.status(400); // Errore 400: Richiesta non valida
		        return new Gson().toJson("Errore: ID Macchinetta mancante");
		    }

		    try {
		        int idMacchinetta = Integer.parseInt(idMacchinettaStr); // Converte l'ID in int
		        String result = database.riempiCapsule(idMacchinetta); // Chiama la funzione nel database
		        res.status(200); // Operazione riuscita
		        return result;
		    } catch (NumberFormatException e) {
		        res.status(400);
		        return new Gson().toJson("Errore: ID Macchinetta non valido");
		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        return new Gson().toJson("Errore del server: " + e.getMessage());
		    }
		});
		
		/*
		 * Aggiorna gli erogatori dopo un'erogazione
		 */
		post("/aggiornaErogatore", (req, res) -> {
		    res.type("application/json"); // Imposta il tipo di risposta come JSON
		    Gson gson = new Gson();

		    try {
		        // Parsing del JSON ricevuto
		        JsonObject jsonBody = gson.fromJson(req.body(), JsonObject.class);

		        // Controllo parametri richiesti
		        if (!jsonBody.has("idErogatore") || !jsonBody.has("nCialde") || !jsonBody.has("zucchero") || !jsonBody.has("bicchieri")) {
		            res.status(400);
		            return gson.toJson("Errore: Parametri mancanti");
		        }

		        // Estrarre i dati dal JSON
		        int idErogatore = jsonBody.get("idErogatore").getAsInt();
		        JsonArray cialdeArray = jsonBody.getAsJsonArray("nCialde");
		        int zucchero = jsonBody.get("zucchero").getAsInt();
		        int bicchieri = jsonBody.get("bicchieri").getAsInt();

		        // Controllo che l'array contenga esattamente 4 valori (caffè, latte, tè, cioccolato)
		        if (cialdeArray.size() != 4) {
		            res.status(400);
		            return gson.toJson("Errore: Array nCialde deve contenere esattamente 4 valori");
		        }

		        // Conversione dell'array JSON in un array di interi
		        int[] nCialde = new int[4];
		        for (int i = 0; i < 4; i++) {
		            nCialde[i] = cialdeArray.get(i).getAsInt();
		        }

		        // Chiamata alla funzione nel database
		        String result = database.aggiornaErogatore(idErogatore, nCialde, zucchero, bicchieri);
		        res.status(200); // Operazione riuscita
		        return result;

		    } catch (JsonSyntaxException e) {
		        res.status(400);
		        return gson.toJson("Errore: JSON malformato");
		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        return gson.toJson("Errore del server: " + e.getMessage());
		    }
		});

		/*
		 * Visualizza un erogatore di una macchinetta
		 *probabilmente non sarà corretta
		 */
		get("/visualizzaErogatore",(req,res)->{
			res.type("application/json");
			String idErogatore = req.queryParams("idErogatore");
			int id = Integer.parseInt(idErogatore);
			try {
				String result = database.visualizzaErogatore(id);
				return result;
			}catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero dei guasti: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }

		});
		
		/*
		 * Aggiunta di una cassa
		 */
		post("/inserisciCassa", (req, res) -> {
		    res.type("application/json"); // Imposta il tipo di risposta come JSON
		    Gson gson = new Gson();

		    try {
		        // Chiamata alla funzione del database
		        String result = database.inserisciCassa();
		        res.status(201); // Codice 201: Created
		        return result;

		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        return gson.toJson("Errore del server: " + e.getMessage());
		    }
		});

		/*
		 * Rimozione di una cassa
		 */
		post("/rimuoviCassa", (req, res) -> {
		    res.type("application/json");
		    
		    String idCassa = req.queryParams("idCassa");
		    
		    if (idCassa == null) {
		        res.status(400); // Bad Request
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Parametro 'idCassa' mancante");
		        return new Gson().toJson(errorJson);
		    }

		    try {
		        int id = Integer.parseInt(idCassa);
		        String result = database.rimuoviCassa(id);

		        // Controllo sulla stringa restituita dalla funzione del database
		        if (result.contains("rimossa con successo")) {
		            res.status(200);
		        } else {
		            res.status(404);
		        }
		        
		        return result; 

		    } catch (NumberFormatException e) {
		        res.status(400);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "ID non valido, deve essere un numero");
		        return new Gson().toJson(errorJson);
		    } catch (Exception e) {
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la rimozione della cassa: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});

		/*
		 * Svuota cassa
		 */
		post("/svuotaCassa",(req,res)->{
			res.type("application/json");
			String idCassa = req.queryParams("idCassa");
		    int id = Integer.parseInt(idCassa);
		    try {
		    	// Funzione del database per rimuovere la macchinetta
		        String result = database.svuotaCassa(id);

		        // Controllo sulla stringa restituita dalla funzione del database
		        if (result.contains("Cassa svuotata con successo")) {
		            res.status(200);
		            return result; // Restituisci il risultato
		        } else {
		            res.status(404);
		            return result; // Restituisci il messaggio di errore
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante lo svuotamento di una cassa: " + e.getMessage());
		        return new Gson().toJson(errorJson); // Risposta con messaggio di errore in formato JSON
		    }
		});
		
		/*
		 * Aggiorna la cassa
		 */
		
		post("/aggiornaCassa", (req, res) -> {
		    res.type("application/json");

		    try {
		        // Legge il body della richiesta
		        String body = req.body();
		        
		        // Estrarre idCassa manualmente
		        String idCassaStr = body.substring(body.indexOf("idCassa") + 9, body.indexOf(",", body.indexOf("idCassa")));
		        int idCassa = Integer.parseInt(idCassaStr.trim());

		        // Estrarre l'array di monete manualmente
		        String moneteStr = body.substring(body.indexOf("[") + 1, body.indexOf("]"));
		        String[] moneteSplit = moneteStr.split(",");
		        if (moneteSplit.length != 6) {
		            res.status(400);
		            return "{\"error\":\"L'array 'monete' deve contenere esattamente 6 valori\"}";
		        }

		        int[] monete = new int[6];
		        for (int i = 0; i < 6; i++) {
		            monete[i] = Integer.parseInt(moneteSplit[i].trim());
		        }

		        // Chiamata alla funzione del database
		        String result = database.aggiornaCassa(idCassa, monete);

		        if (result.contains("prese con successo")) {
		            res.status(200);
		        } else {
		            res.status(404);
		        }

		        return result;

		    } catch (Exception e) {
		        res.status(500);
		        return "{\"error\":\"Errore durante l'aggiornamento della cassa: " + e.getMessage() + "\"}";
		    }
		});
		
		/*
		 * Visualizza una cassa
		 */
		get("/cassa/:idCassa", (req, res) -> {
            res.type("application/json");
            int idCassa = Integer.parseInt(req.params(":idCassa"));
            try {
            	return database.visualizzaCassa(idCassa);
            } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero della cassa: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
        });

		/*
		 * Visualizza tutte le casse
		 */
		get("/casse", (req, res) -> {
		    res.type("application/json"); // Imposta il tipo di contenuto della risposta come JSON

		    try {
		        String result = database.visualizzaCasse(); // Recupera i dati delle macchinette guaste
		        return result;
		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero di tutte le casse: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});
		
		/*
		 * Crea ricavo
		 */
		post("/creaRicavo", (req,res) ->{
			res.type("application/json");
			String idMacchinetta = req.queryParams("idMacchinetta");
			int id = Integer.parseInt(idMacchinetta);
			String tot = req.queryParams("totale");
			Double totale = Double.parseDouble(tot);
			try {
				return database.creaRicavo(id, totale);
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante la crezione del ricavo: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});
		
		/*
		 * Aggiungi ricavo
		 */
		post("/aggiungiRicavo", (req,res) ->{
			res.type("application/json");
			String idMacchinetta = req.queryParams("idMacchinetta");
			int id = Integer.parseInt(idMacchinetta);
			String ric = req.queryParams("ricavo");
			int ricavo = Integer.parseInt(ric);
			try {
				return database.aggiungiRicavo(id, ricavo);
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante l'aggiunta di un ricavo: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});
		
		/*
		 * Elimina ricavo di una macchinetta
		 */
		post("/deleteRicavo",(req,res) ->{
			res.type("application/json");
			String idMacchinetta = req.queryParams("idMacchinetta");
			int id = Integer.parseInt(idMacchinetta);
			try {
				return database.eliminaRicavoCassa(id);
			} catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante l'eliminazione di un ricavo: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});
		
		/*
		 * Visualizzare tutti i ricavi
		 */
		get("/ricavi", (req, res) -> {
		    res.type("application/json"); // Imposta il tipo di contenuto della risposta come JSON

		    try {
		        String result = database.visualizzaRicavi(); // Recupera i dati delle macchinette guaste
		        return result;
		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero di tutti i ricavi: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});
		
		/*
		 * Visualizza tutti i ricavi di una macchinetta
		 */
		get("/ricaviMacchinetta",(req,res)->{
			res.type("application/json");
			String idMacchinetta = req.queryParams("idMacchinetta");
			int id = Integer.parseInt(idMacchinetta);
			try {
		        String result = database.visualizzaRicaviMacchinetta(id); // Recupera i dati delle macchinette guaste
		        return result;
		    } catch (Exception e) {
		        e.printStackTrace();
		        res.status(500);

		        // Restituisci un messaggio di errore in formato JSON
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("error", "Errore durante il recupero di tutti i ricavi di una singola macchinetta: " + e.getMessage());
		        return new Gson().toJson(errorJson);
		    }
		});

	}		
	
	

		 // Metodo invocato quando si perde la connessione MQTT.
		@Override
		public void connectionLost(Throwable cause) {
			// TODO Auto-generated method stub
			
		}

		// Metodo invocato quando arriva un messaggio MQTT su un topic sottoscritto.
		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			// TODO Auto-generated method stub
			
		}

		// Metodo invocato quando un messaggio MQTT è stato consegnato completamente.
		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			// TODO Auto-generated method stub
			
		}


		 // Metodo invocato quando la connessione al broker MQTT viene ristabilita o completata.
		@Override
		public void connectComplete(boolean reconnect, String serverURI) {
			// TODO Auto-generated method stub
			
		}

}

