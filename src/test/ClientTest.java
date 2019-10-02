package test;

import java.io.IOException;
import java.net.Socket;

import org.junit.*;
import static org.junit.Assert.*;

import logica.Cliente;

public class ClientTest {
	
	@Test
	public void testConnection()
	{
		Cliente cliente = null;
		try 
		{
			cliente = new Cliente(null);
			
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Falla la conexión con el servidor");
		}
		
		try 
		{
			cliente.comunicarse();
			fail("Si llega aca, el envio del archivo tuvo un error");
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
	}
}
