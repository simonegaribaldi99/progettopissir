package pissir2025.manager;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Server implements MqttCallbackExtended {
	
	private MqttClient client;
    private String brokerUrl = "tcp://localhost:1883"; // Sostituisci con l'URL del tuo broker
    private String clientId = UUID.randomUUID().toString(); // ID client univoco
    private MemoryPersistence persistence = new MemoryPersistence();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
    
    public void connect() {
        try {
            client = new MqttClient(brokerUrl, clientId, persistence);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true); // Imposta a false per mantenere le sottoscrizioni
            connectOptions.setAutomaticReconnect(true); // Abilita la riconnessione automatica

            client.setCallback(this); // Imposta il callback per gestire i messaggi

            client.connect(connectOptions);
            System.out.println("Connessione MQTT stabilita.");

        } catch (MqttException e) {
            System.err.println("Errore durante la connessione MQTT: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //Implementazione dei Metodi di Callback (MqttCallbackExtended)
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        if (reconnect) {
            System.out.println("Riconnessione MQTT completata.");
            // Puoi ri-sottoscrivere ai topic qui
        } else {
            System.out.println("Connessione MQTT completata.");
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.err.println("Connessione MQTT persa: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Messaggio ricevuto sul topic: " + topic);
        System.out.println("Payload: " + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Chiamato quando un messaggio � stato consegnato (QoS > 0)
    }
    
    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                System.out.println("Disconnessione MQTT effettuata.");
            }
        } catch (MqttException e) {
            System.err.println("Errore durante la disconnessione MQTT: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //metodo per la sottoscrizione di un topic
    public void subscribe(String topic) {
        try {
            client.subscribe(topic, 1); // QoS 1: almeno una volta
            System.out.println("Sottoscritto al topic: " + topic);
        } catch (MqttException e) {
            System.err.println("Errore durante la sottoscrizione al topic: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //metodo per la publicazione di un messagio topic
    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(1); // QoS 1: almeno una volta
            client.publish(topic, mqttMessage);
            System.out.println("Messaggio pubblicato sul topic: " + topic);
        } catch (MqttException e) {
            System.err.println("Errore durante la pubblicazione del messaggio: " + e.getMessage());
            e.printStackTrace();
        }
    }


}

