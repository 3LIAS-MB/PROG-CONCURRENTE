package EJE1;

// Hilo para imprimir patron X
class HiloPatronX extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.print(i + "X");
        }
    }
}

// Hilo para imprimir patron Y
class HiloPatronY extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.print(i + "Y");
        }
    }
}

// -> Punto 1d - Versión con pausa (sleep)

//class HiloPatronXConPausa extends Thread {
//    @Override
//    public void run() {
//        for (int i = 0; i < 100; i++) {
//            System.out.print(i + "X");
//            try {
//                Thread.sleep(1); // Pequeña pausa
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
//class HiloPatronYConPausa extends Thread {
//    @Override
//    public void run() {
//        for (int i = 0; i < 100; i++) {
//            System.out.print(i + "Y");
//            try {
//                Thread.sleep(1); // Pequeña pausa
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}


// Clase principal para ejecutar 10 veces
public class B_MainPunto {
    public static void main(String[] args) {
        System.out.println("Ejecutando 10 veces con hilos:");
        System.out.println("==============================");
        
        for (int ejecucion = 1; ejecucion <= 10; ejecucion++) {
            System.out.println("\n--- Ejecución " + ejecucion + " ---");
            
            HiloPatronX hiloX = new HiloPatronX();
            HiloPatronY hiloY = new HiloPatronY();
            
            hiloX.start();
            hiloY.start();
            
            // Punto 1d - Usar join para esperar que terminen
            try {
                hiloX.join();
                hiloY.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println(); // Salto de línea entre ejecuciones
        }
    }
}