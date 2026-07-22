package py.edu.facitec.sistema_farmacia.modelo.controladores;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionListener;

import py.edu.facitec.reutilizacion.interfaces.AccionesABM;
import py.edu.facitec.sistema_farmacia.modelo.dao.CategoriaDAO;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Categoria;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaCategoria;

public class VentanaCategoriaController implements AccionesABM {

    private VentanaCategoria vista;
    private CategoriaDAO dao;
    private Categoria categoriaSeleccionada;

    public VentanaCategoriaController(VentanaCategoria vista) {
        this.vista = vista;
        this.dao = new CategoriaDAO();

        // El toolbar ahora delega sus clics a este controlador
        vista.getMiToolbar().setAcciones(this);

        // Cuando el usuario hace clic en una fila, cargamos esa categoría al formulario
        vista.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarFila();
            }
        });

        // Búsqueda en vivo mientras el usuario escribe
        vista.gettBuscador().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { buscar(); }
            public void removeUpdate(DocumentEvent e) { buscar(); }
            public void changedUpdate(DocumentEvent e) { buscar(); }
        });

        // Estado inicial: navegando, formulario bloqueado
        vista.getMiToolbar().estadoInicial(true);
        habilitarCampos(false);
        listarTodo();
    }

    // Carga de datos en la tabla

    private void listarTodo() {
        cargarTabla(dao.recuperarTodo());
    }

    private void buscar() {
        String texto = vista.gettBuscador().getText().trim();
        if (texto.isEmpty()) {
            listarTodo();
        } else {
            cargarTabla(dao.recuperarPorFiltro(texto));
        }
    }

    private void cargarTabla(List<Categoria> lista) {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Id", "Descripción", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // que nadie edite directo en la tabla
            }
        };
        for (Categoria c : lista) {
            modelo.addRow(new Object[]{
                    c.getId(),
                    c.getDescripcion(),
                    c.isEstado() ? "Activo" : "Inactivo"
            });
        }
        vista.getTable().setModel(modelo);
    }

    // Cuando el usuario clickea una fila de la tabla
    private void seleccionarFila() {
        int fila = vista.getTable().getSelectedRow();
        if (fila == -1) return;

        int id = (int) vista.getTable().getValueAt(fila, 0);
        categoriaSeleccionada = dao.recuperarPorId(id);

        vista.gettDescripcion().setText(categoriaSeleccionada.getDescripcion());
        vista.getcbxEstado().setSelectedItem(
                categoriaSeleccionada.isEstado() ? "Activo" : "Inactivo");
    }

    //  Habilitar/deshabilitar formulario 

    private void habilitarCampos(boolean habilitado) {
        vista.gettDescripcion().setEnabled(habilitado);
        vista.getcbxEstado().setEnabled(habilitado);
    }

    private void limpiarFormulario() {
        vista.gettDescripcion().setText("");
        vista.getcbxEstado().setSelectedIndex(0);
        categoriaSeleccionada = null;
    }

    // Implementación de AccionesABM

    @Override
    public void nuevo() {
        limpiarFormulario();
        habilitarCampos(true);
        vista.getMiToolbar().estadoInicial(false);
        vista.gettDescripcion().requestFocus();
    }

    @Override
    public void modificar() {
        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná una categoría de la tabla primero.");
            return;
        }
        habilitarCampos(true);
        vista.getMiToolbar().estadoInicial(false);
    }

    @Override
    public void eliminar() {
        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná una categoría de la tabla primero.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar la categoría \"" + categoriaSeleccionada.getDescripcion() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(categoriaSeleccionada);
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

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "La descripción es obligatoria.");
            return;
        }

        Categoria categoria = (categoriaSeleccionada != null) ? categoriaSeleccionada : new Categoria();
        categoria.setDescripcion(descripcion);
        categoria.setEstado("Activo".equals(vista.getcbxEstado().getSelectedItem()));

        try {
            dao.guardar(categoria);
            habilitarCampos(false);
            vista.getMiToolbar().estadoInicial(true);
            limpiarFormulario();
            listarTodo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al guardar: " + e.getMessage());
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
        vista.dispose();
    }
}