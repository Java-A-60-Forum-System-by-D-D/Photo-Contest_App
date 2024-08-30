package com.example.demo.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaceDTO {
    private int place;
    private List<String> users;
}