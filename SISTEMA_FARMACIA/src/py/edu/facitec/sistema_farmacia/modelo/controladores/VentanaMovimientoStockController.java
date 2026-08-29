package py.edu.facitec.sistema_farmacia.modelo.controladores;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import py.edu.facitec.sistema_farmacia.modelo.dao.FuncionarioDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.LoteDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.MovimientoStockDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.ProductoDAO;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Funcionario;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Lote;
import py.edu.facitec.sistema_farmacia.modelo.entidades.MovimientoStock;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Producto;
import py.edu.facitec.sistema_farmacia.modelo.vistas.TransaccionMovimientoStock;

public class VentanaMovimientoStockController {

    private TransaccionMovimientoStock vista;

    private ProductoDAO productoDAO;
    private LoteDAO loteDAO;
    private FuncionarioDAO funcionarioDAO;
    private MovimientoStockDAO movimientoStockDAO;

    private Producto productoSeleccionado;
    private Funcionario funcionarioSeleccionado;

    private static final String[] MOTIVOS_ENTRADA = {
            "Donación", "Ajuste de inventario", "Devolución de cliente", "Otro"
    };
    private static final String[] MOTIVOS_SALIDA = {
            "Vencimiento", "Pérdida / Rotura", "Ajuste de inventario", "Otro"
    };

    // Sentinel para el combo de Lote cuando el usuario quiere cargar uno nuevo (solo Entrada)
    private static final String OPCION_NUEVO_LOTE = "-- Crear nuevo lote --";

    public VentanaMovimientoStockController(TransaccionMovimientoStock vista) {
        this.vista = vista;
        this.productoDAO = new ProductoDAO();
        this.loteDAO = new LoteDAO();
        this.funcionarioDAO = new FuncionarioDAO();
        this.movimientoStockDAO = new MovimientoStockDAO();

        vista.getBtnBuscarProducto().addActionListener(e -> buscarProducto());
        vista.getBtnBuscarFuncionario().addActionListener(e -> buscarFuncionario());
        vista.getCbxTipo().addActionListener(e -> actualizarMotivosYLote());
        vista.getCbxLote().addActionListener(e -> actualizarCamposNuevoLote());
        vista.getMbtnGuardar().addActionListener(e -> guardar());
        vista.getMbtnCancelar().addActionListener(e -> cancelar());

        limpiarFormulario();
    }

    private void buscarProducto() {
        List<Producto> productos = productoDAO.recuperarTodo();
        if (productos == null || productos.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay productos registrados.");
            return;
        }

        Producto seleccionado = (Producto) JOptionPane.showInputDialog(
                vista, "Seleccione un producto:", "Buscar Producto",
                JOptionPane.QUESTION_MESSAGE, null, productos.toArray(), null
        );

        if (seleccionado != null) {
            this.productoSeleccionado = seleccionado;
            vista.gettProducto().setText(seleccionado.getDescripcion());
            actualizarMotivosYLote();
        }
    }

    private void buscarFuncionario() {
        List<Funcionario> lista = funcionarioDAO.recuperarTodo();
        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se encontraron funcionarios.");
            return;
        }

        Funcionario seleccionado = (Funcionario) JOptionPane.showInputDialog(
                vista, "Seleccione el funcionario responsable:", "Buscar Funcionario",
                JOptionPane.QUESTION_MESSAGE, null, lista.toArray(), null
        );

