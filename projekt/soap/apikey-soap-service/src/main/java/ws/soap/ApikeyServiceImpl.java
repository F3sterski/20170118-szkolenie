package ws.soap;

import repository.ApiKeys;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class ApikeyServiceImpl implements ApikeyService {

    final static Logger log = Logger.getLogger(ApikeyServiceImpl.class.getName());
    final Random randomGenerator = new Random();
    final List<String> keys = ApiKeys.keys();

    @Override
    public String productionApiKey(String username) throws NoApikeysAvailableException {
        log.info(String.format("Getting for {User=%s}", username));
        if (!keys.isEmpty()) {
            int index = randomGenerator.nextInt(keys.size());
            String s = keys.get(index);
            log.info(String.format("Apikey granted {User=%s} {Apikey=%s}", username, s));
            return s;
        }

        throw new NoApikeysAvailableException();
    }

    @Override
    public String testApiKey() {
        return ApiKeys.testKey;
    }
}
