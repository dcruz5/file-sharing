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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        InputStream keystoreInputStream = App.class.getClassLoader().getResourceAsStream("keystores/server-keystore.jks");
        String keystorePassword = "grupo8";

        SslPlugin sslPlugin = new SslPlugin(config ->{
            config.keystoreFromInputStream(keystoreInputStream, keystorePassword);
            config.http2 = true;
            config.securePort = 8443;
            config.insecurePort = 8080;
            // config.redirect = true;
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

        app.before(ctx -> {
            log.info("Incoming request: [{}] {} (Protocol: {})", ctx.method(), ctx.path(), ctx.protocol());

            if (!ctx.queryParamMap().isEmpty()) {
                log.info("Query Params: {}", ctx.queryParamMap());
            }
            if (!ctx.formParamMap().isEmpty()) {
                log.info("Form Params: {}", ctx.formParamMap());
            }
            for (Map.Entry<String, String> header : ctx.headerMap().entrySet()) {
                log.info("Header: {} = {}", header.getKey(), header.getValue());
            }
            if (!ctx.body().isEmpty()) {
                log.info("Request Body: {}", ctx.body());
            }
        });


        app.get("/health", ctx -> {
            if (DBConnection.isDBHealthy()) {
                ctx.status(200).result("OK!");
            } else {
                ctx.status(500).result("Database connection error");
            }
        });

        app.get("*", ctx -> ctx.result("Page not found").status(404));
    }

    private static void routes() {
        UserDAO userDAO = new UserDAO();
        UserController userController = new UserController(userDAO);
        FileDAO fileDAO = new FileDAO();
        FileController fileController = new FileController(fileDAO);
        AuthController authController = new AuthController(userDAO);

        ApiBuilder.before("files/upload", AuthController::authenticate);
        ApiBuilder.before("/download", AuthController::authenticate);
        ApiBuilder.before("/users", AuthController::authenticate);
        ApiBuilder.before("/users/{userId}", AuthController::authenticate);

        post("/login", authController::login);
        post("/register", authController::register);
        post("/share", FileController::share);
        post("files/upload", FileController::upload);
        get("download", FileController::download);

        crud("files/{fileId}", fileController);
        crud("users/{userId}", userController);
        get("/users/{userId}/files", UserController::getUserFiles);
    }
}
