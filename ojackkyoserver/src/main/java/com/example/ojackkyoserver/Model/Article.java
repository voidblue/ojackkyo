package com.example.ojackkyoserver.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

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
    Integer viewed;
    @Transient @JsonIgnore
    Integer searchPriority;
    @Transient
    ArrayList<Tag> tags;
    @Transient
    String authorsNickname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", updatable = false)
    @JsonIgnore
    User author;

    public String getAuthorsNickname(){
        if(authorsNickname == null) {
            return author.getNickname();
        }else {
            return authorsNickname;
        }
    }




}
