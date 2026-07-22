package py.edu.facitec.sistema_farmacia.modelo.controladores;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import py.edu.facitec.reutilizacion.interfaces.AccionesABM;
import py.edu.facitec.sistema_farmacia.modelo.dao.ClienteDAO;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Cliente;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaCliente;

public class VentanaClienteController implements AccionesABM {

    private VentanaCliente vista;
    private ClienteDAO dao;
    private Cliente clienteSeleccionado;

    public VentanaClienteController(VentanaCliente vista) {
        this.vista = vista;
        this.dao = new ClienteDAO();

        vista.getMiToolbar().setAcciones(this);

        vista.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarFila();
            }
        });

        vista.gettBuscador().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { buscar(); }
            public void removeUpdate(DocumentEvent e) { buscar(); }
            public void changedUpdate(DocumentEvent e) { buscar(); }
        });

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

    private void cargarTabla(List<Cliente> lista) {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Id", "Documento", "Nombre", "Apellido", "Teléfono", "Dirección", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        for (Cliente c : lista) {
            modelo.addRow(new Object[]{
                    c.getId(),
                    c.getDocumento(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getTelefono(),
                    c.getDireccion(),
                    c.getEmail()
            });
        }
        vista.getTable().setModel(modelo);
    }

    private void seleccionarFila() {
        int fila = vista.getTable().getSelectedRow();
        if (fila == -1) return;

        int id = (int) vista.getTable().getValueAt(fila, 0);
        clienteSeleccionado = dao.recuperarPorId(id);

        if (clienteSeleccionado != null) {
            vista.gettRuc().setText(clienteSeleccionado.getDocumento());
            vista.gettNombre().setText(clienteSeleccionado.getNombre());
            vista.gettApellido().setText(clienteSeleccionado.getApellido());
            vista.gettTelefono().setText(clienteSeleccionado.getTelefono());
            vista.gettDireccion().setText(clienteSeleccionado.getDireccion());
            vista.gettEmail().setText(clienteSeleccionado.getEmail());
        }
    }

    private void habilitarCampos(boolean habilitado) {
        vista.gettRuc().setEnabled(habilitado);
        vista.gettNombre().setEnabled(habilitado);
        vista.gettApellido().setEnabled(habilitado);
        vista.gettTelefono().setEnabled(habilitado);
        vista.gettDireccion().setEnabled(habilitado);
        vista.gettEmail().setEnabled(habilitado);
    }

    private void limpiarFormulario() {
        vista.gettRuc().setText("");
        vista.gettNombre().setText("");
        vista.gettApellido().setText("");
        vista.gettTelefono().setText("");
        vista.gettDireccion().setText("");
        vista.gettEmail().setText("");
        clienteSeleccionado = null;
    }

    @Override
    public void nuevo() {
        limpiarFormulario();
        habilitarCampos(true);
        vista.getMiToolbar().estadoInicial(false);
        vista.gettRuc().requestFocus();
    }

    @Override
    public void modificar() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná un cliente de la tabla primero.");
            return;
        }
        habilitarCampos(true);
        vista.getMiToolbar().estadoInicial(false);
    }

    @Override
    public void eliminar() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Seleccioná un cliente de la tabla primero.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar al cliente \"" + clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellido() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(clienteSeleccionado);
                limpiarFormulario();
                listarTodo();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    @Override
    public void guardar() {
        String documento = vista.gettRuc().getText().trim();
        String nombre = vista.gettNombre().getText().trim();
        String apellido = vista.gettApellido().getText().trim();

        if (documento.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El documento (RUC/CI), nombre y apellido son obligatorios.");
            return;
        }

        Cliente cliente = (clienteSeleccionado != null) ? clienteSeleccionado : new Cliente();
        cliente.setDocumento(documento);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setTelefono(vista.gettTelefono().getText().trim());
        cliente.setDireccion(vista.gettDireccion().getText().trim());
        cliente.setEmail(vista.gettEmail().getText().trim());

        try {
            dao.guardar(cliente);
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