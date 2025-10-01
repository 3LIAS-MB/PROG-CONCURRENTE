package EJE5.Threads;

public class SumaConHilos {
    
    public static double SumRootN(int root) {
        double result = 0;
        for (int i = 1; i < 10000000; i++) {
            result += Math.exp(Math.log(i) / root);
        }
        return result;
    }
    
    static class CalculadorThread extends Thread {
        private int root;
        private double resultado;
        
        public CalculadorThread(int root) {
            this.root = root;
        }
        
        public double getResultado() {
            return resultado;
        }
        
        @Override
        public void run() {
//            System.out.println(this.getName() + " calculando root = " + root);
            resultado = SumRootN(root);
//            System.out.println(this.getName() + " terminó. Resultado: " + resultado);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Iniciando cálculo con hilos (herencia de Thread)...");
        long startTime = System.currentTimeMillis();
        
        CalculadorThread[] hilos = new CalculadorThread[20];
        double[] resultados = new double[20];
        
        // Crear hilos
        for (int i = 0; i < 20; i++) {
            hilos[i] = new CalculadorThread(i + 1);
            hilos[i].setName("Hilo-" + (i + 1));
        }
        
        // Iniciar hilos
        for (int i = 0; i < 20; i++) {
            hilos[i].start();
        }
        
        // Esperar a que todos los hilos terminen
        for (int i = 0; i < 20; i++) {
            try {
                hilos[i].join();
                resultados[i] = hilos[i].getResultado();
            } catch (InterruptedException e) {
                System.out.println("Hilo interrumpido: " + e.getMessage());
            }
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        System.out.println("\n--- RESUMEN EJECUCIÓN CON HERENCIA DE THREAD ---");
        System.out.println("Tiempo total de ejecución: " + totalTime + " ms");
        System.out.println("Tiempo total de ejecución: " + (totalTime / 1000.0) + " segundos");
        
        // Mostrar resultados
        System.out.println("\nResultados obtenidos:");
        for (int i = 0; i < resultados.length; i++) {
            System.out.println("Root " + (i+1) + ": " + resultados[i]);
        }
    }
}