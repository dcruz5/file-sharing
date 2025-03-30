package gh.filesharing.client.classes;

public class Usuario {
     //<editor-fold desc="Propietats">

    private String usuario;
    private String email;

    //</editor-fold>


    //<editor-fold desc="Constructors">

    public Usuario(String usuario, String email) {
        this.usuario = usuario;
        this.email = email;
    }
    public Usuario() {

    }

    //</editor-fold>

    //<editor-fold desc="Getters & Setters">

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //</editor-fold>


    //<editor-fold desc="Mètodes">
@Override
    public String toString() {
        return "Usuario{" +
                "usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    //</editor-fold>
}
