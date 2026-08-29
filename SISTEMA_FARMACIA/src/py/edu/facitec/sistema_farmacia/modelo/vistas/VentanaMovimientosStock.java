package py.edu.facitec.sistema_farmacia.modelo.vistas;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import py.edu.facitec.sistema_farmacia.modelo.dao.MovimientoStockDAO;
import py.edu.facitec.sistema_farmacia.modelo.entidades.MovimientoStock;

public class VentanaMovimientosStock extends JDialog {

	private static final long serialVersionUID = 1L;

	private final Color AZUL_OSCURO = new Color(21, 61, 95);
	private final Color AZUL_PRINCIPAL = new Color(25, 118, 210);
	private final Color GRIS_TEXTO = new Color(55, 71, 79);
	private final Color FONDO = new Color(248, 250, 252);

	private JTextField txtBuscar;
	private JComboBox<String> cbxTipo;
	private JTable table;
	private DefaultTableModel modeloTabla;
	private JButton btnBuscar;
	private JButton btnCerrar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				try {

					VentanaMovimientosStock dialog =
							new VentanaMovimientosStock();

					dialog.setDefaultCloseOperation(
							JDialog.DISPOSE_ON_CLOSE);

					dialog.setVisible(true);

				} catch (Exception e) {

					e.printStackTrace();

				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public VentanaMovimientosStock() {

		setResizable(false);

		setModal(true);

		setTitle("Historial de movimientos - Farmacia");

		setBounds(100, 100, 980, 600);

		setLocationRelativeTo(null);

		getContentPane().setBackground(FONDO);

		getContentPane().setLayout(null);

		// =====================================================
		// ENCABEZADO
		// =====================================================

		JPanel panelHeader = new JPanel();

		panelHeader.setBackground(AZUL_OSCURO);

		panelHeader.setBounds(0, 0, 964, 82);

		panelHeader.setLayout(null);

		getContentPane().add(panelHeader);

		JLabel lblTitulo = new JLabel(
				"HISTORIAL DE MOVIMIENTOS");

		lblTitulo.setForeground(Color.WHITE);

		lblTitulo.setFont(
				new Font("Segoe UI", Font.BOLD, 23));

		lblTitulo.setBounds(25, 12, 500, 32);

		panelHeader.add(lblTitulo);

		JLabel lblSubtitulo = new JLabel(
				"Consulta todos los movimientos registrados en el stock");

		lblSubtitulo.setForeground(
				new Color(220, 235, 250));

		lblSubtitulo.setFont(
				new Font("Segoe UI", Font.PLAIN, 14));

		lblSubtitulo.setBounds(27, 46, 600, 22);

		panelHeader.add(lblSubtitulo);

		// =====================================================
		// BUSCAR
		// =====================================================

		JLabel lblBuscar = new JLabel("Buscar:");

		lblBuscar.setFont(
				new Font("Segoe UI", Font.BOLD, 13));

		lblBuscar.setForeground(GRIS_TEXTO);

		lblBuscar.setBounds(25, 105, 60, 25);

		getContentPane().add(lblBuscar);

		txtBuscar = new JTextField();

		txtBuscar.setFont(
				new Font("Segoe UI", Font.PLAIN, 13));

		txtBuscar.setBounds(
				82, 102, 350, 32);

		txtBuscar.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(
								new Color(205, 213, 220)),
						BorderFactory.createEmptyBorder(
								4, 8, 4, 8)));

		getContentPane().add(txtBuscar);

		// =====================================================
		// BOTÓN BUSCAR
		// =====================================================

		btnBuscar = new JButton("Buscar");

		btnBuscar.setBackground(
				AZUL_PRINCIPAL);

		btnBuscar.setForeground(Color.WHITE);

		btnBuscar.setFont(
				new Font("Segoe UI", Font.BOLD, 13));

		btnBuscar.setFocusPainted(false);

		btnBuscar.setBorder(
				BorderFactory.createEmptyBorder());

		btnBuscar.setMargin(
				new Insets(0, 0, 0, 0));

		btnBuscar.setBounds(
				442, 102, 100, 32);

		getContentPane().add(btnBuscar);

		// =====================================================
		// FILTRO POR TIPO
		// =====================================================

		JLabel lblTipo = new JLabel("Tipo:");

		lblTipo.setFont(
				new Font("Segoe UI", Font.BOLD, 13));

		lblTipo.setForeground(GRIS_TEXTO);

		lblTipo.setBounds(
				570, 105, 45, 25);

		getContentPane().add(lblTipo);

		cbxTipo = new JComboBox<String>();

		cbxTipo.addItem("Todos");
		cbxTipo.addItem("Entrada");
		cbxTipo.addItem("Salida");

		cbxTipo.setFont(
				new Font("Segoe UI", Font.PLAIN, 13));

		cbxTipo.setBounds(
				615, 102, 150, 32);

