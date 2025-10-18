package EJE3;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

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

public class Ejercicio3_Aeropuerto_Runnable {
    
    // Semáforos para las boleterías de cada terminal
    private static Semaphore boleteriasT3 = new Semaphore(4);
    private static Semaphore boleteriasT4 = new Semaphore(5);
    private static Semaphore boleteriasT5 = new Semaphore(3);
    
    
    
// -> Método,Propósito
// .get(),Obtiene el valor actual del entero.
// .incrementAndGet(),Incrementa el valor en 1 y retorna el nuevo valor (de forma atómica).
// .getAndIncrement(),"Retorna el valor actual, y luego lo incrementa en 1 (de forma atómica)."
// .addAndGet(delta),Suma un valor delta al valor actual y retorna el resultado.
    
    // Contadores de pasajeros por terminal
    private static AtomicInteger contadorT3 = new AtomicInteger(0);
    private static AtomicInteger contadorT4 = new AtomicInteger(0);
    private static AtomicInteger contadorT5 = new AtomicInteger(0);
    
    private static Random random = new Random();
    private static AtomicInteger contadorPasajeros = new AtomicInteger(0);
    
    public static void main(String[] args) {
        // Generar taxis indefinidamente
        while (true) {
            try {
                Thread.sleep(120 + random.nextInt(61)); // Entre 120-180ms
                
                // Crear un taxi con 1-4 pasajeros
                int numPasajeros = 1 + random.nextInt(4);
                Runnable taxi = new TaxiRunnable(numPasajeros);
                Thread hilo = new Thread(taxi);
                hilo.start();

//                Runnable taxi = new TaxiRunnable(numPasajeros);
//                new Thread(taxi).start();
 
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class TaxiRunnable implements Runnable {
        private int numPasajeros;
        private String terminal;
        
        public TaxiRunnable(int numPasajeros) {
            this.numPasajeros = numPasajeros;
            // Elegir terminal aleatoriamente
            String[] terminales = {"T3", "T4", "T5"};
            this.terminal = terminales[random.nextInt(3)];
        }
        
        @Override
        public void run() {
            // Actualizar contador de la terminal
            actualizarContador(terminal, numPasajeros);
            
            // Crear hilos de pasajeros
            for (int i = 0; i < numPasajeros; i++) {
                int numPasajero = contadorPasajeros.incrementAndGet();
                Runnable pasajero = new PasajeroRunnable(numPasajero, terminal);
                new Thread(pasajero).start();
            }
        }
    }
    
    static class PasajeroRunnable implements Runnable {
        private int numPasajero;
        private String terminal;
        
        public PasajeroRunnable(int numPasajero, String terminal) {
            this.numPasajero = numPasajero;
            this.terminal = terminal;
        }
        
        @Override
        public void run() {
            try {
                System.out.println("Pasajero " + numPasajero + " llegó a Terminal " + terminal);
                
                // Obtener semáforo correspondiente
                Semaphore boleteria = obtenerBoleteria(terminal);
                
                // Intentar hacer check-in
                boleteria.acquire();
                System.out.println("Pasajero " + numPasajero + " se dirige a hacer check-in en Terminal " + terminal);
                
                // Tiempo de check-in entre 180-280ms
                Thread.sleep(180 + random.nextInt(101));
                
                System.out.println("Pasajero " + numPasajero + " finalizó check-in en Terminal " + terminal);
                boleteria.release();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void actualizarContador(String terminal, int cantidad) {
        int total = 0;
        switch (terminal) {
            case "T3":
                total = contadorT3.addAndGet(cantidad);
                break;
            case "T4":
                total = contadorT4.addAndGet(cantidad);
                break;
            case "T5":
                total = contadorT5.addAndGet(cantidad);
                break;
        }
        System.out.println("Terminal " + terminal + " recibió " + cantidad + " pasajero(s). Total: " + total);
    }
    
    private static Semaphore obtenerBoleteria(String terminal) {
        switch (terminal) {
            case "T3": return boleteriasT3;
            case "T4": return boleteriasT4;
            case "T5": return boleteriasT5;
            default: return boleteriasT3;
        }
    }
}