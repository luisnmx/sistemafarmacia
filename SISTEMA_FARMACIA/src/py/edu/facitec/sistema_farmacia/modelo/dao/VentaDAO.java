package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.Venta;

public class VentaDAO extends GenericDAO<Venta> {

    public VentaDAO() {
        super(Venta.class);
    }

    @Override
    public List<Venta> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from Venta where upper(cliente.nombre) like :filtro "
                + "or upper(cliente.apellido) like :filtro "
                + "order by id desc";

        Query<Venta> query = getSession().createQuery(hql, Venta.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<Venta> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

    // Suma el total de todas las ventas de una fecha dada
    public double sumarTotalPorFecha(Date fecha) {

        iniciarTransaccion();

        String hql = "select coalesce(sum(v.total), 0) from Venta v where v.fecha = :fecha";

        Query<Double> query = getSession().createQuery(hql, Double.class);
        query.setParameter("fecha", fecha);

        Double resultado = query.getSingleResult();

        getSession().getTransaction().commit();

        return resultado;
    }

    // Cuenta cuántas ventas se hicieron en una fecha dada
    public long contarPorFecha(Date fecha) {

        iniciarTransaccion();

        String hql = "select count(v) from Venta v where v.fecha = :fecha";

        Query<Long> query = getSession().createQuery(hql, Long.class);
        query.setParameter("fecha", fecha);

        Long resultado = query.getSingleResult();

        getSession().getTransaction().commit();

        return resultado;
    }
}