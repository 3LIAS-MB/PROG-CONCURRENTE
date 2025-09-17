package UNIDAD3;

/**
 * DEMOSTRACIÓN: HERENCIA DE LA CLASE THREAD
 * 
 * Objetivo: Mostrar la primera forma de crear hilos en Java mediante herencia.
 * 
 * ¿Qué enseña?
 * - Cómo extender la clase Thread para crear hilos personalizados
 * - La sobreescritura del método run() donde va el código a ejecutar concurrentemente
 * - Uso del método start() para iniciar la ejecución del hilo
 * - El método join() para esperar la finalización de hilos
 * 
 * Ventajas: Simple de implementar para casos básicos
 * Desventajas: Limita la herencia (Java no permite herencia múltiple)
 */

public class _01_ThreadInheritance extends Thread {
    private String nombre;
    private int iteraciones;

    public _01_ThreadInheritance(String nombre, int iteraciones) {
        this.nombre = nombre;
        this.iteraciones = iteraciones;
    }

    @Override
    public void run() {
        System.out.println(nombre + " inició ejecución");
        
        for (int i = 1; i <= iteraciones; i++) {
            System.out.println(nombre + " - Iteración: " + i);
            
            try {
                // Simula trabajo
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(nombre + " fue interrumpido");
                return;
            }
        }
        
        System.out.println(nombre + " terminó ejecución");
    }

    public static void main(String[] args) {
        System.out.println("=== HERENCIA DE THREAD ===");
        
        _01_ThreadInheritance hilo1 = new _01_ThreadInheritance("Hilo-A", 5);
        _01_ThreadInheritance hilo2 = new _01_ThreadInheritance("Hilo-B", 5);
        
        // Iniciar ejecución de hilos
        hilo1.start();
        hilo2.start();
        
        try {
            // Esperar a que ambos hilos terminen
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Programa principal terminó");
    }
}