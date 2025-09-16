package EJE5;

import EJE4.Forma;

public class Circulo extends Forma {
    // Atributo específico para un círculo
    private double radio;
    private static final double PI = Math.PI;
    
    // Constructor
    public Circulo(double radio, String nombre) {
        super(nombre);
        if (radio <= 0) {
            throw new IllegalArgumentException("El radio debe ser un valor positivo");
        }
        this.radio = radio;
    }
    
    // Constructor con nombre por defecto
    public Circulo(double radio) {
        this(radio, "Círculo");
    }
    
    // Implementación del método área para un círculo
    @Override
    public double area() {
        return PI * radio * radio;
    }
    
    // Implementación del método perímetro para un círculo
    @Override
    public double perimetro() {
        return 2 * PI * radio;
    }
    
    // Métodos getters y setters
    public double getRadio() {
        return radio;
    }
    
    public void setRadio(double radio) {
        if (radio <= 0) {
            throw new IllegalArgumentException("El radio debe ser un valor positivo");
        }
        this.radio = radio;
    }
    
    public double getDiametro() {
        return 2 * radio;
    }
    
    public void setDiametro(double diametro) {
        if (diametro <= 0) {
            throw new IllegalArgumentException("El diámetro debe ser un valor positivo");
        }
        this.radio = diametro / 2;
    }
    
    // Método para calcular la circunferencia (sinónimo de perímetro para círculos)
    public double circunferencia() {
        return perimetro();
    }
    
    // Método para mostrar información detallada del círculo
    @Override
    public void mostrarInformacion() {
        System.out.println("=== INFORMACIÓN DEL CÍRCULO ===");
        System.out.println("Nombre: " + getNombreForma());
        System.out.println("Radio: " + String.format("%.2f", radio));
        System.out.println("Diámetro: " + String.format("%.2f", getDiametro()));
        System.out.println("Área: " + String.format("%.2f", area()));
        System.out.println("Perímetro: " + String.format("%.2f", perimetro()));
        System.out.println("Circunferencia: " + String.format("%.2f", circunferencia()));
        System.out.println("Valor de PI usado: " + PI);
        System.out.println("===============================");
    }
    
    // Métodos estáticos para cálculos rápidos
    public static double calcularArea(double radio) {
        return PI * radio * radio;
    }
    
    public static double calcularPerimetro(double radio) {
        return 2 * PI * radio;
    }
    
    public static double calcularRadioDesdeArea(double area) {
        return Math.sqrt(area / PI);
    }
    
    public static double calcularRadioDesdePerimetro(double perimetro) {
        return perimetro / (2 * PI);
    }
}