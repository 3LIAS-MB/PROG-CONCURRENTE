package EJE6;

public class Cuadrilatero implements Forma {
    // Atributos
    private String nombre;
    private double lado1;
    private double lado2;
    private double lado3;
    private double lado4;
    private double diagonal;
    private String tipo;
    
    // Constructor para cuadrados
    public Cuadrilatero(double lado, String nombre) {
        this.nombre = nombre;
        this.lado1 = lado;
        this.lado2 = lado;
        this.lado3 = lado;
        this.lado4 = lado;
        this.tipo = "cuadrado";
        this.diagonal = lado * Math.sqrt(2);
    }
    
    // Constructor para rectángulos
    public Cuadrilatero(double base, double altura, String nombre) {
        this.nombre = nombre;
        this.lado1 = base;
        this.lado2 = altura;
        this.lado3 = base;
        this.lado4 = altura;
        this.tipo = "rectangulo";
        this.diagonal = Math.sqrt(base * base + altura * altura);
    }
    
    // Constructor para cuadriláteros irregulares
    public Cuadrilatero(double lado1, double lado2, double lado3, double lado4, 
                       double diagonal, String nombre, String tipo) {
        this.nombre = nombre;
        this.lado1 = lado1;
        this.lado2 = lado2;
        this.lado3 = lado3;
        this.lado4 = lado4;
        this.diagonal = diagonal;
        this.tipo = tipo;
    }
    
    // Implementación de los métodos de la interfaz Forma
    @Override
    public double area() {
        switch (tipo.toLowerCase()) {
            case "cuadrado":
                return lado1 * lado1;
                
            case "rectangulo":
                return lado1 * lado2;
                
            case "rombo":
                double diagonalMenor = Math.sqrt(4 * lado1 * lado1 - diagonal * diagonal);
                return (diagonal * diagonalMenor) / 2;
                
            case "trapecio":
                return ((lado1 + lado3) * lado2) / 2;
                
            case "irregular":
                double semiPerimetro1 = (lado1 + lado2 + diagonal) / 2;
                double areaTriangulo1 = Math.sqrt(semiPerimetro1 * 
                                                (semiPerimetro1 - lado1) * 
                                                (semiPerimetro1 - lado2) * 
                                                (semiPerimetro1 - diagonal));
                
                double semiPerimetro2 = (lado3 + lado4 + diagonal) / 2;
                double areaTriangulo2 = Math.sqrt(semiPerimetro2 * 
                                                (semiPerimetro2 - lado3) * 
                                                (semiPerimetro2 - lado4) * 
                                                (semiPerimetro2 - diagonal));
                
                return areaTriangulo1 + areaTriangulo2;
                
            default:
                return 0;
        }
    }
    
    @Override
    public double perimetro() {
        return lado1 + lado2 + lado3 + lado4;
    }
    
    @Override
    public String getTipoForma() {
        return "Cuadrilátero (" + tipo + ") - " + nombre;
    }
    
    // Sobrescribir el método default para mostrar información más detallada
    @Override
    public void mostrarInformacion() {
        System.out.println("=== INFORMACIÓN DEL CUADRILÁTERO ===");
        System.out.println("Nombre: " + nombre);
        System.out.println("Tipo: " + tipo);
        System.out.println("Lados: " + lado1 + ", " + lado2 + ", " + lado3 + ", " + lado4);
        System.out.println("Diagonal: " + String.format("%.2f", diagonal));
        System.out.println("Área: " + String.format("%.2f", area()) + " " + UNIDAD_MEDIDA + "²");
        System.out.println("Perímetro: " + String.format("%.2f", perimetro()) + " " + UNIDAD_MEDIDA);
        System.out.println("===================================");
    }
    
    // Métodos getters y setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public double getLado1() {
        return lado1;
    }
    
    public void setLado1(double lado1) {
        this.lado1 = lado1;
    }
    
    public double getLado2() {
        return lado2;
    }
    
    public void setLado2(double lado2) {
        this.lado2 = lado2;
    }
    
    public double getLado3() {
        return lado3;
    }
    
    public void setLado3(double lado3) {
        this.lado3 = lado3;
    }
    
    public double getLado4() {
        return lado4;
    }
    
    public void setLado4(double lado4) {
        this.lado4 = lado4;
    }
    
    public double getDiagonal() {
        return diagonal;
    }
    
    public void setDiagonal(double diagonal) {
        this.diagonal = diagonal;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    // Método para verificar si es un cuadrilátero válido
    public boolean esValido() {
        return (lado1 + lado2 + lado3 > lado4) &&
               (lado1 + lado2 + lado4 > lado3) &&
               (lado1 + lado3 + lado4 > lado2) &&
               (lado2 + lado3 + lado4 > lado1);
    }
    
    // Método estático para crear un cuadrado rápido
    public static Cuadrilatero crearCuadrado(double lado) {
        return new Cuadrilatero(lado, "Cuadrado de " + lado + UNIDAD_MEDIDA);
    }
    
    // Método estático para crear un rectángulo rápido
    public static Cuadrilatero crearRectangulo(double base, double altura) {
        return new Cuadrilatero(base, altura, "Rectángulo " + base + "x" + altura + UNIDAD_MEDIDA);
    }
}