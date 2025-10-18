package EJE2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class CircuitoMrOlympia {
    
    // Semáforos para controlar la capacidad de cada pista
    private static Semaphore pista1 = new Semaphore(20); // Máximo 20 reclutas
    private static Semaphore pista2 = new Semaphore(10); // Máximo 10 reclutas
    
    private static Random random = new Random();
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Circuito de Entrenamiento Mr. Olympia ===");
        System.out.println("Pista 1: Capacidad 20 reclutas");
        System.out.println("Pista 2: Capacidad 10 reclutas");
        System.out.println("Total de aspirantes: 200\n");
        
        Thread[] reclutas = new Thread[200];
        
        // Crear los 200 reclutas
        for (int i = 1; i <= 200; i++) {
            reclutas[i - 1] = new Thread(new Recluta(i));
            reclutas[i - 1].start();
            
            // Pequeña pausa para que no todos lleguen exactamente al mismo tiempo
            Thread.sleep(10);
        }
        
        // Esperar a que todos terminen
        for (int i = 0; i < 200; i++) {
            reclutas[i].join();
        }
        
        System.out.println("\n=== Todos los reclutas terminaron el circuito ===");
    }
    
    static class Recluta implements Runnable {
        private int numero;
        
        public Recluta(int numero) {
            this.numero = numero;
        }
        
        @Override
        public void run() {
            try {
                // ===== INGRESO - Esperar lugar en Pista 1 =====
                System.out.println("Recluta " + numero + " esperando en el Ingreso...");
                pista1.acquire(); // Espera hasta que haya lugar en Pista 1
                
                // ===== PISTA 1 =====
                System.out.println("Recluta " + numero + " INGRESÓ a Pista 1");
                
                // Tiempo de entrenamiento en Pista 1: 1100-1300ms
                int tiempoPista1 = 1100 + random.nextInt(201);
                Thread.sleep(tiempoPista1);
                
                System.out.println("Recluta " + numero + " SALIÓ de Pista 1");
                pista1.release(); // Libera lugar en Pista 1
                
                // ===== PASILLO =====
                System.out.println("Recluta " + numero + " está en el PASILLO (esperando Pista 2)");
                
                // ===== PISTA 2 =====
                pista2.acquire(); // Espera hasta que haya lugar en Pista 2
                System.out.println("Recluta " + numero + " INGRESÓ a Pista 2");
                
                // Tiempo de entrenamiento en Pista 2: 600-800ms
                int tiempoPista2 = 600 + random.nextInt(201);
                Thread.sleep(tiempoPista2);
                
                System.out.println("Recluta " + numero + " SALIÓ de Pista 2");
                pista2.release(); // Libera lugar en Pista 2
                
                // ===== EGRESO =====
                System.out.println("Recluta " + numero + " FINALIZÓ la sesión de entrenamiento (EGRESO)");
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


// ============================================================
// VERSIÓN ALTERNATIVA: Con Thread heredado
// ============================================================

class CircuitoMrOlympia_Thread {
    
    private static Semaphore pista1 = new Semaphore(20);
    private static Semaphore pista2 = new Semaphore(10);
    private static Random random = new Random();
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Circuito de Entrenamiento Mr. Olympia ===");
        System.out.println("Pista 1: Capacidad 20 reclutas");
        System.out.println("Pista 2: Capacidad 10 reclutas");
        System.out.println("Total de aspirantes: 200\n");
        
        Thread[] reclutas = new Thread[200];
        
        // Crear los 200 reclutas
        for (int i = 1; i <= 200; i++) {
            reclutas[i - 1] = new ReclutaThread(i);
            reclutas[i - 1].start();
            Thread.sleep(10);
        }
        
        // Esperar a que todos terminen
        for (int i = 0; i < 200; i++) {
            reclutas[i].join();
        }
        
        System.out.println("\n=== Todos los reclutas terminaron el circuito ===");
    }
    
    static class ReclutaThread extends Thread {
        private int numero;
        
        public ReclutaThread(int numero) {
            this.numero = numero;
        }
        
        @Override
        public void run() {
            try {
                // INGRESO - Esperar lugar en Pista 1
                System.out.println("Recluta " + numero + " esperando en el Ingreso...");
                pista1.acquire();
                
                // PISTA 1
                System.out.println("Recluta " + numero + " INGRESÓ a Pista 1");
                int tiempoPista1 = 1100 + random.nextInt(201);
                Thread.sleep(tiempoPista1);
                System.out.println("Recluta " + numero + " SALIÓ de Pista 1");
                pista1.release();
                
                // PASILLO
                System.out.println("Recluta " + numero + " está en el PASILLO (esperando Pista 2)");
                
                // PISTA 2
                pista2.acquire();
                System.out.println("Recluta " + numero + " INGRESÓ a Pista 2");
                int tiempoPista2 = 600 + random.nextInt(201);
                Thread.sleep(tiempoPista2);
                System.out.println("Recluta " + numero + " SALIÓ de Pista 2");
                pista2.release();
                
                // EGRESO
                System.out.println("Recluta " + numero + " FINALIZÓ la sesión de entrenamiento (EGRESO)");
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}