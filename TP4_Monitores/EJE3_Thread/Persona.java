package EJE3_Thread;

//import java.util.Random;

public class Persona extends Thread {
	private Pasillo pasillo;
	int idPersona;
	public Persona(int idPersona,Pasillo pasillo) {
		this.idPersona=idPersona;
		this.pasillo=pasillo;
	}
	public void run() {
	//int i=100,f=200, is=400,fs=700;
	//Random random=new Random();
	//int entrada=random.nextInt((f-i)+1)+i;
	//int salida=random.nextInt((fs-is)+1)+is;
	 try {
		Thread.sleep((long) (Math.random() * (200 - 100 + 1) + 100));
		System.out.println("La persona "+ idPersona +" esta haciendo fila para entrar al zoo");
		pasillo.usarPasillo();
		System.out.println("La persona " + idPersona + " ha entrado al zoo");
		Thread.sleep((long) (Math.random() * (700 - 400 + 1) + 400));
		System.out.println("La persona "+ idPersona +" esta lista para salir del zoo");
		System.out.println("La persona "+ idPersona +" esta en la fila para salir del zoo");
		pasillo.usarPasillo();
		System.out.println("La persona "+ idPersona +" salio del zoo"); 
	 } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
