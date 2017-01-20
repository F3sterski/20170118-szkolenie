package ws.rest;

import ws.libs.dictionary.BablaDictionaryClient;
import ws.libs.dictionary.DictDictionaryClient;
import ws.libs.dictionary.DictionaryClient;
import ws.libs.dictionary.DictionaryWord;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/translate")
public class TranslationResource {

    @Inject
    DictionaryClient client;

    @GET
    @Path("/{word}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DictionaryWord> translateAll(@PathParam("word") String word) {
        System.out.println("client = " + client);

        return client.allTranslationsFor(word);
    }

    @GET
    @Path("/{word}/first")
    public Response translateFirst(@PathParam("word") String word) {
        Optional<DictionaryWord> optional = client.firstTranslationFor(word);
        return Response.ok(optional.get()).build();

    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    // [home, house, doctor]
    public List<DictionaryWord> translateBatch(List<String> words) {
        List<DictionaryWord> collect = words.stream()
                //Stream<String>
                .map(s -> client.firstTranslationFor(s))
                //Stream<Optional<DictionaryWord>>
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return collect;
    }


    //GET /translate/{word}

    //POST /translate

    //GET /translate/{word}/first

}
