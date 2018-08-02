package com.example.ojackkyoserver.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
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
    String file;   //base64 encoding

}
