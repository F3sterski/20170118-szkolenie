//package ws;
//
//import org.apache.cxf.transport.servlet.CXFServlet;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
//import ws.rest.RestInitializationServlet;
//import ws.soap.SoapInitializationServlet;
//
//public class Starter {
//
//    public static void main(final String[] args) throws Exception {
//        Server server = new Server(9999);
//
//        // Register and map the dispatcher servlet
//        final ServletContextHandler context = new ServletContextHandler();
//        context.addServlet(new ServletHolder(new RestInitializationServlet()), "/api/*");
//        context.addServlet(new ServletHolder(new SoapInitializationServlet()), "/services/*");
//
//        server.setHandler(context);
//        server.start();
//        server.join();
//    }
//}
