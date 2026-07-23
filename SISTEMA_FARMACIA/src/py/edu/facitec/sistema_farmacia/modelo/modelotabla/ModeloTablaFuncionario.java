package py.edu.facitec.sistema_farmacia.modelo.modelotabla;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import py.edu.facitec.sistema_farmacia.modelo.entidades.Funcionario;

public class ModeloTablaFuncionario extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnas = { "ID", "DOCUMENTO", "NOMBRE Y APELLIDO", "TELÉFONO", "CARGO", "ESTADO" };
	private List<Funcionario> lista = new ArrayList<>();

	public void setLista(List<Funcionario> lista) {
		this.lista = lista;
		fireTableDataChanged();
	}

	public Funcionario getFuncionarioEn(int fila) {
		if (fila >= 0 && fila < lista.size()) {
			return lista.get(fila);
		}
		return null;
	}

	@Override
	public int getRowCount() {
		return lista.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnas[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Funcionario f = lista.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return f.getId();
		case 1:
			return f.getDocumento();
		case 2:
			return f.getNombre() + " " + f.getApellido();
		case 3:
			return f.getTelefono();
		case 4:
			return f.getCargo();
		case 5:
			return (f.getEstado() != null && f.getEstado()) ? "ACTIVO" : "INACTIVO";
		default:
			return null;
		}
	}
}