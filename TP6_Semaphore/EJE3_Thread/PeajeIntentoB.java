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

class ControladorCabinas {
    private Semaphore[] cabinas;
    private boolean[] disponibles;
    
    public ControladorCabinas(int numCabinas) {
        cabinas = new Semaphore[numCabinas];
        disponibles = new boolean[numCabinas];
        
        for (int i = 0; i < numCabinas; i++) {
            cabinas[i] = new Semaphore(1);
            disponibles[i] = true;
        }
    }
    
    public void bloquearCabina(int numeroCabina) throws InterruptedException {
        cabinas[numeroCabina].acquire();
        disponibles[numeroCabina] = false;
    }
    
    public void habilitarCabina(int numeroCabina) {
        disponibles[numeroCabina] = true;
        cabinas[numeroCabina].release();
    }
    
    public int adquirirCabina() throws InterruptedException {
        while (true) {
            for (int i = 0; i < cabinas.length; i++) {
                if (disponibles[i] && cabinas[i].tryAcquire()) {
                    return i;
                }
            }
            Thread.sleep(50);
        }
    }
    
    public void liberarCabina(int numeroCabina) {
        cabinas[numeroCabina].release();
    }
}

class ClientePeajeB extends Thread {
    private int numeroCliente;
    private ControladorCabinas controlador;
    private Random random;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public ClientePeajeB(int numeroCliente, ControladorCabinas controlador) {
        this.numeroCliente = numeroCliente;
        this.controlador = controlador;
        this.random = new Random();
    }
    
    @Override
    public void run() {
        try {
            int numeroCabina = controlador.adquirirCabina();
            System.out.println("[" + LocalTime.now().format(formatter) + "] Cliente " +
            numeroCliente + " comienza atencion en cabina " + (numeroCabina + 1));
            
            int tiempoAtencion = 1000 + random.nextInt(2000);
            Thread.sleep(tiempoAtencion);
            
            System.out.println("[" + LocalTime.now().format(formatter) + "] Cliente " +
            numeroCliente + " finaliza atencion en cabina " + (numeroCabina + 1));
            controlador.liberarCabina(numeroCabina);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class PeajeIntentoB {
    public static void main(String[] args) {
        ControladorCabinas controlador = new ControladorCabinas(3);
        
        try {
            controlador.bloquearCabina(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        	
        ClientePeajeB[] clientes = new ClientePeajeB[50];
        for (int i = 0; i < 50; i++) {
            clientes[i] = new ClientePeajeB(i + 1, controlador);
            clientes[i].start();
        }
        
        new Thread(() -> {
            try {
                Thread.sleep(15000);
                controlador.habilitarCabina(2);
                System.out.println("Cabina 3 disponible");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
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