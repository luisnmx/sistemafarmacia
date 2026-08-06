package py.edu.facitec.sistema_farmacia.modelo.controladores;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import py.com.cs.xnumberfield.component.NumberTextField;
import py.edu.facitec.sistema_farmacia.modelo.dao.ClienteDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.FuncionarioDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.LoteDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.MovimientoStockDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.ProductoDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.VentaDAO;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Cliente;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Funcionario;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Lote;
import py.edu.facitec.sistema_farmacia.modelo.entidades.MovimientoStock;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Producto;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Venta;
import py.edu.facitec.sistema_farmacia.modelo.entidades.VentaDetalle;
import py.edu.facitec.sistema_farmacia.modelo.modelotabla.ModeloTablaVentaDetalle;
import py.edu.facitec.sistema_farmacia.modelo.vistas.TransaccionVenta;

public class VentanaVentaController {

    private TransaccionVenta vista;

    private VentaDAO ventaDAO;
    private LoteDAO loteDAO;
    private MovimientoStockDAO movimientoStockDAO;
    private ProductoDAO productoDAO;
    private FuncionarioDAO funcionarioDAO;
    private ClienteDAO clienteDAO;

    private ModeloTablaVentaDetalle modeloDetalle;

    private Cliente clienteSeleccionado;
    private Funcionario vendedorSeleccionado;
    private Producto productoSeleccionadoPanel; // el elegido con el botón "..." de arriba

    public VentanaVentaController(TransaccionVenta vista) {
        this.vista = vista;
        this.ventaDAO = new VentaDAO();
        this.loteDAO = new LoteDAO();
        this.movimientoStockDAO = new MovimientoStockDAO();
        this.productoDAO = new ProductoDAO();
        this.funcionarioDAO = new FuncionarioDAO();
        this.clienteDAO = new ClienteDAO();

        this.modeloDetalle = new ModeloTablaVentaDetalle();
        this.vista.getTable().setModel(modeloDetalle);

        vista.getBtnBuscarCliente().addActionListener(e -> buscarCliente());
        vista.getBtnBuscarVendedor().addActionListener(e -> buscarVendedor());
        vista.getBtnBuscarProducto().addActionListener(e -> buscarProductoPanel());
        vista.getBtnAgregarProducto().addActionListener(e -> agregarProducto());
        vista.getBtnQuitarProducto().addActionListener(e -> eliminarLineaSeleccionada());
        vista.getMbtnGuardar().addActionListener(e -> guardar());
        vista.getMbtnCancelar().addActionListener(e -> cancelar());

        // Cantidad por defecto del spinner: arranca en 1
        vista.getSpinnerCantProducto().setValue(1);

        limpiarFormulario();
    }

    // --- Buscar Cliente ---

    private void buscarCliente() {
        List<Cliente> lista = clienteDAO.recuperarTodo();

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se encontraron clientes registrados.");
            return;
        }

        Cliente seleccionado = (Cliente) JOptionPane.showInputDialog(
                vista, "Seleccione un cliente:", "Buscar Cliente",
                JOptionPane.QUESTION_MESSAGE, null, lista.toArray(), null
        );

