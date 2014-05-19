package com.dartalley.filters.jackson;

import com.dartalley.filters.core.LoggingFilter.TracingProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonTracingProvider implements TracingProvider {
    private final ObjectWriter writer;
    
    public JsonTracingProvider(ObjectWriter writer) {
        this.writer = writer;
    }
    
    @Override
    public String getTrace(Object obj) {
        try {
            return writer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj.toString();
        }
    }
}
