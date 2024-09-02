package com.example.demo.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextConverter implements DualConverter<Object, byte[]> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] convertTo(Object source) {
        try {
            return objectMapper.writeValueAsBytes(source);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize SecurityContext", e);
        }
    }

    @Override
    public Object convertFrom(byte[] source) {
        try {
            return objectMapper.readValue(source, SecurityContextHolder.createEmptyContext().getClass());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize SecurityContext", e);
        }
    }
}