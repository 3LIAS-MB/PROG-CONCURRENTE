package UNIDAD4;

/**
 * UNIDAD 4 - EJEMPLO EMPLEADOS-JEFE CON wait()/notifyAll()
 * 
 * Objetivo: Mostrar el uso de notifyAll() y el problema de race condition
 * cuando el notificador llega antes que los waiters.
 * 
 * ¿Qué enseña?
 * - Uso de notifyAll() para despertar múltiples hilos
 * - Problema de race condition cuando las señales se pierden
 * - Solución usando flags booleanos para controlar el estado
 * - Coordinación de múltiples hilos que esperan un evento
 */

public class _05_EmployeesBossExample {

    static class Oficina {
        private boolean jefeLlego = false;
        private final Object lock = new Object();
        private int empleadosEsperando = 0;
        
        public void empleadoLlega(String nombre) throws InterruptedException {
            synchronized (lock) {
                empleadosEsperando++;
                System.out.println(nombre + " llegó a la oficina. Esperando al jefe...");
                
                while (!jefeLlego) {
                    lock.wait(); // Espera hasta que el jefe llegue
                }
                
                System.out.println(nombre + ": ¡Buenos días jefe!");
                empleadosEsperando--;
            }
        }
        
        public void jefeLlega() {
            synchronized (lock) {
                jefeLlego = true;
                System.out.println("=== EL JEFE HA LLEGADO ===");
                System.out.println("Empleados esperando: " + empleadosEsperando);
                lock.notifyAll(); // Despierta a todos los empleados
            }
        }
        
        public void reset() {
            synchronized (lock) {
                jefeLlego = false;
            }
        }
    }

    static class Empleado extends Thread {
        private Oficina oficina;
        private String nombre;
        
        public Empleado(String nombre, Oficina oficina) {
            super(nombre);
            this.nombre = nombre;
            this.oficina = oficina;
        }
        
        @Override
        public void run() {
            try {
                // Simular tiempo de llegada aleatorio
                Thread.sleep((long)(Math.random() * 1000));
                oficina.empleadoLlega(nombre);
            } catch (InterruptedException e) {
                System.out.println(nombre + " interrumpido");
            }
        }
    }

    static class Jefe extends Thread {
        private Oficina oficina;
        
        public Jefe(Oficina oficina) {
            super("JEFE");
            this.oficina = oficina;
        }
        
        @Override
        public void run() {
            try {
                // El jefe llega después de un tiempo aleatorio
                Thread.sleep((long)(Math.random() * 1500));
                oficina.jefeLlega();
            } catch (InterruptedException e) {
                System.out.println("Jefe interrumpido");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== EJEMPLO EMPLEADOS-JEFE ===");
        
        Oficina oficina = new Oficina();
        
        // Crear empleados
        Empleado[] empleados = {
            new Empleado("Empleado-1", oficina),
            new Empleado("Empleado-2", oficina),
            new Empleado("Empleado-3", oficina),
            new Empleado("Empleado-4", oficina)
        };
        
        // Crear jefe
        Jefe jefe = new Jefe(oficina);
        
        System.out.println("Simulación 1: Jefe puede llegar después de los empleados");
        iniciarSimulacion(empleados, jefe, oficina);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nSimulación 2: Jefe puede llegar antes que algunos empleados");
        oficina.reset();
        iniciarSimulacion(empleados, jefe, oficina);
    }
    
    private static void iniciarSimulacion(Empleado[] empleados, Jefe jefe, Oficina oficina) {
        for (Empleado e : empleados) e.start();
        jefe.start();
        
        try {
            for (Empleado e : empleados) e.join();
            jefe.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}