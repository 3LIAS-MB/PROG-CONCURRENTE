package EJE3;

import java.util.Random;

public class ContadorConRetardo {
    private int V = 100;
    private Random random = new Random();
    
    // Hilo que incrementa con retardo
    class HiloIncrementador extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                V++;
                try {
                    // Retardo aleatorio entre 50-150ms
                    Thread.sleep(random.nextInt(101) + 50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // Hilo que decrementa con retardo
    class HiloDecrementador extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                V--;
                try {
                    // Retardo aleatorio entre 50-150ms
                    Thread.sleep(random.nextInt(101) + 50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void ejecutar() {
        HiloIncrementador hiloInc = new HiloIncrementador();
        HiloDecrementador hiloDec = new HiloDecrementador();
        
        hiloInc.start();
        hiloDec.start();
        
        // Usar join para esperar que ambos hilos terminen
        try {
            hiloInc.join();
            hiloDec.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Valor final de V: " + V);
    }
    
    public static void main(String[] args) {
        System.out.println("=== PUNTO 3 - CONTADOR CON RETARDO ALEATORIO ===");
        System.out.println("Valor inicial de V: 100");
        System.out.println("Retardo: 50-150ms");
        
        ContadorConRetardo contador = new ContadorConRetardo();
        contador.ejecutar();
        
        System.out.println("\nNota: El resultado puede variar debido a condiciones de carrera");
        System.out.println("ya que los accesos a la variable V no están sincronizados.");
    }
}