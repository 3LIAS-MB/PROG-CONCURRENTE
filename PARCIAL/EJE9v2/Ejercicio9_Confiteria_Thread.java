package EJE9v2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Ejercicio9_Confiteria_Thread {
    
    private static Random random = new Random();
    private static int contadorClientes = 0;
    private static Semaphore menuListoSemaphore = new Semaphore(0); // Para sincronizar menú listo
    
    public static void main(String[] args) {
        // Generar clientes indefinidamente
        while (true) {
            try {
                Thread.sleep(1000 + random.nextInt(501)); // Entre 1000-1500ms
                
                int numCliente = obtenerNumeroCliente();
                Thread cliente = new ClienteThread(numCliente);
                cliente.start();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Método synchronized para obtener número de cliente
    private static synchronized int obtenerNumeroCliente() {
        contadorClientes++;
        return contadorClientes;
    }
    
    static class ClienteThread extends Thread {
        private int numCliente;
        private String tipoPedido;
        private int tiempoElaboracion;
        
        public ClienteThread(int numCliente) {
            this.numCliente = numCliente;
            // Decidir tipo de pedido
            if (random.nextBoolean()) {
                tipoPedido = "Menú simple";
                tiempoElaboracion = 300 + random.nextInt(201); // 300-500ms
            } else {
                tipoPedido = "Menú con postre";
                tiempoElaboracion = 400 + random.nextInt(201); // 400-600ms
            }
        }
        
        @Override
        public void run() {
            try {
                System.out.println("Cliente " + numCliente + " llegó y pidió: " + tipoPedido);
                
                // Preparar el menú (en otro hilo implícito o simulado aquí)
                Thread preparacion = new Thread(() -> {
                    try {
                        Thread.sleep(tiempoElaboracion);
                        System.out.println("Cliente " + numCliente + " - Su " + tipoPedido + " está listo");
                        menuListoSemaphore.release(); // Señalizar que está listo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                preparacion.start();
                
                // Cliente espera si el menú no está listo
                System.out.println("Cliente " + numCliente + " está esperando su pedido");
                menuListoSemaphore.acquire(); // Espera hasta que esté listo
                
                // Tiempo de atención (retirar el pedido)
                int tiempoAtencion = 100 + random.nextInt(301); // 100-400ms
                System.out.println("Cliente " + numCliente + " está siendo atendido");
                Thread.sleep(tiempoAtencion);
                
                System.out.println("Cliente " + numCliente + " se retira con su " + tipoPedido);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}