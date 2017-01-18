package com.example.cdc.history;

import org.springframework.stereotype.Component;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.builders.PrevaylerBuilder;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@Component
public class RecordsDao {

    private PersistenceController<RecordsList, RecordsList> controller;

    @PostConstruct
    public void setup() {
        this.controller = PrevaylerBuilder.newBuilder()
                .withFolder(Paths.get("profanity-checks").toAbsolutePath().toString())
                .useSupplier(() -> new RecordsList())
                .disableRoyalFoodTester()
                .build();
    }

    public void add(String input, Instant time) {
        controller.execute(records -> records.add(new Record(input, Instant.now())));
    }

    public List<Record> getAll() {
        return controller.query(recordsList -> recordsList.getAll());
    }

}

