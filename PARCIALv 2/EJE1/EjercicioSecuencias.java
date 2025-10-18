package EJE1;

import java.util.concurrent.Semaphore;

public class EjercicioSecuencias {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Secuencia A: ABCABCABCABC ===");
        ejecutarSecuenciaA();
        
        Thread.sleep(2000); // Pausa entre secuencias
        System.out.println("\n\n=== Secuencia B: BCABCABCABCA ===");
        ejecutarSecuenciaB();
    }
    
    // ==================== PARTE A: ABCABCABCABC ====================
    private static void ejecutarSecuenciaA() throws InterruptedException {
        // Semáforos para controlar el orden
        // Inicialmente solo A puede ejecutar (1 permiso)
        Semaphore semA = new Semaphore(1);
        Semaphore semB = new Semaphore(0);
        Semaphore semC = new Semaphore(0);
        
        // Crear los 3 hilos
        Thread hiloA = new Thread(new ProcesoA(semA, semB));
        Thread hiloB = new Thread(new ProcesoB(semB, semC));
        Thread hiloC = new Thread(new ProcesoC(semC, semA));
        
        hiloA.start();
        hiloB.start();
        hiloC.start();
        
        // Esperar a que terminen
        hiloA.join();
        hiloB.join();
        hiloC.join();
    }
    
    static class ProcesoA implements Runnable {
        private Semaphore miSemaforo;
        private Semaphore siguienteSemaforo;
        
        public ProcesoA(Semaphore miSem, Semaphore sigSem) {
            this.miSemaforo = miSem;
            this.siguienteSemaforo = sigSem;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) { // 5 repeticiones de ABC
                    miSemaforo.acquire();     // Espera su turno
                    System.out.print("A");
                    siguienteSemaforo.release(); // Libera a B
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class ProcesoB implements Runnable {
        private Semaphore miSemaforo;
        private Semaphore siguienteSemaforo;
        
        public ProcesoB(Semaphore miSem, Semaphore sigSem) {
            this.miSemaforo = miSem;
            this.siguienteSemaforo = sigSem;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    miSemaforo.acquire();     // Espera su turno
                    System.out.print("B");
                    siguienteSemaforo.release(); // Libera a C
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class ProcesoC implements Runnable {
        private Semaphore miSemaforo;
        private Semaphore siguienteSemaforo;
        
        public ProcesoC(Semaphore miSem, Semaphore sigSem) {
            this.miSemaforo = miSem;
            this.siguienteSemaforo = sigSem;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    miSemaforo.acquire();     // Espera su turno
                    System.out.print("C");
                    siguienteSemaforo.release(); // Libera a A
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // ==================== PARTE B: BCABCABCABCA ====================
    private static void ejecutarSecuenciaB() throws InterruptedException {
        // Semáforos para controlar el orden
        // Inicialmente solo B puede ejecutar (1 permiso)
        Semaphore semA = new Semaphore(0);
        Semaphore semB = new Semaphore(1);
        Semaphore semC = new Semaphore(0);
        
        // Crear los 3 hilos
        Thread hiloA = new Thread(new ProcesoA_SecB(semA, semB));
        Thread hiloB = new Thread(new ProcesoB_SecB(semB, semC));
        Thread hiloC = new Thread(new ProcesoC_SecB(semC, semA));
        
        hiloA.start();
        hiloB.start();
        hiloC.start();
        
        // Esperar a que terminen
        hiloA.join();
        hiloB.join();
        hiloC.join();
    }
    
    static class ProcesoA_SecB implements Runnable {
        private Semaphore miSemaforo;
        private Semaphore siguienteSemaforo;
        
        public ProcesoA_SecB(Semaphore miSem, Semaphore sigSem) {
            this.miSemaforo = miSem;
            this.siguienteSemaforo = sigSem;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) { // 5 repeticiones
                    miSemaforo.acquire();     // Espera su turno
                    System.out.print("A");
                    siguienteSemaforo.release(); // Libera a B
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class ProcesoB_SecB implements Runnable {
        private Semaphore miSemaforo;
        private Semaphore siguienteSemaforo;
        
        public ProcesoB_SecB(Semaphore miSem, Semaphore sigSem) {
            this.miSemaforo = miSem;
            this.siguienteSemaforo = sigSem;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 6; i++) { // 6 repeticiones (B aparece 6 veces)
                    miSemaforo.acquire();     // Espera su turno
                    System.out.print("B");
                    siguienteSemaforo.release(); // Libera a C
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class ProcesoC_SecB implements Runnable {
        private Semaphore miSemaforo;
        private Semaphore siguienteSemaforo;
        
        public ProcesoC_SecB(Semaphore miSem, Semaphore sigSem) {
            this.miSemaforo = miSem;
            this.siguienteSemaforo = sigSem;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    miSemaforo.acquire();     // Espera su turno
                    System.out.print("C");
                    siguienteSemaforo.release(); // Libera a A
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}