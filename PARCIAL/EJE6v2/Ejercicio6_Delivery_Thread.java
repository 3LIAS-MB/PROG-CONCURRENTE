package EJE6v2;

import java.util.Random;

public class Ejercicio6_Delivery_Thread {
    
    private static BufferAlimentos buffer = new BufferAlimentos();
    private static Random random = new Random();
    private static int contadorPedidos = 0;
    
    public static void main(String[] args) {
        // Iniciar productor de hamburguesas
        Thread productorMD = new ProductorThread("MD");
        productorMD.start();
        
        // Iniciar productor de postres
        Thread productorP = new ProductorThread("P");
        productorP.start();
        
        // Generar pedidos de clientes indefinidamente
        while (true) {
            try {
                Thread.sleep(150 + random.nextInt(101)); // Entre 150-250ms
                
                int numPedido = obtenerNumeroPedido();
                Thread pedido = new PedidoThread(numPedido);
                pedido.start();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Método synchronized para obtener número de pedido
    private static synchronized int obtenerNumeroPedido() {
        contadorPedidos++;
        return contadorPedidos;
    }
    
    // Clase Buffer con monitores (wait/notify)
    static class BufferAlimentos {
        private int hamburguesasDisponibles = 0;
        private int postresDisponibles = 0;
        
        // Productor agrega hamburguesa
        public synchronized void producirHamburguesa() {
            hamburguesasDisponibles++;
            System.out.println("MD produjo una hamburguesa. Disponibles: " + hamburguesasDisponibles);
            notifyAll(); // Notificar a los que esperan
        }
        
        // Productor agrega postre
        public synchronized void producirPostre() {
            postresDisponibles++;
            System.out.println("P produjo un postre. Disponibles: " + postresDisponibles);
            notifyAll(); // Notificar a los que esperan
        }
        
        // Consumidor retira hamburguesa
        public synchronized void retirarHamburguesa() throws InterruptedException {
            while (hamburguesasDisponibles == 0) {
                wait(); // Espera hasta que haya hamburguesas
            }
            hamburguesasDisponibles--;
        }
        
        // Consumidor retira postre
        public synchronized void retirarPostre() throws InterruptedException {
            while (postresDisponibles == 0) {
                wait(); // Espera hasta que haya postres
            }
            postresDisponibles--;
        }
    }
    
    // Thread productor
    static class ProductorThread extends Thread {
        private String tipo;
        
        public ProductorThread(String tipo) {
            this.tipo = tipo;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100 + random.nextInt(201)); // Entre 100-300ms
                    
                    if (tipo.equals("MD")) {
                        buffer.producirHamburguesa();
                    } else {
                        buffer.producirPostre();
                    }
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // Thread pedido (cliente + delivery)
    static class PedidoThread extends Thread {
        private int numPedido;
        private String tipoPedido;
        
        public PedidoThread(int numPedido) {
            this.numPedido = numPedido;
            // Elegir tipo de pedido aleatoriamente
            int tipo = random.nextInt(3);
            if (tipo == 0) {
                tipoPedido = "MD+P";
            } else if (tipo == 1) {
                tipoPedido = "MD";
            } else {
                tipoPedido = "P";
            }
        }
        
        @Override
        public void run() {
            try {
                System.out.println("Pedido " + numPedido + " llegó: " + tipoPedido);
                
                // Retirar según el tipo de pedido
                if (tipoPedido.equals("MD+P")) {
                    buffer.retirarHamburguesa();
                    buffer.retirarPostre();
                } else if (tipoPedido.equals("MD")) {
                    buffer.retirarHamburguesa();
                } else {
                    buffer.retirarPostre();
                }
                
                System.out.println("Pedido " + numPedido + " (" + tipoPedido + ") COMPLETADO");
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}