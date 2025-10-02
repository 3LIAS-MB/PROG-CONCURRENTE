package EJE2_Thread;

import java.util.concurrent.Semaphore;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//	2) Implemente, mediante Hilos heredando de Thread, un programa que emplee un semáforo 
//	general inicializado en 3 y simplemente cada hilo duerma por 5”. Debe indicar el 
//	momento antes de empezar a dormir y cuando deja de dormir. Observar el orden de 
//	ejecución de los hilos pasando por parámetro el nombre del mismo. Debe lanzar 10 hilos.  

//	a. En el primer intento genere una clase llamada “Semaforo” y defina e inicialice el 
//	semáforo de control dentro de dicha clase. 

//	b. En el segundo intento defina el semáforo en el main y pase por parámetro a la 
//	clase “Semaforo” dicho objeto de control. 

// ========== INTENTO A: Semáforo dentro de la clase ==========
class SemaforoA extends Thread {
    private String nombre;
    private static Semaphore semaforo = new Semaphore(3); // Semáforo compartido inicializado en 3
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public SemaforoA(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void run() {
        try {
            semaforo.acquire();
            System.out.println("[" + LocalTime.now().format(formatter) + "] " + nombre + " - Inicio de dormir");
            Thread.sleep(5000); // Duerme 5 segundos
            System.out.println("[" + LocalTime.now().format(formatter) + "] " + nombre + " - Fin de dormir");
            semaforo.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// ========== INTENTO B: Semáforo pasado por parámetro ==========
class SemaforoB extends Thread {
    private String nombre;
    private Semaphore semaforo;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public SemaforoB(String nombre, Semaphore semaforo) {
        this.nombre = nombre;
        this.semaforo = semaforo;
    }
    
    @Override
    public void run() {
        try {
            semaforo.acquire();
            System.out.println("[" + LocalTime.now().format(formatter) + "] " + nombre + " - Inicio de dormir");
            Thread.sleep(5000); // Duerme 5 segundos
            System.out.println("[" + LocalTime.now().format(formatter) + "] " + nombre + " - Fin de dormir");
            semaforo.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class EjercicioSemaforoGeneral {
    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("INTENTO A: Semáforo dentro de clase");
        System.out.println("====================================\n");
        
        // Lanzar 10 hilos del tipo A
        SemaforoA[] hilosA = new SemaforoA[10];

        for (int i = 0; i < 10; i++) {
            hilosA[i] = new SemaforoA("Hilo-A-" + (i + 1));
            hilosA[i].start();
        }
        
        // Esperar a que terminen todos los hilos A
        for (int i = 0; i < 10; i++) {
            try {
                hilosA[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\n====================================");
        System.out.println("INTENTO B: Semáforo por parámetro");
        System.out.println("====================================\n");
        
        // Crear semáforo en el main
        Semaphore semaforoB = new Semaphore(3);
        
        // Lanzar 10 hilos del tipo B pasando el semáforo
        SemaforoB[] hilosB = new SemaforoB[10];
        for (int i = 0; i < 10; i++) {
            hilosB[i] = new SemaforoB("Hilo-B-" + (i + 1), semaforoB);
            hilosB[i].start();
        }
        
        // Esperar a que terminen todos los hilos B
        for (int i = 0; i < 10; i++) {
            try {
                hilosB[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\n=== SIMULACIÓN FINALIZADA ===");
    }
}