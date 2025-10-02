package EJE4_Thread;

import java.util.Scanner;

//	4) Debe solicitar el ingreso de una frase por la entrada de teclado, a continuación (una vez 
//	apretado Enter) deberá imprimir 10 veces dicha frase pero carácter por carácter 
//	empleando hilos. 


public class FraseConHilos {
    
    // Hilo para imprimir la frase completa una vez
    class HiloFrase extends Thread {
        private String frase;
        
        public HiloFrase(String frase) {
            this.frase = frase;
        }
        
        @Override
        public void run() {
            for (char c : frase.toCharArray()) {
                System.out.print(c);
                try {
                    Thread.sleep(20); // Pausa entre caracteres
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(); // Salto de línea al terminar
        }
    }
    
    public void ejecutar() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Ingrese una frase: ");
        String frase = scanner.nextLine();
        
        System.out.println("\nImprimiendo 10 veces con hilos:");
        System.out.println("================================");
        
        // Crear 10 hilos, uno por cada repetición
        Thread[] hilos = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            hilos[i] = new HiloFrase(frase);
            hilos[i].start();
        }
        
        // Esperar que todos los hilos terminen
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }
    
    public static void main(String[] args) {
        FraseConHilos app = new FraseConHilos();
        app.ejecutar();
    }
}