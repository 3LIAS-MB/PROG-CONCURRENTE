package UNIDAD3;

/**
 * DEMOSTRACIÓN: MULTIPROCESAMIENTO Y RENDIMIENTO
 * 
 * Objetivo: Comparar ejecución secuencial vs concurrente y mostrar beneficios.
 * 
 * ¿Qué enseña?
 * - Cómo aprovechar múltiples núcleos del procesador
 * - Comparación de tiempos entre ejecución secuencial y concurrente
 * - Identificación de tareas adecuadas para paralelización
 * - Uso de Runtime.availableProcessors() para detectar hardware disponible
 * - Potencial mejora de rendimiento en operaciones intensivas de CPU
 * 
 * Importante: No todas las tareas se benefician de la concurrencia
 */

public class _07_MultiprocessingExample {
    
    static class TareaPesada implements Runnable {
        private String nombre;
        private int calculos;
        
        public TareaPesada(String nombre, int calculos) {
            this.nombre = nombre;
            this.calculos = calculos;
        }
        
        @Override
        public void run() {
            long inicio = System.currentTimeMillis();
            System.out.println(nombre + " inició en hilo: " + Thread.currentThread().getName());
            
            // Simula trabajo intensivo de CPU
            double resultado = 0;
            for (int i = 0; i < calculos; i++) {
                resultado += Math.sin(i) * Math.cos(i);
            }
            
            long fin = System.currentTimeMillis();
            System.out.println(nombre + " terminó en " + (fin - inicio) + "ms. Resultado: " + resultado);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== MULTIPROCESAMIENTO EN JAVA ===");
        System.out.println("Número de procesadores disponibles: " + Runtime.getRuntime().availableProcessors());
        
        // Ejecución secuencial
        System.out.println("\n--- EJECUCIÓN SECUENCIAL ---");
        long inicioSecuencial = System.currentTimeMillis();
        
        TareaPesada tarea1 = new TareaPesada("Tarea-Secuencial-1", 10000000);
        TareaPesada tarea2 = new TareaPesada("Tarea-Secuencial-2", 10000000);
        TareaPesada tarea3 = new TareaPesada("Tarea-Secuencial-3", 10000000);
        
        tarea1.run();
        tarea2.run();
        tarea3.run();
        
        long finSecuencial = System.currentTimeMillis();
        System.out.println("Tiempo secuencial: " + (finSecuencial - inicioSecuencial) + "ms");
        
        // Pequeña pausa
        Thread.sleep(1000);
        
        // Ejecución concurrente
        System.out.println("\n--- EJECUCIÓN CONCURRENTE ---");
        long inicioConcurrente = System.currentTimeMillis();
        
        Thread hilo1 = new Thread(new TareaPesada("Tarea-Concurrente-1", 10000000));
        Thread hilo2 = new Thread(new TareaPesada("Tarea-Concurrente-2", 10000000));
        Thread hilo3 = new Thread(new TareaPesada("Tarea-Concurrente-3", 10000000));
        
        hilo1.start();
        hilo2.start();
        hilo3.start();
        
        hilo1.join();
        hilo2.join();
        hilo3.join();
        
        long finConcurrente = System.currentTimeMillis();
        System.out.println("Tiempo concurrente: " + (finConcurrente - inicioConcurrente) + "ms");
        
        System.out.println("\nMejora de rendimiento: " + 
            ((finSecuencial - inicioSecuencial) - (finConcurrente - inicioConcurrente)) + "ms más rápido");
    }
}