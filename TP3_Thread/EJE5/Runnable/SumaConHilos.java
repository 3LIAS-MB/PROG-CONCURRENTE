package EJE5.Runnable;

public class SumaConHilos {
    
    public static double SumRootN(int root) {
        double result = 0;
        for (int i = 1; i < 10000000; i++) {
            result += Math.exp(Math.log(i) / root);
        }
        return result;
    }
    
    static class Calculador implements Runnable {
        private int root;
        private double resultado;
        
        public Calculador(int root) {
            this.root = root;
        }
        
        public double getResultado() {
            return resultado;
        }
        
        @Override
        public void run() {
//            System.out.println(Thread.currentThread().getName() + " calculando root = " + root);
            resultado = SumRootN(root);
//            System.out.println(Thread.currentThread().getName() + " terminó. Resultado: " + resultado);
        }
    }
    
    public static void main(String[] args) {
         System.out.println("Iniciando cálculo con hilos (Runnable)");
        long startTime = System.currentTimeMillis();
        
        Thread[] hilos = new Thread[20];
        Calculador[] calculadores = new Calculador[20];
        double[] resultados = new double[20];
        
        for (int i = 0; i < 20; i++) {
            calculadores[i] = new Calculador(i + 1);
            hilos[i] = new Thread(calculadores[i], "Hilo-" + (i + 1));
            hilos[i].start();
        }

        for (int i = 0; i < 20; i++) {
            try {
                hilos[i].join();
                resultados[i] = calculadores[i].getResultado();
            } catch (InterruptedException e) {
                System.out.println("Hilo interrumpido: " + e.getMessage());
            }
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        System.out.println("\n-> RESUMEN EJECUCIÓN CON HILOS");
        System.out.println("Tiempo total de ejecución: " + totalTime + " ms");
        System.out.println("Tiempo total de ejecución: " + (totalTime / 1000.0) + " segundos");
        
        System.out.println("\nResultados obtenidos:");
        for (int i = 0; i < resultados.length; i++) {
            System.out.println("Root " + (i+1) + ": " + resultados[i]);
        }
    }
}