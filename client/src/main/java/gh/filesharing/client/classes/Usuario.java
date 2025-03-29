package gh.filesharing.client.classes;

public class Usuario {
     //<editor-fold desc="Propietats">

    private String usuario;
    private String email;
    private String contrasena;


    //</editor-fold>


    //<editor-fold desc="Constructors">

    public Usuario(String usuario, String email, String contrasena) {
        this.usuario = usuario;
        this.email = email;
        this.contrasena = contrasena;
    }
    public Usuario() {

    }

    public Usuario(String username, String s) {
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }


    //</editor-fold>


    //<editor-fold desc="Mètodes">
@Override
    public String toString() {
        return "Usuario{" +
                "usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }

    //</editor-fold>
}
