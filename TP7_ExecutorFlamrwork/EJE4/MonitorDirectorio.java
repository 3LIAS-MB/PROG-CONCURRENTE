package EJE4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitorDirectorio {
    
    private final String rutaDirectorio;
    private final Set<String> archivosConocidos;
    private final DateTimeFormatter formatter;
    private final DecimalFormat df;
    private int contadorChequeos;
    
    public MonitorDirectorio(String rutaDirectorio) {
        this.rutaDirectorio = rutaDirectorio;
        this.archivosConocidos = new HashSet<>();
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.df = new DecimalFormat("#,##0.00");
        this.contadorChequeos = 0;
    }
    
    /**
     * Inicializa el monitor cargando los archivos existentes
     */
    public boolean inicializar() {
        File directorio = new File(rutaDirectorio);
        
        if (!directorio.exists()) {
            System.err.println("❌ ERROR: El directorio no existe: " + rutaDirectorio);
            return false;
        }
        
        if (!directorio.isDirectory()) {
            System.err.println("❌ ERROR: La ruta no es un directorio: " + rutaDirectorio);
            return false;
        }
        
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              MONITOR DE DIRECTORIO - DETECTOR DE NUEVOS ARCHIVOS               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("📁 Directorio monitoreado: " + directorio.getAbsolutePath());
        System.out.println("🔄 Frecuencia de chequeo: cada 5 segundos");
        System.out.println("⏰ Hora de inicio: " + LocalDateTime.now().format(formatter));
        System.out.println();
        
        // Cargar archivos existentes
        cargarArchivosExistentes(directorio);
        
        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("✅ Monitor iniciado correctamente");
        System.out.println("💡 Copie un nuevo archivo al directorio para probar la detección");
        System.out.println("   Presione Ctrl+C para detener el monitor");
        System.out.println();
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println();
        
        return true;
    }
    
    /**
     * Carga los archivos existentes en el directorio
     */
    private void cargarArchivosExistentes(File directorio) {
        File[] archivos = directorio.listFiles();
        
        if (archivos != null && archivos.length > 0) {
            System.out.println("📋 Archivos existentes detectados: " + archivos.length);
            System.out.println();
            
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    archivosConocidos.add(archivo.getName());
                    System.out.println("   • " + archivo.getName() + 
                                     " (" + formatearTamanio(archivo.length()) + ")");
                }
            }
            System.out.println();
        } else {
            System.out.println("📋 El directorio está vacío (aún no hay archivos)");
            System.out.println();
        }
    }
    
    /**
     * Tarea que chequea nuevos archivos en el directorio
     */
    public void chequearNuevosArchivos() {
        contadorChequeos++;
        String horaChequeo = LocalDateTime.now().format(formatter);
        
        File directorio = new File(rutaDirectorio);
        File[] archivos = directorio.listFiles();
        
        // Verificar si el directorio existe y es accesible
        if (archivos == null) {
            System.err.println("[" + horaChequeo + "] ⚠️  ERROR: No se puede acceder al directorio");
            return;
        }
        
        boolean encontroNuevos = false;
        
        // Buscar nuevos archivos
        for (File archivo : archivos) {
            if (archivo.isFile() && !archivosConocidos.contains(archivo.getName())) {
                encontroNuevos = true;
                archivosConocidos.add(archivo.getName());
                
                long tamanio = archivo.length();
                String tamanioFormateado = formatearTamanio(tamanio);
                
                System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
                System.out.println("║  🆕 NUEVO ARCHIVO DETECTADO                                                    ║");
                System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
                System.out.println();
                System.out.println("  📄 Nombre: " + archivo.getName());
                System.out.println("  📊 Tamaño: " + tamanioFormateado + " (" + tamanio + " bytes)");
                System.out.println("  ⏰ Detectado a las: " + horaChequeo);
                System.out.println("  📍 Ruta completa: " + archivo.getAbsolutePath());
                
                // Información adicional
                try {
                    Path path = Paths.get(archivo.getAbsolutePath());
                    String tipoContenido = Files.probeContentType(path);
                    if (tipoContenido != null) {
                        System.out.println("  📝 Tipo: " + tipoContenido);
                    }
                } catch (IOException e) {
                    // Ignorar si no se puede obtener el tipo
                }
                
                System.out.println();
                System.out.println("════════════════════════════════════════════════════════════════════════════════");
                System.out.println();
            }
        }
        
        // Mostrar mensaje de chequeo cada 5 verificaciones (25 segundos)
        if (!encontroNuevos && contadorChequeos % 5 == 0) {
            System.out.println("[" + horaChequeo + "] 🔍 Chequeo #" + contadorChequeos + 
                             " - Monitoreando... (Total archivos: " + archivosConocidos.size() + ")");
        }
    }
    
    /**
     * Formatea el tamaño del archivo en unidades legibles
     */
    private String formatearTamanio(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return df.format(bytes / 1024.0) + " KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            return df.format(bytes / (1024.0 * 1024.0)) + " MB";
        } else {
            return df.format(bytes / (1024.0 * 1024.0 * 1024.0)) + " GB";
        }
    }
    
    /**
     * Inicia el monitoreo del directorio
     */
    public void iniciarMonitoreo() {
        if (!inicializar()) {
            return;
        }
        
        // Crear ScheduledExecutorService con un solo hilo
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        // Programar la tarea de chequeo cada 5 segundos
        scheduler.scheduleAtFixedRate(
            () -> {
                try {
                    chequearNuevosArchivos();
                } catch (Exception e) {
                    System.err.println("❌ Error durante el chequeo: " + e.getMessage());
                    e.printStackTrace();
                }
            },
            5,  // Pausa inicial de 5 segundos
            5,  // Período de 5 segundos
            TimeUnit.SECONDS
        );
        
        // Agregar shutdown hook para cerrar correctamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println();
            System.out.println("════════════════════════════════════════════════════════════════════════════════");
            System.out.println();
            System.out.println("🛑 Deteniendo monitor de directorio...");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
                System.out.println();
                System.out.println("📊 Estadísticas finales:");
                System.out.println("   • Total de chequeos realizados: " + contadorChequeos);
                System.out.println("   • Total de archivos monitoreados: " + archivosConocidos.size());
                System.out.println("   • Hora de finalización: " + LocalDateTime.now().format(formatter));
                System.out.println();
                System.out.println("✅ Monitor finalizado correctamente");
                System.out.println();
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }));
    }
    
    public static void main(String[] args) {
        // CONFIGURACIÓN: Cambiar esta ruta por el directorio que deseas monitorear
        String rutaDirectorio;
        
        if (args.length > 0) {
            // Si se pasa la ruta como argumento
            rutaDirectorio = args[0];
        } else {
            // Ruta por defecto - CAMBIAR SEGÚN TU SISTEMA
            // Ejemplos:
            // Windows: "C:\\Users\\TuUsuario\\Desktop\\MonitorTest"
            // Linux/Mac: "/home/tuusuario/MonitorTest"
            
            // Detectar sistema operativo y usar ruta apropiada
            String home = System.getProperty("user.home");
            rutaDirectorio = home + File.separator + "MonitorTest";
            
            // Crear el directorio si no existe
            File dir = new File(rutaDirectorio);
            if (!dir.exists()) {
                System.out.println("📁 Creando directorio de prueba: " + rutaDirectorio);
                if (dir.mkdirs()) {
                    System.out.println("✅ Directorio creado exitosamente");
                    System.out.println();
                } else {
                    System.err.println("❌ No se pudo crear el directorio");
                    return;
                }
            }
        }
        
        // Crear e iniciar el monitor
        MonitorDirectorio monitor = new MonitorDirectorio(rutaDirectorio);
        monitor.iniciarMonitoreo();
    }
}