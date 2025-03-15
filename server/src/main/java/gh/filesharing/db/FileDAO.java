package gh.filesharing.db;

import gh.filesharing.models.FileMetadata;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class FileDAO {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = JPAUtil.getEntityManagerFactory();

    public void createFile(FileMetadata file) {
        try (EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(file);
            em.getTransaction().commit();
        }
    }

    public FileMetadata getFileById(int id) {
        try (EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            return em.find(FileMetadata.class, id);
        }
    }

    public List<FileMetadata> getAllFiles() {
        try (EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            return em.createQuery("SELECT f FROM FileMetadata f", FileMetadata.class)
                    .getResultList();
        }
    }

    public void updateFile(FileMetadata file) {
        try (EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(file);
            em.getTransaction().commit();
        }
    }

    public void deleteFile(int id) {
        try (EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            em.getTransaction().begin();
            FileMetadata file = em.find(FileMetadata.class, id);
            if (file != null) {
                em.remove(file);
            }
            em.getTransaction().commit();
        }
    }
}
