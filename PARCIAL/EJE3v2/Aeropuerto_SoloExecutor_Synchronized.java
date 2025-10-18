package EJE3v2;

import java.util.Random;
import java.util.concurrent.*;

//	3) El Aeropuerto Internacional de Londres posee 3 terminales (T3, T4 y T5) para la distribución de 
//	los vuelos diarios de las aerolíneas que operan en dicho aeropuerto, por restricciones, cada 
//	terminal solo posee una aerolínea específica. Los taxis llegan a dichas terminales transportando 
//	los pasajeros que luego embarcarán los aviones. Cada taxi se dirige a la terminal que le 
//	corresponde según el destino de los pasajeros (esta elección se debe realizar de forma 
//	aleatoria). Un taxi puede transportar entre uno y cuatro pasajeros. Una vez que los pasajeros 
//	descienden del taxi se dirigen a cualquier boletería para hacer el check-in. Debe simular para 
//	una cantidad indefinida de taxis y pasajeros. Debe controlar en todo momento la cantidad de 
//	pasajeros que recibe cada terminal (informando esto por pantalla), debe mostrar por pantalla 
//	cuando un pasajero llega a la terminal, cuando se dirige a realizar el check-in y cuando finaliza 
//	el check-in, informando para ello el número de pasajero. 
//	La terminal T3 posee 4 boleterías para hacer el check-in. 
//	La terminal T4 posee 5 boleterías para hacer el check-in. 
//	La terminal T5 posee 3 boleterías para hacer el check-in. 
//	Los taxis llegan constantemente a cada terminal entre 120-180ms. 
//	El tiempo de demora en el check-in para cada pasajero es entre 180-280ms.
//	Cada boletería atiende un pasajero a la vez. 
//	
//	-> Se puede resolver mediante ExecutorService o Semáforos o combinaciones de ambos esquemas. 


public class Aeropuerto_SoloExecutor_Synchronized {
    
    // ExecutorService para cada terminal (tamaño = número de boleterías)
    private static ExecutorService boleteriasT3 = Executors.newFixedThreadPool(4);
    private static ExecutorService boleteriasT4 = Executors.newFixedThreadPool(5);
    private static ExecutorService boleteriasT5 = Executors.newFixedThreadPool(3);
    
    // Contadores de pasajeros por terminal (variables normales)
    private static int contadorT3 = 0;
    private static int contadorT4 = 0;
    private static int contadorT5 = 0;
    private static int contadorPasajeros = 0;
    
    private static Random random = new Random();
    
    public static void main(String[] args) {
        // Generar taxis indefinidamente
        while (true) {
            try {
                Thread.sleep(120 + random.nextInt(61)); // Entre 120-180ms
                
                // Crear un taxi con 1-4 pasajeros
                int numPasajeros = 1 + random.nextInt(4);
                String[] terminales = {"T3", "T4", "T5"};
                String terminal = terminales[random.nextInt(3)];
                
                // Actualizar contador de la terminal (synchronized)
                actualizarContador(terminal, numPasajeros);
                
                // Crear pasajeros para ese taxi
                for (int i = 0; i < numPasajeros; i++) {
                    int numPasajero = obtenerNumeroPasajero();
                    
                    // Enviar tarea al ExecutorService correspondiente
                    Runnable tareaPasajero = new TareaPasajero(numPasajero, terminal);
                    obtenerExecutor(terminal).submit(tareaPasajero);
                }
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class TareaPasajero implements Runnable {
        private int numPasajero;
        private String terminal;
        
        public TareaPasajero(int numPasajero, String terminal) {
            this.numPasajero = numPasajero;
            this.terminal = terminal;
        }
        
        @Override
        public void run() {
            try {
                System.out.println("Pasajero " + numPasajero + " llegó a Terminal " + terminal);
                
                // El ExecutorService automáticamente gestiona la cola de espera
                System.out.println("Pasajero " + numPasajero + " se dirige a hacer check-in en Terminal " + terminal);
                
                // Tiempo de check-in entre 180-280ms
                Thread.sleep(180 + random.nextInt(101));
                
                System.out.println("Pasajero " + numPasajero + " finalizó check-in en Terminal " + terminal);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Método synchronized para actualizar contadores
    private static synchronized void actualizarContador(String terminal, int cantidad) {
        int total = 0;
        switch (terminal) {
            case "T3":
                contadorT3 += cantidad;
                total = contadorT3;
                break;
            case "T4":
                contadorT4 += cantidad;
                total = contadorT4;
                break;
            case "T5":
                contadorT5 += cantidad;
                total = contadorT5;
                break;
        }
        System.out.println("Terminal " + terminal + " recibió " + cantidad + " pasajero(s). Total: " + total);
    }
    
    // Método synchronized para obtener número de pasajero
    private static synchronized int obtenerNumeroPasajero() {
        contadorPasajeros++;
        return contadorPasajeros;
    }
    
    private static ExecutorService obtenerExecutor(String terminal) {
        switch (terminal) {
            case "T3": return boleteriasT3;
            case "T4": return boleteriasT4;
            case "T5": return boleteriasT5;
            default: return boleteriasT3;
        }
    }
}