package com.example.ojackkyoserver.Model;


import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class TagArticleMap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String tagName;
    Integer article;
}
