package EJE2;

public class Main {
    public static void main(String[] args) {
        // Crear una cuenta sueldo
        CuentaSueldo cuentaSueldo = new CuentaSueldo(12345678L, 20000.0, 2.5, 
                                                   12345, "Empresa S.A.", 
                                                   "Descuentos en comercios", 
                                                   123456789012345678L, 15000.0);
        
        System.out.println("=== CUENTA SUELDO CREADA ===");
        cuentaSueldo.mostrarDatos();
        
        System.out.println("\n=== PRUEBA DE RETIRO DENTRO DEL TOPE ===");
        cuentaSueldo.retirar(8000.0);
        
        System.out.println("\n=== PRUEBA DE RETIRO FUERA DEL TOPE ===");
        cuentaSueldo.retirar(16000.0);
        
        System.out.println("\n=== PRUEBA DE TRANSFERENCIA CON CBU ===");
        cuentaSueldo.transferir(3000.0, 987654321098765432L);
        
        System.out.println("\n=== PRUEBA DE TRANSFERENCIA CON ALIAS ===");
        cuentaSueldo.transferir(2000.0, "mi.alias.bancario");
        
        System.out.println("\n=== PRUEBA DE TRANSFERENCIA CON ALIAS INVÁLIDO ===");
        cuentaSueldo.transferir(1000.0, "alias-invalido!"); // Caracteres especiales no permitidos
        
        System.out.println("\n=== ESTADO FINAL DE LA CUENTA ===");
        cuentaSueldo.mostrarDatos();
    }
}