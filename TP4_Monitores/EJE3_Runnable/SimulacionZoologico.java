package EJE3_Runnable;

import java.util.Random;

//	3) Modele la entrada a un Zoológico. Dicho Zoo solo tiene una puerta que actúa de entrada 
//	y salida, mediante un pasillo muy angosto en el cual solo puede entrar o salir una persona 
//	a la vez. Las personas van entrando de forma indefinida en un tiempo variable  entre 100
//	200ms, la entrada por el pasillo le demanda 50ms (lo mismo que la salida), luego 
//	permanece en el Zoo un tiempo variable de 400-700, y sale. Indique las acciones de cada 
//	persona (identificada por un ID numérico) desde que hace fila para entrar, entra por el 
//	pasillo, permanece en el zoo, hace fila para salir y finalmente sale del Zoo.

// Monitor que controla el acceso al pasillo del zoológico
class MonitorZoo {
    private boolean pasilloOcupado = false;
    private int personasEnZoo = 0;
    
    // Método sincronizado para entrar por el pasillo
    public synchronized void entrarPorPasillo(int id) throws InterruptedException {
        // Esperar hasta que el pasillo esté libre
        while (pasilloOcupado) {
            System.out.println("Persona " + id + ": Esperando para entrar (pasillo ocupado)");
            wait();
        }
        
        // Ocupar el pasillo
        pasilloOcupado = true;
        System.out.println("Persona " + id + ": Entrando por el pasillo");
        
        // Liberar el monitor temporalmente para atravesar el pasillo
        // (esto permite que otros threads puedan esperar)
    }
    
    public synchronized void finalizarEntrada(int id) {
        pasilloOcupado = false;
        personasEnZoo++;
        System.out.println("Persona " + id + ": Dentro del zoo (Total en zoo: " + personasEnZoo + ")");
        notifyAll();
    }
    
    // Método sincronizado para salir por el pasillo
    public synchronized void salirPorPasillo(int id) throws InterruptedException {
        // Esperar hasta que el pasillo esté libre
        while (pasilloOcupado) {
            System.out.println("Persona " + id + ": Esperando para salir (pasillo ocupado)");
            wait();
        }
        
        // Ocupar el pasillo
        pasilloOcupado = true;
        System.out.println("Persona " + id + ": Saliendo por el pasillo");
    }
    
    public synchronized void finalizarSalida(int id) {
        pasilloOcupado = false;
        personasEnZoo--;
        System.out.println("Persona " + id + ": Fuera del zoo (Total en zoo: " + personasEnZoo + ")");
        notifyAll();
    }
    
    public synchronized int getPersonasEnZoo() {
        return personasEnZoo;
    }
}

// Clase que representa una persona visitando el zoológico
class Persona implements Runnable {
    private final int id;
    private final MonitorZoo monitor;
    private final Random random;
    
    public Persona(int id, MonitorZoo monitor) {
        this.id = id;
        this.monitor = monitor;
        this.random = new Random();
    }
    
    @Override
    public void run() {
        try {
            // 1. Hacer fila para entrar
            System.out.println("Persona " + id + ": Llega al zoo");
            
            // 2. Entrar por el pasillo
            monitor.entrarPorPasillo(id);
            Thread.sleep(50); // Atravesar pasillo (50ms)
            monitor.finalizarEntrada(id);
            
            // 3. Permanecer en el Zoo (400-700ms)
            int tiempoEnZoo = 400 + random.nextInt(301);
            System.out.println("Persona " + id + ": Disfrutando del zoo (" + tiempoEnZoo + "ms)");
            Thread.sleep(tiempoEnZoo);
            
            // 4. Hacer fila para salir
            System.out.println("Persona " + id + ": Lista para salir");
            
            // 5. Salir por el pasillo
            monitor.salirPorPasillo(id);
            Thread.sleep(50); // Atravesar pasillo (50ms)
            monitor.finalizarSalida(id);
            
        } catch (InterruptedException e) {
            System.err.println("Persona " + id + ": Interrumpida");
            Thread.currentThread().interrupt();
        }
    }
}

// Clase principal
public class SimulacionZoologico {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== SIMULACION DE ZOOLOGICO ===");
        System.out.println("Pasillo: 1 persona a la vez");
        System.out.println("Tiempo pasillo: 50ms");
        System.out.println("Tiempo en zoo: 400-700ms");
        System.out.println("Personas a simular: 20");
        System.out.println("================================\n");
        
        MonitorZoo monitor = new MonitorZoo();
        Random random = new Random();
        int numeroPersonas = 20;
        Thread[] personas = new Thread[numeroPersonas];
        
        // Crear y lanzar threads
        for (int i = 0; i < numeroPersonas; i++) {
            personas[i] = new Thread(new Persona(i + 1, monitor));
            personas[i].start();
            
            // Llegada variable (100-200ms)
            int tiempoEspera = 100 + random.nextInt(101);
            Thread.sleep(tiempoEspera);
        }
        
        // Esperar a que todas terminen
        for (int i = 0; i < numeroPersonas; i++) {
            personas[i].join();
        }
        
        System.out.println("\n================================");
        System.out.println("Simulacion finalizada");
        System.out.println("Personas en el zoo: " + monitor.getPersonasEnZoo());
        System.out.println("================================");
    }
}