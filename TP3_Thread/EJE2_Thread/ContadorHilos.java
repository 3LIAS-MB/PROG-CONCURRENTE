package EJE2_Thread;

public class ContadorHilos {
    private int V = 100;
    
    // Hilo que incrementa
    class HiloIncrementador extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                V++;
            }
        }
    }
    
    // Hilo que decrementa
    class HiloDecrementador extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                V--;
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
        System.out.println("=== PUNTO 2 - CONTADOR CON HILOS ===");
        System.out.println("Valor inicial de V: 100");
        
        ContadorHilos contador = new ContadorHilos();
        contador.ejecutar();
    }
}