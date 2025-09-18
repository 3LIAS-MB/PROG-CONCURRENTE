package UNIDAD4;

/**
 * UNIDAD 4 - MANEJO DE EXCEPCIONES EN HILOS
 * 
 * Objetivo: Mostrar cómo manejar excepciones en entornos concurrentes.
 * 
 * ¿Qué enseña?
 * - Uso de try-catch-finally en métodos run() de hilos
 * - Cómo las excepciones no capturadas en hilos pueden terminar el hilo silenciosamente
 * - Diferencia entre excepciones checked y unchecked en hilos
 * - Uso de UncaughtExceptionHandler para capturar excepciones no controladas
 */

public class _01_ExceptionHandling {

    static class HiloConExcepcion extends Thread {
        private String nombre;
        
        public HiloConExcepcion(String nombre) {
            this.nombre = nombre;
        }
        
        @Override
        public void run() {
            System.out.println(nombre + " inició ejecución");
            
            try {
                // Simular trabajo
                for (int i = 1; i <= 5; i++) {
                    System.out.println(nombre + " - Paso: " + i);
                    Thread.sleep(100);
                    
                    // Generar excepción en el paso 3
                    if (i == 3) {
                        throw new RuntimeException("Error crítico en " + nombre);
                    }
                }
            } catch (InterruptedException e) {
                System.out.println(nombre + " fue interrumpido: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println(nombre + " capturó RuntimeException: " + e.getMessage());
                // Relanzar para probar UncaughtExceptionHandler
                throw e;
            } finally {
                System.out.println(nombre + " ejecutando bloque finally (siempre se ejecuta)");
            }
        }
    }

    // Manejador global de excepciones no capturadas
    static class ManejadorExcepciones implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread hilo, Throwable excepcion) {
            System.out.println("=== EXCEPCIÓN NO CAPTURADA ===");
            System.out.println("Hilo: " + hilo.getName());
            System.out.println("Excepción: " + excepcion.getMessage());
            System.out.println("El hilo ha terminado abruptamente");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== MANEJO DE EXCEPCIONES EN HILOS ===");
        
        // Configurar manejador global de excepciones
        Thread.setDefaultUncaughtExceptionHandler(new ManejadorExcepciones());
        
        HiloConExcepcion hilo1 = new HiloConExcepcion("Hilo-Seguro");
        HiloConExcepcion hilo2 = new HiloConExcepcion("Hilo-Inseguro");
        
        // Hilo 1 maneja sus excepciones internamente
        hilo1.start();
        
        try {
            hilo1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n--- Hilo con excepción no manejada ---");
        // Hilo 2 no maneja la excepción, será capturada por el UncaughtExceptionHandler
        hilo2.start();
        
        try {
            hilo2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Programa principal continúa ejecutándose");
    }
}