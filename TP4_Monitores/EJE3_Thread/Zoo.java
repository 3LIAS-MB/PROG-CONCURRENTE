package EJE3_Thread;

//	3) Modele la entrada a un Zoológico. Dicho Zoo solo tiene una puerta que actúa de entrada 
//	y salida, mediante un pasillo muy angosto en el cual solo puede entrar o salir una persona 
//	a la vez. Las personas van entrando de forma indefinida en un tiempo variable  entre 100
//	200ms, la entrada por el pasillo le demanda 50ms (lo mismo que la salida), luego 
//	permanece en el Zoo un tiempo variable de 400-700, y sale. Indique las acciones de cada 
//	persona (identificada por un ID numérico) desde que hace fila para entrar, entra por el 
//	pasillo, permanece en el zoo, hace fila para salir y finalmente sale del Zoo.
//	
//	NOTA 
//	Los puntos obligatorios de este TP y que será presentados por los grupos son: Punto 2 y Punto 3.

public class Zoo {
	public static void main(String[] args) throws InterruptedException {
		Pasillo pasillo = new Pasillo();
		int idPersona=0;
		while(true) {
			idPersona++;
			Persona persona=new Persona(idPersona,pasillo);
			persona.start();
			Thread.sleep(500);
		}
			
	}
}
