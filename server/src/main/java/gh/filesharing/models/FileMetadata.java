package gh.filesharing.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String filename;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    private String encryptedPath;
    private LocalDateTime uploadedAt;

    public FileMetadata() { }

    public FileMetadata(int id, String filename, User owner, String encryptedPath, LocalDateTime uploadedAt) {
        this.id = id;
        this.filename = filename;
        this.owner = owner;
        this.encryptedPath = encryptedPath;
        this.uploadedAt = uploadedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public String getEncryptedPath() {
        return encryptedPath;
    }
    public void setEncryptedPath(String encryptedPath) {
        this.encryptedPath = encryptedPath;
    }
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
