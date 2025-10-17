package EJE5;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LavaderoMegaLimpito {
    private static final Random RND = new Random();
    private static final AtomicInteger contadorAutos = new AtomicInteger(0);
    
    // Recurso Compartido Sincronizado: Saldo (protegido por un AtomicLong)
    private static final AtomicLong saldoCuenta = new AtomicLong(0);

    // Semáforo 1: Controla la capacidad del lavadero (4 autos lavando simultáneamente)
    private static final Semaphore SEM_LAVADO = new Semaphore(4); 

    // Semáforo 2: Controla el espacio en el galpón (capacidad 20)
    // El 'tryAcquire' se usará para ver si hay lugar sin bloquear.
    private static final Semaphore SEM_GALPON = new Semaphore(20); 

    public static void main(String[] args) {
        ExecutorService autoArrivalService = Executors.newCachedThreadPool();
        
        System.out.println("--- Lavadero MegaLimpito Iniciado (Capac. Lavado: 4, Capac. Galpón: 20) ---");
        
        // Ciclo indefinido de llegada de autos
        autoArrivalService.execute(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int autoId = contadorAutos.incrementAndGet();
                    new Thread(new Auto(autoId), "Auto-" + autoId).start();
                    
                    // Autos llegan entre 50-100ms
                    Thread.sleep(RND.nextInt(51) + 50); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Simulación: Permitir que corra por un tiempo limitado
        try {
            Thread.sleep(5000); 
            autoArrivalService.shutdownNow();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\n*** Lavadero Cerrado. Saldo Final: $ " + saldoCuenta.get() + " ***");
    }

    static class Auto implements Runnable {
        private final int id;

        public Auto(int id) { this.id = id; }

        @Override
        public void run() {
            String nombre = Thread.currentThread().getName();
            System.out.printf("[%s] 🚗 Llega al lavadero.\n", nombre);

            try {
                // Intenta ingresar al galpón. Si no hay lugar, no bloquea (espera afuera)
                if (SEM_GALPON.tryAcquire()) {
                    System.out.printf("[%s] 🅿️ Ingresa al galpón. Espacios libres: %d.\n", nombre, SEM_GALPON.availablePermits());
                    
                    // Esperar a que haya un puesto de lavado libre
                    System.out.printf("[%s] ⏱️ Espera puesto de lavado. Puestos libres: %d.\n", nombre, SEM_LAVADO.availablePermits());
                    SEM_LAVADO.acquire(); 
                    
                    System.out.printf("[%s] 💦 INICIA LAVADO. Puestos libres: %d.\n", nombre, SEM_LAVADO.availablePermits());
                    
                    // Tiempo de lavado (150-300ms)
                    Thread.sleep(RND.nextInt(151) + 150); 
                    
                    long nuevoSaldo = saldoCuenta.addAndGet(500); // Actualiza el saldo (operación atómica)
                    
                    SEM_LAVADO.release(); // Libera el puesto de lavado
                    System.out.printf("[%s] 🌟 FINALIZA LAVADO. Saldo: $ %d.\n", nombre, nuevoSaldo);
                    
                    SEM_GALPON.release(); // Libera el espacio en el galpón
                    System.out.printf("[%s] ➡️ Se retira. Espacios libres en galpón: %d.\n", nombre, SEM_GALPON.availablePermits());

                } else {
                    System.out.printf("[%s] ❌ NO HAY LUGAR en el galpón. Espera afuera (Se retira en este modelo simple).\n", nombre);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}