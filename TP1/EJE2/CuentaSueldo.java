package EJE2;

import EJE1.CuentaBancaria;

public class CuentaSueldo extends CuentaBancaria {
    private int legajo;
    private String institucion;
    private String beneficios;
    private long CBU;
    private double tope;
    
    // Constructor por defecto
    public CuentaSueldo() {
        super();
        this.tope = 15000.0;
        this.legajo = 0;
        this.institucion = "";
        this.beneficios = "";
        this.CBU = 0L;
    }
    
    // Constructor con CBU (18 dígitos) y tope fijado en 15.000
    public CuentaSueldo(long CBU) {
        super();
        if (validarCBU(CBU)) {
            this.CBU = CBU;
        } else {
            throw new IllegalArgumentException("El CBU debe tener exactamente 18 dígitos");
        }
        this.tope = 15000.0;
        this.legajo = 0;
        this.institucion = "";
        this.beneficios = "";
    }
    
    // Constructor completo
    public CuentaSueldo(long dniCliente, double saldoActual, double interesAnual,
                       int legajo, String institucion, String beneficios, long CBU, double tope) {
        super(dniCliente, saldoActual, interesAnual);
        if (validarCBU(CBU)) {
            this.CBU = CBU;
        } else {
            throw new IllegalArgumentException("El CBU debe tener exactamente 18 dígitos");
        }
        this.legajo = legajo;
        this.institucion = institucion;
        this.beneficios = beneficios;
        this.tope = tope;
    }
    
    // Método para validar que el CBU tenga 18 dígitos
    private boolean validarCBU(long cbu) {
        return String.valueOf(cbu).length() == 18;
    }
    
    // Sobrescribir el método retirar para incluir validación de tope
    @Override
    public void retirar(double cantidad) {
        if (cantidad > 0) {
            if (getSaldoActual() >= cantidad) {
                if (cantidad <= tope) {
                    super.retirar(cantidad); // Llama al método de la clase padre
                    System.out.println("Retiro exitoso. Nuevo saldo: $" + String.format("%.2f", getSaldoActual()));
                } else {
                    System.out.println("Error: El monto a retirar ($" + cantidad + 
                                     ") excede el tope permitido de $" + tope);
                }
            } else {
                System.out.println("Error: Saldo insuficiente. Saldo actual: $" + 
                                 String.format("%.2f", getSaldoActual()));
            }
        } else {
            System.out.println("Error: La cantidad a retirar debe ser positiva.");
        }
    }
    
    // Método transferir con CBU
    public void transferir(double monto, long CBUDestino) {
        if (monto > 0) {
            if (getSaldoActual() >= monto) {
                if (validarCBU(CBUDestino)) {
                    // Simular la transferencia
                    super.retirar(monto); // Decrementa el saldo
                    System.out.println("Transferencia exitosa de $" + String.format("%.2f", monto) +
                                     " al CBU: " + CBUDestino);
                    System.out.println("Saldo final: $" + String.format("%.2f", getSaldoActual()));
                } else {
                    System.out.println("Error: El CBU destino debe tener 18 dígitos");
                }
            } else {
                System.out.println("Error: Saldo insuficiente para la transferencia");
            }
        } else {
            System.out.println("Error: El monto de transferencia debe ser positivo");
        }
    }
    
    // Sobrecarga del método transferir con alias alfanumérico
    public void transferir(double monto, String alias) {
        if (monto > 0) {
            if (getSaldoActual() >= monto) {
                // Validar que el alias sea alfanumérico (letras y números)
                if (alias != null && alias.matches("^[a-zA-Z0-9]+$")) {
                    // Simular la transferencia
                    super.retirar(monto);
                    System.out.println("Transferencia exitosa de $" + String.format("%.2f", monto) +
                                     " al alias: " + alias);
                    System.out.println("Saldo final: $" + String.format("%.2f", getSaldoActual()));
                } else {
                    System.out.println("Error: El alias debe ser alfanumérico");
                }
            } else {
                System.out.println("Error: Saldo insuficiente para la transferencia");
            }
        } else {
            System.out.println("Error: El monto de transferencia debe ser positivo");
        }
    }
    
    // Sobrescribir el método mostrarDatos para incluir los atributos propios
    @Override
    public void mostrarDatos() {
        super.mostrarDatos();
        System.out.println("=== DATOS ESPECÍFICOS DE CUENTA SUELDO ===");
        System.out.println("Legajo: " + legajo);
        System.out.println("Institución: " + institucion);
        System.out.println("Beneficios: " + beneficios);
        System.out.println("CBU: " + CBU);
        System.out.println("Tope de extracción: $" + String.format("%.2f", tope));
        System.out.println("==========================================");
    }
    
    // Getters y setters para los atributos propios
    public int getLegajo() {
        return legajo;
    }
    
    public void setLegajo(int legajo) {
        this.legajo = legajo;
    }
    
    public String getInstitucion() {
        return institucion;
    }
    
    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }
    
    public String getBeneficios() {
        return beneficios;
    }
    
    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }
    
    public long getCBU() {
        return CBU;
    }
    
    public void setCBU(long CBU) {
        if (validarCBU(CBU)) {
            this.CBU = CBU;
        } else {
            throw new IllegalArgumentException("El CBU debe tener exactamente 18 dígitos");
        }
    }
    
    public double getTope() {
        return tope;
    }
    
    public void setTope(double tope) {
        this.tope = tope;
    }
}