package EJE4_Thread;

import java.util.Scanner;

public class FraseConHilos {
    
    // Hilo para imprimir un carácter específico
    class HiloCaracter extends Thread {
        private char caracter;
        private int repeticiones;
        
        public HiloCaracter(char caracter, int repeticiones) {
            this.caracter = caracter;
            this.repeticiones = repeticiones;
        }
        
        @Override
        public void run() {
            for (int i = 0; i < repeticiones; i++) {
                System.out.print(caracter);
                try {
                    // Pequeña pausa para ver mejor el efecto
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void ejecutar() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Ingrese una frase: ");
        String frase = scanner.nextLine();
        
        System.out.println("\nImprimiendo 10 veces carácter por carácter:");
        System.out.println("============================================");
        
        for (int vez = 1; vez <= 10; vez++) {
            System.out.print("Vez " + vez + ": ");
            
            // Crear un hilo por cada carácter de la frase
            Thread[] hilos = new Thread[frase.length()];
            
            for (int i = 0; i < frase.length(); i++) {
                char c = frase.charAt(i);
                hilos[i] = new HiloCaracter(c, 1);
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
            
            System.out.println(); // Salto de línea después de cada frase
        }
        
        scanner.close();
    }
    
    public static void main(String[] args) {
        System.out.println("=== PUNTO 4 - FRASE CARÁCTER POR CARÁCTER CON HILOS ===");
        
        FraseConHilos fraseHilos = new FraseConHilos();
        fraseHilos.ejecutar();
    }
}