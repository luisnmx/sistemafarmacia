package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.util.ConnectionHelper;


public abstract class GenericDAO<T> {

    protected Class<T> clase;

    public GenericDAO(Class<T> clase) {
        this.clase = clase;
    }

protected Session getSession() {
		
		return ConnectionHelper.getSessionFactory().getCurrentSession();
	}

    protected void iniciarTransaccion() {
        if (!getSession().getTransaction().isActive()) {
            getSession().beginTransaction();
        }
    }

    public void guardar(T entity) throws Exception {
        iniciarTransaccion();
        try {
            getSession().merge(entity);
            getSession().getTransaction().commit();
        } catch (Exception e) {
            getSession().getTransaction().rollback();
            throw e;
        }
    }

    public void eliminar(T entity) throws Exception {
        iniciarTransaccion();
        try {
            getSession().remove(entity);
            getSession().getTransaction().commit();
        } catch (Exception e) {
            getSession().getTransaction().rollback();
            throw e;
        }
    }

    public T recuperarPorId(Serializable id) {
        iniciarTransaccion();
        T resultado = getSession().find(clase, id);
        getSession().getTransaction().commit();
        return resultado;
    }

    public List<T> recuperarTodo() {
        iniciarTransaccion();

        String hql = "from " + clase.getSimpleName() + " order by id desc";

        Query<T> query = getSession().createQuery(hql, clase);

        List<T> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

    public abstract List<T> recuperarPorFiltro(String filtro);

}
