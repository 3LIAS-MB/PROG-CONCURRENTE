package EJE5.Runnable;

//	5) Implemente la ejecución del siguiente procedimiento: 
//	
//	public static double SumRootN(int root) {
//	double result = 0;
//	for (int i = 1; i < 10000000; i++) {
//	    result += Math.exp(Math.log(i) / root);
//	}
//	return result;
//	}
//	
//	a. Se le solicita que lo ejecute 20 veces de forma secuencial (para root entre [1-20]) 
//	y calcule el tiempo de ejecución, y además revise el “Administrador de tareas” de 
//	Windows para comprobar la utilización de la/las CPU/s. 
//	
//	b. Adicionalmente, implemente el mismo procedimiento pero mediante Hilos, 
//	ejecutándolo 20 veces y revisando el “Administrador de tareas” de Windows para 
//	comprobar la utilización de la/las CPU/s. 
//	
//	c. Indique el tiempo de ejecución de ambas alternativas. Comente los resultados. 

public class SumaSecuencial {
    
    public static double SumRootN(int root) {
        double result = 0;
        for (int i = 1; i < 10000000; i++) {
            result += Math.exp(Math.log(i) / root);
        }
        return result;
    }
    
    public static void main(String[] args) {
        System.out.println("Iniciando cálculo secuencial...");
        long startTime = System.currentTimeMillis();
        
        double[] resultados = new double[20];
        
        for (int root = 1; root <= 20; root++) {
//            System.out.println("Calculando root = " + root);
            resultados[root-1] = SumRootN(root);
//            System.out.println("Resultado para root " + root + ": " + resultados[root-1]);
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        System.out.println("\n-> RESUMEN EJECUCIÓN SECUENCIAL");
        System.out.println("Tiempo total de ejecución: " + totalTime + " ms");
        System.out.println("Tiempo total de ejecución: " + (totalTime / 1000.0) + " segundos");
        
        // Mostrar resultados
        System.out.println("\nResultados obtenidos:");
        for (int i = 0; i < resultados.length; i++) {
            System.out.println("Root " + (i+1) + ": " + resultados[i]);
        }
    }
}