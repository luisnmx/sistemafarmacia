package py.edu.facitec.sistema_farmacia.modelo.controladores;

import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import py.edu.facitec.reutilizacion.interfaces.AccionesABM;
import py.edu.facitec.sistema_farmacia.modelo.dao.CompraDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.CompraDetalleDAO;
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
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaCompra;


public class VentanaCompraController implements AccionesABM {

    private VentanaCompra vista;

    //  los DAOs necesarios: uno de cada entidad que utilizamos 
    private CompraDAO compraDAO;
    private CompraDetalleDAO compraDetalleDAO;
    private LoteDAO loteDAO;
    private MovimientoStockDAO movimientoStockDAO;
    private ProductoDAO productoDAO;
    private FuncionarioDAO funcionarioDAO;

    private ModeloTablaCompraDetalle modeloDetalle;

    private Funcionario funcionarioSeleccionado;

    // Formato de la  fecha (dd/MM/yyyy)
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public VentanaCompraController(VentanaCompra vista) {
        this.vista = vista;
        this.compraDAO = new CompraDAO();
        this.compraDetalleDAO = new CompraDetalleDAO();
        this.loteDAO = new LoteDAO();
        this.movimientoStockDAO = new MovimientoStockDAO();
        this.productoDAO = new ProductoDAO();
        this.funcionarioDAO = new FuncionarioDAO();

        // La tabla de la vista pasa a mostrar el detalle de la compra en curso
        this.modeloDetalle = new ModeloTablaCompraDetalle();
        this.vista.getTable().setModel(modeloDetalle);

        // Conectamos el toolbar  con esta clase
        vista.getMiToolbar().setAcciones(this);

        // Botones propios de la transacción 
        vista.getBtnBuscarFuncionario().addActionListener(e -> buscarFuncionario());
        vista.getBtnAgregar().addActionListener(e -> agregarProducto());
        vista.getBtnEliminar().addActionListener(e -> eliminarLineaSeleccionada());
        vista.getBtnActualizar().addActionListener(e -> actualizarLineaSeleccionada());

        // nada esta habilitado hasta que usuario presione nuevo
        vista.getMiToolbar().estadoInicial(true);
        habilitarCampos(false);
        limpiarFormulario();
    }


    
    //  buscamos el funcionario  de Funcionario para la compra


    private void buscarFuncionario() {
       
        List<Funcionario> lista = FuncionarioDAO.recuperarPorFiltro(null); 

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron funcionarios.");
            return;
        }
    
