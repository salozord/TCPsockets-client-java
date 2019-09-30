package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import logica.Cliente;

public class InterfazCliente extends JFrame {
	
	private Cliente cliente;
	private JTextArea logger;
	private JTextField nombreArchivo;
	private JTextField numPaquetes;
	private JTextField tam;
//	private JButton update;
	
	public InterfazCliente() throws Exception {
		
		cliente = new Cliente();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		
		setLayout(new BorderLayout());
		logger = new JTextArea();
		add(logger, BorderLayout.CENTER);
		
		JPanel aux = new JPanel();
		aux.setLayout(new GridLayout(1, 7));
		
		aux.add(new JLabel("Nombre Archivo:"));
		nombreArchivo = new JTextField("NINGUNO");
		nombreArchivo.setEditable(false);
		aux.add(nombreArchivo);
		aux.add(new JLabel("Paquetes Recibidos:"));
		numPaquetes = new JTextField("0");
		numPaquetes.setEditable(false);
		aux.add(numPaquetes);
		aux.add(new JLabel("Tamaño(MiB):"));
		tam = new JTextField("0");
		tam.setEditable(false);
		aux.add(tam);
//		update = new JButton("Actualizar");
//		update.addActionListener(this);
		
		add(aux, BorderLayout.SOUTH);
	}
	
	
	
	public void actualizarLogger(String nuevo) {
		this.logger.setText(nuevo);
	}
	public void actualizarNombreArchivo(String nuevo) {
		this.nombreArchivo.setText(nuevo);
	}
	public void actualizarNumPaquetes(String nuevo) {
		this.numPaquetes.setText(nuevo);
	}
	public void actualizarTam(String nuevo) {
		this.tam.setText(nuevo);
	}
	
	
	public static void main(String[] args) {
		try {
			InterfazCliente i = new InterfazCliente();
			i.setVisible(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
