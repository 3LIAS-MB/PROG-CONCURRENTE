package EJE1;

    public class CuentaBancaria {
        private static long ultimoNumeroCuenta = 100000;
        private static int totalCuentas = 0;

        private long numeroCuenta;
        private long dniCliente;
        private double saldoActual;
        private double interesAnual;
        
        // Constructor por defecto
        public CuentaBancaria() {
            this.numeroCuenta = generarNumeroCuenta();
            this.dniCliente = 0;
            this.saldoActual = 0.0;
            this.interesAnual = 0.0;
            totalCuentas++;
        }
        
        // Constructor con parámetros
        public CuentaBancaria(long dniCliente, double saldoActual, double interesAnual) {
            this.numeroCuenta = generarNumeroCuenta();
            this.dniCliente = dniCliente;
            this.saldoActual = saldoActual;
            this.interesAnual = interesAnual;
            totalCuentas++;
        }
        
        // Método privado para generar número de cuenta correlativo
        private long generarNumeroCuenta() {
            ultimoNumeroCuenta++;
            return ultimoNumeroCuenta;
        }
        
        // Actualizar saldo aplicando interés diario
        public void actualizarSaldo() {
            double interesDiario = (interesAnual / 100) / 365;
            saldoActual += saldoActual * interesDiario;
        }
        
        // Ingresar cantidad a la cuenta
        public void ingresar(double cantidad) {
            if (cantidad > 0) {
                saldoActual += cantidad;
                System.out.println("Se han ingresado $" + cantidad + " correctamente.");
            } else {
                System.out.println("Error: La cantidad a ingresar debe ser positiva.");
            }
        }
        
        // Retirar cantidad de la cuenta
        public void retirar(double cantidad) {
            if (cantidad > 0) {
                if (saldoActual >= cantidad) {
                    saldoActual -= cantidad;
                    System.out.println("Se han retirado $" + cantidad + " correctamente.");
                } else {
                    System.out.println("Error: Saldo insuficiente. Saldo actual: $" + saldoActual);
                }
            } else {
                System.out.println("Error: La cantidad a retirar debe ser positiva.");
            }
        }
        
        // Mostrar todos los datos de la cuenta
        public void mostrarDatos() {
            System.out.println("=== DATOS DE LA CUENTA BANCARIA ===");
            System.out.println("Número de cuenta: " + numeroCuenta);
            System.out.println("DNI del cliente: " + dniCliente);
            System.out.println("Saldo actual: $" + String.format("%.2f", saldoActual));
            System.out.println("Interés anual: " + interesAnual + "%");
            System.out.println("===================================");
        }
        
        // Métodos getters y setters
        public long getNumeroCuenta() {
            return numeroCuenta;
        }
        
        public long getDniCliente() {
            return dniCliente;
        }
        
        public void setDniCliente(long dniCliente) {
            this.dniCliente = dniCliente;
        }
        
        public double getSaldoActual() {
            return saldoActual;
        }
        
        public double getInteresAnual() {
            return interesAnual;
        }
        
        public void setInteresAnual(double interesAnual) {
            this.interesAnual = interesAnual;
        }
        
        
        // Método estático para obtener el último número de cuenta asignado
        public static long getUltimoNumeroCuenta() {
            return ultimoNumeroCuenta;
        }
        
        public static int getTotalCuentas() {
            return totalCuentas;
        }
    }