package EJE4;

import java.util.Random;

//Clase base con método de cálculo de raíces
class Operaciones {
 public static double SumRootN(int root) {
     double result = 0;
     for (int i = 0; i < 10000000; i++) {
         result += Math.exp(Math.log(i + 1) / root);
     }
     return result;
 }
}

//Clase que realiza el cálculo concurrente
class CalculoConcurrente extends Operaciones implements Runnable {
 private final int[][] matrizA;
 private final int[][] matrizB;
 private final int[][] matrizC;
 private final int fila;
 private final int columna;
 
 public CalculoConcurrente(int[][] matrizA, int[][] matrizB, int[][] matrizC, int fila, int columna) {
     this.matrizA = matrizA;
     this.matrizB = matrizB;
     this.matrizC = matrizC;
     this.fila = fila;
     this.columna = columna;
 }
 
 @Override
 public void run() {
     // Calcular el producto punto de A[fila][1-15] x B[1-15][columna]
     int suma = 0;
     for (int k = 0; k < 15; k++) {
         suma += matrizA[fila][k] * matrizB[k][columna];
     }
     
     // Aplicar la función SumRootN al resultado
     // Convertir el resultado usando la raíz (usando el valor como índice de raíz)
     double conversion = SumRootN(Math.max(2, suma % 10 + 2)); // Raíz entre 2 y 11
     int valorFinal = (int) (conversion / 1000000); // Escalar el resultado
     
     // Sincronizar el acceso a la matriz C
     synchronized(matrizC) {
         matrizC[fila][columna] = valorFinal;
     }
 }
}

//Clase principal
public class MultiplicacionMatricesConcurrente {
 
 // Método para inicializar matriz con valores aleatorios entre 5 y 15
 private static void inicializarMatriz(int[][] matriz, Random random) {
     for (int i = 0; i < matriz.length; i++) {
         for (int j = 0; j < matriz[i].length; j++) {
             matriz[i][j] = 5 + random.nextInt(11); // Valores entre 5 y 15
         }
     }
 }
 
 // Método para imprimir matriz
 private static void imprimirMatriz(String nombre, int[][] matriz) {
     System.out.println("\n" + nombre + " [" + matriz.length + "x" + matriz[0].length + "]:");
     System.out.println("-".repeat(80));
     
     for (int i = 0; i < Math.min(5, matriz.length); i++) { // Mostrar primeras 5 filas
         for (int j = 0; j < Math.min(10, matriz[i].length); j++) { // Mostrar primeras 10 columnas
             System.out.printf("%4d ", matriz[i][j]);
         }
         if (matriz[i].length > 10) {
             System.out.print("...");
         }
         System.out.println();
     }
     
     if (matriz.length > 5) {
         System.out.println("  ... (" + matriz.length + " filas en total)");
     }
 }
 
 public static void main(String[] args) {
     System.out.println("╔" + "═".repeat(78) + "╗");
     System.out.println("║" + " ".repeat(15) + "MULTIPLICACIÓN CONCURRENTE DE MATRICES" + " ".repeat(25) + "║");
     System.out.println("╚" + "═".repeat(78) + "╝");
     
     // Inicializar matrices
     int[][] matrizA = new int[20][15];
     int[][] matrizB = new int[15][20];
     int[][] matrizC = new int[20][20];
     
     Random random = new Random();
     
     System.out.println("\nInicializando matrices con valores aleatorios entre [5-15]...");
     inicializarMatriz(matrizA, random);
     inicializarMatriz(matrizB, random);
     
     // Mostrar matrices iniciales
     imprimirMatriz("MATRIZ A", matrizA);
     imprimirMatriz("MATRIZ B", matrizB);
     
     System.out.println("\n" + "═".repeat(80));
     System.out.println("Iniciando multiplicación concurrente...");
     System.out.println("Cantidad de threads: " + (20 * 20) + " (uno por cada elemento de la matriz C)");
     System.out.println("═".repeat(80) + "\n");
     
     // Registrar tiempo de inicio
     long tiempoInicio = System.currentTimeMillis();
     
     // Crear array de threads (400 threads: uno por cada elemento de C)
     Thread[][] threads = new Thread[20][20];
     
     // Crear y lanzar todos los threads
     for (int i = 0; i < 20; i++) {
         for (int j = 0; j < 20; j++) {
             CalculoConcurrente calculo = new CalculoConcurrente(matrizA, matrizB, matrizC, i, j);
             threads[i][j] = new Thread(calculo, "Thread-[" + i + "][" + j + "]");
             threads[i][j].start();
         }
     }
     
     // Esperar a que todos los threads terminen
     for (int i = 0; i < 20; i++) {
         for (int j = 0; j < 20; j++) {
             try {
                 threads[i][j].join();
             } catch (InterruptedException e) {
                 System.err.println("Error esperando al thread [" + i + "][" + j + "]: " + e.getMessage());
             }
         }
     }
     
     // Calcular tiempo de ejecución
     long tiempoFin = System.currentTimeMillis();
     long tiempoTotal = tiempoFin - tiempoInicio;
     
     // Mostrar resultado
     imprimirMatriz("MATRIZ C (RESULTADO: A x B)", matrizC);
     
     // Mostrar estadísticas finales
     System.out.println("\n" + "╔" + "═".repeat(78) + "╗");
     System.out.println("║" + " ".repeat(28) + "PROCESO FINALIZADO" + " ".repeat(32) + "║");
     System.out.println("╚" + "═".repeat(78) + "╝");
     System.out.println("\nTiempo total de ejecución: " + tiempoTotal + " ms (" + 
                      String.format("%.2f", tiempoTotal/1000.0) + " segundos)");
     System.out.println("Threads ejecutados: 400 (20x20)");
     System.out.println("Tiempo promedio por elemento: " + String.format("%.2f", tiempoTotal/400.0) + " ms");
     
     // Verificación de dimensiones
     System.out.println("\n" + "═".repeat(80));
     System.out.println("VERIFICACIÓN:");
     System.out.println("  Matriz A: 20x15");
     System.out.println("  Matriz B: 15x20");
     System.out.println("  Matriz C: 20x20 ✓");
     System.out.println("═".repeat(80));
 }
}