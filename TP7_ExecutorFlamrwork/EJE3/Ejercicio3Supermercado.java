package EJE3;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Ejercicio3Supermercado {
    
    // Formateador para mostrar hora:minuto:segundo
    private static final DateTimeFormatter timeFormatter = 
        DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║     SIMULACIÓN DE SUPERMERCADO - 3 CAJAS ACTIVAS         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println("\n🛒 50 clientes llegan al supermercado");
        System.out.println("💳 3 cajas disponibles");
        System.out.println("⏱️  Tiempo de atención: 1-3 segundos (variable)\n");
        System.out.println("═════════════════════════════════════════════════════════════\n");
        
        // Registrar tiempo de inicio
        long tiempoInicio = System.currentTimeMillis();
        
        // Crear pool de 3 hilos (3 cajas)
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Crear 50 clientes y encolarlos
        for (int i = 1; i <= 50; i++) {
            Runnable cliente = new ClienteRunnable(i);
            executor.submit(cliente);
        }
        
        // Apagar el executor (no acepta más tareas)
        executor.shutdown();
        
        try {
            // Esperar a que todos los clientes sean atendidos
            if (executor.awaitTermination(10, TimeUnit.MINUTES)) {
                long tiempoFin = System.currentTimeMillis();
                long tiempoTotal = tiempoFin - tiempoInicio;
                
                System.out.println("\n═════════════════════════════════════════════════════════════");
                System.out.println("✅ TODOS LOS CLIENTES HAN SIDO ATENDIDOS");
                System.out.println("═════════════════════════════════════════════════════════════");
                System.out.println("⏱️  Tiempo total de simulación: " + 
                                 (tiempoTotal / 1000.0) + " segundos");
                System.out.println("👥 Total de clientes atendidos: 50");
                System.out.println("🏪 Cajas utilizadas: 3");
            } else {
                System.out.println("\n⚠️  El tiempo de espera expiró");
            }
        } catch (InterruptedException e) {
            System.err.println("❌ La simulación fue interrumpida: " + e.getMessage());
            executor.shutdownNow();
        }
    }
    
    /**
     * Clase que representa un cliente siendo atendido
     */
    static class ClienteRunnable implements Runnable {
        private int numeroCliente;
        private static Random random = new Random();
        
        public ClienteRunnable(int numeroCliente) {
            this.numeroCliente = numeroCliente;
        }
        
        @Override
        public void run() {
            try {
                // Obtener el nombre de la caja (hilo actual)
                String nombreCaja = Thread.currentThread().getName();
                
                // Registrar hora de inicio de atención
                String horaInicio = LocalTime.now().format(timeFormatter);
                
                System.out.println("🟢 Cliente " + numeroCliente + 
                                 " | COMIENZA atención en " + nombreCaja + 
                                 " | Hora: " + horaInicio);
                
                // Simular tiempo de atención variable entre 1 y 3 segundos
                int tiempoAtencion = 1000 + random.nextInt(2001); // 1000-3000 ms
                Thread.sleep(tiempoAtencion);
                
                // Registrar hora de fin de atención
                String horaFin = LocalTime.now().format(timeFormatter);
                
                System.out.println("🔴 Cliente " + numeroCliente + 
                                 " | FINALIZA atención en " + nombreCaja + 
                                 " | Hora: " + horaFin + 
                                 " | Duración: " + (tiempoAtencion / 1000.0) + "s");
                
            } catch (InterruptedException e) {
                System.err.println("❌ Atención del cliente " + numeroCliente + 
                                 " interrumpida: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}