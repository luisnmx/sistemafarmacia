package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.MovimientoStock;

public class MovimientoStockDAO extends GenericDAO<MovimientoStock> {

    public MovimientoStockDAO() {
        super(MovimientoStock.class);
    }

    @Override
    public List<MovimientoStock> recuperarPorFiltro(String filtro) {

        iniciarTransaccion();

        String hql = "from MovimientoStock where upper(tipoMovimiento) like :filtro "
                + "or upper(funcionario.nombre) like :filtro "
                + "or upper(funcionario.apellido) like :filtro "
                + "order by id desc";

        Query<MovimientoStock> query = getSession().createQuery(hql, MovimientoStock.class);

        query.setParameter("filtro", "%" + filtro.toUpperCase() + "%");

        List<MovimientoStock> lista = query.getResultList();

        getSession().getTransaction().commit();

        return lista;
    }

}
