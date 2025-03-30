package gh.filesharing.client.classes;

public class CurrentSession {
    private static CurrentSession instance;
    private String username;
    private Long userId;

    private CurrentSession() {}

    public static CurrentSession getInstance() {
        if (instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void clearSession() {
        this.username = null;
        this.userId = null;
    }

    @Override
    public String toString() {
        return "CurrentSession{" +
                "username='" + username + '\'' +
                ", userId=" + userId +
                '}';
    }
}