		cbxTipo.setBackground(Color.WHITE);

		getContentPane().add(cbxTipo);

		// =====================================================
		// TABLA
		// =====================================================

		modeloTabla = new DefaultTableModel(
				new Object[][] {},
				new String[] {
						"ID",
						"Fecha",
						"Producto",
						"Lote",
						"Tipo",
						"Cantidad",
						"Funcionario"
				}) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(
					int row,
					int column) {

				return false;
			}
		};

		table = new JTable(modeloTabla);

		table.setFont(
				new Font("Segoe UI", Font.PLAIN, 13));

		table.setRowHeight(30);

		table.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		table.setAutoCreateRowSorter(true);

		table.getTableHeader().setFont(
				new Font("Segoe UI", Font.BOLD, 13));

		table.getTableHeader().setBackground(
				AZUL_OSCURO);

		table.getTableHeader().setForeground(
				Color.WHITE);

		table.setGridColor(
				new Color(225, 230, 235));

		table.setSelectionBackground(
				new Color(227, 242, 253));

		table.setSelectionForeground(
				GRIS_TEXTO);

		JScrollPane scrollPane =
				new JScrollPane(table);

		scrollPane.setBounds(
				25, 155, 910, 325);

		scrollPane.setBorder(
				BorderFactory.createLineBorder(
						new Color(205, 213, 220)));

		getContentPane().add(scrollPane);

		// =====================================================
		// BOTÓN CERRAR
		// =====================================================

		btnCerrar = new JButton("Cerrar");

		btnCerrar.setBackground(
				new Color(198, 40, 40));

		btnCerrar.setForeground(Color.WHITE);

		btnCerrar.setFont(
				new Font("Segoe UI", Font.BOLD, 13));

		btnCerrar.setFocusPainted(false);

		btnCerrar.setBorder(
				BorderFactory.createEmptyBorder());

		btnCerrar.setBounds(
				800, 500, 135, 40);

		getContentPane().add(btnCerrar);

		// =====================================================
		// EVENTOS
		// =====================================================

		btnBuscar.addActionListener(e ->
				cargarMovimientos());

		txtBuscar.addActionListener(e ->
				cargarMovimientos());

		cbxTipo.addActionListener(e ->
				cargarMovimientos());

		btnCerrar.addActionListener(e ->
				dispose());

		// Cargar automáticamente al abrir
		cargarMovimientos();
	}

	/**
	 * Carga los movimientos desde la base de datos.
	 */
	public void cargarMovimientos() {

		modeloTabla.setRowCount(0);

		try {

			MovimientoStockDAO dao =
					new MovimientoStockDAO();

			List<MovimientoStock> lista;

			String filtro =
					txtBuscar.getText().trim();

			String tipo =
					cbxTipo.getSelectedItem().toString();

			if (filtro.isEmpty()
					&& tipo.equals("Todos")) {

				lista =
						dao.recuperarTodosConDetalles();

			} else {

				lista =
						dao.recuperarPorFiltro(filtro);
			}

			SimpleDateFormat formatoFecha =
					new SimpleDateFormat("dd/MM/yyyy");

			for (MovimientoStock movimiento : lista) {

				if (!tipo.equals("Todos")
						&& !tipo.equalsIgnoreCase(
								movimiento.getTipoMovimiento())) {

					continue;
				}

				String producto = "-";
				String lote = "-";
				String funcionario = "-";
				String fecha = "-";

				// Producto y lote
				if (movimiento.getLote() != null) {

					lote =
							movimiento.getLote()
									.getNumeroLote();

					if (movimiento.getLote()
							.getProducto() != null) {

						producto =
								movimiento.getLote()
										.getProducto()
										.getDescripcion();
					}
				}

				// Funcionario
				if (movimiento.getFuncionario() != null) {

					funcionario =
							movimiento.getFuncionario()
									.getNombre()
							+ " "
							+ movimiento.getFuncionario()
									.getApellido();
				}

				// Fecha
				if (movimiento.getFecha() != null) {

					fecha =
							formatoFecha.format(
									movimiento.getFecha());
				}

				modeloTabla.addRow(
						new Object[] {

								movimiento.getId(),

								fecha,

								producto,

								lote,

								movimiento
										.getTipoMovimiento(),

								movimiento.getCantidad(),

								funcionario
						});
			}

		} catch (Exception e) {

			e.printStackTrace();

			javax.swing.JOptionPane.showMessageDialog(
					this,
					"No se pudieron cargar los movimientos.\n\n"
							+ e.getMessage(),
					"Error",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	public JTable getTable() {
		return table;
	}

	public JTextField getTxtBuscar() {
		return txtBuscar;
	}

	public JComboBox<String> getCbxTipo() {
		return cbxTipo;
	}

	public JButton getBtnBuscar() {
		return btnBuscar;
	}

	public JButton getBtnCerrar() {
		return btnCerrar;
	}
}