package py.edu.facitec.sistema_farmacia.modelo.vistas;

import java.awt.EventQueue;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import py.edu.facitec.reutilizacion.ventanas.MiVentanaGenerica;
import py.edu.facitec.sistema_farmacia.modelo.controladores.VentanaProductoController;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Categoria;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Marca;
import py.com.cs.xnumberfield.component.NumberTextField;

public class VentanaProducto extends MiVentanaGenerica {

	private static final long serialVersionUID = 1L;

	//  Componentes gráficos del formulario 
	private NumberTextField txtId;              // Campo de ID se deshabilita porque es auto
	private JTextField txtCodigo;
	private JTextField txtDescripcion;
	private JTextField txtCantidad;             // Campo de apoyo visual (no pertenece directo al DER)
	private NumberTextField txtCostoCompra;      // Campo de apoyo visual (no pertenece directo al DER)
	private NumberTextField txtPrecioVenta;

	private JTextField txtMarca;                // Muestra la descripción de la marca elegida
	private JButton btnBuscarMarca;             // Botón '...' para abrir el selector de marcas
	private JTextField txtCategoria;            // Muestra la descripción de la categoría elegida
	private JButton btnBuscarCategoria;         // Botón '...' para abrir el selector de categorías

	private JComboBox<String> cbxTipoImpuesto;
	private JTextField txtFormaFarmaceutica;
	private JTextField txtPrincipioActivo;
	private JTextField txtConcentracion;
	private JCheckBox chkRequiereReceta;
	private JCheckBox chkTieneVencimiento;

	// --- Referencias a los objetos completos seleccionados con las lupas/botones ---
	private Marca marcaSeleccionada;
	private Categoria categoriaSeleccionada;

	/**
	 * Método principal para probar la ventana de forma independiente
	 */
	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaProducto dialog = new VentanaProducto();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor: arma todo el diseño visual de la ventana y llama al controlador
	 */
	public VentanaProducto() {
		// Personalizamos un poco los colores de fondo
		getContentPane().setBackground(new Color(255, 250, 240));
		getPanelFormulario().setBackground(new Color(135, 206, 250));

		// Label y Input para ID
		JLabel lblIi = new JLabel("Id:");
		lblIi.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIi.setBounds(67, 44, 80, 16);
		getPanelFormulario().add(lblIi);

		txtId = new NumberTextField();
		txtId.setEditable(false);
		txtId.setBounds(201, 42, 130, 26);
		getPanelFormulario().add(txtId);

		// Label y Input para Código
		JLabel lblCodigo = new JLabel("Codigo:");
		lblCodigo.setBounds(677, 44, 56, 16);
		getPanelFormulario().add(lblCodigo);

		txtCodigo = new JTextField();
		txtCodigo.setBounds(751, 39, 130, 26);
		getPanelFormulario().add(txtCodigo);

		// Label y Input para Descripción
		JLabel lblDescripcion = new JLabel("Descripcion:");
		lblDescripcion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDescripcion.setBounds(67, 84, 92, 16);
		getPanelFormulario().add(lblDescripcion);

		txtDescripcion = new JTextField();
		txtDescripcion.setBounds(201, 79, 250, 26);
		getPanelFormulario().add(txtDescripcion);

		// Label y Input para Cantidad
		JLabel lblCantidad = new JLabel("Cantidad:");
		lblCantidad.setBounds(670, 84, 63, 16);
		getPanelFormulario().add(lblCantidad);

		txtCantidad = new JTextField();
		txtCantidad.setBounds(751, 79, 130, 26);
		getPanelFormulario().add(txtCantidad);

		// Label y Input para Costo de Compra
		JLabel lblCostoCompra = new JLabel("Costo de Compra:");
		lblCostoCompra.setBounds(67, 125, 130, 16);
		getPanelFormulario().add(lblCostoCompra);

		txtCostoCompra = new NumberTextField();
		txtCostoCompra.setBounds(201, 117, 130, 26);
		getPanelFormulario().add(txtCostoCompra);

		// Label y Input para Precio de Venta
		JLabel lblPrecioVenta = new JLabel("Precio de Venta:");
		lblPrecioVenta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrecioVenta.setBounds(624, 125, 109, 16);
		getPanelFormulario().add(lblPrecioVenta);

		txtPrecioVenta = new NumberTextField();
		txtPrecioVenta.setBounds(751, 120, 130, 26);
		getPanelFormulario().add(txtPrecioVenta);

		// Campo y Botón de búsqueda para Marca
		JLabel lblMarca = new JLabel("Marca:");
		lblMarca.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMarca.setBounds(67, 170, 80, 16);
		getPanelFormulario().add(lblMarca);

		txtMarca = new JTextField();
		txtMarca.setEditable(false); // Solo lectura, se completa mediante el botón
		txtMarca.setBounds(201, 165, 130, 26);
		getPanelFormulario().add(txtMarca);

		btnBuscarMarca = new JButton("...");
		btnBuscarMarca.setBounds(336, 165, 35, 26);
		getPanelFormulario().add(btnBuscarMarca);

		// Campo y Botón de búsqueda para Categoría
		JLabel lblCategoria = new JLabel("Categoria:");
		lblCategoria.setBounds(663, 170, 70, 16);
		getPanelFormulario().add(lblCategoria);

		txtCategoria = new JTextField();
		txtCategoria.setEditable(false); // Solo lectura, se completa mediante el botón
		txtCategoria.setBounds(751, 165, 130, 26);
		getPanelFormulario().add(txtCategoria);

		btnBuscarCategoria = new JButton("...");
		btnBuscarCategoria.setBounds(888, 165, 35, 26);
		getPanelFormulario().add(btnBuscarCategoria);

		// Selector combo para el Tipo de Impuesto
		JLabel lblTipoImpuesto = new JLabel("Tipo de Impuesto:");
		lblTipoImpuesto.setBounds(67, 208, 130, 16);
		getPanelFormulario().add(lblTipoImpuesto);

		cbxTipoImpuesto = new JComboBox<String>();
		cbxTipoImpuesto.addItem("IVA 10%");
		cbxTipoImpuesto.addItem("IVA 5%");
		cbxTipoImpuesto.addItem("Exento");
		cbxTipoImpuesto.setBounds(201, 203, 170, 26);
		getPanelFormulario().add(cbxTipoImpuesto);

		// Label y Input para Forma Farmacéutica
		JLabel lblFormaFarmaceutica = new JLabel("Forma Farmaceutica:");
		lblFormaFarmaceutica.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFormaFarmaceutica.setBounds(607, 213, 139, 16);
		getPanelFormulario().add(lblFormaFarmaceutica);

		txtFormaFarmaceutica = new JTextField();
		txtFormaFarmaceutica.setBounds(751, 206, 130, 26);
		getPanelFormulario().add(txtFormaFarmaceutica);

		// Label y Input para Principio Activo
		JLabel lblPrincipioActivo = new JLabel("Principio activo:");
		lblPrincipioActivo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrincipioActivo.setBounds(67, 251, 109, 16);
		getPanelFormulario().add(lblPrincipioActivo);

		txtPrincipioActivo = new JTextField();
		txtPrincipioActivo.setBounds(201, 246, 130, 26);
		getPanelFormulario().add(txtPrincipioActivo);

		// Checkbox para Requiere Receta
		JLabel lblRequiereReceta = new JLabel("Requiere receta:");
		lblRequiereReceta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequiereReceta.setBounds(637, 251, 109, 16);
		getPanelFormulario().add(lblRequiereReceta);

		chkRequiereReceta = new JCheckBox();
		chkRequiereReceta.setBounds(751, 244, 130, 26);
		getPanelFormulario().add(chkRequiereReceta);

		// Label y Input para Concentración
		JLabel lblConcentracion = new JLabel("Concentración:");
		lblConcentracion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConcentracion.setBounds(67, 284, 109, 16);
		getPanelFormulario().add(lblConcentracion);

		txtConcentracion = new JTextField();
		txtConcentracion.setBounds(201, 279, 130, 26);
		getPanelFormulario().add(txtConcentracion);

		// Checkbox para Tiene Vencimiento
		JLabel lblTieneVencimiento = new JLabel("Tiene Vencimiento:");
		lblTieneVencimiento.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTieneVencimiento.setBounds(637, 289, 109, 16);
		getPanelFormulario().add(lblTieneVencimiento);

		chkTieneVencimiento = new JCheckBox();
		chkTieneVencimiento.setBounds(751, 282, 130, 26);
		getPanelFormulario().add(chkTieneVencimiento);

		// Arrancamos el controlador al finalizar el armado visual
		setUpController();
	}

