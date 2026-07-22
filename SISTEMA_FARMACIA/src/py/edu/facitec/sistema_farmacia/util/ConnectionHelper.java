package py.edu.facitec.sistema_farmacia.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class ConnectionHelper {
	private static SessionFactory sessionFactory;
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	static {
		final StandardServiceRegistry registry =
				new StandardServiceRegistryBuilder().configure().build();
		
		try {
			sessionFactory = new MetadataSources(registry)
				.buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(registry);
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		
		Session session = ConnectionHelper.getSessionFactory().getCurrentSession();
		
		
	}
}
