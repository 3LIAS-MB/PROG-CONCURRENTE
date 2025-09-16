package EJE6;

public class TestFormaInterfaz {
    public static void main(String[] args) {
        System.out.println("=== PROGRAMA DE CÁLCULO CON INTERFAZ FORMA ===\n");
        
        // Mostrar descripción estática de la interfaz
        Forma.mostrarDescripcion();
        System.out.println();
        
        // Crear cuadriláteros usando la interfaz Forma
        System.out.println("1. CUADRADO:");
        Forma cuadrado = new Cuadrilatero(5.0, "Mi Cuadrado");
        cuadrado.mostrarInformacion();
        
        System.out.println("2. RECTÁNGULO:");
        Forma rectangulo = new Cuadrilatero(6.0, 4.0, "Mi Rectángulo");
        rectangulo.mostrarInformacion();
        
        System.out.println("3. CUADRILÁTERO IRREGULAR:");
        Forma irregular = new Cuadrilatero(3.0, 4.0, 5.0, 6.0, 5.0, 
                                         "Cuadrilátero Irregular", "irregular");
        irregular.mostrarInformacion();
        
        // Acceder a métodos específicos de Cuadrilatero (necesita casting)
        System.out.println("4. MÉTODOS ESPECÍFICOS:");
        if (irregular instanceof Cuadrilatero) {
            Cuadrilatero cuadrilateroIrregular = (Cuadrilatero) irregular;
            System.out.println("¿Es válido? " + cuadrilateroIrregular.esValido());
            System.out.println("Tipo específico: " + cuadrilateroIrregular.getTipo());
        }
        
        // Usar métodos estáticos de la clase Cuadrilatero
        System.out.println("\n5. MÉTODOS ESTÁTICOS:");
        Forma cuadradoRapido = Cuadrilatero.crearCuadrado(8.0);
        Forma rectanguloRapido = Cuadrilatero.crearRectangulo(10.0, 5.0);
        
        cuadradoRapido.mostrarInformacion();
        rectanguloRapido.mostrarInformacion();
        
        // Array de formas usando polimorfismo con interfaz
        System.out.println("6. ARRAY DE FORMAS:");
        Forma[] formas = {
            new Cuadrilatero(2.0, "Cuadrado Pequeño"),
            new Cuadrilatero(10.0, 5.0, "Rectángulo Grande"),
            new Cuadrilatero(3.0, 4.0, 5.0, 4.0, 5.0, "Trapecio", "trapecio"),
            Cuadrilatero.crearCuadrado(7.0),
            Cuadrilatero.crearRectangulo(12.0, 8.0)
        };
        
        for (int i = 0; i < formas.length; i++) {
            System.out.println("\nForma " + (i + 1) + ":");
            formas[i].mostrarInformacion();
            System.out.println("Tipo: " + formas[i].getTipoForma());
        }
        
        // Calcular suma total de áreas y perímetros
        System.out.println("\n7. ESTADÍSTICAS:");
        double sumaAreas = 0;
        double sumaPerimetros = 0;
        
        for (Forma forma : formas) {
            sumaAreas += forma.area();
            sumaPerimetros += forma.perimetro();
        }
        
        System.out.println("Suma total de áreas: " + String.format("%.2f", sumaAreas) + " " + Forma.UNIDAD_MEDIDA + "²");
        System.out.println("Suma total de perímetros: " + String.format("%.2f", sumaPerimetros) + " " + Forma.UNIDAD_MEDIDA);
        System.out.println("Promedio de áreas: " + String.format("%.2f", sumaAreas / formas.length) + " " + Forma.UNIDAD_MEDIDA + "²");
        System.out.println("Promedio de perímetros: " + String.format("%.2f", sumaPerimetros / formas.length) + " " + Forma.UNIDAD_MEDIDA);
        
        // Demostrar acceso a constantes de la interfaz
        System.out.println("\n8. CONSTANTES DE LA INTERFAZ:");
        System.out.println("Unidad de medida: " + Forma.UNIDAD_MEDIDA);
        System.out.println("Valor de PI: " + Forma.PI);
    }
}