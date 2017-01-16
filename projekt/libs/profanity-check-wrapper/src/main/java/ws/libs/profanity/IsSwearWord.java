package ws.libs.profanity;

public class IsSwearWord {

    final public boolean containsProfanity;
    final public String input;
    final public String output;

    public IsSwearWord(boolean containsProfanity, String input, String output) {
        this.containsProfanity = containsProfanity;
        this.input = input;
        this.output = output;
    }

    public IsSwearWord(boolean containsProfanity, String input) {
        this.containsProfanity = containsProfanity;
        this.input = input;
        this.output = null;
    }
}
