package com.example.demo.models.dto;


import lombok.Data;

import java.util.List;

@Data
public class ContestSummaryDTO {


    private List<PlaceDTO> places;

    private ContestViewDto contestViewDto;
    private int place;
    private List<String> users;


}
