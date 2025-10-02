package EJE2;

import java.util.concurrent.Semaphore;
import java.util.Random;

//	2) Realice un programa que simule un estacionamiento con capacidad para 20 automóviles. 
//	Dicho estacionamiento posee dos entradas y dos salidas, un automóvil permanece un 
//	tiempo y luego abandona el lugar, con lo cual tendrá que ser cuidadoso para controlar el 
//	acceso a dicho estacionamiento de manera de bloquear a los autos que deseen ingresar 
//	cuando la capacidad se ha completado. Simule la E/S de 100 automóviles. El 
//	estacionamiento inicia vacío. 

// Clase que representa el estacionamiento
class Estacionamiento {
    private final int CAPACIDAD = 20;
    private final Semaphore espacios;
    private int autosActuales;
    private final Object lock = new Object();
    
    public Estacionamiento() {
        this.espacios = new Semaphore(CAPACIDAD, true); // true para que sea justo (FIFO)
        this.autosActuales = 0;
    }
    
    public void entrar(String nombreAuto, String entrada) throws InterruptedException {
        System.out.println(nombreAuto + " esperando para entrar por " + entrada);
        
        espacios.acquire(); // Intenta adquirir un espacio
        
        synchronized(lock) {
            autosActuales++;
            System.out.println(">>> " + nombreAuto + " ENTRÓ por " + entrada + 
                             " | Autos en estacionamiento: " + autosActuales + "/" + CAPACIDAD);
        }
    }
    
    public void salir(String nombreAuto, String salida) {
        synchronized(lock) {
            autosActuales--;
            System.out.println("<<< " + nombreAuto + " SALIÓ por " + salida + 
                             " | Autos en estacionamiento: " + autosActuales + "/" + CAPACIDAD);
        }
        
        espacios.release(); // Libera un espacio
    }
    
    public int getAutosActuales() {
        synchronized(lock) {
            return autosActuales;
        }
    }
}

// Clase que representa un automóvil (Thread)
class Automovil implements Runnable {
    private final String nombre;
    private final Estacionamiento estacionamiento;
    private final String entrada;
    private final String salida;
    private final Random random;
    
    public Automovil(int id, Estacionamiento estacionamiento) {
        this.nombre = "Auto-" + id;
        this.estacionamiento = estacionamiento;
        this.random = new Random();
        
        // Asignar entrada aleatoria (Entrada 1 o Entrada 2)
        this.entrada = (random.nextInt(2) == 0) ? "Entrada-1" : "Entrada-2";
        
        // Asignar salida aleatoria (Salida 1 o Salida 2)
        this.salida = (random.nextInt(2) == 0) ? "Salida-1" : "Salida-2";
    }
    
    @Override
    public void run() {
        try {
            // El auto intenta entrar al estacionamiento
            estacionamiento.entrar(nombre, entrada);
            
            // El auto permanece estacionado por un tiempo aleatorio (1-5 segundos)
            int tiempoEstacionado = 1000 + random.nextInt(4000);
            Thread.sleep(tiempoEstacionado);
            
            // El auto sale del estacionamiento
            estacionamiento.salir(nombre, salida);
            
        } catch (InterruptedException e) {
            System.err.println(nombre + " fue interrumpido");
            Thread.currentThread().interrupt();
        }
    }
}

// Clase principal
public class SimulacionEstacionamiento {
    public static void main(String[] args) {
        System.out.println("=== SIMULACIÓN DE ESTACIONAMIENTO ===");
        System.out.println("Capacidad: 20 automóviles");
        System.out.println("Entradas: 2 | Salidas: 2");
        System.out.println("Total de autos a simular: 100");
        System.out.println("=====================================\n");
        
        Estacionamiento estacionamiento = new Estacionamiento();
        Thread[] autos = new Thread[100];
        Random random = new Random();
        
        // Crear y arrancar los 100 threads (automóviles)
        for (int i = 0; i < 100; i++) {
            autos[i] = new Thread(new Automovil(i + 1, estacionamiento));
            autos[i].start();
            
            // Pequeña pausa entre la llegada de cada auto (0-500ms)
            try {
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Esperar a que todos los threads terminen
        for (int i = 0; i < 100; i++) {
            try {
                autos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\n=====================================");
        System.out.println("Simulación finalizada");
        System.out.println("Autos en estacionamiento: " + estacionamiento.getAutosActuales());
        System.out.println("=====================================");
    }
}