package EJE7;

import java.util.Random;

public class LlenadoVector {
    private static final int TAMANIO_VECTOR = 100;
    // Vector compartido por todos los hilos
    private static final int[] VEC = new int[TAMANIO_VECTOR];
    private static final Random RND = new Random();

    // Hilo que llena una posición específica del vector
    static class HiloLlenador extends Thread {
        private final int indice;
        private final int valorImpar;

        public HiloLlenador(int indice) {
            this.indice = indice;
            // El n-ésimo número impar (empezando por el 1) es 2n + 1
            this.valorImpar = 2 * indice + 1; 
        }

        @Override
        public void run() {
            try {
                // Simulación de demora (50-100ms)
                Thread.sleep(RND.nextInt(51) + 50); 
                
                // Llenar la posición correspondiente.
                VEC[indice] = valorImpar;
                System.out.printf("[%s] Llenó VEC[%d] = %d.\n", 
                                  Thread.currentThread().getName(), indice, valorImpar);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Thread[] hilos = new Thread[TAMANIO_VECTOR];
        
        System.out.println("--- Llenado de Vector Iniciado con 100 Hilos ---");

        // 1. Crear e iniciar los 100 hilos
        for (int i = 0; i < TAMANIO_VECTOR; i++) {
            hilos[i] = new HiloLlenador(i);
            hilos[i].start();
        }

        // 2. Esperar a que TODOS los hilos finalicen (usando join)
        System.out.println("\nMain: Esperando a que los 100 hilos terminen...");
        for (int i = 0; i < TAMANIO_VECTOR; i++) {
            try {
                hilos[i].join(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 3. Mostrar el vector completo
        System.out.println("\n--- Vector Finalizado y Llenado ---");
        for (int i = 0; i < TAMANIO_VECTOR; i++) {
            System.out.printf("VEC[%d] = %d\n", i, VEC[i]);
        }
        System.out.println("--- Fin del Programa ---");
    }
}