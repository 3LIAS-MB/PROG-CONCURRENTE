package UNIDAD3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExample {
    
    static class Tarea implements Runnable {
        private int id;
        
        public Tarea(int id) {
            this.id = id;
        }
        
        @Override
        public void run() {
            System.out.println("Tarea " + id + " iniciada por " + Thread.currentThread().getName());
            
            try {
                // Simula trabajo
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Tarea " + id + " interrumpida");
            }
            
            System.out.println("Tarea " + id + " completada por " + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== THREAD POOL EXECUTOR ===");
        
        // Crear un pool de 3 hilos
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Enviar 10 tareas al pool
        for (int i = 1; i <= 10; i++) {
            executor.execute(new Tarea(i));
        }
        
        // No aceptar más tareas
        executor.shutdown();
        
        try {
            // Esperar a que todas las tareas terminen
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        
        System.out.println("Todas las tareas completadas");
    }
}