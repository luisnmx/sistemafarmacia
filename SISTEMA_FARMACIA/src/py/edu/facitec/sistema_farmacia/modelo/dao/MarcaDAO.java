package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.Marca;

public class MarcaDAO extends GenericDAO<Marca> {

    public MarcaDAO() {
        super(Marca.class);
    }

    @Override
    public List<Marca> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from Marca where upper(descripcion) like :filtro order by descripcion";

        Query<Marca> query = getSession().createQuery(hql, Marca.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<Marca> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}
