package HILOS;

public class RaceConditionExample {
    public static void main(String[] args) throws InterruptedException {
        Contador contador = new Contador();
        
        Thread[] hilos = new Thread[100];
        
        // Crear 100 hilos que incrementan el contador
        for (int i = 0; i < 100; i++) {
            hilos[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    contador.incrementar();
                }
            });
            hilos[i].start();
        }
        
        // Esperar a todos los hilos
        for (Thread hilo : hilos) {
            hilo.join();
        }
        
        System.out.println("Valor final (debería ser 100000): " + contador.getValor());
    }
}

class Contador {
    private int valor = 0;
    
    // ❌ SIN sincronización - produce condición de carrera
    public void incrementar() {
        valor++;
    }
    
    // ✅ CON sincronización - solución correcta
    public synchronized void incrementarSeguro() {
        valor++;
    }
    
    public int getValor() {
        return valor;
    }
}