        if (seleccionado != null) {
            this.clienteSeleccionado = seleccionado;
            vista.gettCliente().setText(seleccionado.getNombre() + " " + seleccionado.getApellido());
        }
    }

    // --- Buscar Vendedor (funcionario) ---

    private void buscarVendedor() {
        List<Funcionario> lista = funcionarioDAO.recuperarTodo();

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se encontraron funcionarios.");
            return;
        }

        Funcionario seleccionado = (Funcionario) JOptionPane.showInputDialog(
                vista, "Seleccione el vendedor:", "Buscar Vendedor",
                JOptionPane.QUESTION_MESSAGE, null, lista.toArray(), null
        );

        if (seleccionado != null) {
            this.vendedorSeleccionado = seleccionado;
            vista.gettVendedor().setText(seleccionado.getNombre() + " " + seleccionado.getApellido());
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

        VentaDetalle detalle = pedirDatosDeLinea(productos, productoSeleccionadoPanel, cantidadPanel);
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

    // Lotes con stock disponible para un producto (más antiguos primero, tipo FIFO)
    private List<Lote> lotesConStock(Producto producto) {
        List<Lote> lotes = loteDAO.recuperarPorFiltro(producto.getDescripcion());
        return lotes.stream()
                .filter(l -> l.getProducto() != null && l.getProducto().getId() == producto.getId())
                .filter(l -> l.getStockActual() > 0)
                .sorted((a, b) -> {
                    if (a.getFechaVencimiento() == null) return 1;
                    if (b.getFechaVencimiento() == null) return -1;
                    return a.getFechaVencimiento().compareTo(b.getFechaVencimiento());
                })
                .collect(Collectors.toList());
    }

    // Formulario modal para completar producto + lote + cantidad + precio de una línea
    private VentaDetalle pedirDatosDeLinea(List<Producto> productos, Producto productoPreseleccionado, int cantidadInicial) {

        JComboBox<Producto> cbxProducto = new JComboBox<>(productos.toArray(new Producto[0]));
        if (productoPreseleccionado != null) {
            cbxProducto.setSelectedItem(productoPreseleccionado);
        }

        JComboBox<Lote> cbxLote = new JComboBox<>();
        JTextField txtCantidad = new JTextField(String.valueOf(cantidadInicial));
        NumberTextField txtPrecio = new NumberTextField();

        Runnable actualizarLotes = () -> {
            cbxLote.removeAllItems();
            Producto p = (Producto) cbxProducto.getSelectedItem();
            if (p != null) {
                for (Lote l : lotesConStock(p)) {
                    cbxLote.addItem(l);
                }
                txtPrecio.setText(String.valueOf(p.getPrecioVenta()));
            }
        };
        cbxProducto.addActionListener(e -> actualizarLotes.run());
        actualizarLotes.run();

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Producto:"));
        panel.add(cbxProducto);
        panel.add(new JLabel("Lote (stock disponible):"));
        panel.add(cbxLote);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);
        panel.add(new JLabel("Precio Unitario:"));
        panel.add(txtPrecio);

        int opcion = JOptionPane.showConfirmDialog(
                vista, panel, "Datos del producto vendido",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return null;
        }

        Producto producto = (Producto) cbxProducto.getSelectedItem();
        Lote lote = (Lote) cbxLote.getSelectedItem();

        if (producto == null || lote == null
                || txtCantidad.getText().trim().isEmpty()
                || txtPrecio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos de la línea son obligatorios. "
                    + "Verificá que el producto tenga stock cargado.");
            return null;
        }

        int cantidad;
        double precio;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            precio = Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(vista, "Cantidad y Precio deben ser numéricos.");
            return null;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a cero.");
            return null;
        }

        if (cantidad > lote.getStockActual()) {
            JOptionPane.showMessageDialog(vista, "Stock insuficiente en el lote " + lote.getNumeroLote()
                    + ". Disponible: " + lote.getStockActual());
            return null;
        }

        VentaDetalle detalle = new VentaDetalle();
        detalle.setProducto(producto);
        detalle.setLote(lote);
        detalle.setCantidad(cantidad);
        detalle.setPrecio(precio);

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
        vista.gettCliente().setText("");
        vista.gettVendedor().setText("");
        clienteSeleccionado = null;
        vendedorSeleccionado = null;
        limpiarPanelProducto();
        modeloDetalle.setLista(new ArrayList<>());
    }

    // --- Guardar la transacción completa ---

    public void guardar() {
        if (vendedorSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná el funcionario que realiza la venta.");
            return;
        }

        List<VentaDetalle> detalles = modeloDetalle.getLista();
        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Agregá al menos un producto a la venta.");
            return;
        }

        try {
            Venta venta = new Venta();
            venta.setFecha(vista.gettFecha().getDate() != null ? vista.gettFecha().getDate() : new Date());
            venta.setCliente(clienteSeleccionado);
            venta.setFuncionario(vendedorSeleccionado);
            venta.setTotal(modeloDetalle.calcularTotal());
            venta.setEstado(true);
            venta.setDetalles(detalles);

            for (VentaDetalle detalle : detalles) {
                detalle.setVenta(venta);

                // Descontamos el stock del lote elegido
                Lote lote = detalle.getLote();
                lote.setStockActual(lote.getStockActual() - (int) detalle.getCantidad());
                lote = loteDAO.guardar(lote);
                detalle.setLote(lote);

                MovimientoStock movimiento = new MovimientoStock();
                movimiento.setTipoMovimiento("SALIDA");
                movimiento.setCantidad((int) detalle.getCantidad());
                movimiento.setFecha(new Date());
                movimiento.setLote(detalle.getLote());
                movimiento.setFuncionario(vendedorSeleccionado);
                movimientoStockDAO.guardar(movimiento);
            }

            ventaDAO.guardar(venta);

            JOptionPane.showMessageDialog(vista, "Venta registrada con éxito. Total: " + venta.getTotal());
            vista.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al guardar la venta: " + e.getMessage());
        }
    }

    public void cancelar() {
        vista.dispose();
    }
}
