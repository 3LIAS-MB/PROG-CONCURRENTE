package EJE1;

public class Main {
 public static void main(String[] args) {
     CuentaBancaria cuenta1 = new CuentaBancaria(12345678L, 1000.0, 5.0);
     CuentaBancaria cuenta2 = new CuentaBancaria(87654321L, 2500.0, 3.5);

//     System.out.println("=== CUENTAS CREADAS ===");
//     cuenta1.mostrarDatos();
//     cuenta2.mostrarDatos();

     System.out.println("\n=== OPERACIONES EN CUENTA 1 ===");
     cuenta1.ingresar(500.0);
     cuenta1.retirar(200.0);
     cuenta1.actualizarSaldo();
     cuenta1.mostrarDatos();

     System.out.println("\n=== OPERACIONES EN CUENTA 2 ===");
     cuenta2.retirar(3000.0);
     cuenta2.ingresar(1000.0);
     cuenta2.actualizarSaldo();
     cuenta2.mostrarDatos();

     System.out.println("\n=== INFORMACIÓN GENERAL ===");
     System.out.println("Total de cuentas creadas: " + CuentaBancaria.getTotalCuentas());
     System.out.println("Último número de cuenta asignado: " + CuentaBancaria.getUltimoNumeroCuenta());
 }
}



