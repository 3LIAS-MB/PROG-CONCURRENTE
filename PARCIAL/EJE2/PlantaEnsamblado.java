package EJE2;

import java.util.concurrent.Semaphore;

public class PlantaEnsamblado {
    private static final int CANTIDAD_COMPONENTES = 100;
    private static final int T = 400; // 400ms

    // Recursos Limitados
    private static final Semaphore SEM_MESAS = new Semaphore(3); 
    private static final Semaphore SEM_DESTORNILLADORES = new Semaphore(2);
    private static final Semaphore SEM_PINZAS = new Semaphore(4);
    private static final Semaphore SEM_SARGENTOS = new Semaphore(4);

    public static void main(String[] args) {
        System.out.println("--- Planta de Ensamblado Iniciada (Lote de " + CANTIDAD_COMPONENTES + ") ---");
        
        // Ejecutar los 100 componentes concurrentemente
        for (int i = 1; i <= CANTIDAD_COMPONENTES; i++) {
            new Thread(new Componente(i), "Comp-" + i).start();
        }
    }

    static class Componente implements Runnable {
        private final int id;
        public Componente(int id) { this.id = id; }

        private void simularFase(int ms) throws InterruptedException {
            Thread.sleep(ms);
        }

        @Override
        public void run() {
            try {
                // --------------------- INICIO DEL PROCESO ---------------------
                
                // 1. Obtener una MESA para empezar (solo 3 componentes a la vez)
                SEM_MESAS.acquire();
                System.out.printf("[%s] [Mesa Asignada] -> Esperando FASE 1.\n", Thread.currentThread().getName());

                // --------------------- FASE 1: Destornillador (1) y Pinza (1) ---------------------
                // Se deben adquirir ambas herramientas antes de simular la fase
                SEM_DESTORNILLADORES.acquire(); 
                SEM_PINZAS.acquire();
                System.out.printf("[%s] FASE 1: Iniciada (Des: %d, Pin: %d).\n", 
                                Thread.currentThread().getName(), SEM_DESTORNILLADORES.availablePermits(), SEM_PINZAS.availablePermits());
                simularFase(T);
                
                SEM_DESTORNILLADORES.release(); 
                SEM_PINZAS.release();
                System.out.printf("[%s] FASE 1: Finalizada. Liberó Destornillador y Pinza.\n", Thread.currentThread().getName());

                // --------------------- FASE 2: Sargentos (2) ---------------------
                // Se necesitan 2 permisos de sargentos
                SEM_SARGENTOS.acquire(2); 
                System.out.printf("[%s] FASE 2: Iniciada (Sarg: %d).\n", 
                                Thread.currentThread().getName(), SEM_SARGENTOS.availablePermits());
                simularFase(T / 2);
                
                SEM_SARGENTOS.release(2); 
                System.out.printf("[%s] FASE 2: Finalizada. Liberó 2 Sargentos.\n", Thread.currentThread().getName());

                // --------------------- FASE 3: Pinzas (2) ---------------------
                // Se necesitan 2 permisos de pinzas
                SEM_PINZAS.acquire(2); 
                System.out.printf("[%s] FASE 3: Iniciada (Pin: %d).\n", 
                                Thread.currentThread().getName(), SEM_PINZAS.availablePermits());
                simularFase(T * 2);
                
                SEM_PINZAS.release(2);
                System.out.printf("[%s] FASE 3: Finalizada. Liberó 2 Pinzas.\n", Thread.currentThread().getName());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                SEM_MESAS.release(); // Liberar la mesa al finalizar
                System.out.printf("[%s] 🏅 DISPOSITIVO ENSAMBLADO. Mesa liberada.\n", Thread.currentThread().getName());
            }
        }
    }
}