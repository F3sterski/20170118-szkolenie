package ws.rest;

import ws.libs.profanity.IsSwearWord;
import ws.libs.profanity.ProfanityCheckClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@Path("/profanity/{input}")
public class ProfanityResource {

    @Context
    ProfanityCheckClient client;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response check(@PathParam("input") String input) {
        return Response.ok(client.profanityCheck(input)).build();
    }

//    @GET
//    @Produces(MediaType.APPLICATION_XML)
//    public JAXBElement<IsSwearWord> checkXml(@PathParam("input") String input) {
//        return new JAXBElement<>(new QName("profanity"), IsSwearWord.class, client.profanityCheck(input));
//    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public LocalIsSwearWord checkXml(@PathParam("input") String input) {
        return new LocalIsSwearWord(client.profanityCheck(input));
    }

    @XmlRootElement(name = "profanity")
    public static class LocalIsSwearWord {

        private boolean containsProfanity;
        private String input;
        private String output;

        public LocalIsSwearWord() {

        }

        public LocalIsSwearWord(IsSwearWord i) {
            this.containsProfanity = i.containsProfanity;
            this.input = i.input;
            this.output = i.output;
        }

        public boolean isContainsProfanity() {
            return containsProfanity;
        }

        public void setContainsProfanity(boolean containsProfanity) {
            this.containsProfanity = containsProfanity;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }
    }



}
