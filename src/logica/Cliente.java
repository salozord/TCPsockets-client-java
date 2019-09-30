package logica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

	public static final String SERVIDOR = "25.7.50.208";
	public static final int PUERTO = 8080;
	public static final String INICIO = "[CLIENTE]";
	public static final String PREPARADO = "PREPARADO";
	public static final String RECIBIDO = "RECIBIDO";
	public static final String ERROR = "ERROR";
	public static final String FINARCH = "FINARCH$";
	public static final String NOMBRE = "NOMBRE$";
	
	private String log;
	
	private Socket socket;
	
	private PrintWriter out;
	
	private BufferedReader in;
	
	public Cliente() {
		try {
			socket = new Socket(SERVIDOR, PUERTO);
			log = INICIO + "Cliente inicializado, conectando con el servidor . . .";
			in = new BufferedReader(new InputStreamReader(leerDelCliente));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void escribirEnLog(String mensaje) {
		this.log += "\n" + INICIO + mensaje;
	}
}
