package EJE4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GestionListaNumeros {
    // La lista debe ser sincronizada para proteger su acceso concurrente
    private static final List<Integer> LISTA_CAPICUA = Collections.synchronizedList(new ArrayList<>()); 
    private static final Random RND = new Random();

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        System.out.println("--- Gestión de Lista de Números Iniciada (Cada 5s) ---");

        // Programa la tarea para ejecutarse cada 5 segundos
        scheduler.scheduleAtFixedRate(new TareaGestionLista(), 0, 5, TimeUnit.SECONDS);

        // Opcional: Permitir que corra por un tiempo limitado
        try {
            Thread.sleep(25000); 
            scheduler.shutdown();
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static boolean esCapicua(int n) {
        String s = String.valueOf(n);
        String reversed = new StringBuilder(s).reverse().toString();
        return s.equals(reversed);
    }

    static class TareaGestionLista implements Runnable {
        @Override
        public void run() {
            // El monitor de la LISTA_CAPICUA se usa automáticamente al acceder a ella
            synchronized (LISTA_CAPICUA) { 
                int nuevoNumero = RND.nextInt(41) + 90; // [90-130]
                LISTA_CAPICUA.add(nuevoNumero);
                
                long suma = 0;
                for (int n : LISTA_CAPICUA) {
                    suma += n;
                }
                double promedio = (double) suma / LISTA_CAPICUA.size();
                String capicuaMsg = esCapicua((int) suma) ? "¡SÍ es CAPICÚA!" : "No es capicúa.";

                System.out.println("\n=============================================");
                System.out.printf("Nuevo elemento agregado: %d\n", nuevoNumero);
                System.out.printf("Elementos actuales: %s\n", LISTA_CAPICUA);
                System.out.printf("Suma actual: %d (%s)\n", suma, capicuaMsg);
                System.out.printf("Promedio actual: %.2f\n", promedio);
                System.out.println("=============================================");
            }
        }
    }
}