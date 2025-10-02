package EJE1_Thread_2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

//1) Implemente, mediante Hilos heredando de Thread, el problema del Productor y 
//Consumidor empleando una lista de elementos (para 10 procesos productores y 10 
//procesos consumidores). La lista tendrá el comportamiento de una cola, es decir, se 
//manejará mediante el esquema FIFO. Los productores y consumidores serán lanzados de 
//manera aleatoria, es esperable que ambos procesos posean velocidades distintas, para el 
//caso de que los productores sean más lentos que los consumidores, provocará que los 
//consumidores se encolen a la espera de elementos a consumir. En el caso contrario podría 
//provocar que el contenedor de productos se llene. Los productores y consumidores serán 
//lanzados  a intervalos entre 100ms – 200ms.  

//a. Realice la implementación para una cola infinita. Teniendo en cuenta que un 
//productor es más lento que un consumidor y demora en producir un nuevo 
//elemento entre 1000ms y 1500ms, en cambio un consumidor demora en consumir 
//un elemento entre 400ms y 800ms. 

//b. Realice la implementación para una cola de tamaño 5 que debe definir antes de 
//correr el programa. Si en un momento dado, la cantidad de elementos a producir 
//va a ser mayor a 5, el productor deberá esperar hasta que exista espacio, es decir, 
//hasta que algún elemento sea consumido. Para este caso los productores son más 
//rápidos que los consumidores. Un productor demora entre 400ms y 800ms para 
//producir un nuevo elemento, mientras que un consumidor demora entre 1000ms y 
//1500ms para consumir un elemento. Impleméntelo para un número infinito de 
//productores y consumidores. 

//c. Para ambos casos intercambie las velocidades de ambos procesos y comente los 
//resultados obtenidos. 

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