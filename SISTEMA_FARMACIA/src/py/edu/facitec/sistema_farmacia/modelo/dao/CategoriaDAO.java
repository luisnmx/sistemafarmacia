package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.Categoria;

public class CategoriaDAO extends GenericDAO<Categoria> {

    public CategoriaDAO() {
        super(Categoria.class);
    }

    @Override
    public List<Categoria> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from Categoria where upper(descripcion) like :filtro order by descripcion";

        Query<Categoria> query = getSession().createQuery(hql, Categoria.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<Categoria> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}