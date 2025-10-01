package EJE1_Thread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ColaInfinita implements Cola {
    private final Queue<Integer> cola = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition hayElementos = lock.newCondition();
    private final Random random = new Random();
    
    @Override
    public void producir(int producto, int idProductor) throws InterruptedException {
        // Simular tiempo de producción (1000-1500ms)
        Thread.sleep(random.nextInt(501) + 1000);
        
        lock.lock();
        try {
            cola.offer(producto);
            System.out.printf("[Caso A] Productor %d produjo: %d. Cola: %d elementos%n", 
                             idProductor, producto, cola.size());
            hayElementos.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public int consumir(int idConsumidor) throws InterruptedException {
        lock.lock();
        try {
            while (cola.isEmpty()) {
                System.out.printf("[Caso A] Consumidor %d esperando... Cola vacía%n", idConsumidor);
                hayElementos.await();
            }
            
            int producto = cola.poll();
            
            // Simular tiempo de consumo (400-800ms) después de obtener el producto
            Thread.sleep(random.nextInt(401) + 400);
            
            System.out.printf("[Caso A] Consumidor %d consumió: %d. Cola: %d elementos%n", 
                             idConsumidor, producto, cola.size());
            return producto;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public int getTamanio() {
        return cola.size();
    }
}