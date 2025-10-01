package EJE3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Tarea que descarga y procesa una noticia
class TareaNoticia implements Runnable {
    private final String[] urlNoticias;
    private final int id;
    private final Object lock;
    
    public TareaNoticia(String[] urlNoticias, int id, Object lock) {
        this.urlNoticias = urlNoticias;
        this.id = id;
        this.lock = lock;
    }
    
    @Override
    public void run() {
        try {
            String url = urlNoticias[id];
            
            // Realizar petición HTTP
            String htmlContent = realizarPeticionHTTP(url);
            
            // Extraer el contenido del div con class="article-main-content"
            String cuerpoNoticia = extraerCuerpoNoticia(htmlContent);
            
            // Imprimir resultado (sincronizado para evitar mezcla de salidas)
            synchronized(lock) {
                System.out.println("\n" + "=".repeat(80));
                System.out.println("HILO: " + Thread.currentThread().getName() + " (ID: " + id + ")");
                System.out.println("URL: " + url);
                System.out.println("-".repeat(80));
                
                if (cuerpoNoticia != null && !cuerpoNoticia.isEmpty()) {
                    System.out.println("CONTENIDO:");
                    System.out.println(cuerpoNoticia);
                } else {
                    System.out.println("No se pudo extraer el contenido de la noticia.");
                }
                
                System.out.println("=".repeat(80));
            }
            
        } catch (Exception e) {
            synchronized(lock) {
                System.err.println("\nError en hilo " + Thread.currentThread().getName() + 
                                 " (ID: " + id + "): " + e.getMessage());
            }
        }
    }
    
    private String realizarPeticionHTTP(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Configurar la petición
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", 
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        
        // Leer la respuesta
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        connection.disconnect();
        return content.toString();
    }
    
    private String extraerCuerpoNoticia(String html) {
        String contenido = null;
        
        // Intento 1: Buscar div con class="article-main-content"
        Pattern pattern1 = Pattern.compile(
            "<div[^>]*class=\"[^\"]*article-main-content[^\"]*\"[^>]*>(.*?)</div>",
            Pattern.DOTALL | Pattern.CASE_INSENSITIVE
        );
        Matcher matcher1 = pattern1.matcher(html);
        if (matcher1.find()) {
            contenido = matcher1.group(1);
        }
        
        // Intento 2: Buscar article-body o similar
        if (contenido == null || contenido.trim().isEmpty()) {
            Pattern pattern2 = Pattern.compile(
                "<div[^>]*class=\"[^\"]*article-body[^\"]*\"[^>]*>(.*?)</div>",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE
            );
            Matcher matcher2 = pattern2.matcher(html);
            if (matcher2.find()) {
                contenido = matcher2.group(1);
            }
        }
        
        // Intento 3: Buscar cualquier div con "content" en el class
        if (contenido == null || contenido.trim().isEmpty()) {
            Pattern pattern3 = Pattern.compile(
                "<div[^>]*class=\"[^\"]*content[^\"]*\"[^>]*>(.*?)</div>",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE
            );
            Matcher matcher3 = pattern3.matcher(html);
            if (matcher3.find()) {
                contenido = matcher3.group(1);
            }
        }
        
        // Intento 4: Buscar todos los <p> dentro del artículo
        if (contenido == null || contenido.trim().isEmpty()) {
            Pattern pattern4 = Pattern.compile(
                "<p[^>]*>(.*?)</p>",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE
            );
            Matcher matcher4 = pattern4.matcher(html);
            StringBuilder parrafos = new StringBuilder();
            int count = 0;
            while (matcher4.find() && count < 15) { // Limitar a 15 párrafos
                String parrafo = matcher4.group(1);
                // Filtrar párrafos muy cortos o que parezcan menús
                if (parrafo.length() > 50 && !parrafo.contains("nav") && !parrafo.contains("menu")) {
                    parrafos.append(parrafo).append(" ");
                    count++;
                }
            }
            if (parrafos.length() > 0) {
                contenido = parrafos.toString();
            }
        }
        
        if (contenido != null && !contenido.isEmpty()) {
            // Limpiar HTML tags y espacios extras
            contenido = contenido.replaceAll("<script[^>]*>.*?</script>", "");
            contenido = contenido.replaceAll("<style[^>]*>.*?</style>", "");
            contenido = contenido.replaceAll("<[^>]+>", " ");
            
            // Decodificar entidades HTML comunes
            contenido = contenido.replaceAll("&nbsp;", " ");
            contenido = contenido.replaceAll("&quot;", "\"");
            contenido = contenido.replaceAll("&amp;", "&");
            contenido = contenido.replaceAll("&lt;", "<");
            contenido = contenido.replaceAll("&gt;", ">");
            contenido = contenido.replaceAll("&apos;", "'");
            
            // Decodificar entidades numéricas hexadecimales (&#xHH;)
            contenido = decodificarEntidadesHTML(contenido);
            
            contenido = contenido.replaceAll("\\s+", " ");
            contenido = contenido.trim();
            
            // Limitar longitud si es muy largo
            if (contenido.length() > 2000) {
                contenido = contenido.substring(0, 2000) + "...";
            }
            
            return contenido;
        }
        
        return "No se encontró el contenido de la noticia (estructura HTML no reconocida)";
    }
    
