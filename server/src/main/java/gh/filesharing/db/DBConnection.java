package gh.filesharing.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.GenericJDBCException;

public class DBConnection {
    private static final SessionFactory sessionFactory = JPAUtil.getSessionFactory();

    public static boolean isDBHealthy() {
        try (Session session = sessionFactory.openSession()) {
            session.createQuery("SELECT 1").getResultList();
            return true;
        } catch (GenericJDBCException e) {
            return false;
        }
    }
}
