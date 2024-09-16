package com.example.demo.models.filtering;

import com.example.demo.models.Category;
import com.example.demo.models.Phase;
import com.example.demo.models.Type;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContestFilterOptions {
    private String title;
    private String category;
    private Type type;
    private Phase phase;
    private String username;

    public ContestFilterOptions(String title, String category, Type type, Phase phase, String username) {
        this.title = title;
        this.category = category;
        this.type = type;
        this.phase = phase;
        this.username = username;
    }
}
