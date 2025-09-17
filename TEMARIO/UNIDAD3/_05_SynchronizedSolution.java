package UNIDAD3;

/**
 * DEMOSTRACIÓN: SOLUCIÓN CON SINCRONIZACIÓN
 * 
 * Objetivo: Mostrar cómo resolver problemas de concurrencia con synchronized.
 * 
 * ¿Qué enseña?
 * - Uso de la palabra clave synchronized para métodos
 * - Cómo synchronized crea exclusion mutua (mutex)
 * - Garantía de que solo un hilo ejecuta la sección crítica a la vez
 * - Mantenimiento de la consistencia de datos compartidos
 * - Overhead de la sincronización: puede afectar rendimiento
 * 
 * Importante: Synchronized es la solución básica para exclusión mutua en Java
 */

public class _05_SynchronizedSolution {
    
    // Recurso compartido con sincronización
    static class ContadorSincronizado {
        private int valor = 0;
        
        // Método sincronizado - solo un hilo puede ejecutarlo a la vez
        public synchronized void incrementar() {
            int temp = valor;
            try {
                Thread.sleep(10); // Simula procesamiento
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            valor = temp + 1;
        }
        
        public synchronized int getValor() {
            return valor;
        }
    }
    
    static class HiloIncrementadorSincronizado extends Thread {
        private ContadorSincronizado contador;
        private String nombre;
        private int incrementos;
        
        public HiloIncrementadorSincronizado(String nombre, ContadorSincronizado contador, int incrementos) {
            this.nombre = nombre;
            this.contador = contador;
            this.incrementos = incrementos;
        }
        
        @Override
        public void run() {
            for (int i = 0; i < incrementos; i++) {
                contador.incrementar();
                System.out.println(nombre + " incrementó. Valor actual: " + contador.getValor());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== SOLUCIÓN CON SINCRONIZACIÓN ===");
        System.out.println("Con sincronización - Resultado consistente esperado");
        
        ContadorSincronizado contador = new ContadorSincronizado();
        
        HiloIncrementadorSincronizado hilo1 = new HiloIncrementadorSincronizado("Hilo-A", contador, 5);
        HiloIncrementadorSincronizado hilo2 = new HiloIncrementadorSincronizado("Hilo-B", contador, 5);
        
        hilo1.start();
        hilo2.start();
        
        hilo1.join();
        hilo2.join();
        
        System.out.println("\nValor final esperado: 10");
        System.out.println("Valor final obtenido: " + contador.getValor());
        System.out.println("¡Sincronización exitosa!");
    }
}