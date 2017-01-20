package soap.client;

import javax.xml.soap.*;

public class Main {

    private static String serviceLocation = "https://arcane-spire-76209.herokuapp.com/services/ApiKeys";

    public static void main(String args[]) throws Exception {
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        {
            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createProductionApikeyMessage("Kuba"), serviceLocation);

            // print SOAP Response
            System.out.print("Response SOAP Message:");
            soapResponse.writeTo(System.out);
        }

        System.out.println();

        {
            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createTestApikeyMessage(), serviceLocation);

            // print SOAP Response
            System.out.print("Response SOAP Message:");
            soapResponse.writeTo(System.out);
        }

        soapConnection.close();
    }

    private static SOAPMessage createTestApikeyMessage() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://soap.ws/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ns", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement method = soapBody.addChildElement("testApiKey", "ns");
        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }

    private static SOAPMessage createProductionApikeyMessage(String name) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://soap.ws/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ns", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement method = soapBody.addChildElement("productionApiKey", "ns");
        SOAPElement argument = method.addChildElement("username");
        argument.addTextNode(name);
        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }
}
