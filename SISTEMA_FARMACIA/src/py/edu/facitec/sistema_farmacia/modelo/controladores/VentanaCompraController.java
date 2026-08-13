package py.edu.facitec.sistema_farmacia.modelo.controladores;
import py.edu.facitec.sistema_farmacia.modelo.dao.CompraDetalleDAO;
import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import py.com.cs.xnumberfield.component.NumberTextField;
import py.edu.facitec.sistema_farmacia.modelo.dao.CompraDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.FuncionarioDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.LoteDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.MovimientoStockDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.ProductoDAO;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Compra;
import py.edu.facitec.sistema_farmacia.modelo.entidades.CompraDetalle;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Funcionario;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Lote;
import py.edu.facitec.sistema_farmacia.modelo.entidades.MovimientoStock;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Producto;
import py.edu.facitec.sistema_farmacia.modelo.modelotabla.ModeloTablaCompraDetalle;
import py.edu.facitec.sistema_farmacia.modelo.vistas.TransaccionCompra;

public class VentanaCompraController {

    private TransaccionCompra vista;

    private CompraDAO compraDAO;
    private LoteDAO loteDAO;
    private MovimientoStockDAO movimientoStockDAO;
    private ProductoDAO productoDAO;
    private FuncionarioDAO funcionarioDAO;
    private CompraDetalleDAO compraDetalleDAO; 
    private ModeloTablaCompraDetalle modeloDetalle;

    private Funcionario funcionarioSeleccionado;
    private Producto productoSeleccionadoPanel; // el elegido con el botón "..." de arriba

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public VentanaCompraController(TransaccionCompra vista) {
        this.vista = vista;
        this.compraDAO = new CompraDAO();
        this.compraDetalleDAO = new CompraDetalleDAO(); 
        this.loteDAO = new LoteDAO();
        this.movimientoStockDAO = new MovimientoStockDAO();
        this.productoDAO = new ProductoDAO();
        this.funcionarioDAO = new FuncionarioDAO();

        this.modeloDetalle = new ModeloTablaCompraDetalle();
        this.vista.getTable().setModel(modeloDetalle);

        vista.getBtnBuscarComprador().addActionListener(e -> buscarFuncionario());
        vista.getBtnBuscarProducto().addActionListener(e -> buscarProductoPanel());
        vista.getBtnAgregarProducto().addActionListener(e -> agregarProducto());
        vista.getBtnQuitarProducto().addActionListener(e -> eliminarLineaSeleccionada());
        vista.getMbtnGuardar().addActionListener(e -> guardar());
        vista.getMbtnCancelar().addActionListener(e -> cancelar());

        // Cantidad por defecto del spinner: arranca en 1
        vista.getSpinnerCantProducto().setValue(1);

        limpiarFormulario();
    }

    // --- Buscar Funcionario (comprador)

    private void buscarFuncionario() {
        List<Funcionario> lista = funcionarioDAO.recuperarTodo();

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se encontraron funcionarios.");
            return;
        }

        Funcionario seleccionado = (Funcionario) JOptionPane.showInputDialog(
                vista, "Seleccione un funcionario:", "Buscar Funcionario",
                JOptionPane.QUESTION_MESSAGE, null, lista.toArray(), null
        );

