package EJE1;

import java.util.ArrayList;
import java.util.List;

public class C_AnalizadorSecuencias {
    
    public static String encontrarSecuenciaMasLarga(String texto) {
        if (texto == null || texto.isEmpty()) return "";
        
        char caracterActual = texto.charAt(0);
        int contadorActual = 1;
        int maxContador = 1;
        int inicioMax = 0;
        int inicioActual = 0;
        
        for (int i = 1; i < texto.length(); i++) {
            if (texto.charAt(i) == texto.charAt(i - 1)) {
                contadorActual++;
            } else {
                if (contadorActual > maxContador) {
                    maxContador = contadorActual;
                    inicioMax = inicioActual;
                }
                caracterActual = texto.charAt(i);
                contadorActual = 1;
                inicioActual = i;
            }
        }
        
        // Verificar la última secuencia
        if (contadorActual > maxContador) {
            maxContador = contadorActual;
            inicioMax = inicioActual;
        }
        
        return texto.substring(inicioMax, inicioMax + maxContador);
    }
    
    public static void main(String[] args) {
        // Generar el patrón completo
        StringBuilder patronCompleto = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            patronCompleto.append(i).append("X");
        }
        for (int i = 0; i < 100; i++) {
            patronCompleto.append(i).append("Y");
        }
        String secuenciaMasLarga = encontrarSecuenciaMasLarga(patronCompleto.toString());
        System.out.println("Secuencia más larga: " + secuenciaMasLarga);
        System.out.println("Longitud: " + secuenciaMasLarga.length() + " caracteres");
        System.out.println("Patron competo: " + patronCompleto);
    }
    
}