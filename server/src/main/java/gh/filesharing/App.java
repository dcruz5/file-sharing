package gh.filesharing;

import gh.filesharing.controllers.AuthController;
import gh.filesharing.controllers.FileController;
import gh.filesharing.controllers.UserController;
import gh.filesharing.db.DBConnection;
import gh.filesharing.db.FileDAO;
import gh.filesharing.db.UserDAO;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.community.ssl.TlsConfig;
import io.javalin.plugin.bundled.CorsPluginConfig;

import static io.javalin.apibuilder.ApiBuilder.*;

public class App {
    public static void main(String[] args) {
        String keystorePath = App.class.getClassLoader().getResource("server-keystore.jks").getPath();
        String keystorePassword = "grupo8";

        SslPlugin sslPlugin = new SslPlugin(config ->{
            config.keystoreFromPath(keystorePath, keystorePassword);
            config.http2 = true;
            config.securePort = 8443;
            config.insecurePort = 8080;
            config.redirect = true;
            config.tlsConfig = TlsConfig.MODERN; // TLS configuration
            config.sniHostCheck = false;    // SNI hostname verification.
        });

        // Start Javalin with the Jetty server
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(sslPlugin);
            config.router.apiBuilder(App::routes);
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });

        }).start(8443);

        app.get("/health", ctx -> {
            if (DBConnection.isDBHealthy()) {
                ctx.result("OK!");
            } else {
                ctx.status(500).result("Database connection error");
            }
        });

        app.get("*", ctx -> ctx.result("Page not found").status(404));
    }

    private static void routes() {
        UserDAO userDAO = new UserDAO(); // Create the UserDAO instance
        UserController userController = new UserController(userDAO); // Inject into UserController
        FileDAO fileDAO = new FileDAO();
        FileController fileController = new FileController(fileDAO);

        ApiBuilder.before("files/upload", AuthController::authenticate);
        ApiBuilder.before("/download", AuthController::authenticate);
        ApiBuilder.before("/users", AuthController::authenticate);
        ApiBuilder.before("/users/{userId}", AuthController::authenticate);

        post("login", AuthController::login); // lookup user by username and check if the hashed password is correct
        post("/share", FileController::share);
        post("files/upload", FileController::upload);
        get("download", FileController::download);

//        ApiBuilder.path("files", () -> {
//            post("/upload", FileController::upload);
//            get("{fileId}/download", FileController::download);
//        });

        crud("files/{fileId}", fileController);
        crud("users/{userId}", userController);
        get("/users/{userId}/files", UserController::getUserFiles);
    }
}
