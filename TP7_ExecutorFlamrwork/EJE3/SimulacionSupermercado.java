package EJE3;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulacionSupermercado {
    
    private static final int NUMERO_CAJAS = 3;
    private static final int NUMERO_CLIENTES = 50;
    private static final int TIEMPO_MIN_ATENCION = 1000; // 1 segundo en ms
    private static final int TIEMPO_MAX_ATENCION = 3000; // 3 segundos en ms
    
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    /**
     * Clase que representa la atención de un cliente
     */
    static class AtencionCliente implements Runnable {
        private final int numeroCliente;
        private final Random random;
        
        public AtencionCliente(int numeroCliente) {
            this.numeroCliente = numeroCliente;
            this.random = new Random();
        }
        
        @Override
        public void run() {
            String nombreCaja = Thread.currentThread().getName();
            
            // Calcular tiempo de atención aleatorio entre 1 y 3 segundos
            int tiempoAtencion = TIEMPO_MIN_ATENCION + 
                                random.nextInt(TIEMPO_MAX_ATENCION - TIEMPO_MIN_ATENCION + 1);
            
            // Registrar inicio de atención
            LocalTime horaInicio = LocalTime.now();
            System.out.println(String.format("%-15s | Cliente #%-3d | INICIO atención  | Hora: %s | Duración estimada: %.1fs",
                    nombreCaja, numeroCliente, horaInicio.format(timeFormatter), tiempoAtencion / 1000.0));
            
            try {
                // Simular el tiempo de atención
                Thread.sleep(tiempoAtencion);
            } catch (InterruptedException e) {
                System.err.println("Atención interrumpida para cliente #" + numeroCliente);
                Thread.currentThread().interrupt();
                return;
            }
            
            // Registrar fin de atención
            LocalTime horaFin = LocalTime.now();
            System.out.println(String.format("%-15s | Cliente #%-3d | FIN atención     | Hora: %s | Duración real: %.1fs",
                    nombreCaja, numeroCliente, horaFin.format(timeFormatter), tiempoAtencion / 1000.0));
        }
    }
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║           SIMULACIÓN DE ATENCIÓN EN SUPERMERCADO                              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Configuración:");
        System.out.println("  • Número de cajas: " + NUMERO_CAJAS);
        System.out.println("  • Número de clientes: " + NUMERO_CLIENTES);
        System.out.println("  • Tiempo de atención: entre " + (TIEMPO_MIN_ATENCION/1000) + " y " + 
                          (TIEMPO_MAX_ATENCION/1000) + " segundos");
        System.out.println("  • Todos los clientes llegan al mismo tiempo");
        System.out.println();
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println();
        
        // Crear el ExecutorService con 3 cajas (hilos)
        ExecutorService cajas = Executors.newFixedThreadPool(NUMERO_CAJAS, runnable -> {
            Thread t = new Thread(runnable);
            // Asignar nombres significativos a las cajas
            String nombreCaja = "Caja-" + (Thread.activeCount() % NUMERO_CAJAS + 1);
            t.setName(nombreCaja);
            return t;
        });
        
        // Registrar hora de inicio de la simulación
        long tiempoInicio = System.currentTimeMillis();
        LocalTime horaInicioSim = LocalTime.now();
        System.out.println("▶ INICIO DE SIMULACIÓN: " + horaInicioSim.format(timeFormatter));
        System.out.println("▶ Los " + NUMERO_CLIENTES + " clientes han llegado a la cola");
        System.out.println();
        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        System.out.println();
        
        // Enviar todos los clientes a la cola (llegan todos juntos)
        for (int i = 1; i <= NUMERO_CLIENTES; i++) {
            AtencionCliente cliente = new AtencionCliente(i);
            cajas.execute(cliente);
        }
        
        // Indicar que no se aceptarán más clientes
        cajas.shutdown();
        
        try {
            // Esperar a que todos los clientes sean atendidos (máximo 10 minutos)
            if (!cajas.awaitTermination(10, TimeUnit.MINUTES)) {
                System.err.println("\n⚠ TIMEOUT: Forzando cierre de las cajas");
                cajas.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("\n⚠ Simulación interrumpida");
            cajas.shutdownNow();
            Thread.currentThread().interrupt();
            return;
        }
        
        // Calcular estadísticas
        long tiempoFin = System.currentTimeMillis();
        LocalTime horaFinSim = LocalTime.now();
        long tiempoTotal = tiempoFin - tiempoInicio;
        
        // Mostrar resumen
        System.out.println();
        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         RESUMEN DE LA SIMULACIÓN                               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  ✓ Todos los clientes fueron atendidos exitosamente");
        System.out.println();
        System.out.println("  Hora de inicio:  " + horaInicioSim.format(timeFormatter));
        System.out.println("  Hora de fin:     " + horaFinSim.format(timeFormatter));
        System.out.println();
        System.out.println("  Tiempo total:    " + String.format("%.2f", tiempoTotal / 1000.0) + " segundos");
        System.out.println("                   " + String.format("%.2f", tiempoTotal / 60000.0) + " minutos");
        System.out.println();
        System.out.println("  Clientes atendidos:    " + NUMERO_CLIENTES);
        System.out.println("  Cajas utilizadas:      " + NUMERO_CAJAS);
        System.out.println("  Tiempo promedio/cliente: " + String.format("%.2f", tiempoTotal / 1000.0 / NUMERO_CLIENTES) + " segundos");
        System.out.println();
        
        // Calcular el tiempo teórico si hubiera una sola caja
        double tiempoPromedio = (TIEMPO_MIN_ATENCION + TIEMPO_MAX_ATENCION) / 2000.0;
        double tiempoTeorico1Caja = NUMERO_CLIENTES * tiempoPromedio;
        double eficiencia = (tiempoTeorico1Caja / (tiempoTotal / 1000.0)) * 100;
        
        System.out.println("  Análisis de eficiencia:");
        System.out.println("    - Con 1 caja (teórico):  " + String.format("%.2f", tiempoTeorico1Caja) + " segundos");
        System.out.println("    - Con 3 cajas (real):    " + String.format("%.2f", tiempoTotal / 1000.0) + " segundos");
        System.out.println("    - Mejora de velocidad:   " + String.format("%.1f%%", eficiencia - 100));
        System.out.println();
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
    }
}