        if (seleccionado != null) {
            this.funcionarioSeleccionado = seleccionado;
            vista.gettFuncionario().setText(seleccionado.getNombre() + " " + seleccionado.getApellido());
        }
    }

    private boolean esEntrada() {
        return "Entrada".equals(vista.getCbxTipo().getSelectedItem());
    }

    // Refresca el combo de Motivo (según Entrada/Salida) y el combo de Lote (según producto elegido)
    private void actualizarMotivosYLote() {
        vista.getCbxMotivo().removeAllItems();
        String[] motivos = esEntrada() ? MOTIVOS_ENTRADA : MOTIVOS_SALIDA;
        for (String m : motivos) {
            vista.getCbxMotivo().addItem(m);
        }

        vista.getCbxLote().removeAllItems();

        if (productoSeleccionado == null) {
            actualizarCamposNuevoLote();
            return;
        }

        if (esEntrada()) {
            // En Entrada dejamos elegir un lote existente (para sumarle stock) o crear uno nuevo
            vista.getCbxLote().addItem(OPCION_NUEVO_LOTE);
            for (Lote l : lotesDelProducto(productoSeleccionado)) {
                vista.getCbxLote().addItem(l);
            }
        } else {
            // En Salida solo se puede descontar de lotes que ya tengan stock
            for (Lote l : lotesConStock(productoSeleccionado)) {
                vista.getCbxLote().addItem(l);
            }
        }

        actualizarCamposNuevoLote();
    }

    // Habilita/deshabilita los campos de "lote nuevo" según lo que esté elegido en el combo
    private void actualizarCamposNuevoLote() {
        boolean mostrarNuevoLote = esEntrada()
                && OPCION_NUEVO_LOTE.equals(vista.getCbxLote().getSelectedItem());
        vista.gettNuevoLote().setEnabled(mostrarNuevoLote);
        vista.getDateVencimiento().setEnabled(mostrarNuevoLote);
        if (!mostrarNuevoLote) {
            vista.gettNuevoLote().setText("");
            vista.getDateVencimiento().setDate(null);
        }
    }

    private List<Lote> lotesDelProducto(Producto producto) {
        List<Lote> lotes = loteDAO.recuperarPorFiltro(producto.getDescripcion());
        return lotes.stream()
                .filter(l -> l.getProducto() != null && l.getProducto().getId() == producto.getId())
                .collect(Collectors.toList());
    }

    private List<Lote> lotesConStock(Producto producto) {
        return lotesDelProducto(producto).stream()
                .filter(l -> l.getStockActual() > 0)
                .sorted((a, b) -> {
                    if (a.getFechaVencimiento() == null) return 1;
                    if (b.getFechaVencimiento() == null) return -1;
                    return a.getFechaVencimiento().compareTo(b.getFechaVencimiento());
                })
                .collect(Collectors.toList());
    }

    private void limpiarFormulario() {
        productoSeleccionado = null;
        funcionarioSeleccionado = null;
        vista.gettProducto().setText("");
        vista.gettFuncionario().setText("");
        vista.gettCantidad().setText("");
        vista.gettNuevoLote().setText("");
        vista.getDateVencimiento().setDate(null);
        vista.gettFecha().setDate(new Date());
        vista.getCbxTipo().setSelectedIndex(0);
        actualizarMotivosYLote();
    }

    public void guardar() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Elegí un producto.");
            return;
        }
        if (funcionarioSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Elegí el funcionario responsable del movimiento.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(vista.gettCantidad().getText().trim());
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(vista, "La cantidad debe ser un número.");
            return;
        }
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a cero.");
            return;
        }

        String motivo = (String) vista.getCbxMotivo().getSelectedItem();
        if (motivo == null) {
            JOptionPane.showMessageDialog(vista, "Elegí un motivo.");
            return;
        }

        try {
            Lote lote;

            if (esEntrada()) {
                Object seleccion = vista.getCbxLote().getSelectedItem();

                if (OPCION_NUEVO_LOTE.equals(seleccion)) {
                    String numeroLote = vista.gettNuevoLote().getText().trim();
                    if (numeroLote.isEmpty()) {
                        JOptionPane.showMessageDialog(vista, "Ingresá el número del lote nuevo.");
                        return;
                    }

                    // Evitamos duplicar: si ya existe ese número de lote para el producto, sumamos stock
                    Lote existente = loteDAO.buscarPorProductoYNumero(productoSeleccionado, numeroLote);
                    if (existente != null) {
                        existente.setStockActual(existente.getStockActual() + cantidad);
                        lote = loteDAO.guardar(existente);
                    } else {
                        Lote nuevo = new Lote();
                        nuevo.setNumeroLote(numeroLote);
                        nuevo.setFechaVencimiento(vista.getDateVencimiento().getDate());
                        nuevo.setStockActual(cantidad);
                        nuevo.setProducto(productoSeleccionado);
                        lote = loteDAO.guardar(nuevo);
                    }
                } else if (seleccion instanceof Lote) {
                    lote = (Lote) seleccion;
                    lote.setStockActual(lote.getStockActual() + cantidad);
                    lote = loteDAO.guardar(lote);
                } else {
                    JOptionPane.showMessageDialog(vista, "Elegí un lote o \"" + OPCION_NUEVO_LOTE + "\".");
                    return;
                }

            } else {
                Object seleccion = vista.getCbxLote().getSelectedItem();
                if (!(seleccion instanceof Lote)) {
                    JOptionPane.showMessageDialog(vista, "Elegí el lote del cual vas a descontar stock. "
                            + "Si no aparece ninguno, es porque ese producto no tiene stock disponible.");
                    return;
                }
                lote = (Lote) seleccion;

                if (cantidad > lote.getStockActual()) {
                    JOptionPane.showMessageDialog(vista, "Stock insuficiente en el lote " + lote.getNumeroLote()
                            + ". Disponible: " + lote.getStockActual());
                    return;
                }

                lote.setStockActual(lote.getStockActual() - cantidad);
                lote = loteDAO.guardar(lote);
            }

            MovimientoStock movimiento = new MovimientoStock();
            // Combinamos tipo + motivo en el mismo campo (la tabla no tiene columna aparte para el motivo)
            String tipo = esEntrada() ? "ENTRADA" : "SALIDA";
            movimiento.setTipoMovimiento(tipo + ": " + motivo);
            movimiento.setCantidad(cantidad);
            movimiento.setFecha(vista.gettFecha().getDate() != null ? vista.gettFecha().getDate() : new Date());
            movimiento.setLote(lote);
            movimiento.setFuncionario(funcionarioSeleccionado);

            movimientoStockDAO.guardar(movimiento);

            JOptionPane.showMessageDialog(vista, "Movimiento de stock registrado con éxito.\n"
                    + "Lote " + lote.getNumeroLote() + " → nuevo stock: " + lote.getStockActual());
            vista.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al registrar el movimiento: " + e.getMessage());
        }
    }

    public void cancelar() {
        vista.dispose();
    }
}
