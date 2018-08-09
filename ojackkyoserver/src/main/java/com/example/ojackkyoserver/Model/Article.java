package com.example.ojackkyoserver.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;


@Data
@Entity
@ToString
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String text;
    String title;
    String timeCreated;
    String authorsNickname;
    Integer viewed;
    @Transient @JsonIgnore
    Integer searchPriority;
    @Transient
    ArrayList<Tag> tags;
}
