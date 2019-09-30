package logica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.xml.bind.DatatypeConverter;

import interfaz.InterfazCliente;

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
	
	private InterfazCliente interfaz;
	
	private String nombreArchivo;
	
	private long numPaquetes;
	
	private long tam;
	
	private long tiempo;
	
	public Cliente(InterfazCliente i) throws Exception {
		
		interfaz = i;
		nombreArchivo = "Ninguno";
		numPaquetes = 0;
		tam = 0;
		
		log = INICIO + "Cliente inicializado, conectando con el servidor . . .";
		socket = new Socket(SERVIDOR, PUERTO);
		escribirEnLog("Conexión exitosa con el servidor " + SERVIDOR + ":" + PUERTO);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		try {				
			comunicarse();
		}
		catch (Exception e) {
			escribirEnLog("ERROR :: Ocurrió un error: " + e.getMessage());
		}
	}
	
	public void comunicarse() throws Exception {
		escribirEnLog("Enviando mensaje de " + PREPARADO + " al servidor");
		out.print(PREPARADO);
		escribirEnLog("Mensaje enviado al servidor");

		// Esperando la recepción del nombre del archivo a descargar
		String nombre = new String(convertirABytes(in.readLine()));
		if(nombre.contains(NOMBRE)) {
			String n = nombre.split(SEP)[1];
			escribirEnLog("Nombre del archivo a descargar recibido --> " + n);
		}
		else {
			escribirEnLog("ERROR :: Llegó un mensaje que no debía llegar " + nombre);
			cerrar();
		}
		
		// Contabilizando el tiempo inicial
		long ini = System.currentTimeMillis();
		
		// Recibiendo paquetes del archivo a descargar
		
		
		// Contabilizando el tiempo final
		long fin = System.currentTimeMillis();
		
		String hash = new String(convertirABytes(in.readLine()));
		if(nombre.contains(NOMBRE)) {
			String n = nombre.split(SEP)[1];
			escribirEnLog("Nombre del archivo a descargar recibido --> " + n);
		}
		else {
			escribirEnLog("ERROR :: Llegó un mensaje que no debía llegar " + nombre);
			cerrar();
		}
		
		tiempo = (fin - ini)/1000;
		escribirEnLog("Finalizó el envío del archivo. El tiempo total fue de " + tiempo + " segundos");
		
		// Cierre de los canales
		cerrar();
	}
	
	public void escribirEnLog(String mensaje) {
		this.log += "\n" + INICIO + mensaje;
		interfaz.actualizar();
	}
	public String getLog() {
		return log;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public String getTam() {
		return String.valueOf(tam/(1024^2));
	}
	public String getNumPaquetes() {
		return String.valueOf(numPaquetes);
	}
	
	private void cerrar() throws IOException {
		out.close();
		in.close();
		socket.close();
	}
	private void createFile(String ruta) throws IOException {
	    FileOutputStream fos = new FileOutputStream(new File(ruta));
	    for (byte[] data: blobsArchivo)
	        fos.write(data);
	}
	private byte[] convertirABytes(String cadena) {
		return DatatypeConverter.parseHexBinary(cadena);
	}
	
	private String convertirAHexa(byte[] informacion) {
		return DatatypeConverter.printHexBinary(informacion);
	}
}
