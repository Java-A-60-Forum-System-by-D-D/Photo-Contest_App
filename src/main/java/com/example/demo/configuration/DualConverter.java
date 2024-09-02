package com.example.demo.configuration;

public interface DualConverter<T, U> {
    U convertTo(T source);
    T convertFrom(U source);
}
