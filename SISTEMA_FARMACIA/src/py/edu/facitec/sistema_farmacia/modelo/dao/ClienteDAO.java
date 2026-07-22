package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.Cliente;

public class ClienteDAO extends GenericDAO<Cliente> {

    public ClienteDAO() {
        super(Cliente.class);
    }

    @Override
    public List<Cliente> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from Cliente where upper(nombre) like :filtro "
                + "or upper(apellido) like :filtro "
                + "or upper(documento) like :filtro "
                + "order by id desc";

        Query<Cliente> query = getSession().createQuery(hql, Cliente.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<Cliente> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}
