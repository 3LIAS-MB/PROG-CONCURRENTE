package EJE3;

public class Pasillo {
	private boolean estaOcupado = false;

    public void usarPasillo() throws InterruptedException {
        synchronized (this) {
            while (estaOcupado) {
                wait();
            }
            estaOcupado = true;
        }
        Thread.sleep(500); //50
        
        synchronized (this) { 
            estaOcupado = false;
            notifyAll(); 
        }
    }
}
