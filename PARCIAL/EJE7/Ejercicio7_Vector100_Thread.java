package EJE7;

import java.util.Random;

public class Ejercicio7_Vector100_Thread {
    
    private static int[] vec = new int[100];
    private static Random random = new Random();
    
    public static void main(String[] args) throws InterruptedException {
        Thread[] hilos = new Thread[100];
        
        // Crear e iniciar 100 hilos
        for (int i = 0; i < 100; i++) {
            hilos[i] = new HiloImparThread(i);
            hilos[i].start();
        }
        
        // Esperar a que todos los hilos terminen
        for (int i = 0; i < 100; i++) {
            hilos[i].join();
        }
        
        // Mostrar el vector completo
        System.out.println("\n=== Vector Completo ===");
        for (int i = 0; i < 100; i++) {
            System.out.println("Vec[" + i + "] = " + vec[i]);
        }
    }
    
    // Clase que hereda de Thread
    static class HiloImparThread extends Thread {
        private int posicion;
        
        public HiloImparThread(int posicion) {
            this.posicion = posicion;
        }
        
        @Override
        public void run() {
            try {
                // Simular demora entre 50-100ms
                Thread.sleep(50 + random.nextInt(51));
                
                // Calcular el número impar correspondiente
                // Vec[0]=1, Vec[1]=3, Vec[2]=5, ...
                // Fórmula: posición * 2 + 1
                int numeroImpar = posicion * 2 + 1;
                
                vec[posicion] = numeroImpar;
                
                System.out.println("Hilo " + posicion + " completó: Vec[" + posicion + "] = " + numeroImpar);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}