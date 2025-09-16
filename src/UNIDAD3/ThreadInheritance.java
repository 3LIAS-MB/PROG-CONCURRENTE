package UNIDAD3;

public class ThreadInheritance extends Thread {
    private String nombre;
    private int iteraciones;

    public ThreadInheritance(String nombre, int iteraciones) {
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
        
        ThreadInheritance hilo1 = new ThreadInheritance("Hilo-A", 5);
        ThreadInheritance hilo2 = new ThreadInheritance("Hilo-B", 5);
        
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