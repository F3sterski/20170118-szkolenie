package com.example.security.oauth;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

import static spark.Spark.*;

public class Application {

    private static final String CLIENT_ID = "502207594632-9vfi2sfnh07j9ft682id5d53njsdkado.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "Nk4gKXQBexMIwPZYnfNZmdt5";
    private static final String SESSION_OAUTH = "session_oauth_key";
    private static final String SESSION_NAME_ATTR = "session_name_key";
    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("sun.net.www.protocol.http.HttpURLConnection").setLevel(Level.ALL);

        port(8080); // Bo już mam tak Google+ skonfigurowany
        staticFiles.location("/webapp"); // Static files

        before("/private/*", (request, response) -> {
            boolean authenticated = false;

            String attribute = request.session().attribute(SESSION_NAME_ATTR);
            if (attribute != null && !attribute.isEmpty()) {
                log.info("Authenticated: {}", attribute);
                authenticated = true;
            }

            if (!authenticated) {
                halt(401, "Not allowed. Login <a href=\"/index.html\">here</a>");
            }
        });

        get("/google", (request, response) -> {
            OAuth20Service service = new ServiceBuilder()
                    .apiKey(CLIENT_ID)
                    .apiSecret(CLIENT_SECRET)
                    .callback("http://localhost:8080/oauthcallback")
                    .scope("openid profile email " +
                            "https://www.googleapis.com/auth/plus.login " +
                            "https://www.googleapis.com/auth/plus.me")
                    .build(GoogleApi20.instance());

            request.session(true).attribute(SESSION_OAUTH, service);

            response.redirect(service.getAuthorizationUrl());
            return "";
        });

        get("/oauthcallback", (request, response) -> {
            String error = request.queryParams("error");
            if (error != null && "access_denied".equals(error.trim())) {
                log.info("Access denied: {}", error);
                request.session().invalidate();
//            resp.sendRedirect("/login.jsp");
                return "";
            }

            final OAuth20Service oAuthService = request.session(true).attribute(SESSION_OAUTH);
            final String code = request.queryParams("code");// Access code with token

            OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v2/userinfo",
                    oAuthService);

            oAuthService.signRequest(oAuthService.getAccessToken(code), oAuthRequest);
            Response oAuthResp = oAuthRequest.send();

            JSONObject profile = new JSONObject(oAuthResp.getBody());
            String name = profile.getString("name");
            String email = profile.getString("email");

            System.out.println(String.format("name = %s, email = %s", name, email));

            request.session().attribute("name", name);

//            response.redirect("/");
            return "";
        });

        get("/private/msg", (request, response) -> "This is private message");

        exception(Exception.class, (e, request, response) -> e.printStackTrace());
    }

}
