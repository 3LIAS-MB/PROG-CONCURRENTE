package EJE8;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Ejercicio8_Hemoterapia_Runnable {
    
    private static Semaphore extracciones = new Semaphore(4); // Máximo 4 simultáneas
    private static Random random = new Random();
    
    public static void main(String[] args) throws InterruptedException {
        Thread[] hilos = new Thread[100];
        
        // Crear 100 personas
        for (int i = 1; i <= 100; i++) {
            Runnable persona = new PersonaRunnable(i);
            hilos[i - 1] = new Thread(persona);
        }
        
        // Iniciar personas con llegadas aleatorias
        for (int i = 0; i < 100; i++) {
            hilos[i].start();
            Thread.sleep(200 + random.nextInt(101)); // Entre 200-300ms
        }
        
        // Esperar a que todas terminen
        for (int i = 0; i < 100; i++) {
            hilos[i].join();
        }
        
        System.out.println("\n=== Todas las 100 personas terminaron ===");
    }
    
    static class PersonaRunnable implements Runnable {
        private int numPersona;
        
        public PersonaRunnable(int numPersona) {
            this.numPersona = numPersona;
        }
        
        @Override
        public void run() {
            try {
                System.out.println("Persona " + numPersona + " llegó a la sala");
                
                // Intentar conseguir un lugar para donar
                System.out.println("Persona " + numPersona + " espera su turno");
                extracciones.acquire();
                
                System.out.println("Persona " + numPersona + " está donando sangre");
                
                // Tiempo de donación entre 4-8 segundos
                Thread.sleep(4000 + random.nextInt(4001));
                
                System.out.println("Persona " + numPersona + " terminó de donar y se retira");
                extracciones.release();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}