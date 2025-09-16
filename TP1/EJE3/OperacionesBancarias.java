package EJE3;

public interface OperacionesBancarias extends OperacionesComunes, OperacionesImportantes {
    // Esta interfaz hereda todos los métodos de ambas interfaces padres
    // Puede agregar métodos adicionales si es necesario
    String obtenerEstadoCuenta();
}