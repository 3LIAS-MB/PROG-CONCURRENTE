package EJE1;

import java.util.Random;

class Consumidor extends Thread {
    private final Cola cola;
    private final int id;
    private final String caso;
    private final Random random = new Random();
    
    public Consumidor(Cola cola, int id, String caso) {
        this.cola = cola;
        this.id = id;
        this.caso = caso;
        this.setName("Consumidor-" + id + "-" + caso);
    }
    
    @Override
    public void run() {
        try {
            // Consumir 5 elementos por consumidor
            for (int i = 0; i < 5; i++) {
                cola.consumir(id);
                
                // Pequeña pausa entre consumos
                Thread.sleep(random.nextInt(51) + 50);
            }
            System.out.printf("[%s] Consumidor %d terminó de consumir%n", caso, id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf("[%s] Consumidor %d interrumpido%n", caso, id);
        }
    }
}