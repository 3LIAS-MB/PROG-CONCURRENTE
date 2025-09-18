package UNIDAD4;

/**
 * UNIDAD 4 - PATRÓN PRODUCTOR-CONSUMIDOR
 * 
 * Objetivo: Implementar el clásico patrón productor-consumidor usando wait()/notify().
 * 
 * ¿Qué enseña?
 * - Coordinación entre múltiples productores y consumidores
 * - Uso de condiciones de espera (lista vacía/llena)
 * - Manejo de recursos compartidos con capacidad limitada
 * - Prevención de deadlocks y livelocks
 */

import java.util.LinkedList;
import java.util.Queue;

public class _04_ProducerConsumer {

    static class Buffer {
        private Queue<Integer> cola = new LinkedList<>();
        private final int CAPACIDAD;
        private int contador = 0;
        
        public Buffer(int capacidad) {
            this.CAPACIDAD = capacidad;
        }
        
        public synchronized void producir() throws InterruptedException {
            while (cola.size() == CAPACIDAD) {
                System.out.println("Buffer lleno. Productor " + Thread.currentThread().getName() + " esperando...");
                wait();
            }
            
            int valor = ++contador;
            cola.add(valor);
            System.out.println("Producido: " + valor + " | Buffer: " + cola.size() + "/" + CAPACIDAD);
            notifyAll();
        }
        
        public synchronized void consumir() throws InterruptedException {
            while (cola.isEmpty()) {
                System.out.println("Buffer vacío. Consumidor " + Thread.currentThread().getName() + " esperando...");
                wait();
            }
            
            int valor = cola.remove();
            System.out.println("Consumido: " + valor + " | Buffer: " + cola.size() + "/" + CAPACIDAD);
            notifyAll();
        }
    }

    static class Productor extends Thread {
        private Buffer buffer;
        
        public Productor(String nombre, Buffer buffer) {
            super(nombre);
            this.buffer = buffer;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    buffer.producir();
                    Thread.sleep((long)(Math.random() * 500));
                }
            } catch (InterruptedException e) {
                System.out.println(getName() + " interrumpido");
            }
        }
    }

    static class Consumidor extends Thread {
        private Buffer buffer;
        
        public Consumidor(String nombre, Buffer buffer) {
            super(nombre);
            this.buffer = buffer;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    buffer.consumir();
                    Thread.sleep((long)(Math.random() * 800));
                }
            } catch (InterruptedException e) {
                System.out.println(getName() + " interrumpido");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PATRÓN PRODUCTOR-CONSUMIDOR ===");
        
        Buffer buffer = new Buffer(3);
        
        // Crear múltiples productores y consumidores
        Productor[] productores = {
            new Productor("Productor-1", buffer),
            new Productor("Productor-2", buffer)
        };
        
        Consumidor[] consumidores = {
            new Consumidor("Consumidor-1", buffer),
            new Consumidor("Consumidor-2", buffer),
            new Consumidor("Consumidor-3", buffer)
        };
        
        // Iniciar todos los hilos
        for (Productor p : productores) p.start();
        for (Consumidor c : consumidores) c.start();
        
        // Esperar a que terminen
        try {
            for (Productor p : productores) p.join();
            for (Consumidor c : consumidores) c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Simulación completada");
    }
}