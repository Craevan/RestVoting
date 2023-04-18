package com.crevan.restvoting.util;

import com.crevan.restvoting.config.SecurityConfig;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class JsonDeserializers {

    // https://stackoverflow.com/a/60995048/548473
    public static class PasswordDeserializer extends JsonDeserializer<String> {
        public String deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            String rawPassword = node.asText();
            return SecurityConfig.PASSWORD_ENCODER.encode(rawPassword);
        }
    }
}
