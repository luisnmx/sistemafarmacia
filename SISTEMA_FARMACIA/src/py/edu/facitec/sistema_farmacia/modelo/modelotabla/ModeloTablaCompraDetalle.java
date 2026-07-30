
package py.edu.facitec.sistema_farmacia.modelo.modelotabla;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import py.edu.facitec.sistema_farmacia.modelo.entidades.CompraDetalle;

//Modelo de tabla para mostrar las líneas de detalle de una Compra
// mientras  el usuario las va cargando en pantalla 


public class ModeloTablaCompraDetalle extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    // Formdato de fechp (dd/MM/yyyy)
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    private String[] columnas = { "Producto", "N° Lote", "Vencimiento", "Cantidad", "Costo Unit.", "Subtotal" };

    // Lista los detalles que se van agregando o quitando
    private List<CompraDetalle> lista = new ArrayList<>();

    public void setLista(List<CompraDetalle> lista) {
        this.lista = (lista != null) ? lista : new ArrayList<>();
        fireTableDataChanged();
    }

    public List<CompraDetalle> getLista() {
        return lista;
    }

    // Agregamos  una línea nueva y refresca solo esa fila 
    public void agregar(CompraDetalle detalle) {
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

    // Reemplaza una línea existente usando el  boton actualizar 
    public void actualizar(int fila, CompraDetalle detalle) {
        if (fila >= 0 && fila < lista.size()) {
            lista.set(fila, detalle);
            fireTableRowsUpdated(fila, fila);
        }
    }

    public CompraDetalle getDetalleEn(int fila) {
        if (fila >= 0 && fila < lista.size()) {
            return lista.get(fila);
        }
        return null;
    }

    // Suma de todas las líneas: cantidad * costo. Esto alimenta el total de la Compra.
    public double calcularTotal() {
        double total = 0;
        for (CompraDetalle d : lista) {
            total += d.getCantidad() * d.getCosto();
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
        CompraDetalle d = lista.get(fila);
        switch (columna) {
            case 0: return (d.getProducto() != null) ? d.getProducto().getDescripcion() : "";
            case 1: return (d.getLote() != null) ? d.getLote().getNumeroLote() : "";
            case 2: return (d.getFechaVencimiento() != null) ? FORMATO_FECHA.format(d.getFechaVencimiento()) : "";
            case 3: return d.getCantidad();
            case 4: return d.getCosto();
            case 5: return d.getCantidad() * d.getCosto(); // subtotal calculado, no se guarda como columna aparte
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false; // La edición se hace por diálogo botón Actualizar... no directo en la celda
    }
}