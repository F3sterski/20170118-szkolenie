package client;

public class Main {

    public static void main(String[] args) {
        final String str = "The quick brown fox jumped over the lazy dog";

        //profanity check

        //google translate
        ApiKeysClient apiKeysClient = new ApiKeysClient();
        String testKey = apiKeysClient.getTestKey();

        GoogleTranslationApi googleTranslationApi = new GoogleTranslationApi(testKey);
        String translate = googleTranslationApi.translate(str);

        // str.substring(profanityCheck.output.indexOf(""), profanityCheck.output.lastIndexOf("")+1)
        // translation {englishWord, polishWord}

        System.out.println(translate);
    }

}
