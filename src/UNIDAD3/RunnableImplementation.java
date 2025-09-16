package UNIDAD3;

public class RunnableImplementation implements Runnable {
    private String nombre;
    private int iteraciones;

    public RunnableImplementation(String nombre, int iteraciones) {
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
        
        RunnableImplementation tarea1 = new RunnableImplementation("Tarea-X", 4);
        RunnableImplementation tarea2 = new RunnableImplementation("Tarea-Y", 4);
        
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