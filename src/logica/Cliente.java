package logica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

public class Cliente {

	public static final String SERVIDOR = "3.82.26.85";
	public static final int PUERTO = 8080;
	public static final String INICIO = "[CLIENTE]";
	public static final String PREPARADO = "PREPARADO";
	public static final String RECIBIDO = "RECIBIDO";
	public static final String ERROR = "ERROR";
	public static final String FINARCH = "FINARCH$";
	public static final String NOMBRE = "NOMBRE$";
	public static final String SEP = "$";
	
	
	private String log;
	
	private Socket socket;
	
	private PrintWriter out;
	
	private BufferedReader in;
	
	private ArrayList<byte[]> blobsArchivo;
	
	public Cliente() throws Exception {
			socket = new Socket(SERVIDOR, PUERTO);
			log = INICIO + "Cliente inicializado, conectando con el servidor . . .";
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			try {				
				comunicarse();
			}
			catch (Exception e) {
				escribirEnLog("ERROR :: Ocurrió un error: " + e.getMessage());
			}
	}
	
	public void escribirEnLog(String mensaje) {
		this.log += "\n" + INICIO + mensaje;
	}
	
	public void comunicarse() throws Exception {
		escribirEnLog("Enviando mensaje de " + PREPARADO + " al servidor");
		out.print(PREPARADO);
		escribirEnLog("Mensaje enviado al servidor");

		// Esperando la recepción del nombre del archivo a descargar
		String nombre = new String(convertirABytes(in.readLine()));
		if(nombre.contains(NOMBRE)) {
			String n = nombre.split(SEP)[1];
			
		}
		else {
			escribirEnLog("ERROR :: Llegó un mensaje que no debía llegar " + nombre);
			cerrar();
		}
		
		
		
		
		// Cierre de los canales
		cerrar();
	}
	
	private void cerrar() throws IOException {
		out.close();
		in.close();
		socket.close();
	}
	
	public void createFile(String ruta) throws IOException {
	    FileOutputStream fos = new FileOutputStream(new File(ruta));
	    for (byte[] data: blobsArchivo)
	        fos.write(data);
	}
	
	public byte[] convertirABytes(String cadena) {
		return DatatypeConverter.parseHexBinary(cadena);
	}
	
	public String convertirAHexa(byte[] informacion) {
		return DatatypeConverter.printHexBinary(informacion);
	}
}
