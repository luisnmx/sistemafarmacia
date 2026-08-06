package py.edu.facitec.sistema_farmacia.modelo.dao;

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
                + "or upper(funcionario.nombre) like :filtro "
                + "or upper(funcionario.apellido) like :filtro "
                + "order by id desc";

        Query<Venta> query = getSession().createQuery(hql, Venta.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<Venta> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}
