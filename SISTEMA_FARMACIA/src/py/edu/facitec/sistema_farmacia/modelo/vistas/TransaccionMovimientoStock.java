package py.edu.facitec.sistema_farmacia.modelo.vistas;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

import com.toedter.calendar.JDateChooser;

import py.edu.facitec.sistema_farmacia.modelo.controladores.VentanaMovimientoStockController;
import py.edu.facitec.reutilizacion.botones.MiBoton;

public class TransaccionMovimientoStock extends JDialog {

	private static final long serialVersionUID = 1L;

	private MiBoton mbtnGuardar;
	private MiBoton mbtnCancelar;

	private JTextField tProducto;
	private JButton btnBuscarProducto;

	private JComboBox<String> cbxTipo;
	private JComboBox<String> cbxMotivo;
	private JComboBox<Object> cbxLote;

	private JTextField tNuevoLote;
	private JDateChooser dateVencimiento;

	private JTextField tCantidad;

	private JTextField tFuncionario;
	private JButton btnBuscarFuncionario;

	private JDateChooser tFecha;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TransaccionMovimientoStock dialog = new TransaccionMovimientoStock();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setUpController();
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaMovimientoStockController setUpController() {
		return new VentanaMovimientoStockController(this);
	}

	/**
	 * Create the dialog.
	 */
	public TransaccionMovimientoStock() {

		setTitle("Movimiento de Stock - Farmacia");
		setBounds(100, 100, 620, 520);
		setLocationRelativeTo(this);
		setModal(true);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Datos del movimiento", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 584, 402);
		getContentPane().add(panel);
		panel.setLayout(null);

		// Producto
		JLabel lblProducto = new JLabel("Producto");
		lblProducto.setBounds(12, 25, 100, 17);
		panel.add(lblProducto);

		tProducto = new JTextField();
		tProducto.setEditable(false);
		tProducto.setBounds(120, 23, 350, 21);
		panel.add(tProducto);
		tProducto.setColumns(10);

		btnBuscarProducto = new JButton("...");
		btnBuscarProducto.setBounds(480, 20, 43, 27);
		panel.add(btnBuscarProducto);

		// Tipo
		JLabel lblTipo = new JLabel("Tipo de movimiento");
		lblTipo.setBounds(12, 60, 150, 17);
		panel.add(lblTipo);

		cbxTipo = new JComboBox<>(new String[] { "Entrada", "Salida" });
		cbxTipo.setBounds(170, 58, 150, 22);
		panel.add(cbxTipo);

		// Motivo
		JLabel lblMotivo = new JLabel("Motivo");
		lblMotivo.setBounds(12, 95, 100, 17);
		panel.add(lblMotivo);

		cbxMotivo = new JComboBox<>();
		cbxMotivo.setBounds(120, 93, 300, 22);
		panel.add(cbxMotivo);

		// Lote
		JLabel lblLote = new JLabel("Lote");
		lblLote.setBounds(12, 130, 100, 17);
		panel.add(lblLote);

		cbxLote = new JComboBox<>();
		cbxLote.setBounds(120, 128, 350, 22);
		panel.add(cbxLote);

		// Nuevo lote (solo si eligen crear uno nuevo, en Entrada)
		JLabel lblNuevoLote = new JLabel("N° de lote nuevo");
		lblNuevoLote.setBounds(12, 165, 150, 17);
		panel.add(lblNuevoLote);

		tNuevoLote = new JTextField();
		tNuevoLote.setBounds(170, 163, 150, 21);
		panel.add(tNuevoLote);
		tNuevoLote.setColumns(10);

		JLabel lblVencimiento = new JLabel("Vencimiento (nuevo lote)");
		lblVencimiento.setBounds(12, 200, 180, 17);
		panel.add(lblVencimiento);

		dateVencimiento = new JDateChooser();
		dateVencimiento.setDateFormatString("dd/MM/yyyy");
		dateVencimiento.setBounds(200, 198, 148, 21);
		panel.add(dateVencimiento);

		// Cantidad
		JLabel lblCantidad = new JLabel("Cantidad");
		lblCantidad.setBounds(12, 240, 100, 17);
		panel.add(lblCantidad);

		tCantidad = new JTextField();
		tCantidad.setBounds(120, 238, 100, 21);
		panel.add(tCantidad);
		tCantidad.setColumns(10);

		// Funcionario responsable
		JLabel lblFuncionario = new JLabel("Funcionario responsable");
		lblFuncionario.setBounds(12, 280, 170, 17);
		panel.add(lblFuncionario);

		tFuncionario = new JTextField();
		tFuncionario.setEditable(false);
		tFuncionario.setBounds(190, 278, 280, 21);
		panel.add(tFuncionario);
		tFuncionario.setColumns(10);

		btnBuscarFuncionario = new JButton("...");
		btnBuscarFuncionario.setBounds(480, 275, 43, 27);
		panel.add(btnBuscarFuncionario);

		// Fecha
		JLabel lblFecha = new JLabel("Fecha");
		lblFecha.setBounds(12, 320, 100, 17);
		panel.add(lblFecha);

		tFecha = new JDateChooser();
		tFecha.setDateFormatString("dd/MM/yyyy");
		tFecha.setBounds(120, 318, 148, 21);
		panel.add(tFecha);

		// Botones
		mbtnGuardar = new MiBoton();
		mbtnGuardar.setText("Registrar");
		mbtnGuardar.setBounds(398, 428, 100, 45);
		getContentPane().add(mbtnGuardar);

		mbtnCancelar = new MiBoton();
		mbtnCancelar.setText("Cancelar");
		mbtnCancelar.setBounds(153, 428, 100, 45);
		getContentPane().add(mbtnCancelar);
	}

	public MiBoton getMbtnGuardar() {
		return mbtnGuardar;
	}

	public MiBoton getMbtnCancelar() {
		return mbtnCancelar;
	}

	public JTextField gettProducto() {
		return tProducto;
	}

	public JButton getBtnBuscarProducto() {
		return btnBuscarProducto;
	}

	public JComboBox<String> getCbxTipo() {
		return cbxTipo;
	}

	public JComboBox<String> getCbxMotivo() {
		return cbxMotivo;
	}

	public JComboBox<Object> getCbxLote() {
		return cbxLote;
	}

	public JTextField gettNuevoLote() {
		return tNuevoLote;
	}

	public JDateChooser getDateVencimiento() {
		return dateVencimiento;
	}

	public JTextField gettCantidad() {
		return tCantidad;
	}

	public JTextField gettFuncionario() {
		return tFuncionario;
	}

	public JButton getBtnBuscarFuncionario() {
		return btnBuscarFuncionario;
	}

	public JDateChooser gettFecha() {
		return tFecha;
	}
}
