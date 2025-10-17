package EJE3;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class AeropuertoLondres {
    private static final Random RND = new Random();
    private static final AtomicInteger contadorPasajeros = new AtomicInteger(0);

    // Definición de Terminales y sus Boleterías (recursos limitados)
    private enum Terminal {
        T3(4), T4(5), T5(3);
        
        final Semaphore boleterias;
        final AtomicInteger pasajerosRecibidos = new AtomicInteger(0);

        Terminal(int numBoleterias) {
            this.boleterias = new Semaphore(numBoleterias);
        }
    }

    public static void main(String[] args) {
        ExecutorService taxiService = Executors.newCachedThreadPool();
        System.out.println("--- Aeropuerto Internacional de Londres: Operando ---");
        
        // Ciclo indefinido de llegada de taxis (gestión del tiempo de llegada 120-180ms)
        taxiService.execute(() -> {
            try {
                int taxiId = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    taxiId++;
                    Terminal terminalDestino = Terminal.values()[RND.nextInt(Terminal.values().length)];
                    int numPasajeros = RND.nextInt(4) + 1; // 1 a 4 pasajeros
                    
                    taxiService.submit(new TareaTaxi(taxiId, terminalDestino, numPasajeros));
                    
                    // Tiempo de llegada entre 120-180ms
                    Thread.sleep(RND.nextInt(61) + 120); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Opcional: Para detener después de un tiempo, se debe llamar a taxiService.shutdown()
    }

    static class TareaPasajero implements Runnable {
        private final int id;
        private final Terminal terminal;

        public TareaPasajero(int id, Terminal terminal) {
            this.id = id;
            this.terminal = terminal;
        }

        @Override
        public void run() {
            System.out.printf("[P%d-%s] ✅ Llegó a la Terminal.\n", id, terminal.name());
            terminal.pasajerosRecibidos.incrementAndGet();

            try {
                System.out.printf("[P%d-%s] 🚶 Se dirige al check-in. Boleterías Libres: %d/%d.\n", 
                                  id, terminal.name(), terminal.boleterias.availablePermits(), terminal.boleterias.getQueueLength() + terminal.boleterias.availablePermits());
                
                // Adquirir un permiso (boletería)
                terminal.boleterias.acquire();
                
                System.out.printf("[P%d-%s] 🛃 INICIA check-in. Usando una boletería. Libres: %d.\n", 
                                  id, terminal.name(), terminal.boleterias.availablePermits());
                
                // Demora en el check-in (180-280ms)
                Thread.sleep(RND.nextInt(101) + 180); 
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                terminal.boleterias.release(); // Liberar la boletería
                System.out.printf("[P%d-%s] 🟢 FINALIZA check-in. Total recibidos: %d.\n", 
                                  id, terminal.name(), terminal.pasajerosRecibidos.get());
            }
        }
    }

    static class TareaTaxi implements Runnable {
        private final int id;
        private final Terminal terminal;
        private final int numPasajeros;

        public TareaTaxi(int id, Terminal terminal, int numPasajeros) {
            this.id = id;
            this.terminal = terminal;
            this.numPasajeros = numPasajeros;
        }

        @Override
        public void run() {
            System.out.printf("--- Taxi %d llegó a %s con %d pasajeros. ---\n", id, terminal.name(), numPasajeros);
            
            // Cada pasajero se convierte en una tarea (hilo) individual
            for (int i = 0; i < numPasajeros; i++) {
                int pasajeroId = contadorPasajeros.incrementAndGet();
                new Thread(new TareaPasajero(pasajeroId, terminal), "Pasajero-" + pasajeroId).start();
            }
        }
    }
}