package py.edu.facitec.sistema_farmacia.app;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import py.edu.facitec.reutilizacion.paneles.PanelFondo;
import py.edu.facitec.reutilizacion.botones.MiBoton;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaCategoria;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaCliente;

import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaFuncionario;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaMarca;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaProducto;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// -- Ventanas disponibles ABMs
	private VentanaCategoria vCategoria;
	private VentanaCliente vCliente;
	private VentanaFuncionario vFuncionario;
	private VentanaMarca vMarca;
	private VentanaProducto vProducto;

	private VentanaMarca vVenta;

	// -- Ventanas pendientes (se agregarán cuando estén listas) --
	// private VentanaListaClientes vListaClientes;
	// private VentanaListaProductos vListaProductos;
	// private VentanaListaVentas vListaVentas;
	// private VentanaConfiguracion vConfiguracion;

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
		setLocationRelativeTo(this);
		setExtendedState(MAXIMIZED_BOTH);
		setTitle("Sistema Farmacia");

		// ── MENÚ ──────────────────────────────────────────────────────────────────
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Movimiento
		JMenu mnMovimiento = new JMenu("Movimiento");
		menuBar.add(mnMovimiento);

		JMenuItem mntmVenta = new JMenuItem("Venta");
		mntmVenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//abrirVenta();
			}
		});
		mnMovimiento.add(mntmVenta);

		JMenuItem mntmCompra = new JMenuItem("Compra");
		mntmCompra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCategoria();
			}
		});
		mnMovimiento.add(mntmCompra);

		// Registro
		JMenu mnRegistro = new JMenu("Registro");
		menuBar.add(mnRegistro);

		JMenuItem mntmCliente = new JMenuItem("Cliente");
		mntmCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCliente();
			}
		});
		mnRegistro.add(mntmCliente);

		JMenuItem mntmProducto = new JMenuItem("Producto");
		mntmProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirProducto();
			}
		});
		mnRegistro.add(mntmProducto);

		JMenuItem mntmCategoria = new JMenuItem("Categoria");
		mntmCategoria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCategoria();
			}
		});
		mnRegistro.add(mntmCategoria);

		JMenuItem mntmMarca = new JMenuItem("Marca");
		mntmMarca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirMarca();
			}
		});
		mnRegistro.add(mntmMarca);

		JMenuItem mntmFuncionario = new JMenuItem("Funcionario");
		mntmFuncionario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirFuncionario();
			}
		});
		mnRegistro.add(mntmFuncionario);

		// Listado
		JMenu mnListado = new JMenu("Listado");
		menuBar.add(mnListado);

		JMenuItem mntmListClientes = new JMenuItem("Clientes");
		// conectar cuando VentanaListaClientes esté lista
		// mntmListClientes.addActionListener(e -> abrirListaClientes());
		mnListado.add(mntmListClientes);

		JMenuItem mntmListProductos = new JMenuItem("Productos");
		// TODO: conectar cuando VentanaListaProductos esté lista
		// mntmListProductos.addActionListener(e -> abrirListaProductos());
		mnListado.add(mntmListProductos);

		JMenuItem mntmListVentas = new JMenuItem("Ventas");
		// conectar cuando VentanaListaVentas esté lista
		// mntmListVentas.addActionListener(e -> abrirListaVentas());
		mnListado.add(mntmListVentas);

		// Informes
		JMenu mnInformes = new JMenu("Informes");
		menuBar.add(mnInformes);

		JMenuItem mntmReporteCompra = new JMenuItem("Reporte Compra");
		mntmReporteCompra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCategoria();
			}
		});
		mnInformes.add(mntmReporteCompra);

		// Utilidades
		JMenu mnUtilidades = new JMenu("Utilidades");
		menuBar.add(mnUtilidades);

		JMenuItem mntmConfiguracion = new JMenuItem("Configuracion");
		// conectar cuando VentanaConfiguracion esté lista
		// mntmConfiguracion.addActionListener(e -> abrirConfiguracion());
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
		mbtnVenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//	abrirVenta();
			}
		});
		toolBar.add(mbtnVenta);

		// BOTON COMPRA
		MiBoton mbtnCompra = new MiBoton();
		mbtnCompra.setText("Categoria");
		mbtnCompra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCategoria();
			}
		});
		toolBar.add(mbtnCompra);

		// BOTON CLIENTE
		MiBoton mbtnCliente = new MiBoton();
		mbtnCliente.setText("Cliente");
		mbtnCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCliente();
			}
		});
		toolBar.add(mbtnCliente);

		// BOTON PRODUCTO
		MiBoton mbtnProducto = new MiBoton();
		mbtnProducto.setText("Producto");
		mbtnProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirProducto();
			}
		});
		toolBar.add(mbtnProducto);

		// BOTON FACTURACION
		MiBoton mbtnFacturacion = new MiBoton();
		mbtnFacturacion.setText("Facturacion");
		mbtnFacturacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//	abrirVenta();
			}// abrir facturacion cuando este
		});
		toolBar.add(mbtnFacturacion);

		// BOTON SALIR
		MiBoton mbtnSalir = new MiBoton();
		mbtnSalir.setText("Salir");
		mbtnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salir();
			}
		});
		toolBar.add(mbtnSalir);

		// ── PANEL FONDO
		PanelFondo panelFondo = new PanelFondo();
		panelFondo.setFondo("logo_farma.png");
		contentPane.add(panelFondo, BorderLayout.CENTER);
	}

	// MÉTODOS DISPONIBLES ABM

	public void abrirCliente() {
		vCliente = new VentanaCliente();
		vCliente.setUpController();
		vCliente.setVisible(true);
	}

	public void abrirProducto() {
		vProducto = new VentanaProducto();
		vProducto.setUpController();
		vProducto.setVisible(true);
	}

	public void abrirCategoria() {
		vCategoria = new VentanaCategoria();
		vCategoria.setUpController();
		vCategoria.setVisible(true);
	}

	public void abrirMarca() {
		vMarca = new VentanaMarca();
		vMarca.setUpController();
		vMarca.setVisible(true);
	}

	public void abrirFuncionario() {
		vFuncionario = new VentanaFuncionario();
		vFuncionario.setUpController();
		vFuncionario.setVisible(true);
	}

	//public void abrirVenta() {
	//	vVenta = new Ventana();
	//	vVenta.setUpController();
	//	vVenta.setVisible(true);
	//}

	// ── MÉTODOS PENDIENTES (descomentar cuando las ventanas estén lis

	// public void abrirListaClientes() {
	// vListaClientes = new VentanaListaClientes();
	// vListaClientes.setUpController();
	// vListaClientes.setVisible(true);
	// }

	// public void abrirListaProductos() {
	// vListaProductos = new VentanaListaProductos();
	// vListaProductos.setUpController();
	// vListaProductos.setVisible(true);
	// }

	// public void abrirListaVentas() {
	// vListaVentas = new VentanaListaVentas();
	// vListaVentas.setUpController();
	// vListaVentas.setVisible(true);
	// }

	// public void abrirConfiguracion() {
	// vConfiguracion = new VentanaConfiguracion();
	// vConfiguracion.setUpController();
	// vConfiguracion.setVisible(true);
	// }

	public void salir() {
		int opcion = javax.swing.JOptionPane.showConfirmDialog(this, "¿Desea salir del sistema?", "Confirmar salida",
				javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
		if (opcion == javax.swing.JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}