        Funcionario seleccionado = (Funcionario) JOptionPane.showInputDialog(
            null,                              
            "Seleccione un funcionario:",       
            "Buscar Funcionario",               
            JOptionPane.QUESTION_MESSAGE,      
            null,                              
            lista.toArray(),                    
            null                               
        );       
        if (seleccionado != null) {
            vista.gettBuscador().setText(seleccionado.getNombre() + " " + seleccionado.getApellido()); 
            this.funcionarioSeleccionado = seleccionado; 
        }
    }

    
    
    private void agregarProducto() {
        List<Producto> productos = productoDAO.recuperarTodo();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay productos registrados.");
            return;
        }

        CompraDetalle detalle = pedirDatosDeLinea(productos, null);
        if (detalle != null) {
            modeloDetalle.agregar(detalle);
            recalcularTotal();
        }
    }

    private void actualizarLineaSeleccionada() {
        int fila = vista.getTable().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccioná una línea de la tabla primero.");
            return;
        }

        List<Producto> productos = productoDAO.recuperarTodo();
        CompraDetalle detalleActual = modeloDetalle.getDetalleEn(fila);

        CompraDetalle detalleEditado = pedirDatosDeLinea(productos, detalleActual);
        if (detalleEditado != null) {
            modeloDetalle.actualizar(fila, detalleEditado);
            recalcularTotal();
        }
    }

    private void eliminarLineaSeleccionada() {
        int fila = vista.getTable().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccioná una línea de la tabla primero.");
            return;
        }
        modeloDetalle.quitar(fila);
        recalcularTotal();
    }

    
    
    //  Muestra el formulario modal para agregar o editar un ítem del detalle de compra.
     
    private CompraDetalle pedirDatosDeLinea(List<Producto> productos, CompraDetalle valorInicial) {

        JComboBox<Producto> cbxProducto = new JComboBox<>(productos.toArray(new Producto[0]));
        JTextField txtNumeroLote = new JTextField();
        JTextField txtVencimiento = new JTextField(); // formato dd/MM/yyyy
        JTextField txtCantidad = new JTextField();
        JTextField txtCosto = new JTextField();

        if (valorInicial != null) {
            cbxProducto.setSelectedItem(valorInicial.getProducto());
            if (valorInicial.getLote() != null) {
                txtNumeroLote.setText(valorInicial.getLote().getNumeroLote());
            }
            if (valorInicial.getFechaVencimiento() != null) {
                txtVencimiento.setText(FORMATO_FECHA.format(valorInicial.getFechaVencimiento()));
            }
            txtCantidad.setText(String.valueOf(valorInicial.getCantidad()));
            txtCosto.setText(String.valueOf(valorInicial.getCosto()));
        }

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new javax.swing.JLabel("Producto:"));
        panel.add(cbxProducto);
        panel.add(new javax.swing.JLabel("N° de Lote:"));
        panel.add(txtNumeroLote);
        panel.add(new javax.swing.JLabel("Vencimiento (dd/MM/yyyy):"));
        panel.add(txtVencimiento);
        panel.add(new javax.swing.JLabel("Cantidad:"));
        panel.add(txtCantidad);
        panel.add(new javax.swing.JLabel("Costo Unitario:"));
        panel.add(txtCosto);

        int opcion = JOptionPane.showConfirmDialog(
                vista, panel, "Datos del producto comprado",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return null; // el usuario canceló
        }

        // --- Validaciones 
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
            JOptionPane.showMessageDialog(vista, "La fecha de vencimiento debe tener el formato dd/MM/yyyy.");
            return null;
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(vista, "Cantidad y Costo deben ser valores numéricos válidos.");
            return null;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a cero.");
            return null;
        }

        // Armamos el Lote todavía en memoria, se persiste recién al Guardar la compra
        Lote lote = (valorInicial != null && valorInicial.getLote() != null) ? valorInicial.getLote() : new Lote();
        lote.setNumeroLote(numeroLote);
        lote.setFechaVencimiento(fechaVencimiento);
        lote.setStockActual(cantidad);
        lote.setProducto(producto);

        // Armamos el CompraDetalle correspondiente a esta línea
        CompraDetalle detalle = (valorInicial != null) ? valorInicial : new CompraDetalle();
        detalle.setProducto(producto);
        detalle.setLote(lote);
        detalle.setFechaVencimiento(fechaVencimiento);
        detalle.setCantidad(cantidad);
        detalle.setCosto(costo);

        return detalle;
    }

    // calculamos otv el total sumando todas las líneas y lo muestra en el campo Total
    private void recalcularTotal() {
        vista.gettTotal().setText(String.valueOf(modeloDetalle.calcularTotal()));
    }

 
    //  Habilitar / limpiar formulario
    

    private void habilitarCampos(boolean habilitado) {
        vista.gettFecha().setEnabled(habilitado);
        vista.getBtnBuscarFuncionario().setEnabled(habilitado);
        vista.getBtnAgregar().setEnabled(habilitado);
        vista.getBtnEliminar().setEnabled(habilitado);
        vista.getBtnActualizar().setEnabled(habilitado);
    }

    private void limpiarFormulario() {
        vista.gettId().setText("");
        vista.gettFecha().setText(FORMATO_FECHA.format(new Date())); // fecha de hoy por defecto
        vista.gettTotal().setText("0.0");
        vista.setFuncionarioSeleccionado(null);
        funcionarioSeleccionado = null;
        modeloDetalle.setLista(new java.util.ArrayList<>());
    }
    //  Implementación de AccionesABM botones del toolbar genérico
 
    @Override
    public void nuevo() {
        limpiarFormulario();
        habilitarCampos(true);
        vista.getMiToolbar().estadoInicial(false);
    }

    @Override
    public void modificar() {
    	// Solo permite registrar compras nuevas; no se modifican compras existentes para proteger el stock.
        JOptionPane.showMessageDialog(vista,
                "Las compras ya guardadas no se pueden modificar. Generá un ajuste de stock en su lugar.");
    }

    @Override
    public void eliminar() {
        JOptionPane.showMessageDialog(vista,
                "Las compras ya guardadas no se pueden eliminar, para mantener la trazabilidad del stock.");
    }

    // Registra la transacción completa de compra afectando stock y lote.
     
    @Override
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
            // Cabecera de la compra
            Compra compra = new Compra();
            compra.setFecha(new Date());
            compra.setFuncionario(funcionarioSeleccionado);
            compra.setTotal(modeloDetalle.calcularTotal());
            compraDAO.guardar(compra); // acá Hibernate le asigna el id autogenerado

         // Por cada ítem: guarda el Lote aumenta stock, el CompraDetalle y el MovimientoStock ENTRADA
            for (CompraDetalle detalle : detalles) {
                loteDAO.guardar(detalle.getLote());

                detalle.setCompra(compra);
                compraDetalleDAO.guardar(detalle);

                MovimientoStock movimiento = new MovimientoStock();
                movimiento.setTipoMovimiento("ENTRADA");
                movimiento.setCantidad(detalle.getCantidad());
                movimiento.setFecha(new Date());
                movimiento.setLote(detalle.getLote());
                movimiento.setFuncionario(funcionarioSeleccionado);
                movimientoStockDAO.guardar(movimiento);
            }

            JOptionPane.showMessageDialog(vista, "Compra registrada con éxito. Total: " + compra.getTotal());

            habilitarCampos(false);
            vista.getMiToolbar().estadoInicial(true);
            limpiarFormulario();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al guardar la compra: " + e.getMessage());
        }
    }

    @Override
    public void cancelar() {
        habilitarCampos(false);
        vista.getMiToolbar().estadoInicial(true);
        limpiarFormulario();
    }

    @Override
    public void salir() {
        vista.dispose();
    }
}