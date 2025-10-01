package EJE5.ExecutorService;

public class SumaSecuencial {
    
    public static double sumRootN(int root) {
        double result = 0;
        for (int i = 1; i < 10000000; i++) {
            result += Math.exp(Math.log(i) / root);
        }
        return result;
    }
    
    public static void main(String[] args) {
        System.out.println("=== EJECUCIÓN SECUENCIAL ===");
        System.out.println("Revise el Administrador de Tareas durante la ejecución...");
        System.out.println("Uso de CPU esperado: 1 núcleo al 100%");
        System.out.println("======================================");
        
        long startTime = System.currentTimeMillis();
        
        for (int root = 1; root <= 20; root++) {
            long iterationStart = System.currentTimeMillis();
            double result = sumRootN(root);
            long iterationEnd = System.currentTimeMillis();
            
            System.out.printf("Root %2d: Resultado = %12.2f, Tiempo = %4d ms%n", 
                            root, result, (iterationEnd - iterationStart));
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        System.out.println("======================================");
        System.out.printf("TIEMPO TOTAL SECUENCIAL: %d ms%n", totalTime);
        System.out.println("======================================");
    }
}