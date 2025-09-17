package UNIDAD3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * DEMOSTRACIÓN: THREAD POOL EXECUTOR
 * 
 * Objetivo: Mostrar el uso avanzado de pools de hilos para gestión eficiente.
 * 
 * ¿Qué enseña?
 * - Creación y uso de ExecutorService con Executors
 * - Ventajas de reutilizar hilos en lugar de crearlos/destruirlos
 * - Control del número máximo de hilos concurrentes
 * - shutdown() y awaitTermination() para gestión del ciclo de vida
 * - Mejor escalabilidad y rendimiento en aplicaciones con muchas tareas
 * 
 * Uso ideal: Servidores, aplicaciones con muchas tareas cortas
 */

public class _06_ThreadPoolExample {
    
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