        if (seleccionado != null) {
            this.funcionarioSeleccionado = seleccionado;
            vista.gettComprador().setText(seleccionado.getNombre() + " " + seleccionado.getApellido());
        }
    }

    // --- Buscar Producto (panel superior) ---

    private void buscarProductoPanel() {
        List<Producto> productos = productoDAO.recuperarTodo();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay productos registrados.");
            return;
        }

        Producto seleccionado = (Producto) JOptionPane.showInputDialog(
                vista, "Seleccione un producto:", "Buscar Producto",
                JOptionPane.QUESTION_MESSAGE, null, productos.toArray(), null
        );

        if (seleccionado != null) {
            this.productoSeleccionadoPanel = seleccionado;
            vista.gettProducto().setText(seleccionado.getDescripcion());
        }
    }

    // --- Agregar / quitar líneas de detalle ---

    private void agregarProducto() {
        if (productoSeleccionadoPanel == null) {
            JOptionPane.showMessageDialog(vista, "Elegí un producto con el botón \"...\" antes de agregar.");
            return;
        }

        // Precargamos con lo que el usuario ya eligió arriba (producto + cantidad del spinner)
        int cantidadPanel = (int) vista.getSpinnerCantProducto().getValue();

        CompraDetalle detalle = pedirDatosDeLinea(productoSeleccionadoPanel, cantidadPanel);
        if (detalle != null) {
            modeloDetalle.agregar(detalle);
            limpiarPanelProducto(); // listo para cargar la próxima línea
        }
    }

    private void eliminarLineaSeleccionada() {
        int fila = vista.getTable().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccioná una línea de la tabla primero.");
            return;
        }
        modeloDetalle.quitar(fila);
    }

    
 // Formulario modal para completar producto + lote + cantidad + costo de una línea
 // Formulario modal para completar lote + vencimiento + costo de una línea.
 // El producto ya viene elegido desde el panel de arriba.
 private CompraDetalle pedirDatosDeLinea(Producto producto, int cantidadInicial) {

     JLabel lblProductoElegido = new JLabel(producto.getDescripcion());
     JTextField txtNumeroLote = new JTextField();
     JDateChooser dateVencimiento = new JDateChooser();
     dateVencimiento.setDateFormatString("dd/MM/yyyy");
     //JTextField txtVencimiento = new JTextField();
     JTextField txtCantidad = new JTextField(String.valueOf(cantidadInicial));
     NumberTextField txtCosto = new NumberTextField();

     JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
     panel.add(new JLabel("Producto:"));
     panel.add(lblProductoElegido);
     panel.add(new JLabel("N° de Lote (000 si no aplica):"));
     panel.add(txtNumeroLote);
     panel.add(new JLabel("Vencimiento:"));
     panel.add(dateVencimiento);
     //panel.add(new JLabel("Vencimiento (dd/MM/yyyy, opcional):"));
     //panel.add(txtVencimiento);
     panel.add(new JLabel("Costo Unitario:"));
     panel.add(txtCosto);

     int opcion = JOptionPane.showConfirmDialog(
             vista, panel, "Datos del producto comprado",
             JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

     if (opcion != JOptionPane.OK_OPTION) {
         return null;
     }

     String numeroLote = txtNumeroLote.getText().trim();

     // Obligatorios: número de lote, cantidad y costo.
     // El vencimiento queda opcional (para el caso "000" que no vence).
     if (numeroLote.isEmpty()
             || txtCantidad.getText().trim().isEmpty()
             || txtCosto.getText().trim().isEmpty()) {
         JOptionPane.showMessageDialog(vista, "Número de lote, cantidad y costo son obligatorios.");
         return null;
     }
     Date fechaVencimiento;
     int cantidad;
     double costo;
     try {
         fechaVencimiento = dateVencimiento.getDate(); // null si no seleccionó nada (queda opcional)
         cantidad = Integer.parseInt(txtCantidad.getText().trim());
         costo = Double.parseDouble(txtCosto.getText().trim());
     } catch (NumberFormatException ne) {
         JOptionPane.showMessageDialog(vista, "Cantidad y Costo deben ser numéricos.");
         return null;
     }

     // Este Lote es solo un "borrador" en memoria. La decisión real de
     // "buscar existente o crear nuevo" pasa en guardar(), con obtenerOCrearLote().
     Lote loteBorrador = new Lote();
     loteBorrador.setNumeroLote(numeroLote);
     loteBorrador.setFechaVencimiento(fechaVencimiento);
     loteBorrador.setStockActual(cantidad);
     loteBorrador.setProducto(producto);

     CompraDetalle detalle = new CompraDetalle();
     detalle.setProducto(producto);
     detalle.setLote(loteBorrador);
     detalle.setFechaVencimiento(fechaVencimiento);
     detalle.setCantidad(cantidad);
     detalle.setCosto(costo);

     return detalle;
 }
    
    
    private Lote obtenerOCrearLote(Lote loteBorrador) throws Exception {
        Producto producto = loteBorrador.getProducto();
        String numeroLote = loteBorrador.getNumeroLote();

        Lote loteExistente = loteDAO.buscarPorProductoYNumero(producto, numeroLote);

        if (loteExistente != null) {
            loteExistente.setStockActual(loteExistente.getStockActual() + loteBorrador.getStockActual());
            return loteDAO.guardar(loteExistente);
        } else {
            return loteDAO.guardar(loteBorrador);
        }
    }
    private void limpiarPanelProducto() {
        productoSeleccionadoPanel = null;
        vista.gettProducto().setText("");
        vista.getSpinnerCantProducto().setValue(1);
    }

    // --- Formulario general ---

    private void limpiarFormulario() {
        vista.gettFecha().setDate(new Date());
        vista.gettComprador().setText("");
        funcionarioSeleccionado = null;
        limpiarPanelProducto();
        modeloDetalle.setLista(new ArrayList<>());
    }

    // --- Guardar la transacción completa ---

    public void guardar() {
        if (funcionarioSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná el funcionario responsable de la compra.");
            return;
        }

        List<CompraDetalle> detalles = modeloDetalle.getLista();
        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Agregá al menos un producto a la compra.");
            return;
        }

        try {
            Compra compra = new Compra();
            compra.setFecha(vista.gettFecha().getDate() != null ? vista.gettFecha().getDate() : new Date());
            compra.setFuncionario(funcionarioSeleccionado);
            compra.setTotal(modeloDetalle.calcularTotal());

            compra = compraDAO.guardar(compra); // "compra" pasa a ser la versión gestionada, con id

            for (CompraDetalle detalle : detalles) {
                Lote lote = obtenerOCrearLote(detalle.getLote());
                detalle.setLote(lote);
                detalle.setCompra(compra);

                detalle = compraDetalleDAO.guardar(detalle); // recién ahora el detalle se persiste

                MovimientoStock movimiento = new MovimientoStock();
                movimiento.setTipoMovimiento("ENTRADA");
                movimiento.setCantidad(detalle.getCantidad());
                movimiento.setFecha(new Date());
                movimiento.setLote(detalle.getLote());
                movimiento.setFuncionario(funcionarioSeleccionado);
                movimientoStockDAO.guardar(movimiento);
            }

            JOptionPane.showMessageDialog(vista, "Compra registrada con éxito. Total: " + compra.getTotal());
            vista.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al guardar la compra: " + e.getMessage());
        }
    }

    public void cancelar() {
        vista.dispose();
    }
}