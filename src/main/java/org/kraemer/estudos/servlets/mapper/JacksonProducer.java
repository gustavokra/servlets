package org.kraemer.estudos.servlets.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class JacksonProducer {

    @Produces
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}