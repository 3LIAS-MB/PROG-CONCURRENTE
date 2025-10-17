package EJE2;

import java.util.concurrent.Semaphore;

//	2) Una planta de ensamblado de dispositivos posee 3 mesas donde cada una permite el 
//	ensamblado de un único dispositivo al mismo tiempo, pero simultáneamente. El armado de un 
//	dispositivo transita por 3 fases, la Fase 1 requiere el empleo de un destornillador y una pinza, la 
//	Fase 2 requiere el empleo de dos sargentos y la Fase 3 requiere el empleo de dos pinzas. Cada 
//	Fase si bien independientes, se realizan en la misma mesa y en el orden mencionado (Fase 1, 2 
//	y 3), y como habrá observado, con herramientas distintas. Se desea armar un lote de 100 
//	componentes y la empresa solo cuenta con 4 pinzas, 2 destornilladores y 4 sargentos. El 
//	tiempo que insume cada fase está dado por: Fase 1 es T, el de la Fase 2 es ½ de T y el de la Fase 
//	3 es 2T. Simula la situación empleando semáforos y muestre por pantalla cada etapa del 
//	proceso indicando el estado de avance de cada componente a medida que transita por las 
//	distintas etapas. Tome T = 400ms.  

public class Ejercicio2_PlantaEnsamblado_Thread {
    
    // Recursos compartidos (herramientas)
    private static Semaphore pinzas = new Semaphore(4);
    private static Semaphore destornilladores = new Semaphore(2);
    private static Semaphore sargentos = new Semaphore(4);
    
    // Mesas disponibles
    private static Semaphore mesas = new Semaphore(3);
    
    private static final int T = 400; // Tiempo base en ms
    
    public static void main(String[] args) {
        // Crear 100 componentes
        for (int i = 1; i <= 100; i++) {
            Thread componente = new ComponenteThread(i);
            componente.start();
        }
    }
    
    // Clase que hereda de Thread
    static class ComponenteThread extends Thread {
        private int numComponente;
        
        public ComponenteThread(int num) {
            this.numComponente = num;
        }
        
        @Override
        public void run() {
            try {
                // Esperar por una mesa disponible
                mesas.acquire();
                System.out.println("Componente " + numComponente + " - Ocupó una mesa");
                
                // FASE 1: Requiere 1 destornillador y 1 pinza - Tiempo: T
                destornilladores.acquire();
                pinzas.acquire();
                System.out.println("Componente " + numComponente + " - FASE 1 iniciada");
                Thread.sleep(T);
                System.out.println("Componente " + numComponente + " - FASE 1 completada");
                destornilladores.release();
                pinzas.release();
                
                // FASE 2: Requiere 2 sargentos - Tiempo: T/2
                sargentos.acquire(2);
                System.out.println("Componente " + numComponente + " - FASE 2 iniciada");
                Thread.sleep(T / 2);
                System.out.println("Componente " + numComponente + " - FASE 2 completada");
                sargentos.release(2);
                
                // FASE 3: Requiere 2 pinzas - Tiempo: 2T
                pinzas.acquire(2);
                System.out.println("Componente " + numComponente + " - FASE 3 iniciada");
                Thread.sleep(2 * T);
                System.out.println("Componente " + numComponente + " - FASE 3 completada");
                pinzas.release(2);
                
                // Liberar la mesa
                System.out.println("Componente " + numComponente + " - TERMINADO, liberó la mesa");
                mesas.release();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}