	@Override
	protected String getTitulo() {
		return "Registro de productos";
	}

	@Override
	protected String getTituloFormulario() {
		return "Formulario de Producto";
	}

	// Instanciamos el controlador pasándole esta misma ventana
	@Override
	public void setUpController() {
		new VentanaProductoController(this);
	}

	// --- Getters para dar acceso a los componentes desde el Controlador ---

	public NumberTextField gettId() {
		return txtId;
	}

	public JTextField gettCodigo() {
		return txtCodigo;
	}

	public JTextField gettDescripcion() {
		return txtDescripcion;
	}

	public JTextField gettCantidad() {
		return txtCantidad;
	}

	public NumberTextField gettCostoCompra() {
		return txtCostoCompra;
	}

	public NumberTextField gettPrecioVenta() {
		return txtPrecioVenta;
	}

	public JTextField gettMarca() {
		return txtMarca;
	}

	public JButton getBtnBuscarMarca() {
		return btnBuscarMarca;
	}

	public JTextField gettCategoria() {
		return txtCategoria;
	}

	public JButton getBtnBuscarCategoria() {
		return btnBuscarCategoria;
	}

	public JComboBox<String> getcbxTipoImpuesto() {
		return cbxTipoImpuesto;
	}

	public JTextField gettFormaFarmaceutica() {
		return txtFormaFarmaceutica;
	}

	public JTextField gettPrincipioActivo() {
		return txtPrincipioActivo;
	}

	public JTextField gettConcentracion() {
		return txtConcentracion;
	}

	public JCheckBox getChkRequiereReceta() {
		return chkRequiereReceta;
	}

	public JCheckBox getChkTieneVencimiento() {
		return chkTieneVencimiento;
	}

	// Getter y Setter especial para asignación de Objetos Marca
	public Marca getMarcaSeleccionada() {
		return marcaSeleccionada;
	}

	public void setMarcaSeleccionada(Marca marcaSeleccionada) {
		this.marcaSeleccionada = marcaSeleccionada;
		// Actualiza automáticamente el texto en pantalla cuando elegimos una marca
		this.txtMarca.setText(marcaSeleccionada != null ? marcaSeleccionada.getDescripcion() : "");
	}

	// Getter y Setter especial para asignación de Objetos Categoría
	public Categoria getCategoriaSeleccionada() {
		return categoriaSeleccionada;
	}

	public void setCategoriaSeleccionada(Categoria categoriaSeleccionada) {
		this.categoriaSeleccionada = categoriaSeleccionada;
		// Actualiza automáticamente el texto en pantalla cuando elegimos una categoría
		this.txtCategoria.setText(categoriaSeleccionada != null ? categoriaSeleccionada.getDescripcion() : "");
	}
}