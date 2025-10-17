package EJE2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TareasProgramadas {
    
    // Lista compartida para almacenar las fechas/horas
    private final List<LocalDateTime> listaFechas;
    
    // Formatter para el formato HH:mm:ss:S
    private final DateTimeFormatter formatter;
    
    // Archivos de salida
    private static final String ARCHIVO_PRIMOS = "Primos.txt";
    private static final String ARCHIVO_NO_PRIMOS = "NoPrimos.txt";
    
    public TareasProgramadas() {
        this.listaFechas = new ArrayList<>();
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
    }
    
    /**
     * Tarea 1: Obtiene la fecha/hora actual y la almacena en la lista
     */
    public void tarea1() {
        LocalDateTime ahora = LocalDateTime.now();
        synchronized (listaFechas) {
            listaFechas.add(ahora);
            String horaFormateada = ahora.format(formatter);
            System.out.println("[TAREA 1] Fecha/hora capturada: " + horaFormateada + 
                             " (Total en lista: " + listaFechas.size() + ")");
        }
    }
    
    /**
     * Tarea 2: Procesa el último elemento de la lista y lo clasifica
     */
    public void tarea2() {
        LocalDateTime ultimaFecha;
        
        synchronized (listaFechas) {
            if (listaFechas.isEmpty()) {
                System.out.println("[TAREA 2] Lista vacía, esperando datos...");
                return;
            }
            ultimaFecha = listaFechas.get(listaFechas.size() - 1);
        }
        
        // Obtener milisegundos
        int milisegundos = ultimaFecha.getNano() / 1_000_000;
        String horaFormateada = ultimaFecha.format(formatter);
        
        // Verificar si es primo
        boolean esPrimo = esPrimo(milisegundos);
        
        // Guardar en el archivo correspondiente
        String archivo = esPrimo ? ARCHIVO_PRIMOS : ARCHIVO_NO_PRIMOS;
        String tipoNumero = esPrimo ? "PRIMO" : "NO PRIMO";
        
        guardarEnArchivo(archivo, horaFormateada, milisegundos);
        
        System.out.println("[TAREA 2] Procesado: " + horaFormateada + 
                         " | Milisegundos: " + milisegundos + 
                         " | Tipo: " + tipoNumero + 
                         " | Archivo: " + archivo);
    }
    
    /**
     * Verifica si un número es primo
     */
    private boolean esPrimo(int numero) {
        if (numero <= 1) {
            return false;
        }
        if (numero == 2) {
            return true;
        }
        if (numero % 2 == 0) {
            return false;
        }
        
        // Verificar divisibilidad hasta la raíz cuadrada
        for (int i = 3; i <= Math.sqrt(numero); i += 2) {
            if (numero % i == 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Guarda la información en el archivo correspondiente
     */
    private void guardarEnArchivo(String nombreArchivo, String hora, int milisegundos) {
        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println("Hora: " + hora + " | Milisegundos: " + milisegundos);
            
        } catch (IOException e) {
            System.err.println("Error al escribir en " + nombreArchivo + ": " + e.getMessage());
        }
    }
    
    /**
     * Inicia las tareas programadas
     */
    public void iniciar() {
        System.out.println("=== Sistema de Tareas Programadas ===");
        System.out.println("Pausa inicial: 2 segundos");
        System.out.println("Intervalo de ejecución: 2 segundos");
        System.out.println("Presione Ctrl+C para detener");
        System.out.println("======================================\n");
        
        // Crear el ScheduledExecutorService con 2 hilos
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        
        // Programar Tarea 1: cada 2 segundos, con pausa inicial de 2 segundos
        scheduler.scheduleAtFixedRate(
            () -> {
                try {
                    tarea1();
                } catch (Exception e) {
                    System.err.println("Error en Tarea 1: " + e.getMessage());
                }
            },
            2,  // Pausa inicial
            2,  // Período
            TimeUnit.SECONDS
        );
        
        // Programar Tarea 2: cada 2 segundos, con pausa inicial de 2 segundos
        scheduler.scheduleAtFixedRate(
            () -> {
                try {
                    tarea2();
                } catch (Exception e) {
                    System.err.println("Error en Tarea 2: " + e.getMessage());
                }
            },
            2,  // Pausa inicial
            2,  // Período
            TimeUnit.SECONDS
        );
        
        // Agregar shutdown hook para cerrar correctamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n\n=== Deteniendo tareas ===");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
                System.out.println("Total de fechas capturadas: " + listaFechas.size());
                System.out.println("Sistema finalizado correctamente");
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }));
    }
    
    public static void main(String[] args) {
        TareasProgramadas tareas = new TareasProgramadas();
        tareas.iniciar();
        
        // Mantener el programa corriendo (opcional: limitar el tiempo de ejecución)
        // Descomentar las siguientes líneas para ejecutar por 30 segundos:
        /*
        try {
            Thread.sleep(30000); // 30 segundos
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }
}