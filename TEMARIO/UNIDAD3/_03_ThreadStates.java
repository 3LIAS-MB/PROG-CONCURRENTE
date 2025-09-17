package UNIDAD3;

/**
 * DEMOSTRACIÓN: ESTADOS Y CICLO DE VIDA DE HILOS
 * 
 * Objetivo: Ilustrar los diferentes estados por los que pasa un hilo.
 * 
 * ¿Qué enseña?
 * - Estados del ciclo de vida: NEW, RUNNABLE, RUNNING, BLOCKED, TERMINATED
 * - Uso de getState() para monitorear el estado actual de un hilo
 * - Método yield(): Ceder voluntariamente el tiempo de CPU
 * - Método sleep(): Bloquear el hilo por un tiempo determinado
 * - Método join(): Esperar la finalización de otro hilo
 * - Manejo de InterruptedException
 * 
 * Importante: Entender los estados ayuda a debuggear problemas de concurrencia
 */

public class _03_ThreadStates {
    
    static class HiloEjemplo extends Thread {
        private String nombre;
        
        public HiloEjemplo(String nombre) {
            this.nombre = nombre;
        }
        
        @Override
        public void run() {
            System.out.println(nombre + " - Estado: RUNNABLE -> RUNNING");
            
            // Uso de yield()
            System.out.println(nombre + " cede CPU con yield()");
            Thread.yield();
            
            try {
                // Uso de sleep()
                System.out.println(nombre + " se duerme 1 segundo");
                Thread.sleep(1000);
                
                System.out.println(nombre + " despierta y continúa");
                
            } catch (InterruptedException e) {
                System.out.println(nombre + " fue interrumpido durante el sleep");
            }
            
            System.out.println(nombre + " terminó ejecución");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== ESTADOS Y CONTROL DE HILOS ===");
        
        HiloEjemplo hilo1 = new HiloEjemplo("Hilo-1");
        HiloEjemplo hilo2 = new HiloEjemplo("Hilo-2");
        
        System.out.println("Hilo1 estado: " + hilo1.getState()); // NEW
        
        hilo1.start();
        hilo2.start();
        
        System.out.println("Hilo1 estado después de start(): " + hilo1.getState()); // RUNNABLE
        
        try {
            // Uso de join() - el main espera a que hilo1 termine
            System.out.println("Main espera a que Hilo-1 termine con join()");
            hilo1.join();
            
            System.out.println("Hilo1 estado después de join(): " + hilo1.getState()); // TERMINATED
            
            // Esperar un poco antes de verificar hilo2
            Thread.sleep(500);
            System.out.println("Hilo2 estado durante ejecución: " + hilo2.getState()); // TIMED_WAITING (por sleep)
            
            hilo2.join();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Ambos hilos terminaron");
        System.out.println("Hilo1 estado final: " + hilo1.getState());
        System.out.println("Hilo2 estado final: " + hilo2.getState());
    }
}