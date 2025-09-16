package HILOS;

public class ProductorConsumidor {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(5);
        
        Thread productor = new Thread(new Productor(buffer), "Productor");
        Thread consumidor = new Thread(new Consumidor(buffer), "Consumidor");
        
        productor.start();
        consumidor.start();
    }
}

class Buffer {
    private int[] datos;
    private int tamaño;
    private int contador = 0;
    private int indiceProductor = 0;
    private int indiceConsumidor = 0;
    
    public Buffer(int tamaño) {
        this.tamaño = tamaño;
        datos = new int[tamaño];
    }
    
    public synchronized void producir(int valor) throws InterruptedException {
        while (contador == tamaño) {
            System.out.println("Buffer lleno, productor espera...");
            wait(); // Espera si el buffer está lleno
        }
        
        datos[indiceProductor] = valor;
        indiceProductor = (indiceProductor + 1) % tamaño;
        contador++;
        
        System.out.println("Producido: " + valor + " | Elementos: " + contador);
        notifyAll(); // Notifica a los consumidores
    }
    
    public synchronized int consumir() throws InterruptedException {
        while (contador == 0) {
            System.out.println("Buffer vacío, consumidor espera...");
            wait(); // Espera si el buffer está vacío
        }
        
        int valor = datos[indiceConsumidor];
        indiceConsumidor = (indiceConsumidor + 1) % tamaño;
        contador--;
        
        System.out.println("Consumido: " + valor + " | Elementos: " + contador);
        notifyAll(); // Notifica a los productores
        
        return valor;
    }
}

class Productor implements Runnable {
    private Buffer buffer;
    
    public Productor(Buffer buffer) {
        this.buffer = buffer;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                buffer.producir(i);
                Thread.sleep((int)(Math.random() * 1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumidor implements Runnable {
    private Buffer buffer;
    
    public Consumidor(Buffer buffer) {
        this.buffer = buffer;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                buffer.consumir();
                Thread.sleep((int)(Math.random() * 1500));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}