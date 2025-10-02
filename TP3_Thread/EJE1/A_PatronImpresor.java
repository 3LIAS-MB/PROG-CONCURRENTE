package EJE1;

//	1) Implemente lo siguiente: 

//	a. Una clase que imprima el siguiente patrón de sucesión de X: “0X1X2X3X…” (iX 
//	para i=0……99), a continuación debe imprimir un patrón de sucesión de Y: 
//	“0Y1Y2Y3Y….” (iY para i=0……99). 

//	b. Mediante Hilos heredando de Thread, emplee dos clases para realizar dicha 
//	impresión de forma concurrente. Repita el proceso 10 veces para observar cómo 
//	se imprimen los distintos lanzamientos. 

//	c. Obtenga la porción de la fila que posea la sucesión más larga de cualquier letra. 
//	Imprima toda la sucesión. 

//	d. Si en el inciso b) no se imprimen correctamente utilice una pausa o join. 
//	e. Razone y comente los resultados obtenidos. 

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