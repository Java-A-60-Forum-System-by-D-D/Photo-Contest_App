package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


public enum Phase{

    NOT_STARTED,
    PHASE_1,
    PHASE_2,
    FINISHED;

    @Override
    public String toString() {
        String name = "";
        switch (this){
            case NOT_STARTED:
                name = "Not started";
            break;
            case PHASE_1 :
                name = "Open";
            break;
            case PHASE_2 :
                name = "Voting";
            break;
            case FINISHED :
                name = "Finished";
            break;
        }
        return name;
    }
}
