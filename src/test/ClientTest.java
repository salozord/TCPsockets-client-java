package test;

import java.io.IOException;
import java.net.Socket;

import org.junit.jupiter.api.Test;

public class ClientTest {
	
	@Test
	public void testConnection()
	{
		try {
			Socket s = new Socket("3.82.26.85", 8080);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
