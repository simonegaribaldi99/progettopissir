package pissir2025.model;


import pissir2025.model.Cassa;
import pissir2025.model.Erogatore;
import pissir2025.model.ListaBevande;
import pissir2025.model.Bevanda;
import org.eclipse.paho.client.mqttv3.*;
import javax.net.ssl.SSLSocketFactory;
import java.util.ArrayList;
import java.util.List;


/**
 * La classe `MacchinettaMqtt` gestisce la comunicazione MQTT per la macchinetta, 
 * consentendo l'interazione con il broker MQTT per ricevere e processare ordini di bevande.
 * Implementa l'interfaccia `MqttCallback` per gestire eventi MQTT come la perdita di connessione, 
 * l'arrivo di messaggi e la conferma di consegna.
 */
/*
public class MacchinettaMQTT implements MqttCallback {

	 private Cassa cassa;
	 private Erogatore erogatore;
	 private ListaBevande listaBevande;
	 private MqttClient client;

	 
	 public Macchinetta(int id) throws MqttException {
	        cassa = new Cassa(id);
	        erogatore = new Erogatore(id);
	        listaBevande = new ListaBevande();
	        client = new MqttClient("ssl://broker.hivemq.com:8883", MqttClient.generateClientId());
	        
	        MqttConnectOptions options = new MqttConnectOptions();
	        options.setSocketFactory(SSLSocketFactory.getDefault());
	        client.connect(options);
	        
	        client.subscribe("macchinetta/ordine", this::messageArrived);
	    }
	 
	 public void messageArrived(String topic, MqttMessage message) throws Exception {
	        String payload = new String(message.getPayload());
	        int scelta = Integer.parseInt(payload);
	        Bevanda bevanda = listaBevande.getBevanda(scelta);
	        if (cassa.getCreditoAttuale() >= bevanda.getPrezzo()) {
	            if (erogatore.erogaBevanda(bevanda.getCialdeBevanda(), true)) {
	                cassa.erogaResto(bevanda.getPrezzo());
	                client.publish("macchinetta/stato", new MqttMessage("Bevanda erogata".getBytes()));
	            } else {
	                client.publish("macchinetta/stato", new MqttMessage("Errore erogazione".getBytes()));
	            }
	        } else {
	            client.publish("macchinetta/stato", new MqttMessage("Credito insufficiente".getBytes()));
	        }
	    }
	 
	 public void gestisciOrdine(String ordine) {
	        for (Bevanda bevanda : listaBevande.getBevandeDisponibili()) {
	            if (bevanda.getBevanda().equalsIgnoreCase(ordine)) {
	                if (cassa.verificaResto(bevanda.getPrezzo()) && erogatore.verificaDisponibilita(bevanda)) {
	                    cassa.erogaResto(bevanda.getPrezzo());
	                    erogatore.erogaBevanda(bevanda);
	                    inviaMessaggio("macchinetta/stato", "Bevanda erogata: " + ordine);
	                } else {
	                    inviaMessaggio("macchinetta/stato", "Errore: credito insufficiente o risorse non disponibili");
	                }
	                return;
	            }
	        }
	        inviaMessaggio("macchinetta/stato", "Errore: bevanda non trovata");
	    }

}

*/

import org.eclipse.paho.client.mqttv3.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MacchinettaMqtt implements MqttCallback{

	private static final Logger logger = Logger.getLogger(MacchinettaMqtt.class.getName());
	
    private MqttClient client;
    private Cassa cassa;
    private Erogatore erogatore;
    private ListaBevande listaBevande;

    public MacchinettaMqtt(Cassa cassa, Erogatore erogatore, ListaBevande listaBevande) {
        this.cassa = cassa;
        this.erogatore = erogatore;
        this.listaBevande = listaBevande;
    }
    
    @Override
    public void connectionLost(Throwable cause) {
        logger.warning("MQTT connection lost: " + cause.getMessage());
        // Qui potresti implementare un tentativo di riconnessione
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        logger.info("Messaggio arrivato su topic '" + topic + "': " + payload);
        processMessage(payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("Consegna completata: " + token.isComplete());
    }

    public void start() throws MqttException {
        client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
        client.connect();
        client.subscribe("macchinetta/ordine");

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connessione persa: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Messaggio ricevuto: " + new String(message.getPayload()));
                processMessage(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });

        System.out.println("Macchinetta in ascolto su topic: macchinetta/ordine");
    }

    /**
     * Elabora un ordine ricevuto in formato:
     * "n5;n10;n20;n50;n100;n200;indiceBevanda;zucchero"
     * Es: "1;0;0;2;1;0;3;1" = monete, indice bevanda, zucchero (1 = sì, 0 = no)
     */
    private void processMessage(String payload) {
        try {
            // Split dei dati ricevuti dal messaggio MQTT
            String[] parts = payload.split(";");

            int[] moneteInserite = new int[6];
            for (int i = 0; i < 6; i++) {
                moneteInserite[i] = Integer.parseInt(parts[i]);
            }

            int indiceBevanda = Integer.parseInt(parts[6]);
            boolean zucchero = parts.length > 7 && parts[7].equals("1");

            // Verifica se l'indice è valido
            if (indiceBevanda < 0 || indiceBevanda >= listaBevande.getNumeroBevande()) {
                cassa.sendFailureMessage(client, 5); // Codice 5: bevanda non esistente
                return;
            }

            Bevanda bevanda = listaBevande.getBevanda(indiceBevanda);
            int prezzo = bevanda.getPrezzo();
            int[] cialdeRichieste = bevanda.getCialdeBevanda();

            // Controlla se la cassa può accettare le monete
            boolean moneteAccettate = cassa.accettaMonete(moneteInserite);
            if (!moneteAccettate) {
                cassa.sendFailureMessage(client, 1); // Codice 1: Cassa piena o monete rifiutate
                return;
            }

            // Verifica credito sufficiente
            if (!cassa.verificaResto(prezzo)) {
                cassa.sendFailureMessage(client, 2); // Codice 2: Credito insufficiente
                return;
            }

            // Prova a erogare la bevanda (inclusi bicchieri e zucchero)
            boolean erogata = erogatore.erogaBevanda(cialdeRichieste, zucchero);
            if (!erogata) {
                cassa.sendFailureMessage(client, 3); // Codice 3: Problema con erogatore (cialde o bicchieri)
                return;
            }

            // Tenta di erogare il resto
            int[] resto = cassa.erogaResto(prezzo);
            if (resto == null) {
                cassa.sendFailureMessage(client, 4); // Codice 4: Resto non disponibile
                return;
            }

            // Successo: invia risposta positiva
            cassa.sendSuccessMessage(client, cassa.getCreditoAttuale() + prezzo, prezzo);

            // Puoi anche inviare un altro messaggio success del tipo:
            client.publish("macchinetta/risposta/erogatore", 
                ("SUCCESSO_EROGATORE;BEVANDA=" + bevanda.getBevanda()).getBytes(), 0, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void connectAndSubscribe(String brokerUri, String topic) throws MqttException {
        client.connect();
        logger.info("Connesso a MQTT broker: " + brokerUri);
        client.subscribe(topic);
        logger.info("Sottoscritto al topic: " + topic);
    }
}

	

