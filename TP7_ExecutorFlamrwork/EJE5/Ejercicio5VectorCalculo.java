package EJE5;

import java.math.BigInteger;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Ejercicio5VectorCalculo {
    
    // Constante M para el cálculo
    static BigInteger M = new BigInteger("1999");
    
    // Formateador de tiempo
    private static final DateTimeFormatter timeFormatter = 
        DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║     CÁLCULO CON POOL DE EJECUCIÓN - TAMAÑO 2             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        // Vector de números long
        long[] vector = new long[] { 100477L, 105477L, 112986L, 100078L, 165987L, 142578L };
        
        System.out.println("📊 Vector de números a procesar:");
        for (int i = 0; i < vector.length; i++) {
            System.out.println("   Posición " + i + ": " + vector[i]);
        }
        System.out.println("\n🔧 Pool de ejecución: 2 hilos");
        System.out.println("⏱️  Nota: Cada cálculo puede demorar varios segundos\n");
        System.out.println("═════════════════════════════════════════════════════════════\n");
        
        // Registrar tiempo de inicio
        long tiempoInicio = System.currentTimeMillis();
        
        // Crear pool de ejecución de tamaño 2
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Procesar cada número del vector
        for (int i = 0; i < vector.length; i++) {
            final int indice = i;
            final long numero = vector[i];
            
            // Crear y encolar la tarea
            Runnable tarea = new TareaCalculo(indice, numero);
            executor.submit(tarea);
        }
        
        // Apagar el executor (no acepta más tareas)
        executor.shutdown();
        
        try {
            // Esperar a que todas las tareas terminen (máximo 30 minutos)
            if (executor.awaitTermination(30, TimeUnit.MINUTES)) {
                long tiempoFin = System.currentTimeMillis();
                long tiempoTotal = tiempoFin - tiempoInicio;
                
                System.out.println("\n═════════════════════════════════════════════════════════════");
                System.out.println("✅ TODOS LOS CÁLCULOS HAN FINALIZADO");
                System.out.println("═════════════════════════════════════════════════════════════");
                System.out.println("⏱️  Tiempo total de ejecución: " + 
                                 (tiempoTotal / 1000.0) + " segundos");
                System.out.println("📈 Total de números procesados: " + vector.length);
                System.out.println("🔧 Hilos utilizados: 2 (pool fijo)");
            } else {
                System.out.println("\n⚠️  El tiempo de espera expiró");
            }
        } catch (InterruptedException e) {
            System.err.println("❌ La ejecución fue interrumpida: " + e.getMessage());
            executor.shutdownNow();
        }
    }
    
    /**
     * Función de cálculo pesado que demora varios segundos
     */
    private static BigInteger compute(long n) {
        String s = "";
        for (long i = 0; i < n; i++) {
            s = s + n;
        }
        return new BigInteger(s.toString()).mod(M);
    }
    
    /**
     * Clase que representa una tarea de cálculo
     */
    static class TareaCalculo implements Runnable {
        private int indice;
        private long numero;
        
        public TareaCalculo(int indice, long numero) {
            this.indice = indice;
            this.numero = numero;
        }
        
        @Override
        public void run() {
            try {
                String nombreHilo = Thread.currentThread().getName();
                String horaInicio = LocalTime.now().format(timeFormatter);
                
                System.out.println("🟢 Posición " + indice + 
                                 " | Número: " + numero + 
                                 " | INICIANDO en " + nombreHilo + 
                                 " | Hora: " + horaInicio);
                
                // Registrar tiempo de inicio del cálculo
                long tiempoComienzo = System.currentTimeMillis();
                
                // Realizar el cálculo pesado
                BigInteger resultado = compute(numero);
                
                // Calcular duración del cálculo
                long tiempoFinal = System.currentTimeMillis();
                long duracion = tiempoFinal - tiempoComienzo;
                String horaFin = LocalTime.now().format(timeFormatter);
                
                System.out.println("🔴 Posición " + indice + 
                                 " | Número: " + numero + 
                                 " | FINALIZADO en " + nombreHilo + 
                                 " | Hora: " + horaFin);
                System.out.println("   ✓ Resultado: " + resultado + 
                                 " | Duración: " + (duracion / 1000.0) + "s\n");
                
            } catch (Exception e) {
                System.err.println("❌ Error al procesar posición " + indice + 
                                 ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}