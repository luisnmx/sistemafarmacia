package py.edu.facitec.sistema_farmacia.modelo.controladores;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import py.edu.facitec.reutilizacion.interfaces.AccionesABM;
import py.edu.facitec.sistema_farmacia.modelo.dao.MarcaDAO;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Marca;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaMarca;

public class VentanaMarcaController implements AccionesABM {

    private VentanaMarca vista;
    private MarcaDAO dao;
    private Marca marcaSeleccionada;

    public VentanaMarcaController(VentanaMarca vista) {
        this.vista = vista;
        this.dao = new MarcaDAO();

        // Conectamos el toolbar con las acciones de este controlador
        vista.getMiToolbar().setAcciones(this);

        // Evento al seleccionar una fila de la tabla
        vista.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarFila();
            }
        });

        // Evento de búsqueda en tiempo real
        vista.gettBuscador().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { buscar(); }
            public void removeUpdate(DocumentEvent e) { buscar(); }
            public void changedUpdate(DocumentEvent e) { buscar(); }
        });

        // Estado inicial de la pantalla
        vista.getMiToolbar().estadoInicial(true);
        habilitarCampos(false);
        listarTodo();
    }

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

    private void cargarTabla(List<Marca> lista) {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Id", "Descripción", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        for (Marca m : lista) {
            modelo.addRow(new Object[]{
                    m.getId(),
                    m.getDescripcion(),
                    m.isEstado() ? "Activo" : "Inactivo"
            });
        }
        vista.getTable().setModel(modelo);
    }

    private void seleccionarFila() {
        int fila = vista.getTable().getSelectedRow();
        if (fila == -1) return;

        int id = (int) vista.getTable().getValueAt(fila, 0);
        marcaSeleccionada = dao.recuperarPorId(id);

        vista.gettDescripcion().setText(marcaSeleccionada.getDescripcion());
        vista.getcbxEstado().setSelectedItem(
                marcaSeleccionada.isEstado() ? "Activo" : "Inactivo");
    }

    private void habilitarCampos(boolean habilitado) {
        vista.gettDescripcion().setEnabled(habilitado);
        vista.getcbxEstado().setEnabled(habilitado);
    }

    private void limpiarFormulario() {
        vista.gettDescripcion().setText("");
        if (vista.getcbxEstado().getItemCount() > 0) {
            vista.getcbxEstado().setSelectedIndex(0);
        }
        marcaSeleccionada = null;
    }

    @Override
    public void nuevo() {
        limpiarFormulario();
        habilitarCampos(true);
        vista.getMiToolbar().estadoInicial(false);
        vista.gettDescripcion().requestFocus();
    }

    @Override
    public void modificar() {
        if (marcaSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná una marca de la tabla primero.");
            return;
        }
        habilitarCampos(true);
        vista.getMiToolbar().estadoInicial(false);
    }

    @Override
    public void eliminar() {
        if (marcaSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná una marca de la tabla primero.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar la marca \"" + marcaSeleccionada.getDescripcion() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(marcaSeleccionada);
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

        Marca marca = (marcaSeleccionada != null) ? marcaSeleccionada : new Marca();
        marca.setDescripcion(descripcion);
        marca.setEstado("Activo".equals(vista.getcbxEstado().getSelectedItem()));

        try {
            dao.guardar(marca);
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