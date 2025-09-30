package EJE3;

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
