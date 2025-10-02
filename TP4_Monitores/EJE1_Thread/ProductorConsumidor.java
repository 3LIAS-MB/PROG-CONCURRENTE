package EJE1_Thread;

import java.util.Random;

//	1) Implemente, mediante Hilos heredando de Thread, el problema del Productor y 
//	Consumidor empleando una lista de elementos (para 10 procesos productores y 10 
//	procesos consumidores). La lista tendrá el comportamiento de una cola, es decir, se 
//	manejará mediante el esquema FIFO. Los productores y consumidores serán lanzados de 
//	manera aleatoria, es esperable que ambos procesos posean velocidades distintas, para el 
//	caso de que los productores sean más lentos que los consumidores, provocará que los 
//	consumidores se encolen a la espera de elementos a consumir. En el caso contrario podría 
//	provocar que el contenedor de productos se llene. Los productores y consumidores serán 
//	lanzados  a intervalos entre 100ms – 200ms.  
//	a. Realice la implementación para una cola infinita. Teniendo en cuenta que un 
//	productor es más lento que un consumidor y demora en producir un nuevo 
//	elemento entre 1000ms y 1500ms, en cambio un consumidor demora en consumir 
//	un elemento entre 400ms y 800ms. 
//	b. Realice la implementación para una cola de tamaño 5 que debe definir antes de 
//	correr el programa. Si en un momento dado, la cantidad de elementos a producir 
//	va a ser mayor a 5, el productor deberá esperar hasta que exista espacio, es decir, 
//	hasta que algún elemento sea consumido. Para este caso los productores son más 
//	rápidos que los consumidores. Un productor demora entre 400ms y 800ms para 
//	producir un nuevo elemento, mientras que un consumidor demora entre 1000ms y 
//	1500ms para consumir un elemento. Impleméntelo para un número infinito de 
//	productores y consumidores. 
//	c. Para ambos casos intercambie las velocidades de ambos procesos y comente los 
//	resultados obtenidos. 

public class ProductorConsumidor {
    public static void main(String[] args) {
        System.out.println("=== SIMULACIÓN PRODUCTOR-CONSUMIDOR ===");
        
        // Ejecutar caso a) Cola infinita
        System.out.println("\n--- Caso a) Cola Infinita ---");
//        ejecutarCasoA();
        
        // Pequeña pausa entre casos
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Ejecutar caso b) Cola de tamaño 5
        System.out.println("\n--- Caso b) Cola Tamaño 5 ---");
        ejecutarCasoB();
    }
    
    private static void ejecutarCasoA() {
        ColaInfinita cola = new ColaInfinita();
        ejecutarSimulacion(cola, 5, 5, "Caso A");
    }
    
    private static void ejecutarCasoB() {
        ColaTamanioFijo cola = new ColaTamanioFijo(5);
        ejecutarSimulacion(cola, 5, 5, "Caso B");
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