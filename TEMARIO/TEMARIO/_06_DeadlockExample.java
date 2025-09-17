package TEMARIO;

public class _06_DeadlockExample {
    public static void main(String[] args) {
        final Object recursoA = new Object();
        final Object recursoB = new Object();
        
        Thread hilo1 = new Thread(() -> {
            synchronized (recursoA) {
                System.out.println("Hilo1: Obtuvo recursoA");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                
                synchronized (recursoB) {
                    System.out.println("Hilo1: Obtuvo recursoB");
                }
            }
        });
        
        Thread hilo2 = new Thread(() -> {
            synchronized (recursoB) {
                System.out.println("Hilo2: Obtuvo recursoB");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                
                synchronized (recursoA) {
                    System.out.println("Hilo2: Obtuvo recursoA");
                }
            }
        });
        
        hilo1.start();
        hilo2.start();
        
        // Esperar un tiempo y verificar deadlock
        try {
            Thread.sleep(2000);
            System.out.println("¿Hilo1 vivo? " + hilo1.isAlive());
            System.out.println("¿Hilo2 vivo? " + hilo2.isAlive());
            
            if (hilo1.isAlive() && hilo2.isAlive()) {
                System.out.println("¡DEADLOCK detectado!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}