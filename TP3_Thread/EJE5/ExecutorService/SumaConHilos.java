package EJE5.ExecutorService;

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

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.*;
import java.util.*;

public class SumaConHilos {
    
    public static double SumRootN(int root) {
        double result = 0;
        for (int i = 1; i < 10000000; i++) {
            result += Math.exp(Math.log(i) / root);
        }
        return result;
    }
    
    static class CalculadorCallable implements Callable<Double> {
        private int root;
        
        public CalculadorCallable(int root) {
            this.root = root;
        }
        
        @Override
        public Double call() {
            System.out.println("Tarea " + Thread.currentThread().getName() + " calculando root = " + root);
            double resultado = SumRootN(root);
            System.out.println("Tarea " + Thread.currentThread().getName() + " terminó. Resultado: " + resultado);
            return resultado;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Iniciando cálculo con ExecutorService...");
        long startTime = System.currentTimeMillis();
        
        // Crear ExecutorService con pool de hilos del tamaño del número de procesadores
        int numProcesadores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numProcesadores);
        
        List<Future<Double>> futures = new ArrayList<>();
        double[] resultados = new double[20];
        
        // Enviar todas las tareas al ExecutorService
        for (int i = 1; i <= 20; i++) {
            CalculadorCallable task = new CalculadorCallable(i);
            futures.add(executor.submit(task));
        }
        
        // Recoger resultados
        for (int i = 0; i < futures.size(); i++) {
            try {
                resultados[i] = futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error obteniendo resultado para root " + (i+1) + ": " + e.getMessage());
                resultados[i] = -1;
            }
        }
        
        // Apagar el ExecutorService
        executor.shutdown();
        
        try {
            // Esperar a que todas las tareas terminen
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        System.out.println("\n--- RESUMEN EJECUCIÓN CON EXECUTORSERVICE ---");
        System.out.println("Número de procesadores: " + numProcesadores);
        System.out.println("Tamaño del pool de hilos: " + numProcesadores);
        System.out.println("Tiempo total de ejecución: " + totalTime + " ms");
        System.out.println("Tiempo total de ejecución: " + (totalTime / 1000.0) + " segundos");
        
        // Mostrar resultados
        System.out.println("\nResultados obtenidos:");
        for (int i = 0; i < resultados.length; i++) {
            System.out.println("Root " + (i+1) + ": " + resultados[i]);
        }
    }
}

// -> Menos moderno

//import java.util.concurrent.Callable;
//import java.util.concurrent.FutureTask;
//
//public class MiCallable implements Callable<String> {
//    @Override
//    public String call() throws Exception {
//        return "Resultado del hilo: " + Thread.currentThread().getName();
//    }
//}
//
//// Uso
//FutureTask<String> futureTask = new FutureTask<>(new MiCallable());
//Thread hilo = new Thread(futureTask);
//hilo.start();
//String resultado = futureTask.get(); // Bloquea hasta obtener resultado