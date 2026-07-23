package py.edu.facitec.sistema_farmacia.modelo.controladores;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

import py.edu.facitec.reutilizacion.interfaces.AccionesABM;
import py.edu.facitec.sistema_farmacia.modelo.dao.FuncionarioDAO;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Funcionario;
import py.edu.facitec.sistema_farmacia.modelo.modelotabla.ModeloTablaFuncionario;
import py.edu.facitec.sistema_farmacia.modelo.vistas.VentanaFuncionario;

public class VentanaFuncionarioController implements AccionesABM {

	private VentanaFuncionario vista;
	private FuncionarioDAO dao;
	private ModeloTablaFuncionario modeloTabla;
	private Funcionario funcionarioSeleccionado;

	public VentanaFuncionarioController(VentanaFuncionario vista) {
		this.vista = vista;
		this.dao = new FuncionarioDAO();
		this.modeloTabla = new ModeloTablaFuncionario();

		// Asignamos el controlador al toolbar para que ejecute la interfaz AccionesABM
		this.vista.getMiToolbar().setAcciones(this);
		
		// Asignamos el modelo a la tabla
		this.vista.getTable().setModel(modeloTabla);

		initEvents();
		limpiarFormulario();
		listarTodo();
		habilitarCampos(false);
	}

	private void initEvents() {
		// Evento de búsqueda en tiempo real
		vista.gettBuscador().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtrar();
			}
		});

		// Evento para seleccionar fila en la tabla
		vista.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				seleccionarFila();
			}
		});
	}

	private void listarTodo() {
		modeloTabla.setLista(dao.recuperarTodo());
	}

	private void filtrar() {
		String texto = vista.gettBuscador().getText().trim();
		if (texto.isEmpty()) {
			listarTodo();
		} else {
			modeloTabla.setLista(dao.recuperarPorFiltro(texto));
		}
	}

	private void seleccionarFila() {
		int fila = vista.getTable().getSelectedRow();
		if (fila == -1) return;

		funcionarioSeleccionado = modeloTabla.getFuncionarioEn(fila);
		if (funcionarioSeleccionado != null) {
			vista.gettNombre().setText(funcionarioSeleccionado.getNombre());
			vista.gettApellido().setText(funcionarioSeleccionado.getApellido());
			vista.gettDocumento().setText(funcionarioSeleccionado.getDocumento());
			vista.gettEmail().setText(funcionarioSeleccionado.getEmail());
			vista.gettTelefono().setText(funcionarioSeleccionado.getTelefono());
			vista.gettCargo().setText(funcionarioSeleccionado.getCargo());

			if (funcionarioSeleccionado.getEstado() != null && funcionarioSeleccionado.getEstado()) {
				vista.getcbxEstado().setSelectedItem("Activo");
			} else {
				vista.getcbxEstado().setSelectedItem("Inactivo");
			}
		}
	}

	// --- Métodos de la Interfaz AccionesABM ---

	@Override
	public void nuevo() {
		funcionarioSeleccionado = null;
		limpiarFormulario();
		habilitarCampos(true);
		vista.getMiToolbar().estadoInicial(false);
		vista.gettNombre().requestFocus();
	}

	@Override
	public void modificar() {
		if (funcionarioSeleccionado == null) {
			JOptionPane.showMessageDialog(vista, "Seleccione un funcionario de la tabla.");
			return;
		}
		habilitarCampos(true);
		vista.getMiToolbar().estadoInicial(false);
	}

	@Override
	public void eliminar() {
		if (funcionarioSeleccionado == null) {
			JOptionPane.showMessageDialog(vista, "Seleccione un funcionario para eliminar.");
			return;
		}

		int opc = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar a este funcionario?", "Confirmación", JOptionPane.YES_NO_OPTION);
		if (opc == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(funcionarioSeleccionado);
				JOptionPane.showMessageDialog(vista, "Funcionario eliminado correctamente.");
				cancelar();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vista, "No se puede eliminar porque tiene operaciones asociadas.");
			}
		}
	}

	@Override
	public void guardar() {
		String nombre = vista.gettNombre().getText().trim();
		String apellido = vista.gettApellido().getText().trim();
		String documento = vista.gettDocumento().getText().trim();

		if (nombre.isEmpty() || apellido.isEmpty() || documento.isEmpty()) {
			JOptionPane.showMessageDialog(vista, "Nombre, Apellido y Documento son obligatorios.");
			return;
		}

		Funcionario f = (funcionarioSeleccionado != null) ? funcionarioSeleccionado : new Funcionario();
		f.setNombre(nombre);
		f.setApellido(apellido);
		f.setDocumento(documento);
		f.setEmail(vista.gettEmail().getText().trim());
		f.setTelefono(vista.gettTelefono().getText().trim());
		f.setCargo(vista.gettCargo().getText().trim());
		f.setEstado(vista.getcbxEstado().getSelectedItem().toString().equalsIgnoreCase("Activo"));

		try {
			dao.guardar(f);
			JOptionPane.showMessageDialog(vista, "Funcionario guardado con éxito.");
			cancelar();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(vista, "Error al guardar: " + ex.getMessage());
		}
	}

	@Override
	public void cancelar() {
		funcionarioSeleccionado = null;
		limpiarFormulario();
		habilitarCampos(false);
		vista.getMiToolbar().estadoInicial(true);
		listarTodo();
	}

	@Override
	public void salir() {
		vista.dispose();
	}

	//  Métodos Auxiliares

	private void limpiarFormulario() {
		vista.gettNombre().setText("");
		vista.gettApellido().setText("");
		vista.gettDocumento().setText("");
		vista.gettEmail().setText("");
		vista.gettTelefono().setText("");
		vista.gettCargo().setText("");
		vista.getcbxEstado().setSelectedIndex(0); // Activo
	}

	private void habilitarCampos(boolean habilitado) {
		vista.gettNombre().setEnabled(habilitado);
		vista.gettApellido().setEnabled(habilitado);
		vista.gettDocumento().setEnabled(habilitado);
		vista.gettEmail().setEnabled(habilitado);
		vista.gettTelefono().setEnabled(habilitado);
		vista.gettCargo().setEnabled(habilitado);
		vista.getcbxEstado().setEnabled(habilitado);
	}
}