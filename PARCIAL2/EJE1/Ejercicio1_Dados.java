package EJE1;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//	1) Deseamos saber que tan buena es nuestra máquina de juegos de dados, por ello para testearla 
//	se ejecutará lo siguiente cada 2”. Se lanzarán 6 dados normales y se deberá imprimir por 
//	pantalla si la suma de los dados es par o impar (mostrando la sucesión de dados), 
//	adicionalmente, si la ejecución/tirada/lanzamiento produjo una escalera con los 6 números (1, 
//	2, 3, 4, 5, 6 en cualquier orden) mostrar también en letras mayúsculas “SE HA PRODUCIDO UNA ESCALERA!!!”. 

//	Puntos a evaluar: 
//	a. Compilación sin errores. 
//	b. Generación del tiempo de testeo. 
//	c. Mostrar la sucesión de dados, si la suma es par o impar y el mensaje si se produce una escalera. 

public class Ejercicio1_Dados {
    
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Random random = new Random();
        
//		Forma corta -> Lambda
//      Runnable tareaLanzarDados = () -> {	
//      };
        
        // Tarea que se ejecuta cada 2 seundos
        Runnable tareaLanzarDados = new Runnable() {
            @Override
            public void run() {
                int[] dados = new int[6];
                int suma = 0;
                
                // Lanzar 6 dados
                for (int i = 0; i < 6; i++) {
                    dados[i] = random.nextInt(6) + 1; // Valores entre 1 y 6
                    suma += dados[i];
                }
                
                // Mostrar la sucesión de dados
                System.out.print("Dados: " + Arrays.toString(dados));
                
                // Verificar si la suma es par o impar
                if (suma % 2 == 0) {
                    System.out.print(" - Suma: " + suma + " (PAR)");
                } else {
                    System.out.print(" - Suma: " + suma + " (IMPAR)");
                }
                
                // Verificar si hay escalera (1,2,3,4,5,6 en cualquier orden)
                if (esEscalera(dados)) {
                    System.out.print(" - SE HA PRODUCIDO UNA ESCALERA!!!");
                }
                
                System.out.println();
            }
        };

        // Ejecutar cada 2 segundos (delay inicial: 0, periodo: 2 segundos)
        scheduler.scheduleAtFixedRate(tareaLanzarDados, 0, 2, TimeUnit.SECONDS);
        
        // Opcional: detener después de un tiempo (para pruebas)
        // Descomentar si quieres que termine automáticamente
        /*
        try {
            Thread.sleep(20000); // 20 segundos de prueba
            scheduler.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }
    
    // Método para verificar si hay escalera
    private static boolean esEscalera(int[] dados) {
        int[] ordenados = Arrays.copyOf(dados, dados.length);
        Arrays.sort(ordenados);
        
        // Una escalera debe tener exactamente [1,2,3,4,5,6]
        for (int i = 0; i < 6; i++) {
            if (ordenados[i] != i + 1) {
                return false;
            }
        }
        return true;
    }
}