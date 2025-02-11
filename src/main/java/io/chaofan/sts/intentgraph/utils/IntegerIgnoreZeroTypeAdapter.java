package io.chaofan.sts.intentgraph.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class IntegerIgnoreZeroTypeAdapter extends TypeAdapter<Integer> {
    @Override
    public void write(JsonWriter jsonWriter, Integer integer) throws IOException {
        if (integer == null || integer == 0) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.value(integer);
    }

    @Override
    public Integer read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return 0;
        }

        return jsonReader.nextInt();
    }
}
