package io.chaofan.sts.intentgraph.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BooleanIgnoreFalseTypeAdapter extends TypeAdapter<Boolean> {
    @Override
    public void write(JsonWriter jsonWriter, Boolean value) throws IOException {
        if (value == null || !value) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(true);
    }

    @Override
    public Boolean read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return false;
        }

        return jsonReader.nextBoolean();
    }
}
