package py.edu.facitec.sistema_farmacia.modelo.controladores;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import py.edu.facitec.reutilizacion.interfaces.AccionesABM;
import py.edu.facitec.sistema_farmacia.modelo.dao.CategoriaDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.MarcaDAO;
import py.edu.facitec.sistema_farmacia.modelo.dao.ProductoDAO;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Categoria;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Marca;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Producto;
import py.edu.facitec.sistema_farmacia.modelo.modelotabla.ModeloTablaProductos;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaProducto;

public class VentanaProductoController implements AccionesABM {

    // Variables globales para la vista, los accesos a datos y el modelo de tabla
    private VentanaProducto vista;
    private ProductoDAO dao;
    private MarcaDAO marcaDAO;
    private CategoriaDAO categoriaDAO;
    private ModeloTablaProductos modeloTabla;
    private Producto productoSeleccionado; // Acá guardamos temporalmente el producto que el usuario toca en la tabla

    public VentanaProductoController(VentanaProducto vista) {
        this.vista = vista;
        this.dao = new ProductoDAO();
        this.marcaDAO = new MarcaDAO();
        this.categoriaDAO = new CategoriaDAO();
        
        // Le encajamos nuestro modelo personalizado a la tabla de la vista
        this.modeloTabla = new ModeloTablaProductos();
        this.vista.getTable().setModel(modeloTabla);

        // Enlazamos los botones de la toolbar (Nuevo, Guardar, Eliminar, etc.) con esta clase
        vista.getMiToolbar().setAcciones(this);

        // Listener para saber cuándo el usuario hace clic en una fila de la tabla
        vista.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Evita que se ejecute dos veces al hacer un solo clic
                seleccionarFila();
            }
        });

        // Escuchador del buscador para filtrar en tiempo real mientras se escribe
        vista.gettBuscador().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { buscar(); }
            public void removeUpdate(DocumentEvent e) { buscar(); }
            public void changedUpdate(DocumentEvent e) { buscar(); }
        });

        // Le damos vida a los botones "..." para buscar Marca y Categoría
        vista.getBtnBuscarMarca().addActionListener(e -> buscarMarca());
        vista.getBtnBuscarCategoria().addActionListener(e -> buscarCategoria());

        // Dejamos la pantalla en su estado inicial (campos bloqueados y botones en modo reposo)
        vista.getMiToolbar().estadoInicial(true);
        habilitarCampos(false);
        listarTodo();
    }

    // Abre una ventanita modal desplegable para elegir una Marca
    private void buscarMarca() {
        List<Marca> marcas = marcaDAO.recuperarTodo();
        if (marcas.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay marcas registradas.");
            return;
        }
        Marca seleccionada = (Marca) JOptionPane.showInputDialog(
                vista,
                "Seleccioná una marca:",
                "Buscar Marca",
                JOptionPane.QUESTION_MESSAGE,
                null,
                marcas.toArray(),
                vista.getMarcaSeleccionada()
        );
        if (seleccionada != null) {
            vista.setMarcaSeleccionada(seleccionada); // Asigna y refresca el texto en la vista
        }
    }

    // Lo mismo que arriba pero para Categoría
    private void buscarCategoria() {
        List<Categoria> categorias = categoriaDAO.recuperarTodo();
        if (categorias.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay categorías registradas.");
            return;
        }
        Categoria seleccionada = (Categoria) JOptionPane.showInputDialog(
                vista,
                "Seleccioná una categoría:",
                "Buscar Categoría",
                JOptionPane.QUESTION_MESSAGE,
                null,
                categorias.toArray(),
                vista.getCategoriaSeleccionada()
        );
        if (seleccionada != null) {
            vista.setCategoriaSeleccionada(seleccionada);
        }
    }

    // Trae todos los productos de la BD y refresca la tabla
    private void listarTodo() {
        modeloTabla.setLista(dao.recuperarTodo());
    }

    // Si hay texto en el buscador filtra por ese texto, si no traemos todo otra vez
    private void buscar() {
        String texto = vista.gettBuscador().getText().trim();
        if (texto.isEmpty()) {
            listarTodo();
        } else {
            modeloTabla.setLista(dao.recuperarPorFiltro(texto));
        }
    }

    // Pasa los datos del producto seleccionado en la tabla a las casillas del formulario
    private void seleccionarFila() {
        int fila = vista.getTable().getSelectedRow();
        if (fila == -1) return;

        // Recuperamos el objeto entero directamente desde nuestro modelo personalizado
        productoSeleccionado = modeloTabla.getProductoEn(fila);

        if (productoSeleccionado != null) {
            vista.gettId().setText(String.valueOf(productoSeleccionado.getId()));
            vista.gettCodigo().setText(productoSeleccionado.getCodigo());
            vista.gettDescripcion().setText(productoSeleccionado.getDescripcion());
            vista.gettPrecioVenta().setText(String.valueOf(productoSeleccionado.getPrecioVenta()));
            vista.getcbxTipoImpuesto().setSelectedItem(productoSeleccionado.getImpuesto());
            vista.gettFormaFarmaceutica().setText(productoSeleccionado.getFormaFarmaceutica());
            vista.gettPrincipioActivo().setText(productoSeleccionado.getPrincipioActivo());
            vista.gettConcentracion().setText(productoSeleccionado.getConcentracion());
            
            vista.getChkRequiereReceta().setSelected(productoSeleccionado.isRequiereReceta());
            vista.getChkTieneVencimiento().setSelected(
            	    productoSeleccionado.isTieneVencimiento()
            	

            );

            // Carga los objetos Marca y Categoría en la vista
            vista.setMarcaSeleccionada(productoSeleccionado.getMarca());
            vista.setCategoriaSeleccionada(productoSeleccionado.getCategoria());
        }
    }

    // Habilita o desabilita los inputs para que el usuario pueda escribir o no
    private void habilitarCampos(boolean habilitado) {
        vista.gettCodigo().setEnabled(habilitado);
        vista.gettDescripcion().setEnabled(habilitado);
        vista.gettPrecioVenta().setEnabled(habilitado);
        vista.getcbxTipoImpuesto().setEnabled(habilitado);
        vista.gettFormaFarmaceutica().setEnabled(habilitado);
        vista.gettPrincipioActivo().setEnabled(habilitado);
        vista.gettConcentracion().setEnabled(habilitado);
        vista.getChkRequiereReceta().setEnabled(habilitado);
        vista.getChkTieneVencimiento().setEnabled(habilitado);
        
        vista.getBtnBuscarMarca().setEnabled(habilitado);
        vista.getBtnBuscarCategoria().setEnabled(habilitado);
        
        // Estos dos no pertenecen al registro directo de productos (se manejan por stock/compras)
        vista.gettCantidad().setEnabled(false);
        vista.gettCostoCompra().setEnabled(false);
    }

    // Deja todo en blanco listo para cargar datos nuevos o limpiar la pantalla
    private void limpiarFormulario() {
        vista.gettId().setText("");
        vista.gettCodigo().setText("");
        vista.gettDescripcion().setText("");
        vista.gettPrecioVenta().setText("");
        vista.gettFormaFarmaceutica().setText("");
        vista.gettPrincipioActivo().setText("");
        vista.gettConcentracion().setText("");
        vista.gettCantidad().setText("");
        vista.gettCostoCompra().setText("");
        
        vista.getChkRequiereReceta().setSelected(false);
        vista.getChkTieneVencimiento().setSelected(false);
        
        if (vista.getcbxTipoImpuesto().getItemCount() > 0) {
            vista.getcbxTipoImpuesto().setSelectedIndex(0);
        }

        vista.setMarcaSeleccionada(null);
        vista.setCategoriaSeleccionada(null);
        
        productoSeleccionado = null; // Reseteamos la selección actual
    }

    // --- MÉTODOS DE LA INTERFAZ ACCIONESABM ---

    @Override
    public void nuevo() {
        limpiarFormulario();
        habilitarCampos(true);
        vista.getMiToolbar().estadoInicial(false); // Desbloquea los botones Guardar/Cancelar
        vista.gettCodigo().requestFocus(); // Pone el cursor directo en el primer campo
    }

    @Override
    public void modificar() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná un producto de la tabla primero.");
            return;
        }
        habilitarCampos(true);
        vista.getMiToolbar().estadoInicial(false);
    }

    @Override
    public void eliminar() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná un producto de la tabla primero.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar el producto \"" + productoSeleccionado.getDescripcion() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(productoSeleccionado);
                limpiarFormulario();
                listarTodo();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    @Override
    public void guardar() {
        String descripcion = vista.gettDescripcion().getText().trim();
        String precioTexto = vista.gettPrecioVenta().getText().trim();

        // Validamos lo mínimo  antes de intentar guardar
        if (descripcion.isEmpty() || precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "La descripción y el precio de venta son obligatorios.");
            return;
        }

        double precioVenta = 0;
        try {
            precioVenta = Double.parseDouble(precioTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El precio de venta debe ser un valor numérico válido.");
            return;
        }

        // Si estábamos modificando usamos el objeto cargado, si no creamos uno nuevo
        Producto p = (productoSeleccionado != null) ? productoSeleccionado : new Producto();
        p.setCodigo(vista.gettCodigo().getText().trim());
        p.setDescripcion(descripcion);
        p.setPrecioVenta(precioVenta);
        p.setImpuesto((String) vista.getcbxTipoImpuesto().getSelectedItem());
        p.setFormaFarmaceutica(vista.gettFormaFarmaceutica().getText().trim());
        p.setPrincipioActivo(vista.gettPrincipioActivo().getText().trim());
        p.setConcentracion(vista.gettConcentracion().getText().trim());
        p.setRequiereReceta(vista.getChkRequiereReceta().isSelected());
        p.setTieneVencimiento(vista.getChkTieneVencimiento().isSelected());

        // Enganchamos la Marca y Categoría seleccionadas
        p.setMarca(vista.getMarcaSeleccionada());
        p.setCategoria(vista.getCategoriaSeleccionada());

        try {
            dao.guardar(p);
            habilitarCampos(false);
            vista.getMiToolbar().estadoInicial(true);
            limpiarFormulario();
            listarTodo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al guardar el producto: " + e.getMessage());
        }
    }

    @Override
    public void cancelar() {
        habilitarCampos(false);
        vista.getMiToolbar().estadoInicial(true);
        limpiarFormulario();
        listarTodo();
    }

    @Override
    public void salir() {
        vista.dispose(); // Cierra la ventana
    }
}