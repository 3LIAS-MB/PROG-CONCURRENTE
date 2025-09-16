package EJE4;

public class TestFormas {
    public static void main(String[] args) {
        System.out.println("=== PROGRAMA DE CÁLCULO DE FORMAS GEOMÉTRICAS ===\n");
        
        // Crear un cuadrado
        System.out.println("1. CUADRADO:");
        Cuadrilatero cuadrado = new Cuadrilatero(5.0, "Mi Cuadrado");
        cuadrado.mostrarInformacion();
        
        // Crear un rectángulo
        System.out.println("\n2. RECTÁNGULO:");
        Cuadrilatero rectangulo = new Cuadrilatero(6.0, 4.0, "Mi Rectángulo");
        rectangulo.mostrarInformacion();
        
        // Crear un cuadrilátero irregular
        System.out.println("\n3. CUADRILÁTERO IRREGULAR:");
        Cuadrilatero irregular = new Cuadrilatero(3.0, 4.0, 5.0, 6.0, 5.0, 
                                                "Cuadrilátero Irregular", "irregular");
        irregular.mostrarInformacion();
        
        // Verificar si el cuadrilátero es válido
        System.out.println("¿Es un cuadrilátero válido? " + irregular.esValido());
        
        // Demostración del polimorfismo
        System.out.println("\n4. DEMOSTRACIÓN DE POLIMORFISMO:");
        Forma forma1 = new Cuadrilatero(7.0, "Cuadrado Polimórfico");
        Forma forma2 = new Cuadrilatero(8.0, 3.0, "Rectángulo Polimórfico");
        
        System.out.println("Forma 1 - Área: " + String.format("%.2f", forma1.area()));
        System.out.println("Forma 1 - Perímetro: " + String.format("%.2f", forma1.perimetro()));
        
        System.out.println("Forma 2 - Área: " + String.format("%.2f", forma2.area()));
        System.out.println("Forma 2 - Perímetro: " + String.format("%.2f", forma2.perimetro()));
        
        // Array de formas
        System.out.println("\n5. ARRAY DE FORMAS:");
        Forma[] formas = {
            new Cuadrilatero(2.0, "Cuadrado Pequeño"),
            new Cuadrilatero(10.0, 5.0, "Rectángulo Grande"),
            new Cuadrilatero(3.0, 4.0, 5.0, 4.0, 5.0, "Trapecio", "trapecio")
        };
        
        for (int i = 0; i < formas.length; i++) {
            System.out.println("\nForma " + (i + 1) + ":");
            formas[i].mostrarInformacion();
        }
    }
}