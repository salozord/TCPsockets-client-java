package logica;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import interfaz.InterfazCliente;

public class Cliente {

//	public static final String SERVIDOR = "3.82.26.85";
	public static final String SERVIDOR = "localhost";
//	public static final String SERVIDOR = "25.5.99.233";
	public static final int PUERTO = 8080;
	public static final String INICIO = " [CLIENTE] ";
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
	
//	private ArrayList<byte[]> blobsArchivo;
	
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
//		blobsArchivo = new ArrayList<>();
		
//		log = "";
//		escribirEnLog("Cliente inicializado, conectando con el servidor . . .\n");
		log = "[" + LocalTime.now() + "]" + INICIO + "Cliente inicializado, conectando con el servidor . . .\n";
		socket = new Socket(SERVIDOR, PUERTO);
//		escribirEnLog("Conexi�n exitosa con el servidor " + SERVIDOR + ":" + PUERTO);
		log += "[" + LocalTime.now() + "]" + INICIO + "Conexi�n exitosa con el servidor " + SERVIDOR + ":" + PUERTO;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}
	
	public void comunicarse() {
		try {
			escribirEnLog("Enviando mensaje de " + PREPARADO + " al servidor");
			out.println(PREPARADO);
			escribirEnLog("Mensaje enviado al servidor");
	
			// Esperando la recepci�n del nombre del archivo a descargar
			String nombre = in.readLine();
			if(nombre.contains(NOMBRE)) {
				String n = nombre.replace(NOMBRE, "");
				nombreArchivo = n;
				escribirEnLog("Nombre del archivo a descargar recibido --> " + n);
			}
			else {
				escribirEnLog("ERROR :: Lleg� un mensaje que no deb�a llegar " + nombre);
				cerrar();
			}
			
			// Iniciando recepci�n y escritura del archivo
			String rutaDesc = RUTA_DOWN + nombreArchivo;
			escribirEnLog("Iniciando recepci�n y escribiendo el archivo en la ruta " + rutaDesc + " . . .");
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			File f = new File(rutaDesc);
			FileOutputStream fos = new FileOutputStream(f);
			if(!f.exists())
				f.createNewFile();
			byte[] buffer = new byte[8192];
			
			// Contabilizando el tiempo inicial
			long ini = System.currentTimeMillis();
			int r;
			
			// Recibiendo paquetes del archivo a descargar
			long tamTotal = dis.readLong();
			while (tam < tamTotal && (r = dis.read(buffer)) != -1 ) {
				//System.out.println(r);
				fos.write(buffer, 0, r);
				numPaquetes++;
				tam += (r);
				escribirEnLog("Paquete Recibido! tama�o: " + (r) + " bytes");
			}
			fos.flush(); // POr si acaso algo queda en el buffer de escritura
			fos.close();
			//dis.close();
			
//			byte[] blob = convertirABytes(in.readLine());
//			while(!(new String(blob)).contains(FINARCH)) {
//				blobsArchivo.add(blob);
//				numPaquetes++;
//				tam += blob.length;
//				escribirEnLog("Paquete Recibido! tama�o: " + blob.length + " bytes");
//				blob = convertirABytes(in.readLine());
//			}
			
			// Contabilizando el tiempo final
			long fin = System.currentTimeMillis();
			tiempo = (fin - ini)/1000;
			escribirEnLog("Escritura del archivo exitosa !");
			escribirEnLog("Finaliz� el env�o del archivo. El tiempo total fue de " + tiempo + " segundos");
			escribirEnLog("N�mero total de paquetes recibidos: " + numPaquetes + " paquetes");
			escribirEnLog("Tama�o total del archivo recibido: " + (tam/(1024.0*1024.0)) + " MiBytes");
			
			// Guardando el archivo en local
//			createFile(rutaDesc);
					
			// Verificaci�n de integridad con el hash
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(rutaDesc));
			byte[] completo = new byte[(int)(new File(rutaDesc)).length()];
			bis.read(completo);
			bis.close();
			
			escribirEnLog("Iniciando la Validaci�n de integridad . . . ");
//			String hash = new String(blob);
			String hash = in.readLine();
			if(hash.contains(FINARCH)) {
//				String h = hash.split(SEP)[1];
				String h = hash.replace(FINARCH, "");
				escribirEnLog("Hash Recibido del servidor --> " + h);
				
				MessageDigest hashing = MessageDigest.getInstance("SHA-256");
				hashing.update(completo);
				byte[] fileHashed = hashing.digest();
				String created = convertirAHexa(fileHashed);
				
				escribirEnLog("Hash generado ! --> " + created);
				
				if(created.equals(h)) {
					escribirEnLog("El Archivo se verific� y no contiene errores ! :D");
					escribirEnLog("Se le env�a al servidor confirmaci�n: " + RECIBIDO);
					out.println(RECIBIDO);
					// Cierre de los canales
					escribirEnLog("Mensaje de confimaci�n exitosa enviado correctamente :D !");
					escribirEnLog("Cerrando conexi�n satisfactoriamente . . .!");
					cerrar();
				}
				else {
					escribirEnLog("El archivo se corrompi� :( tiene errores porque los hashes no coinciden");
					escribirEnLog("Se le env�a al servidor mensaje de error: " + ERROR);
					out.println(ERROR);
					// Cierre de los canales
					escribirEnLog("Mensaje de error enviado correctamente :(");
					escribirEnLog("Cerrando conexi�n por finalizaci�n de proceso err�neo :( . . .");
					cerrar();
				}
			}
			else {
				escribirEnLog("ERROR :: Lleg� un mensaje que no deb�a llegar " + hash);
				cerrar();
			}
		}
		catch(Exception e) {
			escribirEnLog("ERROR :: Ocurri� alg�n error inesperado: " + e.getMessage());
			e.printStackTrace();
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
		this.log += "\n[" + LocalTime.now() + "]" + INICIO + mensaje;
		interfaz.actualizar();
	}
	public String getLog() {
		return log;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public String getTam() {
		return String.valueOf((tam/(1024.0*1024.0)));
	}
	public String getNumPaquetes() {
		return String.valueOf(numPaquetes);
	}
	
	private void cerrar() throws IOException {
		// Se debe primero escribir el log
		File f = new File(RUTA_LOG + (new Date()).toString().replace(":", "-") + "-LOG.log");
		escribirEnLog("Registrando todo en archivo LOG local . . .");
		FileOutputStream fos = new FileOutputStream(f);
		if(!f.exists())
			f.createNewFile();
	    fos.write(log.getBytes());
	    fos.close();
		escribirEnLog("Log generado ! Hasta la pr�xima :D !");
		
	    in.close();
		out.close();
		socket.close();
	}
//	private void createFile(String ruta) throws IOException {
//		File f = new File(ruta);
//		FileOutputStream fos = new FileOutputStream(f);
//		if(!f.exists())
//			f.createNewFile();
//	    for (byte[] data: blobsArchivo)
//	        fos.write(data);
//	    fos.close();
//	}
	private byte[] convertirABytes(String cadena) {
		return DatatypeConverter.parseHexBinary(cadena);
	}
	
	private String convertirAHexa(byte[] informacion) {
		return DatatypeConverter.printHexBinary(informacion);
	}
}
