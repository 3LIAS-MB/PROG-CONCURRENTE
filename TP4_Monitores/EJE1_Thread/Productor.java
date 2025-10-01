package EJE1_Thread;

import java.util.Random;

class Productor extends Thread {
    private final Cola cola;
    private final int id;
    private final String caso;
    private int contador = 0;
    private final Random random = new Random();
    
    public Productor(Cola cola, int id, String caso) {
        this.cola = cola;
        this.id = id;
        this.caso = caso;
        this.setName("Productor-" + id + "-" + caso);
    }
    
    @Override
    public void run() {
        try {
            // Producir 5 elementos por productor
            for (int i = 0; i < 5; i++) {
                int producto = ++contador;
                cola.producir(producto, id);
                
                // Pequeña pausa entre producciones
                Thread.sleep(random.nextInt(51) + 50);
            }
            System.out.printf("[%s] Productor %d terminó de producir%n", caso, id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf("[%s] Productor %d interrumpido%n", caso, id);
        }
    }
}