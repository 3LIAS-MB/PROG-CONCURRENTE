package UNIDAD3;

/**
 * DEMOSTRACIÓN: IMPLEMENTACIÓN DE LA INTERFAZ RUNNABLE
 * 
 * Objetivo: Mostrar la segunda y más flexible forma de crear hilos.
 * 
 * ¿Qué enseña?
 * - Implementación de la interfaz Runnable en lugar de heredar de Thread
 * - Separación entre la tarea (Runnable) y el hilo ejecutor (Thread)
 * - Creación de objetos Thread pasando instancias Runnable como parámetro
 * - Mayor flexibilidad: la clase puede heredar de otras clases
 * 
 * Ventajas: Mejor diseño orientado a objetos, más flexible
 * Recomendación: Forma preferida para la mayoría de casos
 */

public class _02_RunnableImplementation implements Runnable {
    private String nombre;
    private int iteraciones;

    public _02_RunnableImplementation(String nombre, int iteraciones) {
        this.nombre = nombre;
        this.iteraciones = iteraciones;
    }

    @Override
    public void run() {
        System.out.println(nombre + " inició ejecución (Runnable)");
        
        for (int i = 1; i <= iteraciones; i++) {
            System.out.println(nombre + " - Iteración: " + i);
            
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                System.out.println(nombre + " fue interrumpido");
                return;
            }
        }
        
        System.out.println(nombre + " terminó ejecución (Runnable)");
    }

    public static void main(String[] args) {
        System.out.println("=== IMPLEMENTACIÓN DE RUNNABLE ===");
        
        _02_RunnableImplementation tarea1 = new _02_RunnableImplementation("Tarea-X", 4);
        _02_RunnableImplementation tarea2 = new _02_RunnableImplementation("Tarea-Y", 4);
        
        Thread hilo1 = new Thread(tarea1);
        Thread hilo2 = new Thread(tarea2);
        
        hilo1.start();
        hilo2.start();
        
        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Programa principal terminó");
    }
}