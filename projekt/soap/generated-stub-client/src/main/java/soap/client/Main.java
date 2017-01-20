package soap.client;

import ws.soap.ApikeyService;
import ws.soap.NoApikeysAvailableException_Exception;

import javax.print.DocFlavor;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws MalformedURLException, NoApikeysAvailableException_Exception {
        Service apiKeyService = Service.create(new URL("https://arcane-spire-76209.herokuapp.com/services/ApiKeys?wsdl"),
                new QName("http://soap.ws/", "ApikeyServiceImplService"));

        ApikeyService service = apiKeyService.getPort(ApikeyService.class);
        String testKey = service.testApiKey();
        System.out.println("testKey = " + testKey);

        String prdKey = service.productionApiKey("Kuba");
        System.out.println("prdKey = " + prdKey);
    }

}
