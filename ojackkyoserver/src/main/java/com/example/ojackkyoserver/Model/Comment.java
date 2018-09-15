package com.example.ojackkyoserver.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Data
@Entity
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String contents;
    private String timeCreated;

    @Transient
    private String authorNickname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId", insertable = false, updatable = false, nullable = false)
    @JsonIgnore
    User author;


    public String getAuthorNickname(){
        if(authorNickname == null) {
            return author.getNickname();
        }else{
            return authorNickname;
        }
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "articleId", insertable = false, updatable = false, nullable = false)
    @JsonIgnore
    private Article article;


    @Transient
    private Integer articleId;

    public Integer getArticleId(){
        if(articleId == null) {
            return article.getId();
        }else{
            return articleId;
        }
    }


}
