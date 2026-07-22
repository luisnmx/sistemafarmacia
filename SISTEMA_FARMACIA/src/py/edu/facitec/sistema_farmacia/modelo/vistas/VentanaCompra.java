package py.edu.facitec.sistema_farmacia.modelo.vistas;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import py.com.cs.xnumberfield.component.NumberTextField;
import py.edu.facitec.reutilizacion.ventanas.MiVentanaGenerica;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Funcionario;

public class VentanaCompra extends MiVentanaGenerica {

	private static final long serialVersionUID = 1L;

	private NumberTextField txtId;
	private JTextField txtFecha;
	private NumberTextField txtTotal;
	private JTextField txtFuncionario;
	private JButton btnBuscarFuncionario;
	private JTable table;
	private JButton btnAgregar;
	private JButton btnEliminar;
	private JButton btnActualizar;

	private Funcionario funcionarioSeleccionado;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(() -> {
			try {
				VentanaCompra dialog = new VentanaCompra();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaCompra() {

		getContentPane().setBackground(new Color(255, 250, 240));
		getPanelFormulario().setBackground(new Color(135, 206, 250));

		//---------------------------------------------
		// ID
		//---------------------------------------------

		JLabel lblId = new JLabel("Id:");
		lblId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblId.setBounds(60, 40, 90, 16);
		getPanelFormulario().add(lblId);

		txtId = new NumberTextField();
		txtId.setEditable(false);
		txtId.setBounds(170, 35, 120, 26);
		getPanelFormulario().add(txtId);

		//---------------------------------------------
		// FECHA
		//---------------------------------------------

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFecha.setBounds(350, 40, 70, 16);
		getPanelFormulario().add(lblFecha);

		txtFecha = new JTextField();
		txtFecha.setBounds(435, 35, 150, 26);
		getPanelFormulario().add(txtFecha);

		//---------------------------------------------
		// TOTAL
		//---------------------------------------------

		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotal.setBounds(620, 40, 70, 16);
		getPanelFormulario().add(lblTotal);

		txtTotal = new NumberTextField();
		txtTotal.setEditable(false);
		txtTotal.setBounds(705, 35, 150, 26);
		getPanelFormulario().add(txtTotal);

		//---------------------------------------------
		// FUNCIONARIO
		//---------------------------------------------

		JLabel lblFuncionario = new JLabel("Funcionario:");
		lblFuncionario.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFuncionario.setBounds(35, 85, 115, 16);
		getPanelFormulario().add(lblFuncionario);

		txtFuncionario = new JTextField();
		txtFuncionario.setEditable(false);
		txtFuncionario.setBounds(170, 80, 250, 26);
		getPanelFormulario().add(txtFuncionario);

		btnBuscarFuncionario = new JButton("...");
		btnBuscarFuncionario.setBounds(425, 80, 35, 26);
		getPanelFormulario().add(btnBuscarFuncionario);

		//---------------------------------------------
		// DETALLES
		//---------------------------------------------

		JLabel lblDetalle = new JLabel("Detalle de la Compra");
		lblDetalle.setHorizontalAlignment(SwingConstants.CENTER);
		lblDetalle.setBounds(20, 130, 900, 16);
		getPanelFormulario().add(lblDetalle);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 155, 900, 220);
		getPanelFormulario().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		//---------------------------------------------
		// BOTONES
		//---------------------------------------------

		btnAgregar = new JButton("Agregar Producto");
		btnAgregar.setBounds(20, 390, 170, 30);
		getPanelFormulario().add(btnAgregar);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(200, 390, 120, 30);
		getPanelFormulario().add(btnEliminar);

		btnActualizar = new JButton("Actualizar");
		btnActualizar.setBounds(330, 390, 120, 30);
		getPanelFormulario().add(btnActualizar);
		
		// Se llama al final, cuando los botones ya existen y son seguros de usar
		setUpController();
	}

	@Override
	protected String getTitulo() {
		return "Registro de Compras";
	}

	@Override
	protected String getTituloFormulario() {
		return "Formulario de Compra";
	}

	@Override
	public void setUpController() {

	}

	// --- Getters para el controlador ---

	public NumberTextField gettId() {
		return txtId;
	}

	public JTextField gettFecha() {
		return txtFecha;
	}

	public NumberTextField gettTotal() {
		return txtTotal;
	}

	public JTextField gettFuncionario() {
		return txtFuncionario;
	}

	public JButton getBtnBuscarFuncionario() {
		return btnBuscarFuncionario;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getBtnAgregar() {
		return btnAgregar;
	}

	public JButton getBtnEliminar() {
		return btnEliminar;
	}

	public JButton getBtnActualizar() {
		return btnActualizar;
	}

	public Funcionario getFuncionarioSeleccionado() {
		return funcionarioSeleccionado;
	}

	public void setFuncionarioSeleccionado(Funcionario funcionarioSeleccionado) {
		this.funcionarioSeleccionado = funcionarioSeleccionado;
		this.txtFuncionario.setText(
				funcionarioSeleccionado != null
						? funcionarioSeleccionado.getNombre() + " " + funcionarioSeleccionado.getApellido()
						: "");
	}

}
