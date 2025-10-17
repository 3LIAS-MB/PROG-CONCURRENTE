package EJE6;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class DeliveryPedidos {
    private static final Random RND = new Random();
    private static final AtomicInteger contadorPedidos = new AtomicInteger(0);

    // Recurso Compartido: Las existencias de productos (protegidas por el monitor del objeto)
    private static int stockMD = 0;
    private static int stockP = 0;
    private static final Object LOCK = new Object(); // Objeto para el monitor/bloqueo

    public static void main(String[] args) {
        ExecutorService productoraService = Executors.newFixedThreadPool(2);
        ExecutorService clienteService = Executors.newCachedThreadPool();

        System.out.println("--- Delivery PedidosAhoritaYa ---");
        
        // Productores (ejecución simultánea e independiente)
        productoraService.execute(new Productor("MD", 100, 300));
        productoraService.execute(new Productor("P", 100, 300));

        // Clientes (llegada constante)
        clienteService.execute(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int pedidoId = contadorPedidos.incrementAndGet();
                    boolean pideMD = RND.nextBoolean();
                    boolean pideP = RND.nextBoolean();
                    
                    // Si el cliente no pide nada, le obligamos a pedir al menos uno
                    if (!pideMD && !pideP) pideMD = true; 

                    clienteService.submit(new Delivery(pedidoId, pideMD, pideP));
                    
                    // Pedidos llegan entre 150-250ms
                    Thread.sleep(RND.nextInt(101) + 150); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Opcional: Cerrar servicios tras un tiempo
    }

    // Productor: Crea stock de MD o P
    static class Productor implements Runnable {
        private final String producto;
        private final int minTime, maxTime;

        public Productor(String producto, int minTime, int maxTime) {
            this.producto = producto;
            this.minTime = minTime;
            this.maxTime = maxTime;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    // Simular tiempo de producción
                    Thread.sleep(RND.nextInt(maxTime - minTime + 1) + minTime); 

                    synchronized (LOCK) {
                        if (producto.equals("MD")) {
                            stockMD++;
                        } else {
                            stockP++;
                        }
                        System.out.printf("[PROD-%s] ✅ Produjo. Stock MD: %d, P: %d.\n", producto, stockMD, stockP);
                        LOCK.notifyAll(); // Notificar a los hilos de Delivery que esperaban
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Consumidor: Recibe pedidos y espera el stock
    static class Delivery implements Runnable {
        private final int id;
        private final boolean pideMD;
        private final boolean pideP;

        public Delivery(int id, boolean pideMD, boolean pideP) {
            this.id = id;
            this.pideMD = pideMD;
            this.pideP = pideP;
        }

        @Override
        public void run() {
            String pedidoStr = (pideMD ? "Hamb. (MD)" : "") + (pideMD && pideP ? " y " : "") + (pideP ? "Postre (P)" : "");
            System.out.printf("[PEDIDO %d] 🛒 LLEGÓ. Cliente pide: %s.\n", id, pedidoStr);

            try {
                synchronized (LOCK) {
                    // La persona del delivery debe esperar si no hay stock suficiente
                    while ((pideMD && stockMD == 0) || (pideP && stockP == 0)) {
                        System.out.printf("[PEDIDO %d] ⏱️ Delivery espera stock. MD: %d, P: %d.\n", id, stockMD, stockP);
                        LOCK.wait(); // Libera el bloqueo y espera a ser notificado
                    }

                    // Stock suficiente, retirar los productos
                    if (pideMD) stockMD--;
                    if (pideP) stockP--;
                    
                    // Tiempo de retiro y entrega (despreciable)
                    
                    System.out.printf("[PEDIDO %d] 🟢 COMPLETO. Entrega %s. Stock Restante MD: %d, P: %d.\n", 
                                       id, pedidoStr, stockMD, stockP);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}