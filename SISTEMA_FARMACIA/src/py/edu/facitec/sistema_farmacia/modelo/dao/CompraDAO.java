package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.Compra;

public class CompraDAO extends GenericDAO<Compra> {

    public CompraDAO() {
        super(Compra.class);
    }

    @Override
    public List<Compra> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from Compra where upper(funcionario.nombre) like :filtro "
                + "or upper(funcionario.apellido) like :filtro "
                + "order by id desc";

        Query<Compra> query = getSession().createQuery(hql, Compra.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<Compra> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}
