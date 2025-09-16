package EJE6;

public interface Forma {
    // Constantes (son automáticamente public, static y final)
    String UNIDAD_MEDIDA = "unidades";
    double PI = Math.PI;
    
    // Métodos abstractos (son automáticamente public y abstract)
    double area();
    double perimetro();
    
    // Método default con implementación
    default void mostrarInformacion() {
        System.out.println("=== INFORMACIÓN DE LA FORMA ===");
        System.out.println("Tipo: " + getTipoForma());
        System.out.println("Área: " + String.format("%.2f", area()) + " " + UNIDAD_MEDIDA + "²");
        System.out.println("Perímetro: " + String.format("%.2f", perimetro()) + " " + UNIDAD_MEDIDA);
        System.out.println("===============================");
    }
    
    // Método estático
    static void mostrarDescripcion() {
        System.out.println("Interfaz Forma para calcular áreas y perímetros de figuras geométricas");
    }
    
    // Método que debe ser implementado para obtener el tipo de forma
    String getTipoForma();
}