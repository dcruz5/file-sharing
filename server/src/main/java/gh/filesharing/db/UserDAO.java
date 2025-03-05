package gh.filesharing.db;

import com.mysql.cj.log.Log;
import gh.filesharing.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger log = Logger.getLogger(UserDAO.class.getName());
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = JPAUtil.getEntityManagerFactory();

    public User findByUsername(String username) {
        try (EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.severe("Error finding user by username ("+ username +"): " + e.getMessage());
            return null;
        }
    }

    public void createUser(User user) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    public User getUserById(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        User user = em.find(User.class, id);
        em.close();
        return user;
    }

    public List<User> getAllUsers() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        em.close();
        return users;
    }

    public void updateUser(User user) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteUser(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
        em.getTransaction().commit();
        em.close();
    }
}