    // Método para decodificar entidades HTML numéricas
    private String decodificarEntidadesHTML(String texto) {
        // Decodificar entidades hexadecimales (&#xHH;)
        Pattern patternHex = Pattern.compile("&#x([0-9A-Fa-f]+);");
        Matcher matcherHex = patternHex.matcher(texto);
        StringBuffer sb = new StringBuffer();
        while (matcherHex.find()) {
            int codePoint = Integer.parseInt(matcherHex.group(1), 16);
            matcherHex.appendReplacement(sb, Character.toString((char) codePoint));
        }
        matcherHex.appendTail(sb);
        texto = sb.toString();
        
        // Decodificar entidades decimales (&#NNN;)
        Pattern patternDec = Pattern.compile("&#([0-9]+);");
        Matcher matcherDec = patternDec.matcher(texto);
        sb = new StringBuffer();
        while (matcherDec.find()) {
            int codePoint = Integer.parseInt(matcherDec.group(1));
            matcherDec.appendReplacement(sb, Character.toString((char) codePoint));
        }
        matcherDec.appendTail(sb);
        
        return sb.toString();
    }
}

// Clase principal
public class ScraperNoticias {
    public static void main(String[] args) {
        // Array con 10 URLs de noticias policiales de El Tribuno de Jujuy
        String[] urlNoticias = {
                "https://eltribunodejujuy.com/policiales/2025-9-30-22-30-0-detuvieron-a-pequeno-j-y-matias-ozorio-en-peru",
                "https://eltribunodejujuy.com/municipios/2025-10-1-0-0-0-inauguraron-observatorio-astronomico-en-susques",
                "https://eltribunodejujuy.com/municipios/2025-9-30-23-18-0-pascuttini-y-mendieta-en-un-operativo-social-en-san-pedro",
                "https://eltribunodejujuy.com/municipios/2025-9-30-23-15-0-planta-de-tratamiento-de-liquidos-cloacales",
                "https://eltribunodejujuy.com/policiales/2025-10-1-0-0-0-se-inicia-juicio-contra-enfermero-que-inyecto-leche-a-un-bebe",
                "https://eltribunodejujuy.com/informacion-general/2025-9-27-16-14-0-nuevas-autoridades-del-colegio-de-escribanos-de-jujuy/amp",
                "https://eltribunodejujuy.com/informacion-general/2025-9-25-0-0-0-pascual-del-abandono-total-a-nadar-en-la-paz-de-los-nogales/amp",
                "https://eltribunodejujuy.com/policiales/2025-9-30-9-29-0-encontraron-en-buen-estado-de-salud-a-raul-sebastian-diaz-quien-era-buscado-intensamente",
                "https://eltribunodejujuy.com/informacion-general/2025-9-30-5-53-0-gran-agilizacion-de-causas-en-la-justicia-laboral-de-jujuy",
                "https://eltribunodejujuy.com/informacion-general/2025-9-30-15-43-0-oriana-sabatini-y-paulo-dybala-anunciaron-que-esperan-a-su-primer-hijo-primer-posteo-de-nosotros-tres"
            };
        
        System.out.println("╔" + "═".repeat(78) + "╗");
        System.out.println("║" + " ".repeat(20) + "SCRAPER DE NOTICIAS - EL TRIBUNO" + " ".repeat(26) + "║");
        System.out.println("╚" + "═".repeat(78) + "╝");
        System.out.println("\nCantidad de noticias: " + urlNoticias.length);
        System.out.println("Cantidad de hilos: 10");
        System.out.println("\nIniciando descarga de noticias...\n");
        
        // Objeto para sincronizar la salida por consola
        Object lock = new Object();
        
        // Array de threads
        Thread[] hilos = new Thread[10];
        
        // Registrar tiempo de inicio
        long tiempoInicio = System.currentTimeMillis();
        
        // Crear y lanzar los 10 hilos
        for (int i = 0; i < 10; i++) {
            TareaNoticia tarea = new TareaNoticia(urlNoticias, i, lock);
            hilos[i] = new Thread(tarea, "Thread-" + (i + 1));
            hilos[i].start();
        }
        
        // Esperar a que todos los hilos terminen
        for (int i = 0; i < 10; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.err.println("Error esperando al hilo " + i + ": " + e.getMessage());
            }
        }
        
        // Calcular tiempo de ejecución
        long tiempoFin = System.currentTimeMillis();
        long tiempoTotal = tiempoFin - tiempoInicio;
        
        // Mostrar resultados finales
        System.out.println("\n" + "╔" + "═".repeat(78) + "╗");
        System.out.println("║" + " ".repeat(28) + "PROCESO FINALIZADO" + " ".repeat(32) + "║");
        System.out.println("╚" + "═".repeat(78) + "╝");
        System.out.println("\nTiempo total de ejecución: " + tiempoTotal + " ms (" + 
                         String.format("%.2f", tiempoTotal/1000.0) + " segundos)");
        System.out.println("Tiempo promedio por noticia: " + (tiempoTotal/10) + " ms");
    }
}