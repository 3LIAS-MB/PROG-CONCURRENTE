package EJE1;

public class A_PatronImpresor {
    
    public void imprimirPatronX() {
        for (int i = 0; i < 100; i++) {
            System.out.print(i + "X");
        }
        System.out.println();
    }
    
    public void imprimirPatronY() {
        for (int i = 0; i < 100; i++) {
            System.out.print(i + "Y");
        }
        System.out.println();
    }
    
    // Método main para ejecutar la clase
    public static void main(String[] args) {
        A_PatronImpresor impresor = new A_PatronImpresor();
        
        System.out.println("=== IMPRESIÓN DE PATRONES SECUENCIAL ===");
        System.out.println("Patrón X:");
        impresor.imprimirPatronX();
        
        System.out.println("\nPatrón Y:");
        impresor.imprimirPatronY();
        
        System.out.println("\n=== FIN DE IMPRESIÓN SECUENCIAL ===");
    }
}