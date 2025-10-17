package EJE5;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Ejercicio5_Lavadero_Thread {
    
    private static Semaphore galpon = new Semaphore(20); // Capacidad para 20 autos
    private static Semaphore lavaderos = new Semaphore(4); // 4 autos simultáneos lavando
    private static AtomicInteger saldo = new AtomicInteger(0);
    private static Random random = new Random();
    private static AtomicInteger contadorAutos = new AtomicInteger(0);
    
    public static void main(String[] args) {
        // Generar autos indefinidamente
        while (true) {
            try {
                Thread.sleep(50 + random.nextInt(51)); // Entre 50-100ms
                
                int numAuto = contadorAutos.incrementAndGet();
                Thread auto = new AutoThread(numAuto);
                auto.start();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class AutoThread extends Thread {
        private int numAuto;
        
        public AutoThread(int numAuto) {
            this.numAuto = numAuto;
        }
        
        @Override
        public void run() {
            try {
                System.out.println("Auto " + numAuto + " llegó al lavadero");
                
                // Intentar entrar al galpón
                if (galpon.tryAcquire()) {
                    System.out.println("Auto " + numAuto + " ingresó al galpón");
                } else {
                    System.out.println("Auto " + numAuto + " espera afuera (galpón lleno)");
                    galpon.acquire(); // Espera hasta que haya lugar
                    System.out.println("Auto " + numAuto + " ingresó al galpón");
                }
                
                // Esperar turno para lavar
                lavaderos.acquire();
                System.out.println("Auto " + numAuto + " inicia el lavado");
                
                // Tiempo de lavado entre 150-300ms
                Thread.sleep(150 + random.nextInt(151));
                
                System.out.println("Auto " + numAuto + " finalizó el lavado");
                lavaderos.release();
                
                // Cobrar y actualizar saldo
                int nuevoSaldo = saldo.addAndGet(500);
                System.out.println("Auto " + numAuto + " pagó $500. Saldo total: $" + nuevoSaldo);
                
                // Liberar el galpón
                galpon.release();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}