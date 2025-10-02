package EJE1_Thread;

import java.util.concurrent.Semaphore;
import java.util.Random;

//	1) Realice un programa que simule un estacionamiento con capacidad para 20 automóviles. 
//	Dicho estacionamiento posee dos entradas y dos salidas, un automóvil permanece un 
//	tiempo y luego abandona el lugar, con lo cual tendrá que ser cuidadoso para controlar el 
//	acceso a dicho estacionamiento de manera de bloquear a los autos que deseen ingresar 
//	cuando la capacidad se ha completado. Simule la E/S de 100 automóviles. El 
//	estacionamiento inicia vacío. 

class Auto extends Thread {
    private int numeroAuto;
    private Semaphore entradas;
    private Semaphore salidas;
    private Semaphore espacios;
    private Random random;
    
    public Auto(int numeroAuto, Semaphore entradas, Semaphore salidas, Semaphore espacios) {
        this.numeroAuto = numeroAuto;
        this.entradas = entradas;
        this.salidas = salidas;
        this.espacios = espacios;
        this.random = new Random();
    }
    
    @Override
    public void run() {
        try {
            // Intentar entrar al estacionamiento
            System.out.println("Auto " + numeroAuto + " llegó al estacionamiento");
            
            // Primero usar una de las dos entradas (física)
            entradas.acquire();
            System.out.println("Auto " + numeroAuto + " está usando una ENTRADA...");
            
            // Verificar si hay espacio disponible (lógica)
            espacios.acquire();
            System.out.println("Auto " + numeroAuto + " INGRESÓ y ocupó un espacio");
            Thread.sleep(500); // Tiempo para pasar por la entrada
            entradas.release();
            
            // Permanecer estacionado (2 a 5 segundos)
            int tiempoEstacionado = 2000 + random.nextInt(3000);
            System.out.println("Auto " + numeroAuto + " está estacionado");
            Thread.sleep(tiempoEstacionado);
            
            // Salir del estacionamiento usando una de las dos salidas
            salidas.acquire();
            System.out.println("Auto " + numeroAuto + " SALIÓ por una salida");
            Thread.sleep(500); // Tiempo para pasar por la salida
            salidas.release();
            
            // Liberar el espacio
            espacios.release();
            System.out.println("Auto " + numeroAuto + " liberó su espacio");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Estacionamiento {
    public static void main(String[] args) {
        // Semáforos
        Semaphore espacios = new Semaphore(20);      // 20 espacios disponibles
        Semaphore entradas = new Semaphore(2);       // 2 entradas
        Semaphore salidas = new Semaphore(2);        // 2 salidas
        
        System.out.println("=== SIMULACIÓN DE ESTACIONAMIENTO ===");
        System.out.println("Capacidad: 20 autos | Entradas: 2 | Salidas: 2\n");
        
        // Crear y lanzar 100 autos
        Auto[] autos = new Auto[100];
        Random random = new Random();
        
        for (int i = 0; i < 100; i++) {
            autos[i] = new Auto(i + 1, entradas, salidas, espacios);
            autos[i].start();
            
            try {
                // Los autos llegan con intervalos aleatorios (100ms a 500ms)
                Thread.sleep(100 + random.nextInt(400));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Esperar a que todos los autos terminen
        for (int i = 0; i < 100; i++) {
            try {
                autos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\n=== SIMULACIÓN FINALIZADA ===");
    }
}