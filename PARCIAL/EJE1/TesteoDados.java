package EJE1;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class TesteoDados {
    // Para el punto b: Generación del tiempo de testeo
    private static final AtomicLong tiempoInicio = new AtomicLong(System.currentTimeMillis());

    public static void main(String[] args) {
        // Ejecutor que puede programar tareas periódicas
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        System.out.println("--- Testeo de Máquina de Dados Iniciado ---");

        // Programa la tarea para que se ejecute cada 2 segundos.
        scheduler.scheduleAtFixedRate(new TareaLanzarDados(), 0, 2, TimeUnit.SECONDS);

        // Opcional: Detener el scheduler después de un tiempo (ej. 10 segundos)
        try {
            // Espera 10 segundos para que se ejecuten 5 tiradas
            scheduler.awaitTermination(10, TimeUnit.SECONDS); 
            scheduler.shutdown();
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    static class TareaLanzarDados implements Runnable {
        @Override
        public void run() {
            int[] dados = new int[6];
            int suma = 0;
            boolean esEscalera;

            // 1. Lanzar 6 dados y calcular la suma
            for (int i = 0; i < 6; i++) {
                dados[i] = (int) (Math.random() * 6) + 1; // [1-6]
                suma += dados[i];
            }

            // 2. Comprobar escalera (1, 2, 3, 4, 5, 6 en cualquier orden)
            int[] dadosOrdenados = Arrays.copyOf(dados, dados.length);
            Arrays.sort(dadosOrdenados);
            esEscalera = (dadosOrdenados[0] == 1 && dadosOrdenados[5] == 6 && 
                          dadosOrdenados[0] + dadosOrdenados[1] == 3 && dadosOrdenados[5] + dadosOrdenados[4] == 11);
            
            // 3. Mostrar la sucesión, par/impar y el mensaje de escalera (Punto c)
            long tiempoActual = System.currentTimeMillis();
            long deltaT = tiempoActual - tiempoInicio.get();

            System.out.println("\n------------------------------------------------");
            System.out.printf("T +%d ms | Dados: %s | Suma (%d): %s\n", 
                               deltaT, Arrays.toString(dados), suma, (suma % 2 == 0 ? "PAR" : "IMPAR"));
            
            if (esEscalera) {
                System.out.println("SE HA PRODUCIDO UNA ESCALERA!!!");
            }
        }
    }
}