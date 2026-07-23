package py.edu.facitec.sistema_farmacia.modelo.modelotabla;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import py.edu.facitec.sistema_farmacia.modelo.entidades.Producto;

public class ModeloTablaProductos extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private String[] columnas = { "ID", "CÓDIGO", "DESCRIPCIÓN", "P. VENTA", "CATEGORÍA", "MARCA" };
    private List<Producto> lista = new ArrayList<>();

    public void setLista(List<Producto> lista) {
        this.lista = (lista != null) ? lista : new ArrayList<>();
        fireTableDataChanged();
    }

    public Producto getProductoEn(int fila) {
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
    public String getColumnName(int c) {
        return columnas[c];
    }

    @Override
    public Object getValueAt(int r, int c) {
        Producto p = lista.get(r);
        return switch (c) {
            case 0 -> p.getId();
            case 1 -> p.getCodigo();
            case 2 -> p.getDescripcion();
            case 3 -> p.getPrecioVenta();
            case 4 -> (p.getCategoria() != null) ? p.getCategoria().getDescripcion() : "";
            case 5 -> (p.getMarca() != null) ? p.getMarca().getDescripcion() : "";
            default -> null;
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) return Integer.class;
        if (columnIndex == 3) return Double.class;
        return String.class;
    }
}
