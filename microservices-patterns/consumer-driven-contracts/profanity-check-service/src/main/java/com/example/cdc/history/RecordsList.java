package com.example.cdc.history;

import pl.setblack.airomem.core.Storable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordsList implements Storable<RecordsList> {

    private List<Record> allRecords = new ArrayList<>();

    @Override
    public RecordsList getImmutable() {
        return this;
    }

    public void add(Record record) {
        allRecords.add(record);
    }

    public List<Record> getAll() {
        return Collections.unmodifiableList(allRecords);
    }
}
