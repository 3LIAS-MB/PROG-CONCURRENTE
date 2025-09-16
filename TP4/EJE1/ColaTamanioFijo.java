package EJE1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ColaTamanioFijo implements Cola {
    private final Queue<Integer> cola = new LinkedList<>();
    private final int capacidad;
    private final Lock lock = new ReentrantLock();
    private final Condition hayElementos = lock.newCondition();
    private final Condition hayEspacio = lock.newCondition();
    private final Random random = new Random();
    
    public ColaTamanioFijo(int capacidad) {
        this.capacidad = capacidad;
    }
    
    @Override
    public void producir(int producto, int idProductor) throws InterruptedException {
        lock.lock();
        try {
            while (cola.size() >= capacidad) {
                System.out.printf("[Caso B] Productor %d esperando... Cola llena (%d/%d)%n", 
                                 idProductor, cola.size(), capacidad);
                hayEspacio.await();
            }
            
            cola.offer(producto);
            System.out.printf("[Caso B] Productor %d produjo: %d. Cola: %d/%d%n", 
                             idProductor, producto, cola.size(), capacidad);
            hayElementos.signalAll();
        } finally {
            lock.unlock();
        }
        
        // Simular tiempo de producción (400-800ms) después de agregar a cola
        Thread.sleep(random.nextInt(401) + 400);
    }
    
    @Override
    public int consumir(int idConsumidor) throws InterruptedException {
        lock.lock();
        try {
            while (cola.isEmpty()) {
                System.out.printf("[Caso B] Consumidor %d esperando... Cola vacía%n", idConsumidor);
                hayElementos.await();
            }
            
            int producto = cola.poll();
            hayEspacio.signalAll();
            
            System.out.printf("[Caso B] Consumidor %d consumió: %d. Cola: %d/%d%n", 
                             idConsumidor, producto, cola.size(), capacidad);
            return producto;
        } finally {
            lock.unlock();
        }
        
        // Simular tiempo de consumo (1000-1500ms) después de obtener el producto
//        Thread.sleep(random.nextInt(501) + 1000);
    }
    
    @Override
    public int getTamanio() {
        return cola.size();
    }
}