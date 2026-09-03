package py.edu.facitec.sistema_farmacia.app;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import py.edu.facitec.sistema_farmacia.modelo.dao.VentaDAO;
import py.edu.facitec.reutilizacion.botones.MiBoton;
import py.edu.facitec.reutilizacion.paneles.PanelFondo;
import py.edu.facitec.sistema_farmacia.modelo.vistas.TransaccionCompra;
import py.edu.facitec.sistema_farmacia.modelo.vistas.TransaccionMovimientoStock;
import py.edu.facitec.sistema_farmacia.modelo.vistas.TransaccionVenta;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaCategoria;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaCliente;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaFuncionario;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaMarca;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaProducto;
import py.edu.facitec.sistema_farmacia.util.ConnectionHelper;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// -- Ventanas disponibles ABMs
	private VentanaCategoria vCategoria;
	private VentanaCliente vCliente;
	private VentanaFuncionario vFuncionario;
	private VentanaMarca vMarca;
	private VentanaProducto vProducto;
	private TransaccionVenta vVenta;
	private TransaccionCompra vCompra;
	private TransaccionMovimientoStock vMovimientoStock;
	private JPanel panelDashboard;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectionHelper.getSessionFactory();
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
		abrirVenta();
		});
		mnMovimiento.add(mntmVenta);

		JMenuItem mntmCompra = new JMenuItem("Compra");
		mntmCompra.addActionListener(e -> abrirCompra());
		mnMovimiento.add(mntmCompra);

		JMenuItem mntmMovimientoStock = new JMenuItem("Movimiento de Stock");
		mntmMovimientoStock.addActionListener(e -> abrirMovimientoStock());
		mnMovimiento.add(mntmMovimientoStock);

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
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.WEST);

		// ── GRUPO: MOVIMIENTOS ───────────────────────────────────────────────
		JLabel lblMovimientos = new JLabel("MOVIMIENTOS");
		lblMovimientos.setFont(lblMovimientos.getFont().deriveFont(11f));
		lblMovimientos.setBorder(new EmptyBorder(8, 10, 4, 0));
		toolBar.add(lblMovimientos);

		MiBoton mbtnVenta = new MiBoton();
		mbtnVenta.setText("Venta");
		aplicarEstiloBoton(mbtnVenta);
		mbtnVenta.addActionListener(e -> {
			// abrirVenta();
		});
		toolBar.add(mbtnVenta);

		MiBoton mbtnCompra = new MiBoton();
		mbtnCompra.setText("Compra");
		aplicarEstiloBoton(mbtnCompra);
		mbtnCompra.addActionListener(e -> {
			// abrirCompra();
		});
		toolBar.add(mbtnCompra);

		// ── GRUPO: MAESTROS ──────────────────────────────────────────────────
		JLabel lblMaestros = new JLabel("MAESTROS");
		lblMaestros.setFont(lblMaestros.getFont().deriveFont(11f));
		lblMaestros.setBorder(new EmptyBorder(12, 10, 4, 0));
		toolBar.add(lblMaestros);

		MiBoton mbtnCliente = new MiBoton();
		mbtnCliente.setText("Cliente");
		mbtnCliente.addActionListener(e -> abrirCliente());
		toolBar.add(mbtnCliente);

		MiBoton mbtnProducto = new MiBoton();
		mbtnProducto.setText("Producto");
		mbtnProducto.addActionListener(e -> abrirProducto());
		toolBar.add(mbtnProducto);

		MiBoton mbtnFuncionario = new MiBoton();
		mbtnFuncionario.setText("Funcionario");
		mbtnFuncionario.addActionListener(e -> abrirFuncionario());
		toolBar.add(mbtnFuncionario);

		MiBoton mbtnCategoria = new MiBoton();
		mbtnCategoria.setText("Categoria");
		mbtnCategoria.addActionListener(e -> abrirCategoria());
		toolBar.add(mbtnCategoria);

		MiBoton mbtnMarca = new MiBoton();
		mbtnMarca.setText("Marca");
		mbtnMarca.addActionListener(e -> abrirMarca());
		toolBar.add(mbtnMarca);

		// ── GRUPO: SISTEMA ───────────────────────────────────────────────────
		JLabel lblSistema = new JLabel("SISTEMA");
		lblSistema.setFont(lblSistema.getFont().deriveFont(11f));
		lblSistema.setBorder(new EmptyBorder(12, 10, 4, 0));
		toolBar.add(lblSistema);

		MiBoton mbtnSalir = new MiBoton();
		mbtnSalir.setText("Salir");
		mbtnSalir.addActionListener(e -> salir());
		toolBar.add(mbtnSalir);
		// ── PANEL DASHBOARD ───────────────────────────────────────────────────────
		panelDashboard = new JPanel();
		panelDashboard.setLayout(new java.awt.GridLayout(1, 2, 20, 0));
		panelDashboard.setBorder(new EmptyBorder(30, 30, 30, 30));
		panelDashboard.setBackground(new java.awt.Color(245, 245, 247));
		contentPane.setBackground(new java.awt.Color(245, 245, 247));
		contentPane.add(panelDashboard, BorderLayout.CENTER);

		refrescarDashboard();

		// Refresca los datos cada vez que la ventana principal vuelve a tener foco
		// (por ejemplo, al cerrar la pantalla de Venta luego de confirmar una venta)
		addWindowFocusListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowGainedFocus(java.awt.event.WindowEvent e) {
				refrescarDashboard();
			}
		});
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
	
	
	private void abrirVenta() {
		if (vVenta == null || !vVenta.isVisible()) {
			vVenta = new TransaccionVenta();
			vVenta.setUpController();
			vVenta.setVisible(true);
		} else {
			vVenta.toFront();
		}
	}

	private void abrirCompra() {
		if (vCompra == null || !vCompra.isVisible()) {
			vCompra = new TransaccionCompra();
			vCompra.setUpController();
			vCompra.setVisible(true);
		} else {
			vCompra.toFront();
		}
	}

	private void abrirMovimientoStock() {
		if (vMovimientoStock == null || !vMovimientoStock.isVisible()) {
			vMovimientoStock = new TransaccionMovimientoStock();
			vMovimientoStock.setUpController();
			vMovimientoStock.setVisible(true);
		} else {
			vMovimientoStock.toFront();
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
	private void refrescarDashboard() {
		panelDashboard.removeAll();
		panelDashboard.add(crearTarjeta("Ventas de hoy", obtenerTotalVentasHoy(), new java.awt.Color(46, 125, 88)));
		panelDashboard.add(crearTarjeta("Cantidad de ventas hoy", obtenerCantidadVentasHoy(), new java.awt.Color(46, 90, 125)));
		panelDashboard.revalidate();
		panelDashboard.repaint();
	}

	private JPanel crearTarjeta(String titulo, String valor, java.awt.Color colorAcento) {
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BorderLayout());
		tarjeta.setBackground(java.awt.Color.WHITE);
		tarjeta.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225), 1, true),
				new EmptyBorder(20, 20, 20, 20)
		));

		JLabel lblTitulo = new JLabel(titulo, SwingConstants.LEFT);
		lblTitulo.setFont(lblTitulo.getFont().deriveFont(java.awt.Font.PLAIN, 14f));
		lblTitulo.setForeground(new java.awt.Color(120, 120, 120));
		tarjeta.add(lblTitulo, BorderLayout.NORTH);

		JLabel lblValor = new JLabel(valor, SwingConstants.LEFT);
		lblValor.setFont(lblValor.getFont().deriveFont(java.awt.Font.BOLD, 36f));
		lblValor.setForeground(colorAcento);
		tarjeta.add(lblValor, BorderLayout.CENTER);

		return tarjeta;
	}
	private String obtenerTotalVentasHoy() {
		try {
			VentaDAO ventaDAO = new VentaDAO();
			double total = ventaDAO.sumarTotalPorFecha(new java.util.Date());
			return String.format("Gs. %,.0f", total);
		} catch (Exception e) {
			return "N/D";
		}
	}

	private String obtenerCantidadVentasHoy() {
		try {
			VentaDAO ventaDAO = new VentaDAO();
			long cantidad = ventaDAO.contarPorFecha(new java.util.Date());
			return String.valueOf(cantidad);
		} catch (Exception e) {
			return "N/D";
		}
	}
	private void aplicarEstiloBoton(MiBoton boton) {
		boton.setBackground(new java.awt.Color(45, 55, 65));
		boton.setForeground(java.awt.Color.WHITE);
	}

	public void salir() {
		int opcion = JOptionPane.showConfirmDialog(this, "¿Desea salir del sistema?", "Confirmar salida",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (opcion == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}