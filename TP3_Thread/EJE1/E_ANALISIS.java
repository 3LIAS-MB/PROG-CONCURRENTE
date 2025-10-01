// Punto 1e - Análisis de resultados
// Observaciones y comentarios:
// 1. Sin join/sleep: Los hilos se ejecutan concurrentemente, por lo que la salida se mezcla (X e Y
// Intercalados). El resultado es Impredecible y diferente en cada ejecución.
// 2. Con join: Los hilos se ejecutan secuencialmente (primero termina X, luego Y). La salida es
// ordenada pero pierde la concurrencia real.
// 3. Con sleep: Los hilos siguen ejecutándose concurrentemente pero con pausas que pueden
// hacer la salida más legible, aunque aún se mezclan.
// 4. Problema de concurrencia: La salida estándar (System.out) es un recurso compartido que
// necesita sincronización para evitar condiciones de carrera.