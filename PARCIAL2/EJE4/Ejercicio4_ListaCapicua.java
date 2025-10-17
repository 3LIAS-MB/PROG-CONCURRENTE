package EJE4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ejercicio4_ListaCapicua {
    
    private static List<Integer> listaNumeros = new ArrayList<>();
    private static Random random = new Random();
    
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        Runnable tareaAgregarNumero = () -> {
            // Generar número aleatorio entre 90-130
            int numero = 90 + random.nextInt(41);
            listaNumeros.add(numero);
            
            System.out.println("\n--- Nuevo elemento agregado ---");
            
            // Mostrar todos los elementos
            System.out.print("Lista completa: ");
            for (int num : listaNumeros) {
                System.out.print(num + " ");
            }
            System.out.println();
            
            // Calcular suma
            int suma = 0;
            for (int num : listaNumeros) {
                suma += num;
            }
            System.out.println("Suma actual: " + suma);
            
            // Calcular promedio
            double promedio = (double) suma / listaNumeros.size();
            System.out.printf("Promedio actual: %.2f%n", promedio);
            
            // Verificar si la suma es capicúa
            if (esCapicua(suma)) {
                System.out.println("La suma " + suma + " ES un número CAPICÚA");
            } else {
                System.out.println("La suma " + suma + " NO es un número capicúa");
            }
        };
        
        // Ejecutar cada 5 segundos
        scheduler.scheduleAtFixedRate(tareaAgregarNumero, 0, 5, TimeUnit.SECONDS);
    }
    
    private static boolean esCapicua(int numero) {
        String numStr = String.valueOf(numero);
        String numReverso = new StringBuilder(numStr).reverse().toString();
        return numStr.equals(numReverso);
    }
}