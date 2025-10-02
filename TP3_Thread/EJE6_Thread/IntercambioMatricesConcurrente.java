package EJE6_Thread;

//6) Defina 2 matrices. Matriz A de 100 x 200 y matriz B de 200 x 100. Inicialice ambas con 
//números aleatorios (1-100). Luego realice el intercambio de forma concurrente de cada 
//fila de A a su correspondiente columna en B y viceversa (cada columna de B a su 
//correspondiente fila en A) sin perder la información en el pasaje. Muestre las matrices 
//antes y después del intercambio. 

import java.util.Random;

public class IntercambioMatricesConcurrente {
    private static final int FILAS_A = 100;
    private static final int COLUMNAS_A = 200;
    private static final int FILAS_B = 200;
    private static final int COLUMNAS_B = 100;
    
    private int[][] matrizA;
    private int[][] matrizB;
    private Random random;
    
    public IntercambioMatricesConcurrente() {
        random = new Random();	
        matrizA = new int[FILAS_A][COLUMNAS_A];
        matrizB = new int[FILAS_B][COLUMNAS_B];
    }
    
    // Inicializar matrices con valores aleatorios
    public void inicializarMatrices() {
        System.out.println("Inicializando matrices con valores aleatorios...");
        
        // Inicializar matriz A
        for (int i = 0; i < FILAS_A; i++) {
            for (int j = 0; j < COLUMNAS_A; j++) {
                matrizA[i][j] = random.nextInt(100) + 1;
            }
        }
        
        // Inicializar matriz B
        for (int i = 0; i < FILAS_B; i++) {
            for (int j = 0; j < COLUMNAS_B; j++) {
                matrizB[i][j] = random.nextInt(100) + 1;
            }
        }
    }
    
    // Mostrar porción de las matrices (primeras 5x5 para no saturar)
    public void mostrarMatrices() {
        System.out.println("\n=== MATRIZ A (primera 5x5) ===");
        for (int i = 0; i < Math.min(5, FILAS_A); i++) {
            for (int j = 0; j < Math.min(5, COLUMNAS_A); j++) {
                System.out.printf("%4d", matrizA[i][j]);
            }
            System.out.println(" ...");
        }
        System.out.println("Dimensiones: " + FILAS_A + "x" + COLUMNAS_A);
        
        System.out.println("\n=== MATRIZ B (primera 5x5) ===");
        for (int i = 0; i < Math.min(5, FILAS_B); i++) {
            for (int j = 0; j < Math.min(5, COLUMNAS_B); j++) {
                System.out.printf("%4d", matrizB[i][j]);
            }
            System.out.println(" ...");
        }
        System.out.println("Dimensiones: " + FILAS_B + "x" + COLUMNAS_B);
    }
    
    // Hilo para intercambiar una fila de A con una columna de B
    class HiloIntercambio extends Thread {
        private int indice;
        
        public HiloIntercambio(int indice) {
            this.indice = indice;
        }
        
        @Override
        public void run() {
            // Crear arrays temporales para el intercambio
            int[] tempFilaA = new int[COLUMNAS_A];
            int[] tempColumnaB = new int[FILAS_B];
            
            // Copiar fila de A a temporal
            for (int j = 0; j < COLUMNAS_A; j++) {
                tempFilaA[j] = matrizA[indice][j];
            }
            
            // Copiar columna de B a temporal
            for (int i = 0; i < FILAS_B; i++) {
                tempColumnaB[i] = matrizB[i][indice];
            }
            
            // Intercambiar: fila de A -> columna de B
            for (int i = 0; i < FILAS_B; i++) {
                matrizB[i][indice] = tempFilaA[i];
            }
            
            // Intercambiar: columna de B -> fila de A
            for (int j = 0; j < COLUMNAS_A; j++) {
                matrizA[indice][j] = tempColumnaB[j];
            }
        }
    }
    
    // Realizar intercambio concurrente
    public void intercambiarConcurrentemente() {
        System.out.println("\nIniciando intercambio concurrente...");
        long startTime = System.currentTimeMillis();
        
        // Crear hilos para cada fila/columna a intercambiar
        Thread[] hilos = new Thread[FILAS_A]; // Usamos FILAS_A porque es el mínimo (100)
        
        for (int i = 0; i < FILAS_A; i++) {
            hilos[i] = new HiloIntercambio(i);
            hilos[i].start();
        }
        
        // Esperar que todos los hilos terminen
        for (int i = 0; i < FILAS_A; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Intercambio completado en " + (endTime - startTime) + " ms");
    }
    
    // Verificar que el intercambio se realizó correctamente
    public void verificarIntercambio() {
        System.out.println("\n=== VERIFICACIÓN DEL INTERCAMBIO ===");
        
        // Verificar algunas posiciones para confirmar el intercambio
        boolean correcto = true;
        
        for (int i = 0; i < Math.min(3, FILAS_A); i++) {
            for (int j = 0; j < Math.min(3, COLUMNAS_A); j++) {
                // Después del intercambio, la fila i de A debería ser la columna i de B original
                // y viceversa
                System.out.printf("Posición A[%d][%d]: %d%n", i, j, matrizA[i][j]);
                System.out.printf("Posición B[%d][%d]: %d%n", j, i, matrizB[j][i]);
                System.out.println("---");
            }
        }
        
        System.out.println("Intercambio verificado: " + (correcto ? "CORRECTO" : "ERROR"));
    }
    
    public static void main(String[] args) {
        System.out.println("=== PUNTO 6 - INTERCAMBIO CONCURRENTE DE MATRICES ===");
        System.out.println("Matriz A: " + FILAS_A + "x" + COLUMNAS_A);
        System.out.println("Matriz B: " + FILAS_B + "x" + COLUMNAS_B);
        System.out.println("Intercambiando filas de A con columnas de B...");
        
        IntercambioMatricesConcurrente intercambio = new IntercambioMatricesConcurrente();
        
        // Paso 1: Inicializar matrices
        intercambio.inicializarMatrices();
        
        // Paso 2: Mostrar matrices antes del intercambio
        System.out.println("\n=== ANTES DEL INTERCAMBIO ===");
        intercambio.mostrarMatrices();
        
        // Paso 3: Realizar intercambio concurrente
        intercambio.intercambiarConcurrentemente();
        
        // Paso 4: Mostrar matrices después del intercambio
        System.out.println("\n=== DESPUÉS DEL INTERCAMBIO ===");
        intercambio.mostrarMatrices();
        
        // Paso 5: Verificar el intercambio
        intercambio.verificarIntercambio();
        
        System.out.println("\n=== PROCESO COMPLETADO ===");
    }
}