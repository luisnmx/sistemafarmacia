package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.Producto;

public class ProductoDAO extends GenericDAO<Producto> {

    public ProductoDAO() {
        super(Producto.class);
    }

    @Override
    public List<Producto> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from Producto where upper(descripcion) like :filtro "
                + "or upper(codigo) like :filtro "
                + "or upper(principioActivo) like :filtro "
                + "order by id desc";

        Query<Producto> query = getSession().createQuery(hql, Producto.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<Producto> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}