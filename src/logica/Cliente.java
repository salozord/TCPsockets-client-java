package logica;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextField;
import javax.xml.bind.DatatypeConverter;

import interfaz.InterfazCliente;

public class Cliente {

//	public static final String SERVIDOR = "3.82.26.85";
	public static final String SERVIDOR = "localhost";
	public static final int PUERTO = 8080;
	public static final String INICIO = "[CLIENTE]";
	public static final String RUTA_LOG = "./data/logs/";
	public static final String RUTA_DOWN = "./data/descargas/";
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
		
		log = INICIO + " Cliente inicializado, conectando con el servidor . . .\n";
		socket = new Socket(SERVIDOR, PUERTO);
		log += INICIO + " Conexi�n exitosa con el servidor " + SERVIDOR + ":" + PUERTO;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}
	
	public void comunicarse() {
		try {
			escribirEnLog("Enviando mensaje de " + PREPARADO + " al servidor");
			out.print(PREPARADO);
			escribirEnLog("Mensaje enviado al servidor");
	
			// Esperando la recepci�n del nombre del archivo a descargar
			String nombre = new String(convertirABytes(in.readLine()));
			if(nombre.contains(NOMBRE)) {
				String n = nombre.split(SEP)[1];
				escribirEnLog("Nombre del archivo a descargar recibido --> " + n);
			}
			else {
				escribirEnLog("ERROR :: Lleg� un mensaje que no deb�a llegar " + nombre);
				cerrar();
			}
			
			// Contabilizando el tiempo inicial
			long ini = System.currentTimeMillis();
			
			// Recibiendo paquetes del archivo a descargar
			byte[] blob = convertirABytes(in.readLine());
			while(!(new String(blob)).contains(FINARCH)) {
				blobsArchivo.add(blob);
				blob = convertirABytes(in.readLine());
			}
			
			// Contabilizando el tiempo final
			long fin = System.currentTimeMillis();
			tiempo = (fin - ini)/1000;
			escribirEnLog("Finaliz� el env�o del archivo. El tiempo total fue de " + tiempo + " segundos");
			escribirEnLog("N�mero total de paquetes recibidos: " + numPaquetes + " paquetes");
			escribirEnLog("Tama�o total del archivo recibido: " + tam/(1024^2) + " MiBytes");
			
			// Guardando el archivo en local
			String rutaDesc = RUTA_DOWN + nombreArchivo;
			escribirEnLog("Escribiendo el archivo en la ruta " + rutaDesc);
			createFile(rutaDesc);
			escribirEnLog("Escritura del archivo exitosa !");
					
			// Verificaci�n de integridad con el hash
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(rutaDesc));
			byte[] completo = new byte[(int)(new File(rutaDesc)).length()];
			bis.read(completo);
			
			escribirEnLog("Iniciando la Validaci�n de integridad . . . ");
			String hash = new String(blob);
			if(hash.contains(FINARCH)) {
				String h = convertirAHexa(hash.split(SEP)[1].getBytes());
				escribirEnLog("Hash Recibido del servidor --> " + h);
				
				MessageDigest hashing = MessageDigest.getInstance("SHA-256");
				hashing.update(completo);
				byte[] fileHashed = hashing.digest();
				String created = convertirAHexa(fileHashed);
				
				escribirEnLog("Hash generado ! --> " + created);
				
				if(created.equals(h)) {
					escribirEnLog("El Archivo se verific� y no contiene errores ! :D");
					escribirEnLog("Se le env�a al servidor confirmaci�n: " + RECIBIDO);
					out.print(RECIBIDO);
				}
				else {
					escribirEnLog("El archivo se corrompi� :( tiene errores porque los hashes no coinciden");
					escribirEnLog("Se le env�a al servidor mensaje de error: " + ERROR);
					out.print(ERROR);
				}
			}
			else {
				escribirEnLog("ERROR :: Lleg� un mensaje que no deb�a llegar " + hash);
				cerrar();
			}
			// Cierre de los canales
			cerrar();
		}
		catch(Exception e) {
			escribirEnLog("ERROR :: Ocurri� alg�n error inesperado: " + e.getMessage());
		}
		finally {
			try {
				cerrar();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void escribirEnLog(String mensaje) {
		this.log += "\n" + INICIO + " " + mensaje;
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
		// Se debe primero escribir el log
		File f = new File(RUTA_LOG + (new Date()).toString() + "-LOG.log");
		FileOutputStream fos = new FileOutputStream(f);
		if(!f.exists())
			f.createNewFile();
	    fos.write(log.getBytes());
	    fos.close();
		
	    in.close();
		out.close();
		socket.close();
	}
	private void createFile(String ruta) throws IOException {
		File f = new File(ruta);
		FileOutputStream fos = new FileOutputStream(f);
		if(!f.exists())
			f.createNewFile();
	    for (byte[] data: blobsArchivo)
	        fos.write(data);
	    fos.close();
	}
	private byte[] convertirABytes(String cadena) {
		return DatatypeConverter.parseHexBinary(cadena);
	}
	
	private String convertirAHexa(byte[] informacion) {
		return DatatypeConverter.printHexBinary(informacion);
	}
}
