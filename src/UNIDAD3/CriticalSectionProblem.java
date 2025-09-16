package UNIDAD3;

public class CriticalSectionProblem {
    
    // Recurso compartido (región crítica)
    static class ContadorCompartido {
        private int valor = 0;
        
        public void incrementar() {
            // Simula una operación no atómica
            int temp = valor;
            try {
                Thread.sleep(10); // Simula procesamiento
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            valor = temp + 1;
        }
        
        public int getValor() {
            return valor;
        }
    }
    
    static class HiloIncrementador extends Thread {
        private ContadorCompartido contador;
        private String nombre;
        private int incrementos;
        
        public HiloIncrementador(String nombre, ContadorCompartido contador, int incrementos) {
            this.nombre = nombre;
            this.contador = contador;
            this.incrementos = incrementos;
        }
        
        @Override
        public void run() {
            for (int i = 0; i < incrementos; i++) {
                contador.incrementar();
                System.out.println(nombre + " incrementó. Valor actual: " + contador.getValor());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== PROBLEMA DE REGIÓN CRÍTICA ===");
        System.out.println("Sin sincronización - Resultado inconsistente esperado");
        
        ContadorCompartido contador = new ContadorCompartido();
        
        HiloIncrementador hilo1 = new HiloIncrementador("Hilo-A", contador, 5);
        HiloIncrementador hilo2 = new HiloIncrementador("Hilo-B", contador, 5);
        
        hilo1.start();
        hilo2.start();
        
        hilo1.join();
        hilo2.join();
        
        System.out.println("\nValor final esperado: 10");
        System.out.println("Valor final obtenido: " + contador.getValor());
        System.out.println("¡Problema de condición de carrera detectado!");
    }
}