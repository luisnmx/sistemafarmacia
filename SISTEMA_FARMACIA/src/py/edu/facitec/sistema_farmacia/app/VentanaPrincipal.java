package py.edu.facitec.sistema_farmacia.app;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import py.edu.facitec.reutilizacion.botones.MiBoton;
import py.edu.facitec.reutilizacion.paneles.PanelFondo;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaCategoria;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaCliente;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaFuncionario;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaMarca;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaProducto;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// -- Ventanas disponibles ABMs
	private VentanaCategoria vCategoria;
	private VentanaCliente vCliente;
	private VentanaFuncionario vFuncionario;
	private VentanaMarca vMarca;
	private VentanaProducto vProducto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
		setTitle("Sistema Farmacia");

		// ── MENÚ ──────────────────────────────────────────────────────────────────
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Movimiento
		JMenu mnMovimiento = new JMenu("Movimiento");
		menuBar.add(mnMovimiento);

		JMenuItem mntmVenta = new JMenuItem("Venta");
		mntmVenta.addActionListener(e -> {
			// abrirVenta();
		});
		mnMovimiento.add(mntmVenta);

		JMenuItem mntmCompra = new JMenuItem("Compra");
		mntmCompra.addActionListener(e -> {
			// abrirCompra();
		});
		mnMovimiento.add(mntmCompra);

		// Registro
		JMenu mnRegistro = new JMenu("Registro");
		menuBar.add(mnRegistro);

		JMenuItem mntmCliente = new JMenuItem("Cliente");
		mntmCliente.addActionListener(e -> abrirCliente());
		mnRegistro.add(mntmCliente);

		JMenuItem mntmProducto = new JMenuItem("Producto");
		mntmProducto.addActionListener(e -> abrirProducto());
		mnRegistro.add(mntmProducto);

		JMenuItem mntmCategoria = new JMenuItem("Categoria");
		mntmCategoria.addActionListener(e -> abrirCategoria());
		mnRegistro.add(mntmCategoria);

		JMenuItem mntmMarca = new JMenuItem("Marca");
		mntmMarca.addActionListener(e -> abrirMarca());
		mnRegistro.add(mntmMarca);

		JMenuItem mntmFuncionario = new JMenuItem("Funcionario");
		mntmFuncionario.addActionListener(e -> abrirFuncionario());
		mnRegistro.add(mntmFuncionario);

		// Listado
		JMenu mnListado = new JMenu("Listado");
		menuBar.add(mnListado);

		JMenuItem mntmListClientes = new JMenuItem("Clientes");
		mnListado.add(mntmListClientes);

		JMenuItem mntmListProductos = new JMenuItem("Productos");
		mnListado.add(mntmListProductos);

		JMenuItem mntmListVentas = new JMenuItem("Ventas");
		mnListado.add(mntmListVentas);

		// Informes
		JMenu mnInformes = new JMenu("Informes");
		menuBar.add(mnInformes);

		JMenuItem mntmReporteCompra = new JMenuItem("Reporte Compra");
		mnInformes.add(mntmReporteCompra);

		// Utilidades
		JMenu mnUtilidades = new JMenu("Utilidades");
		menuBar.add(mnUtilidades);

		JMenuItem mntmConfiguracion = new JMenuItem("Configuracion");
		mnUtilidades.add(mntmConfiguracion);

		// ── PANEL PRINCIPAL ───────────────────────────────────────────────────────
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JToolBar toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		contentPane.add(toolBar, BorderLayout.WEST);

		// BOTON VENTA
		MiBoton mbtnVenta = new MiBoton();
		mbtnVenta.setText("Venta");
		mbtnVenta.addActionListener(e -> {
			// abrirVenta();
		});
		toolBar.add(mbtnVenta);

		// BOTON CLIENTE
		MiBoton mbtnCliente = new MiBoton();
		mbtnCliente.setText("Cliente");
		mbtnCliente.addActionListener(e -> abrirCliente());
		toolBar.add(mbtnCliente);

		// BOTON PRODUCTO
		MiBoton mbtnProducto = new MiBoton();
		mbtnProducto.setText("Producto");
		mbtnProducto.addActionListener(e -> abrirProducto());
		toolBar.add(mbtnProducto);

		// BOTON FUNCIONARIO
		MiBoton mbtnFuncionario = new MiBoton();
		mbtnFuncionario.setText("Funcionario");
		mbtnFuncionario.addActionListener(e -> abrirFuncionario());
		toolBar.add(mbtnFuncionario);

		// BOTON CATEGORIA
		MiBoton mbtnCategoria = new MiBoton();
		mbtnCategoria.setText("Categoria");
		mbtnCategoria.addActionListener(e -> abrirCategoria());
		toolBar.add(mbtnCategoria);

		// BOTON MARCA
		MiBoton mbtnMarca = new MiBoton();
		mbtnMarca.setText("Marca");
		mbtnMarca.addActionListener(e -> abrirMarca());
		toolBar.add(mbtnMarca);

		// BOTON SALIR
		MiBoton mbtnSalir = new MiBoton();
		mbtnSalir.setText("Salir");
		mbtnSalir.addActionListener(e -> salir());
		toolBar.add(mbtnSalir);

		// ── PANEL FONDO
		PanelFondo panelFondo = new PanelFondo();
		panelFondo.setFondo("fondo1.png");
		contentPane.add(panelFondo, BorderLayout.CENTER);
	}

	// MÉTODOS ABM

	public void abrirCliente() {
		if (vCliente == null || !vCliente.isVisible()) {
			vCliente = new VentanaCliente();
			vCliente.setVisible(true);
		} else {
			vCliente.toFront();
		}
	}

	public void abrirProducto() {
		if (vProducto == null || !vProducto.isVisible()) {
			vProducto = new VentanaProducto();
			vProducto.setVisible(true);
		} else {
			vProducto.toFront();
		}
	}

	public void abrirCategoria() {
		if (vCategoria == null || !vCategoria.isVisible()) {
			vCategoria = new VentanaCategoria();
			vCategoria.setVisible(true);
		} else {
			vCategoria.toFront();
		}
	}

	public void abrirMarca() {
		if (vMarca == null || !vMarca.isVisible()) {
			vMarca = new VentanaMarca();
			vMarca.setVisible(true);
		} else {
			vMarca.toFront();
		}
	}

	public void abrirFuncionario() {
		if (vFuncionario == null || !vFuncionario.isVisible()) {
			vFuncionario = new VentanaFuncionario();
			vFuncionario.setVisible(true);
		} else {
			vFuncionario.toFront();
		}
	}

	public void salir() {
		int opcion = JOptionPane.showConfirmDialog(this, "¿Desea salir del sistema?", "Confirmar salida",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (opcion == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}