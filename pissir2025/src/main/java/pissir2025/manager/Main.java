package pissir2025.manager;

import pissir2025.database.database;
import pissir2025.database.DatabaseManager;
import pissir2025.manager.Serverhttp;
import spark.Spark;

public class Main {
	public static void main(String[] args) {
        // Inizializza il database richiamando il metodo `init()` della classe `database`.
        DatabaseManager.init();
        
        // Imposta la porta del server HTTP a `3000` utilizzando `Spark`.
        Spark.port(3000);
        
        // Avvia il server richiamando il metodo `start()` della classe `Server`.
        Serverhttp.start();
    }
}
