package UNIDAD4;

/**
 * UNIDAD 4 - INTERRUPCIÓN DE HILOS
 * 
 * Objetivo: Mostrar cómo interrumpir hilos que están en wait() o sleep().
 * 
 * ¿Qué enseña?
 * - Uso de interrupt() para despertar hilos bloqueados
 * - Manejo de InterruptedException
 * - Diferenciar entre despertar por notificación vs interrupción
 * - Buenas prácticas para limpieza de recursos al interrumpir
 */

public class _06_ThreadInterruption {

    static class RecursoConWait {
        private final Object lock = new Object();
        private boolean condicion = false;
        
        public void esperarCondicion() throws InterruptedException {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " esperando condición...");
                
                while (!condicion) {
                    lock.wait(); // Espera hasta que la condición sea true
                }
                
                System.out.println(Thread.currentThread().getName() + " condición cumplida");
            }
        }
        
        public void establecerCondicion() {
            synchronized (lock) {
                condicion = true;
                lock.notifyAll();
                System.out.println("Condición establecida - notificando a todos");
            }
        }
        
        public void reset() {
            synchronized (lock) {
                condicion = false;
            }
        }
    }

    static class HiloEsperador extends Thread {
        private RecursoConWait recurso;
        private boolean interrumpidoPorUsuario = false;
        
        public HiloEsperador(String nombre, RecursoConWait recurso) {
            super(nombre);
            this.recurso = recurso;
        }
        
        @Override
        public void run() {
            try {
                recurso.esperarCondicion();
                System.out.println(getName() + " completó su trabajo normalmente");
            } catch (InterruptedException e) {
                interrumpidoPorUsuario = true;
                System.out.println(getName() + " fue interrumpido mientras esperaba");
                // Restablecer el estado de interrupción
                Thread.currentThread().interrupt();
            } finally {
                System.out.println(getName() + " ejecutando limpieza...");
                if (interrumpidoPorUsuario) {
                    System.out.println(getName() + " liberando recursos por interrupción");
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== INTERRUPCIÓN DE HILOS ===");
        
        RecursoConWait recurso = new RecursoConWait();
        
        HiloEsperador hilo1 = new HiloEsperador("Hilo-1", recurso);
        HiloEsperador hilo2 = new HiloEsperador("Hilo-2", recurso);
        
        System.out.println("Caso 1: Interrupción antes de que se cumpla la condición");
        hilo1.start();
        hilo2.start();
        
        // Dar tiempo a que los hilos entren en wait()
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Interrumpir los hilos
        System.out.println("Interrumpiendo hilos...");
        hilo1.interrupt();
        hilo2.interrupt();
        
        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nCaso 2: Cumplir condición normalmente");
        recurso.reset();
        HiloEsperador hilo3 = new HiloEsperador("Hilo-3", recurso);
        hilo3.start();
        
        // Esperar un poco y luego cumplir la condición
        try {
            Thread.sleep(1000);
            recurso.establecerCondicion();
            hilo3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Demo de interrupción completada");
    }
}