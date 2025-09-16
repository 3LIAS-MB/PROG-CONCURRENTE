package EJE5;

import EJE4.Forma;

public class TestCirculo {
    public static void main(String[] args) {
        System.out.println("=== PROGRAMA DE CÁLCULO DE CÍRCULOS ===\n");
        
        // Crear un círculo
        System.out.println("1. CÍRCULO BÁSICO:");
        Circulo circulo1 = new Circulo(5.0, "Mi Círculo");
        circulo1.mostrarInformacion();
        
        // Crear otro círculo con constructor simplificado
        System.out.println("\n2. CÍRCULO CON CONSTRUCTOR SIMPLIFICADO:");
        Circulo circulo2 = new Circulo(7.5);
        circulo2.mostrarInformacion();
        
        // Demostración del polimorfismo
        System.out.println("\n3. DEMOSTRACIÓN DE POLIMORFISMO:");
        Forma forma = new Circulo(3.0, "Círculo Polimórfico");
        
        System.out.println("Nombre de la forma: " + forma.getNombreForma());
        System.out.println("Área: " + String.format("%.2f", forma.area()));
        System.out.println("Perímetro: " + String.format("%.2f", forma.perimetro()));
        
        // Usar métodos específicos de Circulo (necesita casting)
        if (forma instanceof Circulo) {
            Circulo circuloPolimorfico = (Circulo) forma;
            System.out.println("Radio: " + String.format("%.2f", circuloPolimorfico.getRadio()));
            System.out.println("Diámetro: " + String.format("%.2f", circuloPolimorfico.getDiametro()));
        }
        
        // Probar métodos estáticos
        System.out.println("\n4. MÉTODOS ESTÁTICOS:");
        double radioEjemplo = 10.0;
        System.out.println("Para radio = " + radioEjemplo + ":");
        System.out.println("Área calculada: " + String.format("%.2f", Circulo.calcularArea(radioEjemplo)));
        System.out.println("Perímetro calculado: " + String.format("%.2f", Circulo.calcularPerimetro(radioEjemplo)));
        
        // Cálculos inversos
        System.out.println("\n5. CÁLCULOS INVERSOS:");
        double areaEjemplo = 78.54;
        double perimetroEjemplo = 31.42;
        
        System.out.println("Para área = " + areaEjemplo + ", radio = " + 
                          String.format("%.2f", Circulo.calcularRadioDesdeArea(areaEjemplo)));
        System.out.println("Para perímetro = " + perimetroEjemplo + ", radio = " + 
                          String.format("%.2f", Circulo.calcularRadioDesdePerimetro(perimetroEjemplo)));
        
        // Array de formas que incluye círculos
        System.out.println("\n6. ARRAY DE FORMAS (POLIMORFISMO):");
        Forma[] formas = {
            new Circulo(2.0, "Círculo Pequeño"),
            new Circulo(8.0, "Círculo Mediano"),
            new Circulo(15.0, "Círculo Grande")
        };
        
        for (int i = 0; i < formas.length; i++) {
            System.out.println("\nForma " + (i + 1) + ":");
            formas[i].mostrarInformacion();
        }
        
        // Probar modificación de propiedades
        System.out.println("\n7. MODIFICACIÓN DE PROPIEDADES:");
        Circulo circuloModificable = new Circulo(4.0, "Círculo Modificable");
        circuloModificable.mostrarInformacion();
        
        // Cambiar el radio
        circuloModificable.setRadio(6.0);
        System.out.println("\nDespués de cambiar el radio a 6.0:");
        circuloModificable.mostrarInformacion();
        
        // Cambiar el diámetro
        circuloModificable.setDiametro(20.0);
        System.out.println("\nDespués de cambiar el diámetro a 20.0:");
        circuloModificable.mostrarInformacion();
        
        // Probar validación de valores inválidos
        System.out.println("\n8. VALIDACIÓN DE DATOS:");
        try {
            Circulo circuloInvalido = new Circulo(-5.0, "Círculo Inválido");
        } catch (IllegalArgumentException e) {
            System.out.println("Error al crear círculo: " + e.getMessage());
        }
        
        try {
            Circulo circuloValido = new Circulo(5.0);
            circuloValido.setRadio(-2.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al modificar radio: " + e.getMessage());
        }
    }
}