package EJE2_Runnable;

import java.util.concurrent.ThreadLocalRandom;

//	2) Imagine el escenario de una panadería que produce Bizcochos y Facturas que son 
//	colocados en un mostrador, cada cliente se lleva un Bizcocho y una Factura, si los 
//	productos aún no están producidos, los clientes esperan. La producción de elementos y la 
//	compra son indefinidas (una panadería que trabaja las 24hs sin descansar recibiendo en 
//	todo momento clientes compradores). Además hay que destacar que los Bizcochos y 
//	Facturas se producen de a uno por vez, puesto que existen dos hornos muy pequeños y 
//	solo permiten la producción de un elemento de cada tipo en cualquier momento pero 
//	simultáneamente. Simule la situación indicando en cada momento todo lo que sucede en 
//	la Panadería: producción de un Bizcocho, producción de una Factura, cliente comprando, 
//	cliente esperando, cliente retirándose del local y mostrador vacío. Tenga en cuenta que el 
//	tiempo de producción de un Bizcocho es entre 400ms y 600ms, el de una Factura es entre 
//	1000ms y 1300ms, y la llegada de los clientes se produce entre 800ms y 1500ms, la 
//	compra y la retirada de los clientes del local demanda un tiempo entre 200ms y 400ms. 
//	Debe identificar a cada cliente. 

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