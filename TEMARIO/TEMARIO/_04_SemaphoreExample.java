package TEMARIO;

import java.util.concurrent.Semaphore;

public class _04_SemaphoreExample {
    public static void main(String[] args) {
        // Semáforo que permite 3 accesos simultáneos
        Semaphore semaforo = new Semaphore(3);
        
        for (int i = 1; i <= 10; i++) {
            new Thread(new UsuarioRecurso(i, semaforo)).start();
        }
    }
}

class UsuarioRecurso implements Runnable {
    private int id;
    private Semaphore semaforo;
    
    public UsuarioRecurso(int id, Semaphore semaforo) {
        this.id = id;
        this.semaforo = semaforo;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Usuario " + id + " intentando acceder al recurso");
            
            semaforo.acquire(); // Solicita permiso
            
            System.out.println("Usuario " + id + " ACCEDIÓ al recurso. Permisos disponibles: " 
                             + semaforo.availablePermits());
            
            // Simular uso del recurso
            Thread.sleep(2000);
            
            System.out.println("Usuario " + id + " LIBERANDO el recurso");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforo.release(); // Libera el permiso
        }
    }
}