package com.example.security.jwt;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class Main {

    private final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

        JsonObject config = new JsonObject()
                .put("public-key", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjMtanOF+ekEyqjQcwSyaKQWjdLlsmV7zjrjM8mxXgsA5Ld/rJvF+pbXQMAhiqP8CwD+gLAt6VGZnpiPR9oJ0HSnzRN3X+Hnxh5ZnWMOWhqvC/kAiiah+iy50lZ4qtx7ORG1l3qimkokJrUaNQDL+YMVqO1pOBG/wNtysNHz1q5eP1DfLI7IqZE6zuaUfIznwctjXUrttP18QEAt2GcZnUrbKgGWdQ9RU1JHgwEuZa8gxg5reqrqD4p7mbr014DDdK341fJTUcJlMYOM4pfZEePFpiHsm+gLpoL0jFnHLCx0Qs9bJL8tgTxs1SIqdJfKb49VrwpOMt01N9Lhs3ZxLXQIDAQAB")
                .put("permissionsClaimKey", "realm_access/roles");

        JWTAuth authProvider = JWTAuth.create(vertx, config);
        router.route("/private/*").handler(JWTAuthHandler.create(authProvider));
        router.route("/private/*").handler(Main::logUser);

        router.route("/private/msg").handler(rc -> rc.response().end("This is private msg"));
        router.route("/public/*").handler(rc -> rc.response().end("This is public message"));

        HttpServer httpServer = vertx.createHttpServer().requestHandler(router::accept).listen(8000);
        log.info("Server started: " + httpServer.actualPort());
    }

    private static void logUser(RoutingContext rc) {
        User user = rc.user();
        if (user != null) {
            JsonObject principal = user.principal();
            log.info("Request by " + principal.getString("preferred_username"));
            log.info("Full details: " + principal.getString("given_name") + " " + principal.getString("family_name") + " [" + principal.getString("email") + "]");

            log.info("Moar data " + principal.getString("additional_text"));
        } else {
            log.error("No logged in user");
        }

        rc.next();
    }

}
