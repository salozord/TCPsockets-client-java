package interfaz;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import logica.Cliente;

public class InterfazCliente extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Cliente cliente;
	private JTextArea logger;
	private JTextField nombreArchivo;
	private JTextField numPaquetes;
	private JTextField tam;
	
	public InterfazCliente() throws Exception {
		
		cliente = new Cliente(this);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 400);
		setResizable(false);
		
		setLayout(new BorderLayout());
		logger = new JTextArea(cliente.getLog());
		logger.setEditable(false);
		logger.setFont(new Font(Font.MONOSPACED,Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(logger);
		new SmartScroller( scroll );
		add(scroll, BorderLayout.CENTER);
		
		JPanel aux = new JPanel();
		aux.setLayout(new GridLayout(1, 7));
		
		aux.add(new JLabel("Nombre Archivo:"));
		nombreArchivo = new JTextField(cliente.getNombreArchivo());
		nombreArchivo.setEditable(false);
		aux.add(nombreArchivo);
		aux.add(new JLabel("Paquetes Recibidos:"));
		numPaquetes = new JTextField(cliente.getNumPaquetes());
		numPaquetes.setEditable(false);
		aux.add(numPaquetes);
		aux.add(new JLabel("Tama�o(MiB):"));
		tam = new JTextField(cliente.getTam());
		tam.setEditable(false);
		aux.add(tam);
		
		add(aux, BorderLayout.SOUTH);
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void actualizar() {
		this.logger.setText(cliente.getLog());
		this.nombreArchivo.setText(cliente.getNombreArchivo());
		this.numPaquetes.setText(cliente.getNumPaquetes());
		this.tam.setText(cliente.getTam());
	}
	
	public static void main(String[] args) {
		try {
			InterfazCliente i = new InterfazCliente();
			i.setVisible(true);
			i.getCliente().comunicarse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
