package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.Lote;

public class LoteDAO extends GenericDAO<Lote> {

    public LoteDAO() {
        super(Lote.class);
    }

    @Override
    public List<Lote> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from Lote where upper(numeroLote) like :filtro "
                + "or upper(producto.descripcion) like :filtro "
                + "order by id desc";

        Query<Lote> query = getSession().createQuery(hql, Lote.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<Lote> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}
