package ws.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://soap.ws/")
public interface ApikeyService {

    @WebMethod
    String testApiKey();

    @WebMethod
    String productionApiKey(@WebParam(name = "username") String username) throws NoApikeysAvailableException;

}
