package EJE1.Gemini;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

// Clase del buffer (la cola compartida)
class Buffer {
    private Queue<Integer> lista = new LinkedList<>();

    // Método para producir (añadir) un elemento
    public synchronized void producir(int elemento) {
        lista.add(elemento);
        System.out.println("Productor produjo: " + elemento + ". Tamaño del buffer: " + lista.size());
        notifyAll(); // Notifica a los consumidores que hay un nuevo elemento
    }

    // Método para consumir (sacar) un elemento
    public synchronized int consumir() throws InterruptedException {
        while (lista.isEmpty()) {
            System.out.println("Consumidor esperando, buffer vacío.");
            wait(); // Espera si la cola está vacía
        }
        int elemento = lista.poll();
        System.out.println("Consumidor consumió: " + elemento + ". Tamaño del buffer: " + lista.size());
        return elemento;
    }
}

// Hilo productor
class Productor extends Thread {
    private Buffer buffer;
    private Random rand = new Random();

    public Productor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int elemento = rand.nextInt(100);
                buffer.producir(elemento);
                // Retardo del productor: 1000ms a 1500ms
                Thread.sleep(1000 + rand.nextInt(501)); 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Hilo consumidor
class Consumidor extends Thread {
    private Buffer buffer;
    private Random rand = new Random();

    public Consumidor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.consumir();
                // Retardo del consumidor: 400ms a 800ms
                Thread.sleep(400 + rand.nextInt(401)); 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase principal
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Buffer buffer = new Buffer();
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            new Productor(buffer).start();
            Thread.sleep(100 + rand.nextInt(101)); // Intervalo de lanzamiento
        }
        
        for (int i = 0; i < 10; i++) {
            new Consumidor(buffer).start();
            Thread.sleep(100 + rand.nextInt(101)); // Intervalo de lanzamiento
        }
    }
}