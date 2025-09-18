package UNIDAD4;

/**
 * UNIDAD 4 - LISTA SINCRONIZADA CON wait()/notify()
 * 
 * Objetivo: Implementar una lista sincronizada que use wait() y notify()
 * para coordinar el acceso entre productores y consumidores.
 * 
 * ¿Qué enseña?
 * - Sincronización de estructuras de datos complejas
 * - Uso de wait() cuando la lista está vacía o llena
 * - Uso de notify()/notifyAll() cuando cambia el estado de la lista
 * - Prevención de condiciones de carrera en estructuras compartidas
 */

import java.util.LinkedList;
import java.util.Queue;

public class _03_SynchronizedList {

    static class ListaSincronizada {
        private Queue<String> lista = new LinkedList<>();
        private final int CAPACIDAD_MAXIMA = 3;
        
        public synchronized void agregar(String elemento) throws InterruptedException {
            while (lista.size() == CAPACIDAD_MAXIMA) {
                System.out.println("Lista llena. Productor esperando...");
                wait(); // Espera si la lista está llena
            }
            
            lista.add(elemento);
            System.out.println("Agregado: " + elemento + " | Tamaño: " + lista.size());
            notifyAll(); // Notifica a todos los consumidores
        }
        
        public synchronized String remover() throws InterruptedException {
            while (lista.isEmpty()) {
                System.out.println("Lista vacía. Consumidor esperando...");
                wait(); // Espera si la lista está vacía
            }
            
            String elemento = lista.remove();
            System.out.println("Removido: " + elemento + " | Tamaño: " + lista.size());
            notifyAll(); // Notifica a todos los productores
            return elemento;
        }
        
        public synchronized int tamaño() {
            return lista.size();
        }
    }

    static class Productor extends Thread {
        private ListaSincronizada lista;
        private String nombre;
        
        public Productor(String nombre, ListaSincronizada lista) {
            this.nombre = nombre;
            this.lista = lista;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 1; i <= 5; i++) {
                    String elemento = nombre + "-Elemento-" + i;
                    lista.agregar(elemento);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                System.out.println(nombre + " interrumpido");
            }
        }
    }

    static class Consumidor extends Thread {
        private ListaSincronizada lista;
        private String nombre;
        
        public Consumidor(String nombre, ListaSincronizada lista) {
            this.nombre = nombre;
            this.lista = lista;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 1; i <= 5; i++) {
                    lista.remover();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println(nombre + " interrumpido");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== LISTA SINCRONIZADA ===");
        System.out.println("Capacidad máxima: 3 elementos");
        
        ListaSincronizada lista = new ListaSincronizada();
        
        Productor productor1 = new Productor("Productor1", lista);
        Productor productor2 = new Productor("Productor2", lista);
        Consumidor consumidor1 = new Consumidor("Consumidor1", lista);
        Consumidor consumidor2 = new Consumidor("Consumidor2", lista);
        
        productor1.start();
        productor2.start();
        consumidor1.start();
        consumidor2.start();
        
        try {
            productor1.join();
            productor2.join();
            consumidor1.join();
            consumidor2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Estado final de la lista: " + lista.tamaño() + " elementos");
    }
}