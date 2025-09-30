package EJE1_Thread;


import java.util.Random;


public class ProductorConsumidor {
    public static void main(String[] args) {
        System.out.println("=== SIMULACIÓN PRODUCTOR-CONSUMIDOR ===");
        
        // Ejecutar caso a) Cola infinita
        System.out.println("\n--- Caso a) Cola Infinita ---");
        ejecutarCasoA();
        
        // Pequeña pausa entre casos
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Ejecutar caso b) Cola de tamaño 5
        System.out.println("\n--- Caso b) Cola Tamaño 5 ---");
        ejecutarCasoB();
    }
    
    private static void ejecutarCasoA() {
        ColaInfinita cola = new ColaInfinita();
        ejecutarSimulacion(cola, 10, 10, "Caso A");
    }
    
    private static void ejecutarCasoB() {
        ColaTamanioFijo cola = new ColaTamanioFijo(5);
        ejecutarSimulacion(cola, 10, 10, "Caso B");
    }
    
    private static void ejecutarSimulacion(Cola cola, int numProductores, int numConsumidores, String caso) {
        Thread[] productores = new Thread[numProductores];
        Thread[] consumidores = new Thread[numConsumidores];
        
        // Crear productores
        for (int i = 0; i < numProductores; i++) {
            productores[i] = new Productor(cola, i + 1, caso);
        }
        
        // Crear consumidores
        for (int i = 0; i < numConsumidores; i++) {
            consumidores[i] = new Consumidor(cola, i + 1, caso);
        }
        
        // Iniciar productores con delays aleatorios
        Random random = new Random();
        for (Thread productor : productores) {
            try {
                Thread.sleep(random.nextInt(101) + 100); // 100-200ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            productor.start();
        }
        
        // Iniciar consumidores con delays aleatorios
        for (Thread consumidor : consumidores) {
            try {
                Thread.sleep(random.nextInt(101) + 100); // 100-200ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            consumidor.start();
        }
        
        // Esperar a que todos terminen
        try {
            for (Thread productor : productores) productor.join();
            for (Thread consumidor : consumidores) consumidor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}