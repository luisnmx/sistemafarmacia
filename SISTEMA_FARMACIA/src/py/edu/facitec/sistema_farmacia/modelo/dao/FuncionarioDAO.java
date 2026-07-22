package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.Funcionario;

public class FuncionarioDAO extends GenericDAO<Funcionario> {

    public FuncionarioDAO() {
        super(Funcionario.class);
    }

    @Override
    public List<Funcionario> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from Funcionario where upper(nombre) like :filtro "
                + "or upper(apellido) like :filtro "
                + "or upper(documento) like :filtro "
                + "or upper(cargo) like :filtro "
                + "order by id desc";

        Query<Funcionario> query = getSession().createQuery(hql, Funcionario.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<Funcionario> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}
