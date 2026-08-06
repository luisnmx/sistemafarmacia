package py.edu.facitec.sistema_farmacia.modelo.vistas;

import java.awt.EventQueue;
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import py.edu.facitec.sistema_farmacia.modelo.controladores.VentanaVentaController;
import py.edu.facitec.reutilizacion.botones.MiBoton;

public class TransaccionVenta extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private MiBoton mbtnCancelar;
	private MiBoton mbtnGuardar;
	private JTextField tCliente;
	private JButton btnBuscarCliente;
	private JTextField tVendedor;
	private JButton btnBuscarVendedor;
	private JTextField tProducto;
	private JSpinner spinnerCantProducto;
	private JButton btnBuscarProducto;
	private JButton btnAgregarProducto;
	private JButton btnQuitarProducto;
	private JDateChooser tFecha;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TransaccionVenta dialog = new TransaccionVenta();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setUpController();
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaVentaController setUpController() {
		return new VentanaVentaController(this);
	}

	/**
	 * Create the dialog.
	 */
	public TransaccionVenta() {

		setTitle("Punto de Ventas - Farmacia");
		setBounds(100, 100, 800, 500);
		setLocationRelativeTo(this);
		setModal(true);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Datos de cabecera", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 776, 115);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setBounds(12, 25, 85, 17);
		panel.add(lblCliente);

		tCliente = new JTextField();
		tCliente.setEditable(false);
		tCliente.setBounds(96, 23, 313, 21);
		panel.add(tCliente);
		tCliente.setColumns(10);

		btnBuscarCliente = new JButton("...");
		btnBuscarCliente.setBounds(421, 20, 43, 27);
		panel.add(btnBuscarCliente);

		JLabel lblVendedor = new JLabel("Vendedor");
		lblVendedor.setBounds(480, 25, 85, 17);
		panel.add(lblVendedor);

		tVendedor = new JTextField();
		tVendedor.setEditable(false);
		tVendedor.setBounds(560, 23, 150, 21);
		panel.add(tVendedor);
		tVendedor.setColumns(10);

		btnBuscarVendedor = new JButton("...");
		btnBuscarVendedor.setBounds(717, 20, 43, 27);
		panel.add(btnBuscarVendedor);

		JLabel lblFecha = new JLabel("Fecha");
		lblFecha.setBounds(12, 59, 85, 17);
		panel.add(lblFecha);

		tFecha = new JDateChooser();
		tFecha.setBounds(96, 57, 148, 21);
		panel.add(tFecha);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Agregar producto / medicamento", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(12, 129, 776, 46);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblProducto = new JLabel("Producto");
		lblProducto.setBounds(12, 20, 85, 17);
		panel_1.add(lblProducto);

		tProducto = new JTextField();
		tProducto.setEditable(false);
		tProducto.setColumns(10);
		tProducto.setBounds(76, 18, 428, 21);
		panel_1.add(tProducto);

		btnBuscarProducto = new JButton("...");
		btnBuscarProducto.setBounds(516, 15, 43, 27);
		panel_1.add(btnBuscarProducto);

		spinnerCantProducto = new JSpinner();
		spinnerCantProducto.setBounds(571, 18, 61, 22);
		panel_1.add(spinnerCantProducto);

		btnAgregarProducto = new JButton("Agregar");
		btnAgregarProducto.setBounds(644, 12, 105, 27);
		panel_1.add(btnAgregarProducto);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 181, 724, 177);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"Producto / Medicamento", "Lote", "Cantidad", "Precio Venta", "Subtotal"
			}
		));
		scrollPane.setViewportView(table);

		mbtnGuardar = new MiBoton();
		mbtnGuardar.setText("Guardar");
		mbtnGuardar.setBounds(667, 371, 100, 80);
		getContentPane().add(mbtnGuardar);

		mbtnCancelar = new MiBoton();
		mbtnCancelar.setText("Cancelar");
		mbtnCancelar.setBounds(555, 371, 100, 80);
		getContentPane().add(mbtnCancelar);

		btnQuitarProducto = new JButton("X");
		btnQuitarProducto.setBackground(new Color(192, 28, 40));
		btnQuitarProducto.setForeground(new Color(255, 255, 255));
		btnQuitarProducto.setBounds(736, 187, 52, 27);
		getContentPane().add(btnQuitarProducto);

	}

	public JTable getTable() {
		return table;
	}

	public MiBoton getMbtnCancelar() {
		return mbtnCancelar;
	}

	public MiBoton getMbtnGuardar() {
		return mbtnGuardar;
	}

	public JButton getBtnBuscarCliente() {
		return btnBuscarCliente;
	}

	public JTextField gettCliente() {
		return tCliente;
	}

	public JButton getBtnBuscarVendedor() {
		return btnBuscarVendedor;
	}

	public JTextField gettVendedor() {
		return tVendedor;
	}

	public JTextField gettProducto() {
		return tProducto;
	}

	public JSpinner getSpinnerCantProducto() {
		return spinnerCantProducto;
	}

	public JButton getBtnBuscarProducto() {
		return btnBuscarProducto;
	}

	public JButton getBtnAgregarProducto() {
		return btnAgregarProducto;
	}

	public JButton getBtnQuitarProducto() {
		return btnQuitarProducto;
	}

	public JDateChooser gettFecha() {
		return tFecha;
	}
}
