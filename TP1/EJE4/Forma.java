package EJE4;

public abstract class Forma {
    // Atributo protegido para que las clases hijas puedan acceder
    protected String nombreForma;
    
    // Constructor
    public Forma(String nombreForma) {
        this.nombreForma = nombreForma;
    }
    
    // Métodos abstractos que deben ser implementados por las clases hijas
    public abstract double area();
    public abstract double perimetro();
    
    // Método concreto para obtener el nombre de la forma
    public String getNombreForma() {
        return nombreForma;
    }
    
    public void setNombreForma(String nombreForma) {
        this.nombreForma = nombreForma;
    }
    
    // Método para mostrar información básica de la forma
    public void mostrarInformacion() {
        System.out.println("Forma: " + nombreForma);
        System.out.println("Área: " + String.format("%.2f", area()));
        System.out.println("Perímetro: " + String.format("%.2f", perimetro()));
    }
}