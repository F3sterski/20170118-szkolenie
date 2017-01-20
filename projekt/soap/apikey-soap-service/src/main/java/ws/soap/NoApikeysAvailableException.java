package ws.soap;

public class NoApikeysAvailableException extends Exception {

    public NoApikeysAvailableException() {
        super("No Apikeys available");
    }
}
