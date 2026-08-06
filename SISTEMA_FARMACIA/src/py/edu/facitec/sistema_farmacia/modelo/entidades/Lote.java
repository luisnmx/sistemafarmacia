package py.edu.facitec.sistema_farmacia.modelo.entidades;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lote")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "numero_lote", nullable = false, length = 100)
    private String numeroLote;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_vencimiento")
    private Date fechaVencimiento;

    @Column(name = "cantidad", nullable = false)
    private int stockActual;

    // Muchos lotes pertenecen a un producto
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public Lote() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    
}