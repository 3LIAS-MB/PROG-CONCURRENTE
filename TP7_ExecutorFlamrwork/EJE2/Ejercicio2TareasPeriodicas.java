package EJE2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ejercicio2TareasPeriodicas {
    
    // Lista sincronizada para almacenar las fechas
    private static List<LocalDateTime> listaFechas = 
        Collections.synchronizedList(new ArrayList<>());
    
    // Variable para controlar el último índice procesado
    private static int ultimoIndiceProcesado = -1;
    
    // Formateador de fecha
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
    
    public static void main(String[] args) {
        System.out.println("=== Iniciando tareas periódicas ===");
        System.out.println("Pausa inicial: 2 segundos");
        System.out.println("Intervalo de ejecución: cada 2 segundos");
        System.out.println("Presione Ctrl+C para detener...\n");
        
        // Crear pool programado con 2 hilos
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        
        // Programar Tarea 1: Obtener fecha/hora cada 2 segundos
        scheduler.scheduleWithFixedDelay(
            () -> tarea1(),
            2, // Pausa inicial de 2 segundos
            2, // Repetir cada 2 segundos después de terminar
            TimeUnit.SECONDS
        );
        
        // Programar Tarea 2: Procesar milisegundos cada 2 segundos
        scheduler.scheduleWithFixedDelay(
            () -> tarea2(),
            2, // Pausa inicial de 2 segundos
            2, // Repetir cada 2 segundos después de terminar
            TimeUnit.SECONDS
        );
        
        // Nota: El programa seguirá ejecutándose indefinidamente
        // Para detenerlo usar Ctrl+C o agregar lógica de parada
    }
    
    /**
     * Tarea 1: Obtiene la fecha/hora actual y la guarda en la lista
     */
    private static void tarea1() {
        try {
            // Obtener fecha y hora actual
            LocalDateTime fechaHora = LocalDateTime.now();
            
            // Agregar a la lista
            listaFechas.add(fechaHora);
            
            String fechaFormateada = fechaHora.format(formatter);
            System.out.println("[TAREA 1] Fecha/hora capturada: " + fechaFormateada + 
                             " | Total en lista: " + listaFechas.size());
            
        } catch (Exception e) {
            System.err.println("[TAREA 1] Error: " + e.getMessage());
        }
    }
    
    /**
     * Tarea 2: Procesa el último número de milisegundos de la lista
     * Solo procesa datos nuevos que no hayan sido procesados antes
     */
    private static void tarea2() {
        try {
            // Verificar si hay datos nuevos para procesar
            if (listaFechas.isEmpty()) {
                System.out.println("[TAREA 2] Lista vacía, esperando datos...");
                return;
            }
            
            // Obtener el índice del último elemento
            int ultimoIndice = listaFechas.size() - 1;
            
            // Verificar que no hayamos procesado ya este dato
            if (ultimoIndice <= ultimoIndiceProcesado) {
                System.out.println("[TAREA 2] Sin datos nuevos para procesar");
                return;
            }
            
            // Obtener la última fecha agregada
            LocalDateTime ultimaFecha = listaFechas.get(ultimoIndice);
            
            // Obtener los milisegundos (0-999)
            int milisegundos = ultimaFecha.getNano() / 1_000_000;
            
            String fechaFormateada = ultimaFecha.format(formatter);
            System.out.println("[TAREA 2] Procesando: " + fechaFormateada + 
                             " | Milisegundos: " + milisegundos);
            
            // Verificar si es primo
            if (esPrimo(milisegundos)) {
                guardarEnArchivo("Primos.txt", milisegundos, fechaFormateada);
                System.out.println("[TAREA 2] ✓ " + milisegundos + 
                                 " es PRIMO - guardado en Primos.txt");
            } else {
                guardarEnArchivo("NoPrimos.txt", milisegundos, fechaFormateada);
                System.out.println("[TAREA 2] ✗ " + milisegundos + 
                                 " NO es primo - guardado en NoPrimos.txt");
            }
            
            // Marcar este índice como procesado
            ultimoIndiceProcesado = ultimoIndice;
            
        } catch (Exception e) {
            System.err.println("[TAREA 2] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica si un número es primo
     */
    private static boolean esPrimo(int numero) {
        // 0 y 1 no son primos
        if (numero <= 1) {
            return false;
        }
        
        // 2 y 3 son primos
        if (numero <= 3) {
            return true;
        }
        
        // Números divisibles por 2 o 3 no son primos
        if (numero % 2 == 0 || numero % 3 == 0) {
            return false;
        }
        
        // Verificar divisores hasta la raíz cuadrada
        for (int i = 5; i * i <= numero; i += 6) {
            if (numero % i == 0 || numero % (i + 2) == 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Guarda un número en el archivo especificado
     */
    private static void guardarEnArchivo(String nombreArchivo, 
                                         int milisegundos, 
                                         String fechaFormateada) {
        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println("Milisegundos: " + milisegundos + 
                      " | Fecha/Hora: " + fechaFormateada + 
                      " | Timestamp: " + System.currentTimeMillis());
            
        } catch (IOException e) {
            System.err.println("Error al escribir en " + nombreArchivo + 
                             ": " + e.getMessage());
        }
    }
}