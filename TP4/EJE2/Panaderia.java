package EJE2;

import java.util.concurrent.ThreadLocalRandom;

public class Panaderia {
    static class Mostrador {
        private int bizcochosDisponibles = 0;
        private int facturasDisponibles = 0;
        public synchronized void producirBizcocho() {
            bizcochosDisponibles++;
            System.out.println(">> BIZCOCHO PRODUCIDO. Stock: " + bizcochosDisponibles + " Bizcochos, ");
            notifyAll();
        }
        public synchronized void producirFactura() {
            facturasDisponibles++;
            System.out.println(">> FACTURA PRODUCIDA. Stock: "+ facturasDisponibles + " Facturas.");
            notifyAll();
        }

        public synchronized void comprarPan(String nombreCliente) throws InterruptedException {
            while (bizcochosDisponibles < 1 || facturasDisponibles < 1) {
                System.out.println("--- " + nombreCliente + " esperando... No hay suficientes productos. ---");
                wait();
            }
            bizcochosDisponibles--;
            facturasDisponibles--;
            System.out.println("El " + nombreCliente + " está comprando 1 Bizcocho y 1 Factura.");
            if (bizcochosDisponibles == 0 && facturasDisponibles == 0) {
                System.out.println("!! El mostrador ha quedado vacío. !!");
            }
        }
    }

    static class ProductorBizcochos implements Runnable {
        private final Mostrador mostrador;

        public ProductorBizcochos(Mostrador mostrador) {
            this.mostrador = mostrador;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int tiempoProduccion = ThreadLocalRandom.current().nextInt(400, 601);
                    Thread.sleep(tiempoProduccion);
                    mostrador.producirBizcocho();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class ProductorFacturas implements Runnable {
        private final Mostrador mostrador;

        public ProductorFacturas(Mostrador mostrador) {
            this.mostrador = mostrador;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int tiempoProduccion = ThreadLocalRandom.current().nextInt(1000, 1301);
                    Thread.sleep(tiempoProduccion);
                    mostrador.producirFactura();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Cliente implements Runnable {
        private final Mostrador mostrador;
        private final String nombre;

        public Cliente(Mostrador mostrador, String nombre) {
            this.mostrador = mostrador;
            this.nombre = nombre;
        }

        @Override
        public void run() {
            try {
                System.out.println("-> " + nombre + " ha llegado a la panadería.");
                mostrador.comprarPan(nombre);
                int tiempoCompra = ThreadLocalRandom.current().nextInt(200, 401);
                Thread.sleep(tiempoCompra);
                System.out.println("<- " + nombre + " se ha retirado.");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Mostrador mostrador = new Mostrador();
        Thread panaderoBizcochos = new Thread(new ProductorBizcochos(mostrador));
        panaderoBizcochos.start();
        Thread panaderoFacturas = new Thread(new ProductorFacturas(mostrador));
        panaderoFacturas.start();
        int numeroCliente = 1;
        try {
            while (true) {
                int tiempoLlegada = ThreadLocalRandom.current().nextInt(800, 1501);
                Thread.sleep(tiempoLlegada);
                String nombreCliente = "Cliente " + numeroCliente++;
                Thread hiloCliente = new Thread(new Cliente(mostrador, nombreCliente));
                hiloCliente.start();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}