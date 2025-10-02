package EJE4_Thread;

import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

//	4) Un supermercado posee 3 cajas de atención y 15 carritos para que los clientes realicen las 
//	compras. Cuando los 15 carritos están ocupados, los clientes esperan afuera del 
//	supermercado a que se desocupe uno de estos carritos y así poder entrar al supermercado 
//	a comprar. Cada cliente demora en realizar sus compras un tiempo aleatorio entre 4”-7” y 
//	cada cajero demora en atender a cada cliente un tiempo aleatorio entre 2”- 4”. Debe 
//	mostrar un mensaje indicando que el “Cliente X entró al Súper y tomó un carrito”, otro 
//	mensaje cuando el “Cliente X está comprando”, otro mensaje cuando “Cliente X está 
//	pagando en la caja” y un último mensaje cuando “Cliente X abandona el Súper”. Los 
//	clientes llegan de forma indefinida al supermercado en un tiempo aleatorio entre 300ms y 
//	500ms.

class Cliente extends Thread {
    private int numeroCliente;
    private Semaphore carritos;
    private Semaphore cajas;
    private Random random;
    private static AtomicInteger contadorClientes = new AtomicInteger(0);
    
    public Cliente(Semaphore carritos, Semaphore cajas) {
        this.numeroCliente = contadorClientes.incrementAndGet();
        this.carritos = carritos;
        this.cajas = cajas;
        this.random = new Random();
    }
    
    @Override
    public void run() {
        try {
            // 1. Esperar para obtener un carrito
            System.out.println("Cliente " + numeroCliente + " está esperando un carrito...");
            carritos.acquire();
            System.out.println("Cliente " + numeroCliente + " entró al Súper y tomó un carrito");
            
            // 2. Realizar las compras (4-7 segundos)
            System.out.println("Cliente " + numeroCliente + " está comprando");
            int tiempoCompra = 4000 + random.nextInt(3000);
            Thread.sleep(tiempoCompra);
            
            // 3. Ir a pagar a una caja (2-4 segundos)
            cajas.acquire();
            System.out.println("Cliente " + numeroCliente + " está pagando en la caja");
            int tiempoCaja = 2000 + random.nextInt(2000);
            Thread.sleep(tiempoCaja);
            cajas.release();
            
            // 4. Abandonar el supermercado y devolver el carrito
            System.out.println("Cliente " + numeroCliente + " abandona el Súper");
            carritos.release();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Supermercado {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    SIMULACIÓN DE SUPERMERCADO");
        System.out.println("========================================");
        System.out.println("Carritos: 15 | Cajas: 3");
        System.out.println("Clientes llegan cada 300-500ms\n");
        
        // Semáforos
        Semaphore carritos = new Semaphore(15);  // 15 carritos disponibles
        Semaphore cajas = new Semaphore(3);       // 3 cajas de atención
        
        Random random = new Random();
        
        // Generador de clientes indefinido
        Thread generadorClientes = new Thread(() -> {
            while (true) {
                try {
                    // Crear y lanzar un nuevo cliente
                    Cliente cliente = new Cliente(carritos, cajas);
                    cliente.start();
                    
                    // Esperar antes de que llegue el siguiente cliente (300-500ms)
                    int tiempoEspera = 300 + random.nextInt(200);
                    Thread.sleep(tiempoEspera);
                    
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        
        generadorClientes.setDaemon(true); // Hilo demonio para que termine con el programa
        generadorClientes.start();
        
        // Mantener el programa corriendo (puedes cambiar el tiempo o usar un Scanner)
        try {
            System.out.println(">>> La simulación se ejecutará durante 60 segundos...\n");
            Thread.sleep(60000); // 60 segundos de simulación
            System.out.println("\n========================================");
            System.out.println("    SIMULACIÓN FINALIZADA");
            System.out.println("========================================");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}