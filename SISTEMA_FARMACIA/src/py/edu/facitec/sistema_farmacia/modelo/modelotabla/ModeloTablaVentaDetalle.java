
package py.edu.facitec.sistema_farmacia.modelo.modelotabla;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import py.edu.facitec.sistema_farmacia.modelo.entidades.VentaDetalle;

// Modelo de tabla para mostrar las líneas de detalle de una Venta
// mientras el usuario las va cargando en pantalla

public class ModeloTablaVentaDetalle extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private String[] columnas = { "Producto", "N° Lote", "Cantidad", "Precio Unit.", "Subtotal" };

    // Lista los detalles que se van agregando o quitando
    private List<VentaDetalle> lista = new ArrayList<>();

    public void setLista(List<VentaDetalle> lista) {
        this.lista = (lista != null) ? lista : new ArrayList<>();
        fireTableDataChanged();
    }

    public List<VentaDetalle> getLista() {
        return lista;
    }

    // Agregamos una línea nueva y refresca solo esa fila
    public void agregar(VentaDetalle detalle) {
        lista.add(detalle);
        fireTableRowsInserted(lista.size() - 1, lista.size() - 1);
    }

    // Quita una línea por índice de fila
    public void quitar(int fila) {
        if (fila >= 0 && fila < lista.size()) {
            lista.remove(fila);
            fireTableRowsDeleted(fila, fila);
        }
    }

    // Reemplaza una línea existente
    public void actualizar(int fila, VentaDetalle detalle) {
        if (fila >= 0 && fila < lista.size()) {
            lista.set(fila, detalle);
            fireTableRowsUpdated(fila, fila);
        }
    }

    public VentaDetalle getDetalleEn(int fila) {
        if (fila >= 0 && fila < lista.size()) {
            return lista.get(fila);
        }
        return null;
    }

    // Suma de todas las líneas: cantidad * precio. Esto alimenta el total de la Venta.
    public double calcularTotal() {
        double total = 0;
        for (VentaDetalle d : lista) {
            total += d.getCantidad() * d.getPrecio();
        }
        return total;
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
    public Object getValueAt(int fila, int columna) {
        VentaDetalle d = lista.get(fila);
        switch (columna) {
            case 0: return (d.getProducto() != null) ? d.getProducto().getDescripcion() : "";
            case 1: return (d.getLote() != null) ? d.getLote().getNumeroLote() : "";
            case 2: return d.getCantidad();
            case 3: return d.getPrecio();
            case 4: return d.getCantidad() * d.getPrecio(); // subtotal calculado, no se guarda como columna aparte
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false; // La edición se hace por diálogo, no directo en la celda
    }
}
