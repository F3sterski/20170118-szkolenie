package ws.libs.profanity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ProfanityCheckClient {

    private static Logger log = Logger.getLogger(ProfanityCheckClient.class.getName());
    final ExecutorService exec = Executors.newFixedThreadPool(10);
    ContainsProfanityService containsProfanity;
    ProfanityEscapeService escapeProfanity;

    public ProfanityCheckClient() {
        this.containsProfanity = new ContainsProfanityServiceImpl();
        this.escapeProfanity = new ProfanityEscapeServiceImpl();
    }

    public ProfanityCheckClient(ContainsProfanityService containsProfanity, ProfanityEscapeService escapeProfanity) {
        this.containsProfanity = containsProfanity;
        this.escapeProfanity = escapeProfanity;
    }

    public IsSwearWord profanityCheck(String input) {
        CompletableFuture<Boolean> profanityFlag = CompletableFuture
                .supplyAsync(() -> containsProfanity.test(input));
        CompletableFuture<String> escapedText = CompletableFuture
                .supplyAsync(() -> escapeProfanity.escape(input));

        return profanityFlag
                .thenComposeAsync(b -> {
                    if (!b) {
                        log.fine("No profanity found, completing");
                        return CompletableFuture.completedFuture(new    IsSwearWord(b, input));
                    } else {
                        log.fine("Profanity, escaping swear words");
                        return escapedText.thenCompose(t ->
                                CompletableFuture.completedFuture(new IsSwearWord(b, input, t))
                        );
                    }
                })
                .join();

    }

}
