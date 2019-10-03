package test;

import org.junit.*;
import static org.junit.Assert.*;


import logica.Cliente;

public class ClientTest {
	
	Cliente cliente = null;
	
	@Test
	public void testConnection()
	{
		try 
		{
			cliente = new Cliente(null);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Falla la conexión con el servidor");
		}
	}
	
	@Test
	public void testCommunication()
	{
		System.out.println("Aca");
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