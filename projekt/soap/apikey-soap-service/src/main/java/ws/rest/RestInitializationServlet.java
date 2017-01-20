package ws.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;

import javax.servlet.annotation.WebServlet;
import java.util.Arrays;
import java.util.HashSet;

@WebServlet(urlPatterns = "/api/*")
public class RestInitializationServlet extends CXFNonSpringJaxrsServlet {

        public RestInitializationServlet() {
                super(new HashSet<>(Arrays.asList(new JacksonJsonProvider(), new AdminResource())));
        }

}
