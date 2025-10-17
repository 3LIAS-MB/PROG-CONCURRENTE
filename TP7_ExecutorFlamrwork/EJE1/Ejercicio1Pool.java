package EJE1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Ejercicio1Pool {
    
    public static void main(String[] args) {
        // Marca de tiempo inicial
        long tiempoInicio = System.currentTimeMillis();
        
        // Crear pool de ejecución con tamaño fijo de 3 hilos
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        System.out.println("=== Iniciando ejecución de 10 tareas con pool de tamaño 3 ===\n");
        
        // Crear y encolar 10 tareas
        for (int i = 1; i <= 10; i++) {
            Runnable tarea = new TareaCalculo(i);
            executor.submit(tarea);
        }
        
        // Apagar el executor (no acepta más tareas nuevas)
        executor.shutdown();
        
        try {
            // Esperar a que todas las tareas terminen (máximo 5 minutos)
            if (executor.awaitTermination(5, TimeUnit.MINUTES)) {
                // Calcular tiempo total de ejecución
                long tiempoFin = System.currentTimeMillis();
                long tiempoTotal = tiempoFin - tiempoInicio;
                
                System.out.println("\n=== Todas las tareas finalizaron ===");
                System.out.println("Tiempo total de ejecución: " + tiempoTotal + " ms");
                System.out.println("Tiempo total de ejecución: " + (tiempoTotal / 1000.0) + " segundos");
            } else {
                System.out.println("El tiempo de espera expiró");
            }
        } catch (InterruptedException e) {
            System.err.println("La ejecución fue interrumpida: " + e.getMessage());
            executor.shutdownNow();
        }
    }
    
    // Clase que implementa Runnable para ejecutar el cálculo
    static class TareaCalculo implements Runnable {
        private int numeroTarea;
        
        public TareaCalculo(int numeroTarea) {
            this.numeroTarea = numeroTarea;
        }
        
        @Override
        public void run() {
            System.out.println("Iniciando tarea " + numeroTarea + " en " + Thread.currentThread().getName());
            
            // Realizar el cálculo pesado
            SumRootN(numeroTarea);
            
            System.out.println("Finalizó tarea " + numeroTarea + " en " + Thread.currentThread().getName());
        }
        
        // Método de cálculo pesado según el enunciado
        public void SumRootN(int root) {
            double result = 0;
            for (int i = 1; i < 10000000; i++) { // Comienza en 1 para evitar log(0)
                result += Math.exp(Math.log(i) / root);
            }
            System.out.println("Resultado tarea " + root + ": " + result);
        }
    }
}