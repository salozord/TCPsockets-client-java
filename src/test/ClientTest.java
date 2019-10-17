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
			cliente = new Cliente();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Falla la conexi�n con el servidor");
		}
	}
	
	@Test
	public void testCommunication()
	{
		System.out.println("Aca");
		try 
		{
			int res = cliente.comunicarse();
			assertEquals("Deber�a estar bien el estado porque deber�a funcionar correctamente", Cliente.BIEN, res);
		} 
		catch (Exception e) {
			fail("Si llega aca, el envio del archivo tuvo un error");
			// TODO: handle exception
		}
	}
}