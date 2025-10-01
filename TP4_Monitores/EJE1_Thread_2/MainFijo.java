package EJE1_Thread_2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

// Clase del buffer (la cola compartida)
class BufferFijo {
    private final int MAX_SIZE = 5;
    private Queue<Integer> lista = new LinkedList<>();

    // Método para producir (añadir) un elemento
    public synchronized void producir(int elemento) throws InterruptedException {
        while (lista.size() == MAX_SIZE) {
            System.out.println("Productor esperando, buffer lleno.");
            wait(); // Espera si la cola está llena
        }
        lista.add(elemento);
        System.out.println("Productor produjo: " + elemento + ". Tamaño del buffer: " + lista.size());
        notifyAll(); // Notifica a los consumidores y otros productores
    }

    // Método para consumir (sacar) un elemento
    public synchronized int consumir() throws InterruptedException {
        while (lista.isEmpty()) {
            System.out.println("Consumidor esperando, buffer vacío.");
            wait(); // Espera si la cola está vacía
        }
        int elemento = lista.poll();
        System.out.println("Consumidor consumió: " + elemento + ". Tamaño del buffer: " + lista.size());
        notifyAll(); // Notifica a los productores que hay espacio
        return elemento;
    }
}

// Hilo productor
class ProductorFijo extends Thread {
    private BufferFijo buffer;
    private Random rand = new Random();

    public ProductorFijo(BufferFijo buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int elemento = rand.nextInt(100);
                buffer.producir(elemento);
                // Retardo del productor: 400ms a 800ms
                Thread.sleep(400 + rand.nextInt(401)); 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Hilo consumidor
class ConsumidorFijo extends Thread {
    private BufferFijo buffer;
    private Random rand = new Random();

    public ConsumidorFijo(BufferFijo buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.consumir();
                // Retardo del consumidor: 1000ms a 1500ms
                Thread.sleep(1000 + rand.nextInt(501)); 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase principal
public class MainFijo {
    public static void main(String[] args) throws InterruptedException {
        BufferFijo buffer = new BufferFijo();
        Random rand = new Random();

        // Lanzamiento de un número infinito de productores y consumidores
        for (int i = 0; i < 10; i++) {
            new ProductorFijo(buffer).start();
            new ConsumidorFijo(buffer).start();
            Thread.sleep(100 + rand.nextInt(101));
        }
    }
}