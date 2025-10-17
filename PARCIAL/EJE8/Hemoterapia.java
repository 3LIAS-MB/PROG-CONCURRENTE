package EJE8;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Hemoterapia {
    private static final int MAX_PERSONAS = 100;
    private static final int CAPACIDAD_ATENCION = 4;
    private static final Random RND = new Random();
    private static final AtomicInteger contadorPersonas = new AtomicInteger(0);
    
    // Semáforo que controla el acceso a las 4 sillas de donación
    private static final Semaphore SEM_ATENCION = new Semaphore(CAPACIDAD_ATENCION); 

    public static void main(String[] args) {
        System.out.println("--- Sala de Hemoterapia Iniciada (Capacidad: 4) ---");
        
        // Simular la llegada de 100 personas
        for (int i = 0; i < MAX_PERSONAS; i++) {
            final int personaId = contadorPersonas.incrementAndGet();
            new Thread(new Donante(personaId), "Persona-" + personaId).start();

            try {
                // Tiempo de llegada (200-300ms)
                Thread.sleep(RND.nextInt(101) + 200); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Main: Todas las 100 personas han llegado al sistema.");
    }

    static class Donante implements Runnable {
        private final int id;

        public Donante(int id) { this.id = id; }

        @Override
        public void run() {
            String nombre = Thread.currentThread().getName();
            System.out.printf("[%s] 🚶 Llega a la sala.\n", nombre);

            try {
                // Si el semáforo está lleno, la persona espera (espera en la cola del semáforo)
                if (SEM_ATENCION.availablePermits() == 0) {
                    System.out.printf("[%s] 🪑 Espera en la sala. Sala llena.\n", nombre);
                }

                SEM_ATENCION.acquire(); // Intenta acceder a una silla de atención
                
                System.out.printf("[%s] ❤️ INICIA DONACIÓN. Sillas libres: %d.\n", nombre, SEM_ATENCION.availablePermits());
                
                // Tiempo de donación (4000-8000ms, o 4s a 8s)
                Thread.sleep(RND.nextInt(4001) + 4000); 
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                SEM_ATENCION.release(); // Libera la silla
                System.out.printf("[%s] ➡️ Se retira. Donación finalizada. Sillas libres: %d.\n", nombre, SEM_ATENCION.availablePermits());
            }
        }
    }
}