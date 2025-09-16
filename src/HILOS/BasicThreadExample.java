package HILOS;

public class BasicThreadExample {
    public static void main(String[] args) {
        // =====================================================================
        // CREACIÓN DE HILOS
        // =====================================================================
        
        // Forma 1: Extendiendo la clase Thread
        // - Se crea una subclase de Thread y se sobrescribe el método run()
        Thread hilo1 = new MiHilo("Hilo-A");
        
        // Forma 2: Implementando la interfaz Runnable
        // - Se crea una clase que implementa Runnable y se pasa al constructor de Thread
        // - Ventaja: Permite heredar de otra clase (Thread ya no está disponible para herencia)
        Thread hilo2 = new Thread(new MiTarea(), "Hilo-B");
        
        // =====================================================================
        // MÉTODO START() - INICIO DE EJECUCIÓN DE HILOS
        // =====================================================================
        
        // start() NO ejecuta directamente run(), sino que:
        // 1. Registra el hilo con el planificador (scheduler) de threads de Java
        // 2. Crea un nuevo contexto de ejecución paralelo al hilo principal (main)
        // 3. El planificador decide CUÁNDO iniciar realmente el hilo (depende del SO)
        // 4. Cuando el hilo se inicia, AUTOMÁTICAMENTE llama a su método run()
        // 
        // IMPORTANTE: Nunca llamar run() directamente, ya que se ejecutaría
        // en el mismo hilo actual, no en uno nuevo.
        hilo1.start();
        hilo2.start();
        
        // =====================================================================
        // MÉTODO JOIN() - SINCRONIZACIÓN DE HILOS
        // =====================================================================
        
        try {
            // join(): Hace que el hilo actual (main) espere a que estos hilos terminen
            // 
            // Funcionamiento:
            // 1. El hilo principal (main) se bloquea hasta que hilo1 termine
            // 2. Luego se bloquea hasta que hilo2 termine
            // 
            // Sin join(), el hilo principal continuaría inmediatamente y podría
            // imprimir "Todos los hilos han terminado" antes de que los hilos completen
            hilo1.join(); 
            hilo2.join();
            
        // =====================================================================
        // MANEJO DE INTERRUPCIONES
        // =====================================================================
        
        } catch (InterruptedException e) {
            // InterruptedException ocurre cuando un hilo es interrumpido mientras
            // está en estado de espera (durante sleep(), join() o wait())
            
            // printStackTrace() muestra la traza de error con el método y línea
            // donde ocurrió la interrupción (útil para debugging)
            e.printStackTrace();
            
            // Thread.currentThread().interrupt() NO hace que el hilo continúe
            // Su propósito es:
            // 1. Restablecer el estado de interrupción del hilo actual
            // 2. Permitir que código superior sepa que hubo una interrupción
            // 3. Mantener la consistencia del mecanismo de interrupción de Java
            Thread.currentThread().interrupt();
            
            // =================================================================
            // OPCIONES AL MANEJAR UNA INTERRUPCIÓN
            // =================================================================
            
            // Opción 1: Terminar el método actual de manera controlada
            // return;
            
            // Opción 2: Lanzar una excepción RuntimeException si no se puede
            // manejar adecuadamente en este nivel
            // throw new RuntimeException("Hilo interrumpido", e);
            
            // Opción 3: Terminar el programa si la interrupción es crítica
            // System.exit(1);
        }
        
        // Este mensaje se imprimirá sólo después de que ambos hilos hayan terminado
        // gracias al uso de join()
        System.out.println("Todos los hilos han terminado. Hilo principal finalizando.");
    }
}

// =============================================================================
// CLASE QUE EXTIIENDE THREAD (PRIMERA FORMA DE CREAR HILOS)
// =============================================================================
class MiHilo extends Thread {	
    // Constructor que establece el nombre del hilo
    public MiHilo(String name) {
        super(name); // Llama al constructor de la clase padre (Thread)
    }
    
    // Método run(): Contiene el código que se ejecutará en el hilo
    @Override
    public void run() {
        // Simula una tarea que realiza 5 iteraciones
        for (int i = 1; i <= 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
            try {
                // Pausa el hilo por 500ms para simular procesamiento
                // Durante este tiempo, el planificador puede dar tiempo de CPU a otros hilos
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Se produce si otro hilo interrumpe este hilo durante el sleep()
                e.printStackTrace();
            }
        }
    }
}

// =============================================================================
// CLASE QUE IMPLEMENTA RUNNABLE (SEGUNDA FORMA DE CREAR HILOS)
// =============================================================================
class MiTarea implements Runnable {
    // Método run(): Contiene el código que se ejecutará cuando el hilo inicie
    @Override
    public void run() {
        // Simula una tarea que realiza 5 iteraciones (a diferente velocidad que MiHilo)
        for (int i = 1; i <= 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
            try {
                // Pausa más corta (300ms) que en MiHilo, por lo que este hilo
                // probablemente terminará primero
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}