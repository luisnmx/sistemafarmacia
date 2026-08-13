package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.CompraDetalle;

public class CompraDetalleDAO extends GenericDAO<CompraDetalle> {

    public CompraDetalleDAO() {
        super(CompraDetalle.class);
    }

    @Override
    public List<CompraDetalle> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from CompraDetalle where upper(producto.descripcion) like :filtro "
                + "or upper(lote.numeroLote) like :filtro "
                + "order by id desc";

        Query<CompraDetalle> query = getSession().createQuery(hql, CompraDetalle.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<CompraDetalle> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}