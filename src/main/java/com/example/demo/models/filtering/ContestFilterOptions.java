package com.example.demo.models.filtering;

import com.example.demo.models.Category;
import com.example.demo.models.Phase;
import lombok.Data;

@Data
public class ContestFilterOptions {
    private String title;
    private Category category;
    //todo type?
    private Phase phase;

    public ContestFilterOptions(String title, Category category, Phase phase) {
        this.title = title;
        this.category = category;
        this.phase = phase;
    }
}
