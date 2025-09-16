package EJE3;

public class Main {
    public static void main(String[] args) {
        // Crear una cuenta sueldo
        CuentaSueldo cuentaSueldo = new CuentaSueldo(12345678L, 200000.0, 2.5, 
                                                   12345, "Empresa S.A.", 
                                                   "Descuentos en comercios", 
                                                   123456789012345678L, 15000.0);
        
        System.out.println("=== CUENTA SUELDO CREADA ===");
        cuentaSueldo.mostrarDatos();
        
        System.out.println("\n=== PRUEBA DE INTERFACES ===");
        
        // Probar métodos de la interfaz OperacionesComunes
        System.out.println("\n1. Cambiar alias:");
        cuentaSueldo.cambiarAlias("mi.cuenta.sueldo");
        
        System.out.println("\n2. Pagar servicio:");
        cuentaSueldo.pagarServicio("Luz", 2500.0);
        
        // Probar métodos de la interfaz OperacionesImportantes
        System.out.println("\n3. Transferencia de alto monto:");
        cuentaSueldo.transferenciaAltoMonto(120000.0);
        
        // Probar método adicional de OperacionesBancarias
        System.out.println("\n4. Estado de cuenta:");
        System.out.println(cuentaSueldo.obtenerEstadoCuenta());
        
        System.out.println("\n=== PRUEBA DE LÍMITE DE OPERACIONES ===");
        for (int i = 0; i < 5; i++) {
            System.out.println("\nOperación " + (i + 1) + ":");
            cuentaSueldo.retirar(1000.0);
        }
        
        System.out.println("\n=== ESTADO FINAL ===");
        cuentaSueldo.mostrarDatos();
    }
}