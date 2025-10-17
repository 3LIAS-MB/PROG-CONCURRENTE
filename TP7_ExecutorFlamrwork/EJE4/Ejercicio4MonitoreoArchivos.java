package EJE4;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ejercicio4MonitoreoArchivos {
    
    // ⚠️ IMPORTANTE: Cambiar esta ruta por la carpeta que desees monitorear
    // Ejemplos:
    // Windows: "C:/Users/TuUsuario/Ejercicio4"
    // Linux/Mac: "/home/usuario/Ejercicio4"
    private static final String DIRECTORIO_A_MONITOREAR = "C:/Ejercicio4";
    
    // Set para almacenar los nombres de archivos conocidos
    private static Set<String> archivosConocidos = new HashSet<>();
    
    // Formateador de fecha y hora
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║        MONITOREO DE ARCHIVOS - CADA 5 SEGUNDOS           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        System.out.println("📁 Directorio monitoreado: " + DIRECTORIO_A_MONITOREAR);
        System.out.println("⏱️  Intervalo de verificación: 5 segundos");
        System.out.println("🔄 Presione Ctrl+C para detener el monitoreo\n");
        System.out.println("═════════════════════════════════════════════════════════════\n");
        
        // Verificar que el directorio existe
        File directorio = new File(DIRECTORIO_A_MONITOREAR);
        if (!directorio.exists()) {
            System.err.println("❌ ERROR: El directorio no existe: " + DIRECTORIO_A_MONITOREAR);
            System.err.println("Por favor, crea la carpeta o cambia la ruta en el código.");
            return;
        }
        
        if (!directorio.isDirectory()) {
            System.err.println("❌ ERROR: La ruta especificada no es un directorio.");
            return;
        }
        
        // Realizar la carga inicial de archivos existentes
        cargarEstadoInicial(directorio);
        
        // Crear executor programado con un solo hilo
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        
        // Programar la tarea de verificación cada 5 segundos
        scheduler.scheduleWithFixedDelay(
            () -> verificarNuevosArchivos(directorio),
            5,  // Pausa inicial de 5 segundos
            5,  // Repetir cada 5 segundos
            TimeUnit.SECONDS
        );
        
        // Nota: El programa seguirá ejecutándose indefinidamente
        // Para detenerlo usar Ctrl+C
    }
    
    /**
     * Carga el estado inicial del directorio (archivos existentes)
     */
    private static void cargarEstadoInicial(File directorio) {
        System.out.println("🔍 Cargando estado inicial del directorio...\n");
        
        File[] archivos = directorio.listFiles();
        
        if (archivos == null || archivos.length == 0) {
            System.out.println("📂 El directorio está vacío (no hay archivos iniciales)");
        } else {
            System.out.println("📋 Archivos existentes al iniciar:");
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    archivosConocidos.add(archivo.getName());
                    long tamano = archivo.length();
                    System.out.println("   • " + archivo.getName() + 
                                     " (" + formatearTamano(tamano) + ")");
                }
            }
            System.out.println("\n✅ Total de archivos iniciales: " + archivosConocidos.size());
        }
        
        System.out.println("\n═════════════════════════════════════════════════════════════");
        System.out.println("🚀 Monitoreo activo - Esperando nuevos archivos...");
        System.out.println("═════════════════════════════════════════════════════════════\n");
    }
    
    /**
     * Verifica si hay nuevos archivos en el directorio
     */
    private static void verificarNuevosArchivos(File directorio) {
        try {
            String horaVerificacion = LocalDateTime.now().format(formatter);
            
            File[] archivos = directorio.listFiles();
            
            if (archivos == null) {
                System.err.println("⚠️  [" + horaVerificacion + 
                                 "] Error al leer el directorio");
                return;
            }
            
            boolean hayNuevos = false;
            
            // Verificar cada archivo del directorio
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    String nombreArchivo = archivo.getName();
                    
                    // Si el archivo NO está en el set de conocidos, es NUEVO
                    if (!archivosConocidos.contains(nombreArchivo)) {
                        long tamano = archivo.length();
                        
                        System.out.println("🆕 [" + horaVerificacion + "] " +
                                         "Nuevo archivo: " + nombreArchivo + 
                                         ", con tamaño: " + formatearTamano(tamano));
                        
                        // Agregar al set de archivos conocidos
                        archivosConocidos.add(nombreArchivo);
                        hayNuevos = true;
                    }
                }
            }
            
            // Opcional: Detectar archivos eliminados
            verificarArchivosEliminados(archivos);
            
            // Si no hay cambios, mostrar mensaje periódico cada 30 segundos
            // (comentar esta línea si no quieres ver mensajes cuando no hay cambios)
            // if (!hayNuevos) {
            //     System.out.println("ℹ️  [" + horaVerificacion + "] Sin cambios detectados");
            // }
            
        } catch (Exception e) {
            System.err.println("❌ Error durante la verificación: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica si algún archivo conocido fue eliminado (OPCIONAL)
     */
    private static void verificarArchivosEliminados(File[] archivosActuales) {
        // Crear set con los nombres de archivos actuales
        Set<String> nombresActuales = new HashSet<>();
        for (File archivo : archivosActuales) {
            if (archivo.isFile()) {
                nombresActuales.add(archivo.getName());
            }
        }
        
        // Buscar archivos que estaban pero ya no están
        Set<String> archivosEliminados = new HashSet<>();
        for (String nombreConocido : archivosConocidos) {
            if (!nombresActuales.contains(nombreConocido)) {
                archivosEliminados.add(nombreConocido);
            }
        }
        
        // Mostrar archivos eliminados
        if (!archivosEliminados.isEmpty()) {
            String horaDeteccion = LocalDateTime.now().format(formatter);
            for (String nombreEliminado : archivosEliminados) {
                System.out.println("🗑️  [" + horaDeteccion + "] " +
                                 "Archivo eliminado: " + nombreEliminado);
                archivosConocidos.remove(nombreEliminado);
            }
        }
    }
    
    /**
     * Formatea el tamaño del archivo en unidades legibles
     */
    private static String formatearTamano(long bytes) {
        if (bytes < 1024) {
            return bytes + " bytes";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}