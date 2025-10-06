package EJE3_Thread;

import java.util.concurrent.Semaphore;
import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//	3) Simule la atención en una estación de peaje donde existen 3 cabinas. Cada cabina demora 
//	en atender a un cliente un tiempo variable entre 1” y 3”. Suponga que la cola de espera es 
//	de 50 autos. Deberá indicar el Número de cliente que es atendido, cuando comienza la 
//	atención y cuando finaliza la misma. Y para complicarnos la vida, supongamos además 
//	que una de las 3 cabinas inicia NO Disponible, es decir, el empleado de una de las 
//	cabinas fue al baño y vuelve 15” después de haber iniciado la atención sus dos 
//	compañeros. 

//	a. En el primer intento la ejecución no individualiza cada cabina. 

//	b. En el segundo intento se le pide que individualice cada cabina, es decir, 
//	necesitamos saber que cabina atiende a cada cliente, ¿será esto posible? ¿De qué 
//	modo cree que podría resolverlo?  

class ClientePeajeA extends Thread {
    private int numeroCliente;
    private Semaphore cabinas;
    private Random random;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public ClientePeajeA(int numeroCliente, Semaphore cabinas) {
        this.numeroCliente = numeroCliente;
        this.cabinas = cabinas;
        this.random = new Random();
    }
     
    @Override
    public void run() {
        try {
            cabinas.acquire();
            
            System.out.println("[" + LocalTime.now().format(formatter) + "] Cliente " + numeroCliente + " comienza atencion");
            
            int tiempoAtencion = 1000 + random.nextInt(2000);
            Thread.sleep(tiempoAtencion);
            
            System.out.println("[" + LocalTime.now().format(formatter) + "] Cliente " + numeroCliente + " finaliza atencion");
            
            cabinas.release();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class PeajeIntentoA {
    public static void main(String[] args) {
        Semaphore cabinas = new Semaphore(2);
        
        ClientePeajeA[] clientes = new ClientePeajeA[50];
        for (int i = 0; i < 50; i++) {
            clientes[i] = new ClientePeajeA(i + 1, cabinas);
            clientes[i].start();
        }
        
        new Thread(() -> {
            try {
                Thread.sleep(15000);
                cabinas.release();
                System.out.println("Cabina 3 disponible");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
//      new Thread(new Runnable() {
//      @Override
//      public void run() {
//          try {
//              System.out.println("\n>>> Empleado fue al baño, cabina 3 no disponible...\n");
//              Thread.sleep(15000);
//              cabinas.release();
//              System.out.println("\n>>> Empleado regresó! Cabina 3 ahora disponible\n");
//          } catch (InterruptedException e) {
//              e.printStackTrace();
//          }
//      }
//  }).start();
        
        for (int i = 0; i < 50; i++) {
            try {
                clientes[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Todos los clientes atendidos");
    }
}