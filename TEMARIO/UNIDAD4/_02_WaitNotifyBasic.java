package UNIDAD4;

/**
 * UNIDAD 4 - SINCRONIZACIÓN CON wait() Y notify()
 * 
 * Objetivo: Mostrar el uso básico de wait() y notify() para coordinación entre hilos.
 * 
 * ¿Qué enseña?
 * - Los métodos wait() y notify() deben usarse dentro de bloques synchronized
 * - wait() libera el monitor y pone el hilo en espera
 * - notify() despierta un hilo en espera (aleatoriamente)
 * - notifyAll() despierta todos los hilos en espera
 * - Uso de contadores internos en el monitor
 */

public class _02_WaitNotifyBasic {

    static class RecursoCompartido {
	        private boolean disponible = false;
	        
	        public synchronized void producir() {
	        	while (disponible) {
	                try {
	                    System.out.println("Productor esperando...");
	                    
						// Libera el monitor (lock) automáticamente
						// Pone el hilo en estado WAITING
						// Espera hasta que otro hilo llame a notify()
	                    
	                    // Espera si ya hay recurso disponible
	                    wait(); 
	                } catch (InterruptedException e) {
	                    Thread.currentThread().interrupt();
	                }
	            }
	            
	            disponible = true;
	            System.out.println("Productor: Recurso producido");
				// Despierta un hilo que estaba en wait()
				// No libera el lock inmediatamente
				// El hilo despertado competirá por el lock
	            
	            // Notifica al consumidor
	            notify(); 
	        }
	        
	        public synchronized void consumir() {
	            while (!disponible) {
	                try {
	                    System.out.println("Consumidor esperando...");
	                    wait(); // Espera hasta que haya recurso disponible
	                } catch (InterruptedException e) {
	                    Thread.currentThread().interrupt();
	                }
	            }
	            
	            disponible = false;
	            System.out.println("Consumidor: Recurso consumido");
	            notify(); // Notifica al productor
	        }
	    }

    // Productor produce → disponible = true → notify()
    static class Productor extends Thread {
        private RecursoCompartido recurso;
        
        public Productor(RecursoCompartido recurso) {
            this.recurso = recurso;
        }
        
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                recurso.producir();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Consumidor consume → disponible = false → notify()
    static class Consumidor extends Thread {
        private RecursoCompartido recurso;
        
        public Consumidor(RecursoCompartido recurso) {
            this.recurso = recurso;
        }
        
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                recurso.consumir();
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== wait() Y notify() BÁSICO ===");
        
        RecursoCompartido recurso = new RecursoCompartido();
        
        Productor productor = new Productor(recurso);
        Consumidor consumidor = new Consumidor(recurso);
        // Consumidor consumidor2 = new Consumidor(recurso);
        
        productor.start();
        consumidor.start();
        // consumidor2.start();
        
        try {
            productor.join();
            consumidor.join();
            // consumidor2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Ejemplo completado");
    }
}