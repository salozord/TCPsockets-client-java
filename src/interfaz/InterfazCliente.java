package interfaz;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import logica.Cliente;

public class InterfazCliente extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Cliente cliente;
	private JTextArea logger;
	private JTextField nombreArchivo;
	private JTextField numPaquetes;
	private JTextField tam;
	private JTextField tiempo;
	private Timer timer;
	
	public InterfazCliente() throws Exception {
		
		cliente = new Cliente();
		timer = new Timer(100, this);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 430);
		setResizable(false);
		
		setLayout(new BorderLayout());
		logger = new JTextArea(cliente.getLog());
		logger.setEditable(false);
		logger.setFont(new Font(Font.MONOSPACED,Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(logger);
		new SmartScroller( scroll );
		add(scroll, BorderLayout.CENTER);
		
		JPanel aux = new JPanel();
		aux.setLayout(new GridLayout(1, 8, 7, 15));
		
		aux.add(new JLabel("Nombre Archivo:"));
		nombreArchivo = new JTextField(cliente.getNombreArchivo());
		nombreArchivo.setEditable(false);
		aux.add(nombreArchivo);
		aux.add(new JLabel("Paquetes Recibidos:"));
		numPaquetes = new JTextField(cliente.getNumPaquetes());
		numPaquetes.setEditable(false);
		aux.add(numPaquetes);
		aux.add(new JLabel("Tamaño(MiB):"));
		tam = new JTextField(cliente.getTam());
		tam.setEditable(false);
		aux.add(tam);
		aux.add(new JLabel("Tiempo(s):"));
		tiempo = new JTextField(cliente.getTiempo());
		tiempo.setEditable(false);
		aux.add(tiempo);
		
		add(aux, BorderLayout.SOUTH);
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void iniciarActualizacion() {
		timer.start();
	}
	
	public void finalizar() {
		timer.stop();
	}
	
	public void actualizar() {
		this.logger.setText(cliente.getLog());
		this.nombreArchivo.setText(cliente.getNombreArchivo());
		this.numPaquetes.setText(cliente.getNumPaquetes());
		this.tam.setText(cliente.getTam());
		this.tiempo.setText(cliente.getTiempo());
	}
	
	public void actionPerformed(ActionEvent e) {
		actualizar();
	}
	
	public static void main(String[] args) {
		try {
			InterfazCliente i = new InterfazCliente();
			i.setVisible(true);
			i.iniciarActualizacion();
			i.getCliente().comunicarse();
			i.finalizar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
