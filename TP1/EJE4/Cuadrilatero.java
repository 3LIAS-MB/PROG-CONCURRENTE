package EJE4;

public class Cuadrilatero extends Forma {
    // Atributos específicos para un cuadrilátero
    private double lado1;
    private double lado2;
    private double lado3;
    private double lado4;
    private double diagonal; // Para cálculos de área en cuadriláteros irregulares
    private String tipo; // "cuadrado", "rectangulo", "rombo", "trapecio", "irregular"
    
    // Constructor para cuadrados (todos los lados iguales)
    public Cuadrilatero(double lado, String nombre) {
        super(nombre);
        this.lado1 = lado;
        this.lado2 = lado;
        this.lado3 = lado;
        this.lado4 = lado;
        this.tipo = "cuadrado";
        this.diagonal = lado * Math.sqrt(2); // Diagonal de un cuadrado
    }
    
    // Constructor para rectángulos (lados opuestos iguales)
    public Cuadrilatero(double base, double altura, String nombre) {
        super(nombre);
        this.lado1 = base;
        this.lado2 = altura;
        this.lado3 = base;
        this.lado4 = altura;
        this.tipo = "rectangulo";
        this.diagonal = Math.sqrt(base * base + altura * altura); // Teorema de Pitágoras
    }
    
    // Constructor para cuadriláteros irregulares
    public Cuadrilatero(double lado1, double lado2, double lado3, double lado4, 
                       double diagonal, String nombre, String tipo) {
        super(nombre);
        this.lado1 = lado1;
        this.lado2 = lado2;
        this.lado3 = lado3;
        this.lado4 = lado4;
        this.diagonal = diagonal;
        this.tipo = tipo;
    }
    
    // Implementación del método área (depende del tipo de cuadrilátero)
    @Override
    public double area() {
        switch (tipo.toLowerCase()) {
            case "cuadrado":
                return lado1 * lado1;
                
            case "rectangulo":
                return lado1 * lado2;
                
            case "rombo":
                // Área del rombo = (diagonal mayor * diagonal menor) / 2
                // Asumimos que la diagonal proporcionada es la mayor
                double diagonalMenor = Math.sqrt(4 * lado1 * lado1 - diagonal * diagonal);
                return (diagonal * diagonalMenor) / 2;
                
            case "trapecio":
                // Área del trapecio = ((base mayor + base menor) * altura) / 2
                // Asumimos que lado1 y lado3 son las bases, lado2 es la altura
                return ((lado1 + lado3) * lado2) / 2;
                
            case "irregular":
                // Fórmula de Herón para cuadriláteros irregulares (dividiendo en dos triángulos)
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
    
    // Implementación del método perímetro
    @Override
    public double perimetro() {
        return lado1 + lado2 + lado3 + lado4;
    }
    
    // Métodos getters y setters para los atributos específicos
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
    
    // Método para mostrar información detallada del cuadrilátero
    @Override
    public void mostrarInformacion() {
        System.out.println("=== INFORMACIÓN DEL CUADRILÁTERO ===");
        System.out.println("Nombre: " + getNombreForma());
        System.out.println("Tipo: " + tipo);
        System.out.println("Lados: " + lado1 + ", " + lado2 + ", " + lado3 + ", " + lado4);
        System.out.println("Diagonal: " + String.format("%.2f", diagonal));
        System.out.println("Área: " + String.format("%.2f", area()));
        System.out.println("Perímetro: " + String.format("%.2f", perimetro()));
        System.out.println("===================================");
    }
    
    // Método para verificar si es un cuadrilátero válido
    public boolean esValido() {
        // En un cuadrilátero, la suma de tres lados debe ser mayor que el cuarto lado
        return (lado1 + lado2 + lado3 > lado4) &&
               (lado1 + lado2 + lado4 > lado3) &&
               (lado1 + lado3 + lado4 > lado2) &&
               (lado2 + lado3 + lado4 > lado1);
    }
}