package EJE3;

import EJE1.CuentaBancaria;
//import EJE3.OperacionesBancarias;

public class CuentaSueldo extends CuentaBancaria implements OperacionesBancarias {
    // Atributos propios de CuentaSueldo
    private int legajo;
    private String institucion;
    private String beneficios;
    private long CBU;
    private double tope;
    private String alias;
    private int operacionesHoy;
    
    // Constructor por defecto
    public CuentaSueldo() {
        super();
        this.tope = 15000.0;
        this.legajo = 0;
        this.institucion = "";
        this.beneficios = "";
        this.CBU = 0L;
        this.alias = "sin.alias";
        this.operacionesHoy = 0;
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
        this.alias = "sin.alias";
        this.operacionesHoy = 0;
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
        this.alias = "sin.alias";
        this.operacionesHoy = 0;
    }
    
    // Método para validar que el CBU tenga 18 dígitos
    private boolean validarCBU(long cbu) {
        return String.valueOf(cbu).length() == 18;
    }
    
    // Implementación de métodos de la interfaz OperacionesComunes
    
    @Override
    public void pagarServicio(String servicio, double monto) {
        if (operacionesHoy >= MAX_OPERACIONES_DIARIAS) {
            System.out.println("Error: Límite de operaciones diarias alcanzado");
            return;
        }
        
        if (monto > 0) {
            if (getSaldoActual() >= monto + COMISION_BASICA) {
                super.retirar(monto + COMISION_BASICA);
                operacionesHoy++;
                System.out.println("Servicio '" + servicio + "' pagado: $" + monto);
                System.out.println("Comisión: $" + COMISION_BASICA);
                System.out.println("Total debitado: $" + (monto + COMISION_BASICA));
                System.out.println("Saldo actual: $" + String.format("%.2f", getSaldoActual()));
            } else {
                System.out.println("Error: Saldo insuficiente para pagar el servicio");
            }
        } else {
            System.out.println("Error: El monto debe ser positivo");
        }
    }
    
    @Override
    public boolean cambiarAlias(String nuevoAlias) {
        if (nuevoAlias != null && nuevoAlias.matches("^[a-zA-Z0-9.]+$") && nuevoAlias.length() >= 5) {
            this.alias = nuevoAlias;
            System.out.println("Alias cambiado exitosamente a: " + nuevoAlias);
            return true;
        } else {
            System.out.println("Error: El alias debe ser alfanumérico y tener al menos 5 caracteres");
            return false;
        }
    }
    
    // Implementación de métodos de la interfaz OperacionesImportantes
    
    @Override
    public boolean transferenciaAltoMonto(double monto) {
        if (operacionesHoy >= MAX_OPERACIONES_DIARIAS) {
            System.out.println("Error: Límite de operaciones diarias alcanzado");
            return false;
        }
        
        if (monto > 100000) { // Consideramos alto monto a partir de $100,000
            if (getSaldoActual() >= monto) {
                // Requiere validación adicional (en un sistema real sería más complejo)
                System.out.println("Transferencia de alto monto en proceso...");
                System.out.println("Se requiere validación de seguridad adicional");
                super.retirar(monto);
                operacionesHoy++;
                System.out.println("Transferencia de alto monto realizada: $" + monto);
                return true;
            } else {
                System.out.println("Error: Saldo insuficiente para la transferencia de alto monto");
                return false;
            }
        } else {
            System.out.println("Error: El monto debe ser mayor a $100,000 para ser considerado alto monto");
            return false;
        }
    }
    
    // Implementación del método adicional de OperacionesBancarias
    
    @Override
    public String obtenerEstadoCuenta() {
        return "=== ESTADO DE CUENTA ===\n" +
               "Número: " + getNumeroCuenta() + "\n" +
               "Saldo: $" + String.format("%.2f", getSaldoActual()) + "\n" +
               "Alias: " + alias + "\n" +
               "Operaciones hoy: " + operacionesHoy + "/" + MAX_OPERACIONES_DIARIAS + "\n" +
               "Tipo: " + TIPO_CUENTA;
    }
    
    // Sobrescribir el método retirar para incluir validación de tope y contador de operaciones
    @Override
    public void retirar(double cantidad) {
        if (operacionesHoy >= MAX_OPERACIONES_DIARIAS) {
            System.out.println("Error: Límite de operaciones diarias alcanzado");
            return;
        }
        
        if (cantidad > 0) {
            if (getSaldoActual() >= cantidad) {
                if (cantidad <= tope) {
                    super.retirar(cantidad);
                    operacionesHoy++;
                    System.out.println("Retiro exitoso. Nuevo saldo: $" + String.format("%.2f", getSaldoActual()));
                    System.out.println("Operaciones realizadas hoy: " + operacionesHoy + "/" + MAX_OPERACIONES_DIARIAS);
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
    
    // Método transferir con CBU (actualizado con contador de operaciones)
    public void transferir(double monto, long CBUDestino) {
        if (operacionesHoy >= MAX_OPERACIONES_DIARIAS) {
            System.out.println("Error: Límite de operaciones diarias alcanzado");
            return;
        }
        
        if (monto > 0) {
            if (getSaldoActual() >= monto) {
                if (validarCBU(CBUDestino)) {
                    super.retirar(monto);
                    operacionesHoy++;
                    System.out.println("Transferencia exitosa de $" + String.format("%.2f", monto) +
                                     " al CBU: " + CBUDestino);
                    System.out.println("Saldo final: $" + String.format("%.2f", getSaldoActual()));
                    System.out.println("Operaciones realizadas hoy: " + operacionesHoy + "/" + MAX_OPERACIONES_DIARIAS);
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
    
    // Sobrecarga del método transferir con alias alfanumérico (actualizado)
    public void transferir(double monto, String alias) {
        if (operacionesHoy >= MAX_OPERACIONES_DIARIAS) {
            System.out.println("Error: Límite de operaciones diarias alcanzado");
            return;
        }
        
        if (monto > 0) {
            if (getSaldoActual() >= monto) {
                if (alias != null && alias.matches("^[a-zA-Z0-9]+$")) {
                    super.retirar(monto);
                    operacionesHoy++;
                    System.out.println("Transferencia exitosa de $" + String.format("%.2f", monto) +
                                     " al alias: " + alias);
                    System.out.println("Saldo final: $" + String.format("%.2f", getSaldoActual()));
                    System.out.println("Operaciones realizadas hoy: " + operacionesHoy + "/" + MAX_OPERACIONES_DIARIAS);
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
        System.out.println("Alias: " + alias);
        System.out.println("Tope de extracción: $" + String.format("%.2f", tope));
        System.out.println("Operaciones hoy: " + operacionesHoy + "/" + MAX_OPERACIONES_DIARIAS);
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
    
    public String getAlias() {
        return alias;
    }
    
    public int getOperacionesHoy() {
        return operacionesHoy;
    }
    
    public void resetOperacionesDiarias() {
        this.operacionesHoy = 0;
        System.out.println("Contador de operaciones diarias reiniciado");
    }
}