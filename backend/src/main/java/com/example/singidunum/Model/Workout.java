package com.example.singidunum.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Workout")
public class Workout {
    @Id
    private String id;
    private String name;
    private ArrayList<String> moves;
    private ArrayList<Long> movesTime;
    private int roundNumber;
    private int roundMin;
    private int roundSec;
    private int restMin;
    private int restSec;
    private int preparetMin;
    private int prepareSec;

}