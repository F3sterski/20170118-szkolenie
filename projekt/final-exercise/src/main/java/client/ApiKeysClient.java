package client;

import ws.soap.ApikeyService;
import ws.soap.ApikeyServiceImplService;

import javax.xml.ws.Service;

public class ApiKeysClient {

    private final ApikeyService port;

    public ApiKeysClient() {
        Service service = new ApikeyServiceImplService();
        this.port = service.getPort(ApikeyService.class);
    }

    public String getTestKey() {
        return port.testApiKey();
    }

}
