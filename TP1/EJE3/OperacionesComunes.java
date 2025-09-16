package EJE3;

public interface OperacionesComunes {
    // Atributos (son automáticamente public, static y final)
    String TIPO_CUENTA = "Bancaria";
    double COMISION_BASICA = 50.0;
    int MAX_OPERACIONES_DIARIAS = 10;
    
    // Métodos (son automáticamente public y abstract)
    void pagarServicio(String servicio, double monto);
    boolean cambiarAlias(String nuevoAlias);
}