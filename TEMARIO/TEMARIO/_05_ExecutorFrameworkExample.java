package TEMARIO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class _05_ExecutorFrameworkExample {
    public static void main(String[] args) {
        // Crear pool de 3 hilos
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Enviar 10 tareas al executor
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("Tarea " + taskId + " ejecutada por: " 
                                 + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // Simular trabajo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Tarea " + taskId + " completada");
            });
        }
        
        // Cerrar el executor
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