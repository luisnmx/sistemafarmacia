package py.edu.facitec.sistema_farmacia.modelo.dao;

import java.util.List;

import org.hibernate.query.Query;

import py.edu.facitec.sistema_farmacia.modelo.entidades.MovimientoStock;

public class MovimientoStockDAO
		extends GenericDAO<MovimientoStock> {

	public MovimientoStockDAO() {

		super(MovimientoStock.class);
	}

	@Override
	public List<MovimientoStock> recuperarPorFiltro(
			String filtro) {

		iniciarTransaccion();

		String hql =
				"select distinct m "
				+ "from MovimientoStock m "
				+ "left join fetch m.lote l "
				+ "left join fetch l.producto p "
				+ "left join fetch m.funcionario f "
				+ "where upper(m.tipoMovimiento) like :filtro "
				+ "or upper(p.descripcion) like :filtro "
				+ "or upper(l.numeroLote) like :filtro "
				+ "or upper(f.nombre) like :filtro "
				+ "or upper(f.apellido) like :filtro "
				+ "order by m.id desc";

		Query<MovimientoStock> query =
				getSession().createQuery(
						hql,
						MovimientoStock.class);

		query.setParameter(
				"filtro",
				"%" + filtro.toUpperCase() + "%");

		List<MovimientoStock> lista =
				query.getResultList();

		getSession()
				.getTransaction()
				.commit();

		return lista;
	}

	/**
	 * Recupera todos los movimientos
	 * junto con lote, producto y funcionario.
	 */
	public List<MovimientoStock>
			recuperarTodosConDetalles() {

		iniciarTransaccion();

		String hql =
				"select distinct m "
				+ "from MovimientoStock m "
				+ "left join fetch m.lote l "
				+ "left join fetch l.producto p "
				+ "left join fetch m.funcionario f "
				+ "order by m.id desc";

		Query<MovimientoStock> query =
				getSession().createQuery(
						hql,
						MovimientoStock.class);

		List<MovimientoStock> lista =
				query.getResultList();

		getSession()
				.getTransaction()
				.commit();

		return lista;
	}
}