package py.edu.facitec.sistema_farmacia.modelo.controladores;

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

    private ModeloTablaCompraDetalle modeloDetalle;

    private Funcionario funcionarioSeleccionado;
    private Producto productoSeleccionadoPanel; // el elegido con el botón "..." de arriba

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public VentanaCompraController(TransaccionCompra vista) {
        this.vista = vista;
        this.compraDAO = new CompraDAO();
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
        List<Producto> productos = productoDAO.recuperarTodo();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay productos registrados.");
            return;
        }

        // Precargamos con lo que el usuario ya eligió arriba (producto + cantidad del spinner)
        int cantidadPanel = (int) vista.getSpinnerCantProducto().getValue();

        CompraDetalle detalle = pedirDatosDeLinea(productos, productoSeleccionadoPanel, cantidadPanel);
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
    private CompraDetalle pedirDatosDeLinea(List<Producto> productos, Producto productoPreseleccionado, int cantidadInicial) {

        JComboBox<Producto> cbxProducto = new JComboBox<>(productos.toArray(new Producto[0]));
        if (productoPreseleccionado != null) {
            cbxProducto.setSelectedItem(productoPreseleccionado);
        }

        JTextField txtNumeroLote = new JTextField();
        JTextField txtVencimiento = new JTextField();
        JTextField txtCantidad = new JTextField(String.valueOf(cantidadInicial));
        NumberTextField txtCosto = new NumberTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Producto:"));
        panel.add(cbxProducto);
        panel.add(new JLabel("N° de Lote:"));
        panel.add(txtNumeroLote);
        panel.add(new JLabel("Vencimiento (dd/MM/yyyy):"));
        panel.add(txtVencimiento);
        panel.add(new JLabel("Costo Unitario:"));
        panel.add(txtCosto);

        int opcion = JOptionPane.showConfirmDialog(
                vista, panel, "Datos del producto comprado",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return null;
        }

        Producto producto = (Producto) cbxProducto.getSelectedItem();
        String numeroLote = txtNumeroLote.getText().trim();

        if (producto == null || numeroLote.isEmpty()
                || txtVencimiento.getText().trim().isEmpty()
                || txtCantidad.getText().trim().isEmpty()
                || txtCosto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos de la línea son obligatorios.");
            return null;
        }

        Date fechaVencimiento;
        int cantidad;
        double costo;
        try {
            fechaVencimiento = FORMATO_FECHA.parse(txtVencimiento.getText().trim());
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            costo = Double.parseDouble(txtCosto.getText().trim());
        } catch (ParseException pe) {
            JOptionPane.showMessageDialog(vista, "La fecha debe tener el formato dd/MM/yyyy.");
            return null;
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(vista, "Cantidad y Costo deben ser numéricos.");
            return null;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a cero.");
            return null;
        }

        Lote lote = new Lote();
        lote.setNumeroLote(numeroLote);
        lote.setFechaVencimiento(fechaVencimiento);
        lote.setStockActual(cantidad);
        lote.setProducto(producto);

        CompraDetalle detalle = new CompraDetalle();
        detalle.setProducto(producto);
        detalle.setLote(lote);
        detalle.setFechaVencimiento(fechaVencimiento);
        detalle.setCantidad(cantidad);
        detalle.setCosto(costo);

        return detalle;
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
            compra.setDetalles(detalles);

            for (CompraDetalle detalle : detalles) {
            	detalle.setCompra(compra);
                Lote lote = loteDAO.guardar(detalle.getLote());
                detalle.setLote(lote);

                MovimientoStock movimiento = new MovimientoStock();
                movimiento.setTipoMovimiento("ENTRADA");
                movimiento.setCantidad(detalle.getCantidad());
                movimiento.setFecha(new Date());
                movimiento.setLote(detalle.getLote());
                movimiento.setFuncionario(funcionarioSeleccionado);
                movimientoStockDAO.guardar(movimiento);
            }
            
            
            compraDAO.guardar(compra);


            JOptionPane.showMessageDialog(vista, "Compra registrada con éxito. Total: " + compra.getTotal());
            vista.dispose();

        } catch (Exception e) {
        	e.printStackTrace();
            //JOptionPane.showMessageDialog(vista, "Error al guardar la compra: " + e.getMessage());
        }
    }

    public void cancelar() {
        vista.dispose();
    }
}