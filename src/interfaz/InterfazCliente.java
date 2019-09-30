package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import logica.Cliente;

public class InterfazCliente extends JFrame {
	
	private Cliente cliente;
	private JTextArea logger;
	private JLabel nombreArchivo;
	private JLabel numPaquetes;
	private JLabel tam;
	
	public InterfazCliente() {
		
		cliente = new Cliente();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		
		setLayout(new BorderLayout());
		logger = new JTextArea();
		
		JPanel aux = new JPanel();
		aux.setLayout(new GridLayout(1, 2));
		
		
		
		add(aux, BorderLayout.SOUTH);
	}
	
	
	
	public void actualizarLogger(String nuevo) {
		this.logger.setText(nuevo);
	}
	
	
	public void actualizarNombreArchivo(String nuevo) {
		this.
	}
	
	
	
	public static void main(String[] args) {
		
		InterfazCliente i = new InterfazCliente();
		i.setVisible(true);
	}
	
}
